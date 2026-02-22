package id.ac.ui.cs.advprog.eshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EshopApplication {

    public static void main(String[] args) {
        new EshopApplication().run(args);
    }

    void run(String[] args) {
        SpringApplication.run(EshopApplication.class, args);
    }

}
