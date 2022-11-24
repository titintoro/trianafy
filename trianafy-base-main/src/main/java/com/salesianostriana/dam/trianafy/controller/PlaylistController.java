package com.salesianostriana.dam.trianafy.controller;

import com.salesianostriana.dam.trianafy.dto.PlaylistDtoConverter;
import com.salesianostriana.dam.trianafy.dto.PlaylistRequest;
import com.salesianostriana.dam.trianafy.dto.PlaylistResponse;
import com.salesianostriana.dam.trianafy.model.Playlist;
import com.salesianostriana.dam.trianafy.model.Song;
import com.salesianostriana.dam.trianafy.repos.PlaylistRepository;
import com.salesianostriana.dam.trianafy.repos.SongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class PlaylistController {

    private final PlaylistRepository playlistRepo;
    private final PlaylistDtoConverter dtoConverter;
    private final SongRepository sRepository;





    @GetMapping("/list/")
    public ResponseEntity<List<PlaylistResponse>> findAll() {
        List<Playlist> playlists = playlistRepo.findAll();

        if (playlists.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            List<PlaylistResponse> result = playlists.stream().map(dtoConverter::playlistToPlaylistResponse).collect(Collectors.toList());
            return ResponseEntity.ok().body(result);
        }
    }


    @GetMapping("/list/{id}")
    public ResponseEntity<Playlist> findOne( @PathVariable Long id) {
        if (playlistRepo.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity
                .ok()
                .body(playlistRepo.findById(id).orElse(null));
    }


    @PostMapping("/list/")
    public ResponseEntity<PlaylistRequest> create(@RequestBody PlaylistRequest playlistRequest) {

        Playlist newPlaylist = dtoConverter.PlaylistRequestToPlaylist(playlistRequest);

        playlistRepo.save(newPlaylist);

        PlaylistRequest shoPlaylistRequest = dtoConverter.playlistToPlaylistResponseToPlaylistRequest(newPlaylist);

        return ResponseEntity.status(HttpStatus.CREATED).body(shoPlaylistRequest);
    }


    @PutMapping("/list/{id}")
    public ResponseEntity<Playlist> edit(
            @RequestBody Playlist p,
            @PathVariable Long id) {
        if (playlistRepo.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.of(
                playlistRepo.findById(id).map(m -> {
                    m.setName(p.getName());
                    m.setDescription(p.getDescription());
                    playlistRepo.save(m);
                    return m;
                })
        );
    }


    @DeleteMapping("/list/{id}")
    public ResponseEntity<?> deletePlaylist(@PathVariable Long id) {
        playlistRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/list/{id}/song/")
    public ResponseEntity<Playlist> findAllSongsOfAPlaylist(@PathVariable Long id) {
        if (playlistRepo.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity
                .ok()
                .body(playlistRepo.findById(id).orElse(null));
    }


    @PostMapping("/list/{id1}/song/{id2}")
    public ResponseEntity<Playlist> addSongToPlaylist(@PathVariable Long id1, @PathVariable Long id2) {

        Optional<Playlist> p = playlistRepo.findById(id1);
        Optional<Song> s = sRepository.findById(id2);

        Playlist playlist;
        Song song;

        if (p.isEmpty() || s.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            playlist = p.get();
            song = s.get();
        }

        playlist.getSongs().add(song);
        return ResponseEntity.ok(playlistRepo.save(playlist));

    }


    @DeleteMapping("/list/{id1}/song/{id2}")
    public ResponseEntity<Song> DeleteOneSongFromPlaylist(@PathVariable Long id1,
                                              @PathVariable Long id2) {

        if (playlistRepo.findById(id1).isPresent()) {
            Playlist newPlaylist = playlistRepo.findById(id1).get();
            if (sRepository.findById(id2).isPresent()){
                newPlaylist.getSongs().remove(sRepository.findById(id2).get());
                playlistRepo.save(newPlaylist);
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.notFound().build();
    }


    @GetMapping("/list/{id1}/song/{id2}")
    public ResponseEntity<Song> findOneSongInPlaylist(
            @PathVariable Long id1,
            @PathVariable Long id2
    ) {

        Playlist playlist = new Playlist();
        Song songSelected= new Song();


        if (playlistRepo.findById(id1).isPresent()) {
             playlist = playlistRepo.findById(id1).get();

             if (sRepository.findById(id2).isPresent()) {
                  songSelected = sRepository.findById(id2).get();

                  if ( playlist.getSongs().contains(songSelected) ) {
                    return ResponseEntity
                             .ok().body(songSelected);
                  }
                  return ResponseEntity.notFound().build();
             }
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.notFound().build();
    }

}
