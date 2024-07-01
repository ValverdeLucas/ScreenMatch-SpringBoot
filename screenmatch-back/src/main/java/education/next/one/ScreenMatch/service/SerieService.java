package education.next.one.ScreenMatch.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import education.next.one.ScreenMatch.dto.EpisodioDTO;
import education.next.one.ScreenMatch.dto.SerieDTO;
import education.next.one.ScreenMatch.model.Categoria;
import education.next.one.ScreenMatch.model.Serie;
import education.next.one.ScreenMatch.repository.SerieRepository;

@Service
public class SerieService {

    @Autowired
    private SerieRepository repositorio;

    public List<SerieDTO> obterTodasAsSeries() {
        return converteDados(repositorio.findAll());
    }

    public List<SerieDTO> obterTop5Series() {
        return converteDados(repositorio.findTop5ByOrderByAvaliacaoDesc());
    }

    private List<SerieDTO> converteDados(List<Serie> series) {
        return series.stream()
                .map(s -> new SerieDTO(s.getId(), s.getTitulo(), s.getTotalTemporadas(), s.getGenero(),
                        s.getAvaliacao(), s.getSinopse(), s.getAtores(), s.getPoster()))
                .collect(Collectors.toList());
    }

    public List<SerieDTO> obterLancamentos() {
        return converteDados(repositorio.lancamentosMaisRecentes());
    }

    public SerieDTO obterPorID(Long id) {
         Optional<Serie> serie = repositorio.findById(id);

        if (serie.isPresent()) {
            Serie s = serie.get();
            return new SerieDTO(s.getId(), s.getTitulo(), s.getTotalTemporadas(), s.getGenero(), s.getAvaliacao(), s.getSinopse(), s.getAtores(), s.getPoster());
        }
        return null;
    }

    public List<EpisodioDTO> obterTodasAsTemporadas(Long id) {
        Optional<Serie> serie = repositorio.findById(id);

        if (serie.isPresent()) {
            Serie s = serie.get();
            return s.getEpisodios().stream()
            .map(e -> new EpisodioDTO(e.getTemporada(), e.getNumeroEpisodio(), e.getTitulo()))
            .collect(Collectors.toList());
        }
        return null;
    }

    public List<EpisodioDTO> obterTemporadasPorNumero(Long id, Long numero) {
      return repositorio.obterEpisodiosPorTemporada(id, numero)
      .stream()
      .map(e -> new EpisodioDTO(e.getTemporada(), e.getNumeroEpisodio(), e.getTitulo()))
      .collect(Collectors.toList());
    }

    public List<SerieDTO> obterSeriesPorCategoria(String categoria) {
       Categoria generoSerie = Categoria.fromPortugues(categoria);
       return converteDados(repositorio.findByGenero(generoSerie));
    }
}