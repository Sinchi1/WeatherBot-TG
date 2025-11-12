package org.truskovski;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.truskovski.store.repository.WeatherRepository;

@SpringBootApplication
//@EnableJpaRepositories(basePackages = "org.truskovski.store.*")
public class BackEndMain {
    public static void main(String[] args) {
        SpringApplication.run(BackEndMain.class, args);
    }
}
