package com.salesianostriana.dam.trianafy.dto;

import com.salesianostriana.dam.trianafy.model.Playlist;
import com.salesianostriana.dam.trianafy.model.Song;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PlaylistDtoConverter {

    final SongDtoConverter songDtoConverter = new SongDtoConverter();
    public Playlist PlaylistRequestToPlaylist(PlaylistRequest p) {
        return Playlist.builder()
                .name(p.getName())
                .id(p.getId())
                .description(p.getDescription())
                .build();
    }

    public PlaylistCreateResponse playlistToPlaylistCreateResponse(Playlist playlist){

        List<SongDtoToArtist> songResponseList = new ArrayList<>();

       for(Song s: playlist.getSongs()){
          songResponseList.add(songDtoConverter.conversorPostSong(s)) ;
       }

        PlaylistCreateResponse playlistCreateResponse = new PlaylistCreateResponse();
        playlistCreateResponse.setDescription(playlist.getDescription());
        playlistCreateResponse.setSongs(songResponseList);
        playlistCreateResponse.setId(playlist.getId());
        playlistCreateResponse.setName(playlist.getName());
        return playlistCreateResponse;
    }
    public PlaylistResponse playlistToPlaylistResponse(Playlist p) {
        int nSongs = p.getSongs().size();
        PlaylistResponse result = new PlaylistResponse();
        result.setId(p.getId());
        result.setName(p.getName());
        result.setDescription(p.getDescription());
        result.setSongs(nSongs);
        return result;
    }

    public PlaylistRequest playlistToPlaylistRequest(Playlist p) {
        PlaylistRequest result = new PlaylistRequest();
        result.setId(p.getId());
        result.setName(p.getName());
        result.setDescription(p.getDescription());
        return result;
    }

    public PlaylistEditResponse playlistToPlaylistEditResponse(Playlist p){
        PlaylistEditResponse result = new PlaylistEditResponse();
        result.setId(p.getId());
        result.setName(p.getName());
        result.setSongs(p.getSongs().size());
        return result;
    }
}
