package education.next.one.ScreenMatch;

import education.next.one.ScreenMatch.model.DadosSerie;
import education.next.one.ScreenMatch.service.ConverteDados;
import education.next.one.ScreenMatch.service.RequisicaoAPI;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenMatchApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ScreenMatchApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        var requisicaoAPI = new RequisicaoAPI();
        var json = requisicaoAPI.obterDados("AAA");
        ConverteDados conversor = new ConverteDados();
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        System.out.println(dados);
    }
}
