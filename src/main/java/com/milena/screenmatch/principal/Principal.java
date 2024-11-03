package com.milena.screenmatch.principal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.milena.screenmatch.model.DadosSerie;
import com.milena.screenmatch.model.DadosTemporada;
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
        //addDados.add(dados);
        Serie serie = new Serie(dados);
        repository.save(serie);
        System.out.println(dados);
    }

    private void listaSeries(){
        List<Serie> series = repository.findAll();
        //series = addDados.stream().map(d->new Serie(d)).collect(Collectors.toList());
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero));
        series.forEach(System.out::println);
    }

    private DadosSerie getDadosSerie() throws JsonProcessingException {
        System.out.println("Qual série que deseja pesquisar:");
        String serieEscolhida = leitor.nextLine();
        var json = consumo.obterDados(ENDERECO + serieEscolhida.replace(" ", "+") + API_KEY);
        dadosSerie = conversor.obterDados(json, DadosSerie.class);
        return dadosSerie;
    }

    private void getDadosTemporada() throws JsonProcessingException {
        DadosSerie dadosSerie = getDadosSerie();
        List<DadosTemporada> addTemporadas = new ArrayList<>();
        for (int i = 1; i <= dadosSerie.totalTemporadas(); i++) {
            var json = consumo.obterDados(ENDERECO + dadosSerie.titulo().replace(" ", "+") + "&season=" + i + API_KEY);
            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
            addTemporadas.add(dadosTemporada);
        }
        addTemporadas.forEach(System.out::println);
    }

        /*

         addTemporadas.forEach(t -> t.listaEpisodios().forEach(e -> System.out.println(e.titulo())));

         List<DadosEpisodio> avaliacaoEpisodios = addTemporadas.stream()
                .flatMap(t -> t.listaEpisodios()
                        .stream())
                .collect(Collectors.toList());


        List<DadosEpisodio> avaliacoes = avaliacaoEpisodios.stream().filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
                .limit(5)
                .collect(Collectors.toList());


        System.out.println("TOP 5");
        avaliacoes.forEach(System.out::println);

        List<Episodio> episodios = addTemporadas.stream()
                .flatMap(t -> t.listaEpisodios()
                        .stream().map(e -> new Episodio(t.numero(), e))).collect(Collectors.toList());

        episodios.forEach(System.out::println);

        System.out.println("Qual ano quer pesquisar:");
        int ano = leitor.nextInt();
        leitor.nextLine();

        LocalDate anoBusca = LocalDate.of(ano,1,1);

        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        var episodiosAno = episodios.stream().filter(e-> e.getDataLancamento() !=null && e.getDataLancamento().isAfter(anoBusca)).collect(Collectors.toList());

      episodiosAno.forEach(d-> System.out.println(
              "Temporada = " + d.getTemporada() +
                      "Episódio = " + d.getTitulo() +
                      "Data de Lançamento = " + d.getDataLancamento().format(formatador)
      ));


        System.out.println("Digite uma parte do título do espisódio que deseja consultar: ");
        var trechoTitulo = leitor.nextLine();

        Optional<Episodio> tituloBuscado = episodios.stream().filter(e->e.getTitulo().toLowerCase().contains(trechoTitulo.toLowerCase())).findFirst();

        if(tituloBuscado.isPresent()){
            System.out.println("Informações do Episódio" + '\'' +
                    " Episódio: " + tituloBuscado.get().getTitulo() +
                    "Temporada: " + tituloBuscado.get().getTemporada()
            );
        }

        Map<Integer, Double> avaliacoesTemporadas = episodios.stream().filter(e->e.getAvaliacao()>0)
        .collect(Collectors.groupingBy(Episodio::getTemporada,Collectors.averagingDouble(Episodio::getAvaliacao)));

        System.out.println(avaliacoesTemporadas);


        DoubleSummaryStatistics est = episodios.stream().filter(e->e.getAvaliacao()>0)
                .collect(Collectors.summarizingDouble(Episodio::getAvaliacao));

        System.out.println("Média de Avaliação: " + est.getAverage());
        System.out.println("Melhor Avaliação: " + est.getMax());
        System.out.println("Pior Avaliação: " + est.getMin());
        System.out.println("Toal de episódios contabilizados: " + est.getCount());

    }*/
}


