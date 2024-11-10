package com.milena.screenmatch.service;

import com.milena.screenmatch.dto.EpisodioDTO;
import com.milena.screenmatch.dto.SerieDTO;
import com.milena.screenmatch.model.Categoria;
import com.milena.screenmatch.model.Episodio;
import com.milena.screenmatch.model.Serie;
import com.milena.screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SerieService {

    @Autowired
    private SerieRepository repository;

    public List<SerieDTO> obterSeries(){
        return converteDados(repository.findAll());

    }

    public List<SerieDTO> obterTop5Series() {
        return converteDados(repository.findTop5ByOrderByAvaliacaoDesc());
    }

    public List<SerieDTO> obterLancamentos() {
        return converteDados(repository.encontrarEpisodiosMaisRecentes());
    }

    public List<SerieDTO> converteDados(List <Serie> series){
        return series.stream()
                .map(s->new SerieDTO(s.getId(),s.getTitulo(),s.getTotalTemporadas(),s.getAvaliacao(),s.getGenero(),s.getAtores(),s.getPoster(),s.getSinopse()))
                .collect(Collectors.toList());
    }

    public SerieDTO obterPorId(Long id) {

        Optional<Serie> serie = repository.findById(id);

        if (serie.isPresent()){
            Serie s = serie.get();
            return new SerieDTO(s.getId(),s.getTitulo(),s.getTotalTemporadas(),s.getAvaliacao(),s.getGenero(),s.getAtores(),s.getPoster(),s.getSinopse());
        }else{
            return null;
        }
    }

    public List <EpisodioDTO> obterTodasAsTemporadas(Long id) {
        Optional<Serie> serie = repository.findById(id);

        if (serie.isPresent()){
            Serie s = serie.get();
        List<EpisodioDTO> episodios = s.getEpisodios().stream().map(e->new EpisodioDTO(e.getTitulo(),e.getNumeroEpisodio(),e.getTemporada())).collect(Collectors.toList());
        return episodios;
        }else{
            return null;
        }
    }

    public List<EpisodioDTO> obterEpisodiosPorTemporadas(Long id, Long numero) {
        return repository.obterEpisodiosPorTemporada(id,numero).stream().map(e->new EpisodioDTO(e.getTitulo(),e.getNumeroEpisodio(),e.getTemporada())).collect(Collectors.toList());
        }

    public List<SerieDTO> obterSeriePorGenero(String nomeCategoria) {
        Categoria categoria = Categoria.fromPortugues(nomeCategoria);

        return converteDados(repository.findByGenero(categoria));
    }
}

