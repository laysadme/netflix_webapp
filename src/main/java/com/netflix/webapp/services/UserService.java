package com.netflix.webapp.services;

import com.netflix.webapp.dtos.UserDTO;
import java.util.List;

public interface UserService {
    UserDTO save(UserDTO UserDTO);
    List<UserDTO> saveAll(List<UserDTO> userEntities);

    List<UserDTO> findAll();
    List<UserDTO> findAll(List<String> ids);
    UserDTO findOne(String id);
    UserDTO findOneByUsername(String username);

    long count();
    long delete(String id);
    long delete(List<String> ids);
    long deleteAll();

    UserDTO update(UserDTO UserDTO);
    long update(List<UserDTO> userEntities);
}
