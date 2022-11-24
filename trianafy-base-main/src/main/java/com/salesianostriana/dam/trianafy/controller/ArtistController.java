package com.salesianostriana.dam.trianafy.controller;

import com.salesianostriana.dam.trianafy.model.Artist;
import com.salesianostriana.dam.trianafy.repos.ArtistRepository;
import com.salesianostriana.dam.trianafy.repos.SongRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Parameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/artist/")
@RequiredArgsConstructor
public class ArtistController {

    private final ArtistRepository artistRepository;
    private final SongRepository songRepository;


    @GetMapping("/artist/{id}")
    public ResponseEntity<Artist> findOne(@PathVariable Long id) {
        if (artistRepository.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.of(artistRepository.findById(id));
    }


    @PostMapping("/artist/")
    public ResponseEntity<Artist> create(@RequestBody Artist nuevoArtista) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(artistRepository.save(nuevoArtista));
    }


    @GetMapping("/artist/")
    public ResponseEntity<List<Artist>> findAll() {
        if (artistRepository.findAll().isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity
                .ok()
                .body(artistRepository.findAll());
    }


    @DeleteMapping("/artist/{id}")
    public ResponseEntity<?> deleteArtist(@PathVariable Long id) {
        Artist artistBorrar = artistRepository.findById(id).get();

        songRepository.findAll()
                .stream()
                .filter(s -> s.getArtist().equals(artistBorrar))
                .forEach(s -> s.setArtist(null));

        artistRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }


    @PutMapping("/artist/{id}")
    public ResponseEntity<Artist> edit(@RequestBody Artist e, @PathVariable Long id) {

        if (artistRepository.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.of(
                artistRepository
                        .findById(id)
                        .map(a -> {
                            a.setName(e.getName());
                            artistRepository.save(a);
                            return a;
                        })
        );
    }
}
