package ir.mrmoein.quezapplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableElasticsearchRepositories("ir.mrmoein.quezapplication.repository.elastic")
@EnableJpaRepositories("ir.mrmoein.quezapplication.repository.jpa")
public class QuezApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuezApplication.class, args);
    }

}
