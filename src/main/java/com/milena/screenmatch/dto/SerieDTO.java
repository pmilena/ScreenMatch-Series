package com.milena.screenmatch.dto;

import com.milena.screenmatch.model.Categoria;
import jakarta.persistence.*;

public record SerieDTO(Long id, String titulo, Integer totalTemporadas, Double avaliacao, Categoria genero, String atores, String poster, String sinopse
) {
}
