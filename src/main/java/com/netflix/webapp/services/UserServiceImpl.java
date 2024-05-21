package com.netflix.webapp.services;

import com.netflix.webapp.dtos.UserDTO;
import com.netflix.webapp.entities.UserEntity;
import com.netflix.webapp.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDTO save(UserDTO UserDTO) {
        return new UserDTO(userRepository.save(UserDTO.toEntity()));
    }

    @Override
    public List<UserDTO> saveAll(List<UserDTO> userEntities) {
        return userEntities.stream().map(UserDTO::toEntity).peek(userRepository::save).map(UserDTO::new).toList();
    }

    @Override
    public List<UserDTO> findAll() {
        return userRepository.findAll().stream().map(UserDTO::new).toList();
    }

    @Override
    public List<UserDTO> findAll(List<String> ids) {
        return userRepository.findAll(ids).stream().map(UserDTO::new).toList();
    }

    @Override
    public UserDTO findOne(String id) {
        UserEntity userEntity = userRepository.findOne(id);

        if (userEntity == null) {
            return null;
        }

        return new UserDTO(userEntity);
    }

    @Override
    public UserDTO findOneByUsername(String username) {
        UserEntity userEntity = userRepository.findOneByUsername(username);

        if (userEntity == null) {
            return null;
        }

        return new UserDTO(userEntity);
    }

    @Override
    public long count() {
        return userRepository.count();
    }

    @Override
    public long delete(String id) {
        return userRepository.delete(id);
    }

    @Override
    public long delete(List<String> ids) {
        return userRepository.delete(ids);
    }

    @Override
    public long deleteAll() {
        return userRepository.deleteAll();
    }

    @Override
    public UserDTO update(UserDTO UserDTO) {
        return new UserDTO(userRepository.update(UserDTO.toEntity()));
    }

    @Override
    public long update(List<UserDTO> userEntities) {
        return userRepository.update(userEntities.stream().map(UserDTO::toEntity).toList());
    }
}
