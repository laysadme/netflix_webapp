package com.netflix.webapp.dtos;

import com.netflix.webapp.entities.GenreEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GenreDTO {
    private String id;
    private String name;
    private Integer numOfItems;

    public GenreDTO(GenreEntity userEntity) {
        this(userEntity.getId() == null ? new ObjectId().toHexString() : userEntity.getId().toHexString(),
                userEntity.getName(), userEntity.getNumOfItems());
    }

    public GenreEntity toEntity() {
        ObjectId _id = id == null ? new ObjectId() : new ObjectId(id);
        return new GenreEntity(_id, name, numOfItems);
    }
}
