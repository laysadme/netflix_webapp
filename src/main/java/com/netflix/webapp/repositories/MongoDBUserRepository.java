package com.netflix.webapp.repositories;

import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.TransactionOptions;
import com.mongodb.WriteConcern;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.ReplaceOneModel;
import com.netflix.webapp.entities.UserEntity;
import jakarta.annotation.PostConstruct;
import org.bson.BsonDocument;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.in;
import static com.mongodb.client.model.ReturnDocument.AFTER;

@Repository
public class MongoDBUserRepository implements UserRepository {
    private static final TransactionOptions txnOptions = TransactionOptions.builder()
            .readPreference(ReadPreference.primary()).readConcern(ReadConcern.MAJORITY)
            .writeConcern(WriteConcern.MAJORITY).build();

    private final MongoClient client;
    private MongoCollection<UserEntity> userCollection;

    public MongoDBUserRepository(MongoClient mongoClient) {
        this.client = mongoClient;
    }

    @PostConstruct
    void init() {
        userCollection = client.getDatabase("netflix-genres").getCollection("users", UserEntity.class);
    }

    @Override
    public List<UserEntity> findAll() {
        return userCollection.find().into(new ArrayList<>());
    }

    @Override
    public List<UserEntity> findAll(List<String> ids) {
        return userCollection.find(in("_id", mapToObjectIds(ids))).into(new ArrayList<>());
    }

    @Override
    public UserEntity findOne(String id) {
        return userCollection.find(eq("_id", new ObjectId(id))).first();
    }

    @Override
    public UserEntity findOneByUsername(String username) {
        return userCollection.find(eq("username", username)).first();
    }

    @Override
    public long count() {
        return userCollection.countDocuments();
    }

    @Override
    public long delete(String id) {
        return userCollection.deleteOne(eq("_id", new ObjectId(id))).getDeletedCount();
    }

    @Override
    public long delete(List<String> ids) {
        try (ClientSession clientSession = client.startSession()) {
            return clientSession.withTransaction(
                    () -> userCollection.deleteMany(clientSession, in("_id", mapToObjectIds(ids))).getDeletedCount(),
                    txnOptions);
        }
    }

    @Override
    public long deleteAll() {
        try (ClientSession clientSession = client.startSession()) {
            return clientSession.withTransaction(
                    () -> userCollection.deleteMany(clientSession, new BsonDocument()).getDeletedCount(), txnOptions);
        }
    }

    @Override
    public UserEntity update(UserEntity userEntity) {
        FindOneAndReplaceOptions options = new FindOneAndReplaceOptions().returnDocument(AFTER);

        return userCollection.findOneAndReplace(eq("_id", userEntity.getId()), userEntity, options);
    }

    @Override
    public long update(List<UserEntity> userEntities) {
        List<ReplaceOneModel<UserEntity>> writes = userEntities.stream()
                .map(user -> new ReplaceOneModel<>(eq("_id", user.getId()), user)).toList();

        try (ClientSession clientSession = client.startSession()) {
            return clientSession.withTransaction(
                    () -> userCollection.bulkWrite(clientSession, writes).getModifiedCount(), txnOptions);
        }
    }

    @Override
    public UserEntity save(UserEntity userEntity) {
        userEntity.setId(new ObjectId());
        userCollection.insertOne(userEntity);

        return userEntity;
    }

    @Override
    public List<UserEntity> saveAll(List<UserEntity> userEntities) {
        try (ClientSession clientSession = client.startSession()) {
            return clientSession.withTransaction(() -> {
                userEntities.forEach(user -> user.setId(new ObjectId()));
                userCollection.insertMany(clientSession, userEntities);
                return userEntities;
            }, txnOptions);
        }
    }

    private List<ObjectId> mapToObjectIds(List<String> ids) {
        return ids.stream().map(ObjectId::new).toList();
    }
}
