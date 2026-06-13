package pl.krasmap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
// import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(scanBasePackages = {
        "pl.krasmap.common",
        "pl.krasmap.debug",
        "pl.krasmap.iam",
        "pl.krasmap.interaction",
        "pl.krasmap.krasnal",
        "pl.krasmap.submission",
})
// @SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@EnableScheduling
public class KrasmapApplication {

    public static void main(String[] args) {
        SpringApplication.run(KrasmapApplication.class, args);
    }

    public KrasmapApplication() {

    }

}