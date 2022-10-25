package com.codegym.service;

import com.codegym.model.Song;

import java.util.List;

public interface SongService {
    List<Song> findAll();

    Song findOne(Long id);

    Song save(Song song);

    List<Song> save(List<Song> customers);

    boolean exists(Long id);

    List<Song> findAll(List<Long> ids);

    long count();

    void delete(Long id);

    void delete(Song customer);

    void delete(List<Song> customers);

    void deleteAll();
}
