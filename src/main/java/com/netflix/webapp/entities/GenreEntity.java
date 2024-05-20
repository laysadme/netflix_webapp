package com.netflix.webapp.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GenreEntity {
    private ObjectId id;
    private String name;
    private Integer numOfItems;
}
