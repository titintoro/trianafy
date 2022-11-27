package com.salesianostriana.dam.trianafy.dto;

import com.salesianostriana.dam.trianafy.model.Song;
import org.springframework.stereotype.Component;

@Component
public class SongDtoConverter {


    public Song toSong(SongRequest dto){
        return Song.builder()
                .title(dto.getTitle())
                .album(dto.getAlbum())
                .year(dto.getYear())
                .build();
    }

    public SongResponse SongToSongResponse(Song s) {

        return SongResponse.builder()
                .title(s.getTitle())
                .album(s.getAlbum())
                .year(s.getYear())
                .artist(s.getArtist().getName())
                .build();

    }

    public SongDtoToArtist conversorPostSong(Song s) {
        SongDtoToArtist result = new SongDtoToArtist();
        if (s.getArtist()== null) {
            result.setId(s.getId());
            result.setTitle(s.getTitle());
            result.setArtist(null);
            result.setAlbum(s.getAlbum());
            result.setYear(s.getYear());
            return result;
        } else {
            result.setId(s.getId());
            result.setTitle(s.getTitle());
            result.setArtist(s.getArtist().getName());
            result.setAlbum(s.getAlbum());
            result.setYear(s.getYear());
            return result;
        }
    }
}
