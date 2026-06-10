package pl.krasmap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class KrasmapApplication {

    public static void main(String[] args) {
        // Ta linijka wybudza cały framework Spring Boot i stawia lokalny serwer
        SpringApplication.run(KrasmapApplication.class, args);
    }

    public KrasmapApplication() {

    }

}