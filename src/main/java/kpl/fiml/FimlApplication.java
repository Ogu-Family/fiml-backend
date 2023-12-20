package kpl.fiml;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class FimlApplication {

    public static void main(String[] args) {
        SpringApplication.run(FimlApplication.class, args);
    }
}
