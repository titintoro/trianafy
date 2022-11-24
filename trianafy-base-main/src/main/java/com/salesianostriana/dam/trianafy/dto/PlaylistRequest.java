package com.salesianostriana.dam.trianafy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlaylistRequest {

    @Id
    @GeneratedValue
    private Long id;


    private String name;
    private String description;
}
