package br.senac.sp.tads.dsw.exemplo6.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginDTO (
    @NotBlank String login, @NotBlank  String senha){

    }
    

