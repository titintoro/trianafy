package com.salesianostriana.dam.trianafy.controller;

import com.salesianostriana.dam.trianafy.dto.SongDtoConverter;
import com.salesianostriana.dam.trianafy.dto.SongDtoToArtist;
import com.salesianostriana.dam.trianafy.dto.SongRequest;
import com.salesianostriana.dam.trianafy.model.Artist;
import com.salesianostriana.dam.trianafy.model.Song;
import com.salesianostriana.dam.trianafy.repos.ArtistRepository;
import com.salesianostriana.dam.trianafy.repos.SongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class SongController {

    private final SongRepository songRepo;
    private final ArtistRepository artistRepo;
    private final SongDtoConverter sDtoConverter;

    @GetMapping("/song/")
    public ResponseEntity<List<SongDtoToArtist>> findAllSongs() {
        List<Song> songsList = songRepo.findAll();

        if(songsList.isEmpty()){
            return ResponseEntity.notFound().build();
        }else {
            List<SongDtoToArtist> result = songsList.stream().map(sDtoConverter::conversorPostSong).collect(Collectors.toList());
            return ResponseEntity.ok().body(result);
        }
    }


    @GetMapping("/song/{id}")
    public ResponseEntity<Song> findOneSong(@PathVariable Long id) {
        if (songRepo.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity
                .of(songRepo.findById(id));
    }


    @PostMapping("/song/")
    public ResponseEntity<SongDtoToArtist> addSong(@RequestBody SongRequest songRequest) {

        if (songRequest.getArtistId() == null) {
            return ResponseEntity.notFound().build();
        }

        Song newSong = sDtoConverter.toSong(songRequest);

        Artist artist = artistRepo.findById(songRequest.getArtistId()).orElse(null);

        newSong.setArtist(artist);
        songRepo.save(newSong);

        SongDtoToArtist songDtoShowPost = sDtoConverter.conversorPostSong(newSong);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(songDtoShowPost);
    }


    @PutMapping("/song/{id}")
    public ResponseEntity<SongDtoToArtist> editSong(@RequestBody SongRequest dto, @PathVariable Long id) {

        if (songRepo.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            Song edit = sDtoConverter.toSong(dto);
            Artist artist = artistRepo.findById(dto.getArtistId()).orElse(null);

            edit.setArtist(artist);

            return ResponseEntity.of(
                    songRepo.findById(id).map(s -> {
                        s.setArtist(edit.getArtist());
                        s.setAlbum(edit.getAlbum());
                        s.setTitle(edit.getTitle());
                        s.setYear(edit.getYear());
                        songRepo.save(s);
                        return sDtoConverter.conversorPostSong(s);
                    }));

        }

    }


    @DeleteMapping("/song/{id}")
    public ResponseEntity<?> deleteSong(@PathVariable Long id) {
        if (songRepo.existsById(id)){
            songRepo.deleteById(id);
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
