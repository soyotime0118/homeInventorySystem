package me.mason.management;

import me.mason.config.EnableStubAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableStubAutoConfiguration
@SpringBootApplication
public class HomeInventoryManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(HomeInventoryManagementApplication.class, args);
    }
}
