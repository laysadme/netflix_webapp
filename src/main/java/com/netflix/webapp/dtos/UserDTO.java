package com.netflix.webapp.dtos;

import com.netflix.webapp.entities.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String password;
    private List<String> roles;

    public UserDTO(UserEntity userEntity) {
        this(userEntity.getId() == null ? new ObjectId().toHexString() : userEntity.getId().toHexString(),
                userEntity.getFirstName(), userEntity.getLastName(), userEntity.getEmail(), userEntity.getUsername(), userEntity.getPassword(),
                userEntity.getRoles());
    }

    public UserEntity toEntity() {
        ObjectId _id = id == null ? new ObjectId() : new ObjectId(id);
        return new UserEntity(_id, firstName, lastName, email, username, password, roles);
    }
}
