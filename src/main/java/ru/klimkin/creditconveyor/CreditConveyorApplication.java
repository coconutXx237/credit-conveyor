package ru.klimkin.creditconveyor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:temp.properties")
@Slf4j
@SpringBootApplication
public class CreditConveyorApplication {
    public static void main(String[] args) {
        SpringApplication.run(CreditConveyorApplication.class, args);
        log.info("*** *** *** The app is running! *** *** ***\n");
    }
}
