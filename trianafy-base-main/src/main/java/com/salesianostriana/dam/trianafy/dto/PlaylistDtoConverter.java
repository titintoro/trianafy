package com.salesianostriana.dam.trianafy.dto;

import com.salesianostriana.dam.trianafy.model.Playlist;
import org.springframework.stereotype.Component;

@Component
public class PlaylistDtoConverter {

    public Playlist PlaylistRequestToPlaylist(PlaylistRequest p) {
        return Playlist.builder()
                .name(p.getName())
                .id(p.getId())
                .description(p.getDescription())
                .build();
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

    public PlaylistRequest playlistToPlaylistResponseToPlaylistRequest(Playlist p) {
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
