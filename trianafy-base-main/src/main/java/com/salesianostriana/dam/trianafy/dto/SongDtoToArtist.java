package com.salesianostriana.dam.trianafy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class SongDtoToArtist {

        private Long id;
        private String title;
        private String artist;
        private String year;
        private String album;
    }

