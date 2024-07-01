package education.next.one.ScreenMatch;

import education.next.one.ScreenMatch.main.Main;
import education.next.one.ScreenMatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenMatchApplicationWeb {

    public static void main(String[] args) {
        SpringApplication.run(ScreenMatchApplicationWeb.class, args);
    }
}
