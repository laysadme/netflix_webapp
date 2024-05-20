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
import com.netflix.webapp.entities.GenreEntity;
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
public class MongoDBGenreRepository implements GenreRepository {
    private static final TransactionOptions txnOptions = TransactionOptions.builder()
            .readPreference(ReadPreference.primary()).readConcern(ReadConcern.MAJORITY)
            .writeConcern(WriteConcern.MAJORITY).build();

    private final MongoClient client;
    private MongoCollection<GenreEntity> genreCollection;

    public MongoDBGenreRepository(MongoClient mongoClient) {
        this.client = mongoClient;
    }

    @PostConstruct
    void init() {
        genreCollection = client.getDatabase("netflix-genres").getCollection("genres", GenreEntity.class);
    }

    @Override
    public List<GenreEntity> findAll() {
        return genreCollection.find().into(new ArrayList<>());
    }

    @Override
    public List<GenreEntity> findAll(List<String> ids) {
        return genreCollection.find(in("_id", mapToStrings(ids))).into(new ArrayList<>());
    }

    @Override
    public GenreEntity findOne(String id) {
        return genreCollection.find(eq("_id", new ObjectId(id))).first();
    }

    @Override
    public GenreEntity findOneByName(String name) {
        return genreCollection.find(eq("name", name)).first();
    }

    @Override
    public long count() {
        return genreCollection.countDocuments();
    }

    @Override
    public long delete(String id) {
        return genreCollection.deleteOne(eq("_id", new ObjectId(id))).getDeletedCount();
    }

    @Override
    public long delete(List<String> ids) {
        try (ClientSession clientSession = client.startSession()) {
            return clientSession.withTransaction(
                    () -> genreCollection.deleteMany(clientSession, in("_id", mapToStrings(ids))).getDeletedCount(),
                    txnOptions);
        }
    }

    @Override
    public long deleteAll() {
        try (ClientSession clientSession = client.startSession()) {
            return clientSession.withTransaction(
                    () -> genreCollection.deleteMany(clientSession, new BsonDocument()).getDeletedCount(), txnOptions);
        }
    }

    @Override
    public GenreEntity update(GenreEntity genreEntity) {
        FindOneAndReplaceOptions options = new FindOneAndReplaceOptions().returnDocument(AFTER);

        return genreCollection.findOneAndReplace(eq("_id", genreEntity.getId()), genreEntity, options);
    }

    @Override
    public long update(List<GenreEntity> genreEntities) {
        List<ReplaceOneModel<GenreEntity>> writes = genreEntities.stream()
                .map(genre -> new ReplaceOneModel<>(eq("_id", genre.getId()), genre)).toList();

        try (ClientSession clientSession = client.startSession()) {
            return clientSession.withTransaction(
                    () -> genreCollection.bulkWrite(clientSession, writes).getModifiedCount(), txnOptions);
        }
    }

    @Override
    public GenreEntity save(GenreEntity genreEntity) {
        genreEntity.setId(new ObjectId());
        genreCollection.insertOne(genreEntity);

        return genreEntity;
    }

    @Override
    public List<GenreEntity> saveAll(List<GenreEntity> genreEntities) {
        try (ClientSession clientSession = client.startSession()) {
            return clientSession.withTransaction(() -> {
                genreEntities.forEach(genre -> genre.setId(new ObjectId()));
                genreCollection.insertMany(clientSession, genreEntities);
                return genreEntities;
            }, txnOptions);
        }
    }

    private List<String> mapToStrings(List<String> ids) {
        return ids.stream().map(String::new).toList();
    }
}
