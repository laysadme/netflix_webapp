package com.netflix.webapp.repositories;

import com.netflix.webapp.entities.GenreEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GenreRepository {
    GenreEntity save(GenreEntity genreEntity);
    List<GenreEntity> saveAll(List<GenreEntity> genreEntities);

    List<GenreEntity> findAll();
    List<GenreEntity> findAll(List<String> ids);
    GenreEntity findOne(String id);
    GenreEntity findOneByName(String name);

    long count();
    long delete(String id);
    long delete(List<String> ids);
    long deleteAll();

    GenreEntity update(GenreEntity genreEntity);
    long update(List<GenreEntity> genreEntities);
}
