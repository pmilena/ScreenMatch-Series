package com.milena.screenmatch.principal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.milena.screenmatch.model.DadosSerie;
import com.milena.screenmatch.model.DadosTemporada;
import com.milena.screenmatch.model.Episodio;
import com.milena.screenmatch.model.Serie;
import com.milena.screenmatch.repository.SerieRepository;
import com.milena.screenmatch.service.ConsumoApi;
import com.milena.screenmatch.service.ConverteDados;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private Scanner leitor = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private final String ENDERECO = "https://omdbapi.com/?t=";
    private final String API_KEY = "&apikey=8ffb465d";
    private ConverteDados conversor = new ConverteDados();
    private DadosSerie dadosSerie;

    private List<DadosSerie> addDados = new ArrayList<>();

    private SerieRepository repository;

    private List<Serie> series = new ArrayList<>();

    public Principal(SerieRepository repository) {
        this.repository = repository;
    }


    public void ExibeMenu() throws JsonProcessingException {

        var opcao = -1;

        while (opcao != 0) {

            System.out.println("""
                
                1- Buscar séries
                2- Buscar episódios
                3- Listar séries buscadas
                4- Buscar série por título
                5- Buscar série por Ator
                0- Sair
                
                """);
            opcao = leitor.nextInt();
            leitor.nextLine();

            switch (opcao) {
                case 1:
                    buscarSerie();
                    break;

                case 2:
                    getDadosTemporada();
                    break;

                case 3:
                    listaSeries();
                    break;

                case 4:
                    buscarSeriePorTitulo();
                    break;

                case 5:
                    buscarSeriePorAtor();
                    break;

                case 0:
                    System.out.println("Saindo...");
                    break;

                default:
                    System.out.println("Opção inválida");
            }

        }
    }

    private void buscarSerie() throws JsonProcessingException {
        DadosSerie dados = getDadosSerie();
        Serie serie = new Serie(dados);

        Optional<Serie> serieExistente = repository.findByTituloContainingIgnoreCase(serie.getTitulo());
        if (serieExistente.isEmpty()) {
            repository.save(serie);
        }

        System.out.println(dados);
    }

    private void listaSeries(){
        series = repository.findAll();
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero));

        System.out.println(series);
    }

    private DadosSerie getDadosSerie() throws JsonProcessingException {
        //listaSeries();
        System.out.println("Qual série que deseja pesquisar:");
        String serieEscolhida = leitor.nextLine();
        var json = consumo.obterDados(ENDERECO + serieEscolhida.replace(" ", "+") + API_KEY);
        dadosSerie = conversor.obterDados(json, DadosSerie.class);
        return dadosSerie;
    }

    private void getDadosTemporada() throws JsonProcessingException {
        //DadosSerie dadosSerie = getDadosSerie();
        System.out.println("Digite o nome da série que deseja pesquisar: ");
        var nomeSerie = leitor.nextLine();

        Optional<Serie> serie = repository.findByTituloContainingIgnoreCase(nomeSerie);

        if (serie.isPresent()) {

            var serieEncontrada = serie.get();

            List<DadosTemporada> addTemporadas = new ArrayList<>();
            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                var json = consumo.obterDados(ENDERECO + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
                addTemporadas.add(dadosTemporada);
            }
            addTemporadas.forEach(System.out::println);

            List<Episodio> episodios=  addTemporadas.stream()
                    .flatMap(d->d.listaEpisodios().stream()
                            .map(e -> new Episodio(d.numero(),e)))
                    .collect(Collectors.toList());

            serieEncontrada.setEpisodios(episodios);

            repository.save(serieEncontrada);
        } else {
            System.out.println("Série não encontrada.");
        }

    }
    private void buscarSeriePorTitulo(){
        System.out.println("Digite o nome da série que deseja pesquisar: ");
        var nomeSerie = leitor.nextLine();

        Optional<Serie> serie = repository.findByTituloContainingIgnoreCase(nomeSerie);

        if (serie.isPresent()){
            System.out.println("Dados da série: " + serie.get());
        }else{
            System.out.println("Série não encontrada.");
        }

    }
    public void buscarSeriePorAtor() {
        System.out.println("Digite o nome do ator que deseja buscar: ");
        var nomeAtor = leitor.nextLine();

        System.out.println("A partir de qual nota deseja pesquisar? ");
        var avaliacao = leitor.nextDouble();
        leitor.nextLine();

        List<Serie> series = repository.findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(nomeAtor,avaliacao);
        System.out.println("Séries em que " + nomeAtor + " trabalhou:");
        series.forEach(s-> System.out.println(s.getTitulo() + " avaliação: " + s.getAvaliacao()));

        }

    }

