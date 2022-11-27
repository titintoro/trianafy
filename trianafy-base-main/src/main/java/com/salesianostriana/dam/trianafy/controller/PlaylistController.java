package com.salesianostriana.dam.trianafy.controller;

import com.salesianostriana.dam.trianafy.dto.*;
import com.salesianostriana.dam.trianafy.model.Artist;
import com.salesianostriana.dam.trianafy.model.Playlist;
import com.salesianostriana.dam.trianafy.model.Song;
import com.salesianostriana.dam.trianafy.service.PlaylistService;
import com.salesianostriana.dam.trianafy.service.SongService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Tag(name = "PlaylistController", description = "Playlist Controller Class")
public class PlaylistController {

    private final PlaylistDtoConverter dtoConverter;
    private final SongService songService;
    private final PlaylistService playlistService;


    @Operation(summary = "Get a list of all Playlists")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Playlists Found",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = List.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            [
                                                 {
                                                     "id": 13,
                                                     "name": "Hits de Michael Jackson",
                                                     "description": "Una lista del gran artista",
                                                     "songs": 0
                                                 },
                                                 {
                                                     "id": 17,
                                                     "name": "Pop",
                                                     "description": "Una lista muy divertida",
                                                     "songs": 0
                                                 }
                                             ]                                          
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "404",
                    description = "No Playlists Found",
                    content = @Content),
    })
    @GetMapping("/list/")
    public ResponseEntity<List<PlaylistResponse>> findAll() {
        List<Playlist> playlists = playlistService.findAll();

        if (playlists.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            List<PlaylistResponse> result = playlists.stream().map(dtoConverter::playlistToPlaylistResponse).collect(Collectors.toList());
            return ResponseEntity.ok().body(result);
        }
    }


    @Operation(summary = "Get a single Playlist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Playlist Found",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = List.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            [
                                                {
                                                     "id": 13,
                                                     "name": "Hits de Michael Jackson",
                                                     "description": "Una lista del gran artista",
                                                     "songs": 0
                                                 }
                                            ]                                          
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "404",
                    description = "No Playlist Found",
                    content = @Content),
    })
    @GetMapping("/list/{id}")
    public ResponseEntity<Playlist> findOne( @PathVariable Long id) {
        if (playlistService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity
                .ok()
                .body(playlistService.findById(id).orElse(null));
    }


    @Operation(summary = "Create a new Playlist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Playlist Created Successfully",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Artist.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            [
                                                    {
                                                        "id": 12,
                                                        "name": "Random",
                                                        "description": "Una lista muy loca",
                                                        "songs": 4
                                                    }
                                            ]                                          
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "400",
                    description = "Bad Artist Creation Request",
                    content = @Content),
    })
    @PostMapping("/list/")
    public ResponseEntity<PlaylistRequest> create(@RequestBody PlaylistRequest playlistRequest) {

        Playlist newPlaylist = dtoConverter.PlaylistRequestToPlaylist(playlistRequest);

        playlistService.add(newPlaylist);

        PlaylistRequest shoPlaylistRequest = dtoConverter.playlistToPlaylistResponseToPlaylistRequest(newPlaylist);

        return ResponseEntity.status(HttpStatus.CREATED).body(shoPlaylistRequest);
    }


    @Operation(summary = "Update a Playlist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Playlist Created Successfully",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Artist.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            [
                                                    {
                                                        "id": 12,
                                                        "name": "Random",
                                                        "description": "Una lista muy loca",
                                                        "songs": 4
                                                    }
                                            ]                                          
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "400",
                    description = "Bad Artist Creation Request",
                    content = @Content),
    })
    @PutMapping("/list/{id}")
    public ResponseEntity<PlaylistEditResponse> edit(
            @RequestBody PlaylistEditRequest p,
            @PathVariable Long id) {

        if (playlistService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.of(
                playlistService.findById(id).map(m -> {
            m.setName(p.getName());
            m.setDescription(p.getDescription());
                    playlistService.edit(m);
            return dtoConverter.playlistToPlaylistEditResponse(m);
        }));

    }


    @Operation(summary = "Delete a Playlist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Playlist Deleted Successfully",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = List.class))
                    )}),
    })
    @DeleteMapping("/list/{id}")
    public ResponseEntity<?> deletePlaylist(@PathVariable Long id) {
        if (playlistService.existsById(id)){
            playlistService.deleteById(id);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @Operation(summary = "Get a list of Songs of a Playlist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Songs Found",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = List.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            "id": 12,
                                                      "name": "Hits de Michael Jackson",
                                                      "description": "Una lista del gran artista",
                                                      "songs": [
                                                          {
                                                              "id": 9,
                                                              "title": "Enter Sandman",
                                                              "album": "Metallica",
                                                              "year": "1991",
                                                              "artist": {
                                                                  "id": 3,
                                                                  "name": "Metallica"
                                                              }
                                                          },
                                                          {
                                                              "id": 8,
                                                              "title": "Love Again",
                                                              "album": "Future Nostalgia",
                                                              "year": "2021",
                                                              "artist": null
                                                          },
                                                          
                                            ]                                          
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "404",
                    description = "No Playlist/Songs Found",
                    content = @Content),
    })
    @GetMapping("/list/{id}/song/")
    public ResponseEntity<Playlist> findAllSongsOfAPlaylist(@PathVariable Long id) {
        if (playlistService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity
                .ok()
                .body(playlistService.findById(id).orElse(null));
    }


    @Operation(summary = "Add a Songs to a Playlist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Song Added Succesfully",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = List.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            "id": 12,
                                                      "name": "Hits de Michael Jackson",
                                                      "description": "Una lista del gran artista",
                                                      "songs": [
                                                          {
                                                              "id": 9,
                                                              "title": "Enter Sandman",
                                                              "album": "Metallica",
                                                              "year": "1991",
                                                              "artist": {
                                                                  "id": 3,
                                                                  "name": "Metallica"
                                                              }
                                                          },
                                                          {
                                                              "id": 8,
                                                              "title": "Love Again",
                                                              "album": "Future Nostalgia",
                                                              "year": "2021",
                                                              "artist": null
                                                          },
                                                          
                                            ]                                          
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "404",
                    description = "Bad add Song to playlist request",
                    content = @Content),
    })
    @PostMapping("/list/{id1}/song/{id2}")
    public ResponseEntity<Playlist> addSongToPlaylist(@PathVariable Long id1, @PathVariable Long id2) {

        Optional<Playlist> p = playlistService.findById(id1);
        Optional<Song> s = songService.findById(id2);

        Playlist playlist;
        Song song;

        if (p.isEmpty() || s.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            playlist = p.get();
            song = s.get();
        }

        playlist.getSongs().add(song);
        return ResponseEntity.ok(playlistService.add(playlist));

    }


    @DeleteMapping("/list/{id1}/song/{id2}")
    public ResponseEntity<Song> DeleteOneSongFromPlaylist(@PathVariable Long id1,
                                              @PathVariable Long id2) {

        if (playlistService.findById(id1).isPresent()) {
            Playlist newPlaylist = playlistService.findById(id1).get();
            if (songService.findById(id2).isPresent()){
                newPlaylist.getSongs().remove(songService.findById(id2).get());
                playlistService.add(newPlaylist);
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
        Song songSelected = new Song();


        if (playlistService.findById(id1).isPresent()) {
             playlist = playlistService.findById(id1).get();

             if (songService.findById(id2).isPresent()) {
                  songSelected = songService.findById(id2).get();

                  if ( playlist.getSongs().contains(songSelected) ) {
                    return ResponseEntity
                             .ok().body(songSelected);
                  }
             }
        }
        return ResponseEntity.notFound().build();
    }

}
