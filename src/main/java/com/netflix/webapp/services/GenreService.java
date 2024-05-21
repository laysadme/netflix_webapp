package com.netflix.webapp.services;

import com.netflix.webapp.dtos.GenreDTO;

import java.util.List;

public interface GenreService {
    GenreDTO save(GenreDTO GenreDTO);
    List<GenreDTO> saveAll(List<GenreDTO> genreEntities);

    List<GenreDTO> findAll();
    List<GenreDTO> findAll(List<String> ids);
    GenreDTO findOne(String id);
    GenreDTO findOneByName(String name);

    long count();
    long delete(String id);
    long delete(List<String> ids);
    long deleteAll();

    GenreDTO update(GenreDTO GenreDTO);
    long update(List<GenreDTO> genreEntities);
}
