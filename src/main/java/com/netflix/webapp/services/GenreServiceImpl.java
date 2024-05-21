package com.netflix.webapp.services;

import com.netflix.webapp.dtos.GenreDTO;
import com.netflix.webapp.entities.GenreEntity;
import com.netflix.webapp.repositories.GenreRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    public GenreServiceImpl(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    public GenreDTO save(GenreDTO GenreDTO) {
        return new GenreDTO(genreRepository.save(GenreDTO.toEntity()));
    }

    @Override
    public List<GenreDTO> saveAll(List<GenreDTO> genreEntities) {
        return genreEntities.stream().map(GenreDTO::toEntity).peek(genreRepository::save).map(GenreDTO::new).toList();
    }

    @Override
    public List<GenreDTO> findAll() {
        return genreRepository.findAll().stream().map(GenreDTO::new).toList();
    }

    @Override
    public List<GenreDTO> findAll(List<String> ids) {
        return genreRepository.findAll(ids).stream().map(GenreDTO::new).toList();
    }

    @Override
    public GenreDTO findOne(String id) {
        GenreEntity genreEntity = genreRepository.findOne(id);

        if (genreEntity == null) {
            return null;
        }

        return new GenreDTO(genreEntity);
    }

    @Override
    public GenreDTO findOneByName(String name) {
        GenreEntity genreEntity = genreRepository.findOneByName(name);

        if (genreEntity == null) {
            return null;
        }

        return new GenreDTO(genreEntity);
    }

    @Override
    public long count() {
        return genreRepository.count();
    }

    @Override
    public long delete(String id) {
        return genreRepository.delete(id);
    }

    @Override
    public long delete(List<String> ids) {
        return genreRepository.delete(ids);
    }

    @Override
    public long deleteAll() {
        return genreRepository.deleteAll();
    }

    @Override
    public GenreDTO update(GenreDTO GenreDTO) {
        return new GenreDTO(genreRepository.update(GenreDTO.toEntity()));
    }

    @Override
    public long update(List<GenreDTO> genreEntities) {
        return genreRepository.update(genreEntities.stream().map(GenreDTO::toEntity).toList());
    }
}
