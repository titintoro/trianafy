package com.salesianostriana.dam.trianafy.controller;

import com.salesianostriana.dam.trianafy.dto.SongDtoConverter;
import com.salesianostriana.dam.trianafy.dto.SongDtoToArtist;
import com.salesianostriana.dam.trianafy.dto.SongRequest;
import com.salesianostriana.dam.trianafy.model.Artist;
import com.salesianostriana.dam.trianafy.model.Playlist;
import com.salesianostriana.dam.trianafy.model.Song;
import com.salesianostriana.dam.trianafy.service.ArtistService;
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
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Tag(name = "SongController", description = "Song Controller Class")
public class SongController {

    private final SongService songService;
    private final ArtistService artistService;
    private final SongDtoConverter sDtoConverter;

    private final PlaylistService playlistService;

    @Operation(summary = "Get a list of all Songs")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Songs Found",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Song.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            [
                                                {
                                                    "id": 5,
                                                    "title": "Donde habita el olvido",
                                                    "artist": "Joaquín Sabina",
                                                    "year": "1999",
                                                    "album": "19 días y 500 noches"                                                                                      
                                                },
                                                {
                                                    "id": 5,
                                                    "title": "Donde habita el olvido",
                                                    "artist": "Joaquín Sabina",
                                                    "year": "1999",
                                                    "album": "19 días y 500 noches"
                                                }
                                            ]                                          
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "404",
                    description = "No Songs Found",
                    content = @Content),
    })
    @GetMapping("/song/")
    public ResponseEntity<List<SongDtoToArtist>> findAllSongs() {
        List<Song> songsList = songService.findAll();

        if (songsList.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            List<SongDtoToArtist> result = songsList.stream().map(sDtoConverter::conversorPostSong).collect(Collectors.toList());
            return ResponseEntity.ok().body(result);
        }
    }


    @Operation(summary = "Get a single Song")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Song Found",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Song.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            [
                                                {
                                                    "id": 4,
                                                    "title": "19 días y 500 noches",
                                                    "album": "19 días y 500 noches",
                                                    "year": "1999",
                                                    "artist": {
                                                        "id": 1,
                                                        "name": "Joaquín Sabina"
                                                    }
                                                }
                                            ]                                          
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "404",
                    description = "No Song Found",
                    content = @Content),
    })
    @GetMapping("/song/{id}")
    public ResponseEntity<Song> findOneSong(@PathVariable Long id) {
        if (songService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        SongDtoConverter songDtoConverter = new SongDtoConverter();

        return ResponseEntity
                .ok()
                .body(songService.findById(id).get());

    }


    @Operation(summary = "Create a new Song")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Song Created Successfully",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Song.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            [
                                                {
                                                    "id": 13,
                                                    "title": "Titi me preguntó",
                                                    "artist": "Joaquín Sabina",
                                                    "year": "2022",
                                                    "album": "Un verano sin ti"
                                                }
                                            ]                                          
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "400",
                    description = "Bad Song Creation Request",
                    content = @Content),
    })
    @PostMapping("/song/")
    public ResponseEntity<SongDtoToArtist> addSong(@RequestBody SongRequest songRequest) {

        if (songRequest.getArtistId() == null) {
            return ResponseEntity.notFound().build();
        }

        Song newSong = sDtoConverter.toSong(songRequest);

        Artist artist = artistService.findById(songRequest.getArtistId()).orElse(null);

        newSong.setArtist(artist);
        songService.add(newSong);

        SongDtoToArtist songDtoShowPost = sDtoConverter.conversorPostSong(newSong);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(songDtoShowPost);
    }


    @Operation(summary = "Update data of a Song")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Song Updated Successfully",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Song.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            [
                                                {
                                                    "id": 6,
                                                    "title": "Safaera",
                                                    "artist": "Joaquín Sabina",
                                                    "year": "2022",
                                                    "album": "19 días y 500 noches"
                                                }
                                            ]                                         
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "400",
                    description = "Bad Song Update Request",
                    content = @Content),
    })
    @PutMapping("/song/{id}")
    public ResponseEntity<SongDtoToArtist> editSong(@RequestBody SongRequest dto, @PathVariable Long id) {

        if (songService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            Song edit = sDtoConverter.toSong(dto);
            Artist artist = artistService.findById(dto.getArtistId()).orElse(null);

            edit.setArtist(artist);

            return ResponseEntity.of(
                    songService.findById(id).map(s -> {
                        s.setArtist(edit.getArtist());
                        s.setAlbum(edit.getAlbum());
                        s.setTitle(edit.getTitle());
                        s.setYear(edit.getYear());
                        songService.edit(s);
                        return sDtoConverter.conversorPostSong(s);
                    }));

        }

    }


    @Operation(summary = "Delete a Song")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Song Deleted Successfully",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Song.class))
                    )}),
    })
    @DeleteMapping("/song/{id}")
    public ResponseEntity<?> deleteSong(@PathVariable Long id) {
        if (songService.existsById(id)) {
            List<Playlist> list = playlistService.findAll().stream().filter(playlist -> playlist.getSongs().contains(songService.findById(id).get())).toList();
            list.forEach(playlist -> {
                while (playlist.getSongs().contains(songService.findById(id).get())){
                    playlist.deleteSong(songService.findById(id).get());
                }
                playlistService.edit(playlist);
            });
            songService.deleteById(id);
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
