package education.next.one.ScreenMatch.dto;

import education.next.one.ScreenMatch.model.Categoria;

public record SerieDTO(Long id,
        String titulo,
        Integer totalTemporadas,
        Categoria genero,
        Double avaliacao,
        String sinopse,
        String atores,
        String poster) {

}
