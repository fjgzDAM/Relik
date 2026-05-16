package org.example.arquealia;

import org.example.arquealia.dominio.*;
import org.example.arquealia.modelo.ModeloInterfaceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

@SpringBootApplication
public class ArquealiaApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ArquealiaApplication.class, args);
    }

    @Autowired
    private ModeloInterfaceImpl modeloInterfaceImpl;

    @Override
    public void run(String... args) throws Exception {
        // Crear museos por cada época disponible (solo si no existen)
        String[] epocas = {"Paleolitico", "Neolitico", "Calcolitico"};
        for (String epoca : epocas) {
            String nombre = "Museo de " + epoca;
            // Evitar insertar duplicados si ya existe un museo con ese nombre
            boolean existeMuseo = modeloInterfaceImpl.listarMuseos().stream()
                    .anyMatch(m -> m.getNombre() != null && m.getNombre().equals(nombre));
            if (!existeMuseo) {
                Museo museo = new Museo(nombre, epoca);
                modeloInterfaceImpl.insertar(museo);
            }
        }

        // Crear yacimientos (solo si no existen) para evitar claves duplicadas
        String[] nombresYacimiento = {"La Postiga", "El Pozo", "Paleta", "Cueva Negra", "Molinos de Papel", "Altamira", "Atapuerca"};
        for (String nombre : nombresYacimiento) {
            // comprobar existencia por nombre
            if (modeloInterfaceImpl.consultarYacimientoPorNombre(nombre) == null) {
                Yacimiento yacimiento = new Yacimiento();
                yacimiento.setNombre(nombre);
                modeloInterfaceImpl.insertar(yacimiento);
            }
        }

        String[] nombres = {"Juan", "Pedro", "Luis", "Ana", "Maria", "Isabel", "Carmen", "Jose", "Antonio", "Manuel"};
        String[] emails = {"@gmail.com", "@hotmail.com", "@outlook.com", "@yahoo.com", "@protonmail.com"};
        String[] tipologias = {"Herramienta", "Estructura", "Animal", "Vegetal", "Humano"};

        // Crear arqueólogos y registrar hallazgos
        for (int i = 1; i <= 15; i++) {
            String nombre = nombres[(int) (Math.random() * nombres.length)];
            boolean existeEmail=true;
            String email;
            do{
                email = nombre + emails[(int) (Math.random() * emails.length)];
                if(modeloInterfaceImpl.consultarArqueologoPorCorreo(email) == null) {
                    existeEmail = false;
                }
                }while (existeEmail);
            String contrasena = "1234";
            Arqueologo arqueologo = new Arqueologo(nombre, email, contrasena);
            arqueologo = modeloInterfaceImpl.insertar(arqueologo);
            System.out.println("\n ARQUEÓLOGO " + i + " registrado: " + arqueologo);

            // Registrar entre 3 y 5 hallazgos por arqueólogo
            int numHallazgos = 3 + (int) (Math.random() * 3);
            for (int j = 1; j <= numHallazgos; j++) {

                // Crear resto material

                String nombreResto = "Resto " + j;
                String epoca = epocas[(int) (Math.random() * epocas.length)];
                String tipologia = tipologias[(int) (Math.random() * tipologias.length)];
                RestoMaterial restoMaterial = new RestoMaterial(nombreResto, epoca, tipologia);
                modeloInterfaceImpl.asignarMuseo(restoMaterial);
                modeloInterfaceImpl.insertar(restoMaterial);

                // Asignar museo al resto material
                restoMaterial = modeloInterfaceImpl.asignarMuseo(restoMaterial);
                restoMaterial = modeloInterfaceImpl.insertar(restoMaterial);
                System.out.println("\n RESTO MATERIAL registrado: " + restoMaterial);


                // Crear hallazgo
                Hallazgo hallazgo = new Hallazgo();
                hallazgo.setArqueologo(arqueologo);
                hallazgo.setYacimiento(modeloInterfaceImpl.consultarYacimientoPorNombre(nombresYacimiento[(int) (Math.random() * nombresYacimiento.length)]));
                hallazgo.setRestoMaterial(restoMaterial);
                hallazgo.setFechaHallazgo(LocalDateTime.now().minusDays((int) (Math.random() * 365)));
                modeloInterfaceImpl.insertar(hallazgo);
            }
        }
        System.out.println("Listado de arqueólogos, museos, yacimientos, restos materiales y hallazgos:");
        System.out.println(modeloInterfaceImpl.listarArqueologos());
        System.out.println();
        System.out.println(modeloInterfaceImpl.listarMuseos());
        System.out.println();
        System.out.println(modeloInterfaceImpl.listarYacimientos());
        System.out.println();
        System.out.println(modeloInterfaceImpl.listarRestosMateriales());
        System.out.println();
        System.out.println(modeloInterfaceImpl.listarHallazgos());
        System.out.println();
        System.out.println("Top 10 arqueólogos por hallazgos:");
        System.out.println(modeloInterfaceImpl.topArqueologosPorHallazgos());
        System.out.println();
        System.out.println("Resumen de hallazgos por yacimiento en los últimos 120 días:");
        System.out.println(modeloInterfaceImpl.resumenHallazgosYacimientoRangoFecha(1, LocalDate.now().minusDays(120), LocalDate.now()));
        System.out.println();
        System.out.println("Resumen de hallazgos por fecha:");
        ArrayList<Hallazgo> hallazgosFecha = modeloInterfaceImpl.resumenHallazgosFecha(LocalDate.now().minusDays(120));
        if (hallazgosFecha.isEmpty()) {
            System.out.println("No se han encontrado hallazgos en la fecha indicada.");
        } else {
            hallazgosFecha.forEach(System.out::println);
        }
        System.out.println();
        System.out.println("Resumen de hallazgos por yacimiento:");
        System.out.println(modeloInterfaceImpl.resumenHallazgosYacimiento(2));

    }
}

