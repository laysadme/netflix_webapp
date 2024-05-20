package com.netflix.webapp.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    private ObjectId id;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String password;
    private List<String> roles;
}
