package education.next.one.ScreenMatch.main;

import education.next.one.ScreenMatch.model.*;
import education.next.one.ScreenMatch.repository.SerieRepository;
import education.next.one.ScreenMatch.service.ConverteDados;
import education.next.one.ScreenMatch.service.RequisicaoAPI;

import java.util.*;
import java.util.stream.Collectors;

public class Main {

    private Scanner input = new Scanner(System.in);
    private RequisicaoAPI requisicaoAPI = new RequisicaoAPI();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=1420d10d";
    private List<DadosSerie> dadosSeries = new ArrayList<>();
    private SerieRepository repositorio;
    private List<Serie> series = new ArrayList<>();
    private Optional<Serie> serieBusca;

    public Main(SerieRepository repositorio) {
        this.repositorio = repositorio;
    }

    public static int validarOpcao(Scanner input) {
        while (!input.hasNextInt()) {
            System.out.println("Por favor, insira um valor númerico válido:");
            input.next();
        }
        return input.nextInt();
    }


    public void exibeMenu() {


        var opcao = -1;

        while (opcao != 0) {
            var menu = """
                    1- Buscar série
                    2- Buscar episódios
                    3- Buscar série por título
                    4- Listar séries buscadas
                    5- Buscar séries por ator
                    6- Buscar séries por genero
                    7- Buscar séries por total de temporadas
                    8- Buscar episódio por trecho do nome
                    9- Buscar episódio por data
                    10- Top 5 episódios por série
                    11- Top 5 séries
                                        
                    0- Sair
                    """;
            System.out.println(menu);
            opcao = validarOpcao(input);
            input.nextLine();

            switch (opcao) {
                case 1:
                    buscarSerie();
                    break;
                case 2:
                    buscarEpisodio();
                    break;
                case 3:
                    buscarSeriePorTitulo();
                    break;
                case 4:
                    listarSeriesBuscadas();
                    break;
                case 5:
                    buscarSeriePorAtor();
                    break;
                case 6:
                    buscarSeriePorGenero();
                    break;
                case 7:
                    buscarSeriePorTotalTemporadas();
                    break;
                case 8:
                    buscarEpisodioPorTrecho();
                    break;
                case 9:
                    buscarEpisodioPorData();
                    break;
                case 10:
                    topEpisodiosPorSerie();
                    break;
                case 11:
                    buscarTop5Series();
                    break;
                case 0:
                    System.out.println("Finalizando aplicação! Obrigado por usar nossos serviços!");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }

    }

    private void buscarEpisodioPorData() {
        buscarSeriePorTitulo();
        if (serieBusca.isPresent()) {
            System.out.println("Digite o ano desejado para busca");
            var anoLancamento = input.nextInt();
            input.nextLine();

            Serie serie = serieBusca.get();
            List<Episodio> episodiosAno = repositorio.episodiosPorSerieEData(serie, anoLancamento);
            episodiosAno.forEach(System.out::println);
        }
    }

    private void topEpisodiosPorSerie() {
        buscarSeriePorTitulo();
        if (serieBusca.isPresent()) {
            Serie serie = serieBusca.get();
            List<Episodio> topEpisodios = repositorio.topEpisodiosPorSerie(serie);
            topEpisodios.forEach(e ->
                    System.out.printf("Série: %s | Temporada %s - Episódio %s - %s | Nota: %s\n", e.getSerie().getTitulo(), e.getTemporada(), e.getNumeroEpisodio(), e.getTitulo(), e.getAvaliacao()));
        }
    }

    private void buscarEpisodioPorTrecho() {
        System.out.println("Qual o nome do episódio para busca?");
        var trechoEpisodio = input.nextLine();
        List<Episodio> episodiosEncontrados = repositorio.episodiosPorTrecho(trechoEpisodio);
        episodiosEncontrados.forEach(e ->
                System.out.printf("Série: %s | Temporada %s - Episódio %s - %s\n", e.getSerie().getTitulo(), e.getTemporada(), e.getNumeroEpisodio(), e.getTitulo()));
    }

    private void buscarSeriePorGenero() {
        System.out.println("Digite o genero deseejado para buscar as séries");
        var genero = input.nextLine();
        Categoria categoria = Categoria.fromPortugues(genero);
        List<Serie> seriesPorCategoria = repositorio.findByGenero(categoria);
        System.out.println("Séries da categaoria: " + genero);
        seriesPorCategoria.forEach(System.out::println);
    }

    private void buscarSeriePorTotalTemporadas() {
        System.out.println("Digite o total máximo de temporadas que deseja:");
        var totalTemporadas = input.nextInt();
        input.nextLine();

        System.out.println("Digite a nota mínima para a série:");
        var avaliacao = input.nextDouble();

        List<Serie> seriesPorTemporada = repositorio.seriesPorTemporadaEAvaliacao(totalTemporadas, avaliacao);

        System.out.println("Séries com no máximo " + totalTemporadas + " e com nota igual ou maior que " + avaliacao + ":");
        seriesPorTemporada.forEach(s -> System.out.println(s.getTitulo() + " | Nota: " + s.getAvaliacao()));
    }

    private void buscarTop5Series() {
        List<Serie> seriesTop = repositorio.findTop5ByOrderByAvaliacaoDesc();
        seriesTop.forEach(System.out::println);
    }

    private void buscarSeriePorTitulo() {
        System.out.println("Escolha uma série pelo seu nome: ");
        var nomeSerie = input.nextLine();
        serieBusca = repositorio.findByTituloContainingIgnoreCase(nomeSerie);

        if (serieBusca.isPresent()) {
            System.out.println("Dados da série" + serieBusca.get());
        } else {
            System.out.println("Série não encontrada! )=");
        }
    }

    private void buscarSeriePorAtor() {
        System.out.println("Digite o nome do ator que deseja buscar a filmografia: ");
        var nomeAtor = input.nextLine();
        System.out.println("Digite a nota mínima para a série:");
        var avaliacao = input.nextDouble();
        List<Serie> seriesEncontradas = repositorio.findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(nomeAtor, avaliacao);
        System.out.println("Séries em que " + nomeAtor + " trabalhou com nota igual ou maior que " + avaliacao + ":");
        seriesEncontradas.forEach(s -> System.out.println(s.getTitulo() + " | Nota: " + s.getAvaliacao()));
    }

    private void buscarSerie() {
        DadosSerie dados = getDadosSerie();
        Serie serie = new Serie(dados);
        repositorio.save(serie);
        System.out.println(dados);
    }

    private DadosSerie getDadosSerie() {
        System.out.println("Digite o nome da série para busca");
        var nomeSerie = input.nextLine();
        var json = requisicaoAPI.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        return dados;
    }

    private void buscarEpisodio() {

        listarSeriesBuscadas();
        System.out.println("Escolha uma das série já buscada anteriormente para encontrar os seus episódios:");
        var nomeSerie = input.nextLine();


        Optional<Serie> serie = repositorio.findByTituloContainingIgnoreCase(nomeSerie);

        if (serie.isPresent()) {

            var serieEncontrada = serie.get();
            List<DadosTemporada> temporadas = new ArrayList<>();

            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                var json = requisicaoAPI.obterDados(ENDERECO + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + "&apikey=1420d10d");
                DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
                temporadas.add(dadosTemporada);
            }
            temporadas.forEach(System.out::println);

            List<Episodio> episodios = temporadas.stream()
                    .flatMap(d -> d.episodios().stream()
                            .map(e -> new Episodio(d.numero(), e)))
                    .collect(Collectors.toList());
            serieEncontrada.setEpisodios(episodios);
            repositorio.save(serieEncontrada);
        } else {
            System.out.println("Série não localizada! )=");
        }
    }

    private void listarSeriesBuscadas() {


        series = repositorio.findAll();

        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }
}


