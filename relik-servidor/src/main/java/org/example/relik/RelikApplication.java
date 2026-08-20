package org.example.relik;

import org.example.relik.modelo.ModeloInterfaceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RelikApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(RelikApplication.class, args);
    }

    @Autowired
    private ModeloInterfaceImpl modeloInterfaceImpl;

    @Override
    public void run(String... args) throws Exception {
    }
}

