package com.salesianostriana.dam.trianafy.controller;

import com.salesianostriana.dam.trianafy.dto.ArtistEditRequest;
import com.salesianostriana.dam.trianafy.model.Artist;
import com.salesianostriana.dam.trianafy.model.Song;
import com.salesianostriana.dam.trianafy.repos.ArtistRepository;
import com.salesianostriana.dam.trianafy.repos.SongRepository;
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

@RestController
@RequiredArgsConstructor
@Tag(name = "ArtistController", description = "Artist Controller Class")
public class ArtistController {

    private final ArtistRepository artistRepository;
    private final SongRepository songRepository;


    @Operation(summary = "Get a single Artist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Artist Found",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Artist.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            [
                                                {
                                                  "id": 1,
                                                  "name": "Joaquín Sabina"
                                                }
                                            ]                                          
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "404",
                    description = "No Artist Found",
                    content = @Content),
    })
    @GetMapping("/artist/{id}")
    public ResponseEntity<Artist> findOne(@PathVariable Long id) {
        if (artistRepository.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.of(artistRepository.findById(id));
    }


    @Operation(summary = "Create a new Artist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Artist Created Successfully",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Artist.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            [
                                                {
                                                  "id": 14,
                                                  "name": "Joaquín bunny"
                                                }
                                            ]                                          
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "400",
                    description = "Bad Artist Creation Request",
                    content = @Content),
    })
    @PostMapping("/artist/")
    public ResponseEntity<Artist> create(@RequestBody Artist nuevoArtista) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(artistRepository.save(nuevoArtista));
    }


    @Operation(summary = "Get a list of all Artists")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Artists Founded",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Artist.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            [
                                                {
                                                     "id": 1,
                                                     "name": "Joaquín Sabina"
                                                },
                                                {
                                                     "id": 2,
                                                     "name": "Dua Lipa"
                                                }
                                            ]                                          
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "404",
                    description = "No Artists Found",
                    content = @Content),
    })
    @GetMapping("/artist/")
    public ResponseEntity<List<Artist>> findAll() {
        if (artistRepository.findAll().isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity
                .ok()
                .body(artistRepository.findAll());
    }


    @Operation(summary = "Delete an Artist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Artist Deleted Successfully",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Artist.class))
                    )}),
    })
    @DeleteMapping("/artist/{id}")
    public ResponseEntity<?> deleteArtist(@PathVariable Long id) {

        if(artistRepository.findById(id).isPresent()) {

            Artist artistDeleted = artistRepository.findById(id).get();

            if (artistRepository.existsById(id)){
                songRepository.findAll()
                        .stream()
                        .filter(s -> s.getArtist().equals(artistDeleted))
                        .forEach(s -> s.setArtist(null));
                artistRepository.deleteById(id);
            }
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();


    }


    @Operation(summary = "Update data of an Artist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Artist Updated Successfully",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Artist.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            [
                                                {
                                                  "id": 14,
                                                  "name": "Joaquín bunny"
                                                }
                                            ]                                          
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "400",
                    description = "Bad Artist Update Request",
                    content = @Content),
    })
    @PutMapping("/artist/{id}")
    public ResponseEntity<Artist> edit(@RequestBody ArtistEditRequest artistEditRequest, @PathVariable Long id) {

        if (artistRepository.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.of(
                artistRepository
                        .findById(id)
                        .map(a -> {
                            a.setName(artistEditRequest.getName());
                            artistRepository.save(a);
                            return a;
                        })
        );
    }
}
