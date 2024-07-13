package com.alura.Literatura.principal;

import com.alura.Literatura.services.ConsumoApi;
import com.alura.Literatura.services.ConvierteDatos;
import com.alura.Literatura.model.*;
import com.alura.Literatura.repository.IAutorRepository;
import com.alura.Literatura.repository.ILibroRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Principal {
    private static final Logger logger = LoggerFactory.getLogger(Principal.class);
    private final Scanner teclado = new Scanner(System.in);
    private final ConsumoApi consumoApi = new ConsumoApi();
    private final String URL_BASE = "https://gutendex.com/books/?search=";
    private final ConvierteDatos conversor = new ConvierteDatos();

    private final ILibroRepository libroRepository;
    private final IAutorRepository autorRepository;

    public Principal(ILibroRepository libroRepository, IAutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public void menu() {
        int option = -1;

        while (option != 0) {
            mostrarMenu();

            try {
                System.out.print("Elige una opción: ");
                if (teclado.hasNextInt()) {
                    option = teclado.nextInt();
                    teclado.nextLine(); // Consumir el salto de línea
                } else {
                    System.out.println("Error: Por favor ingresa un número válido.");
                    teclado.next(); // Limpiar la entrada no válida
                    continue;
                }

                ejecutarOpcion(option);
            } catch (InputMismatchException e) {
                System.out.println("Error: Por favor ingresa un número válido.");
                teclado.next(); // Limpiar la entrada no válida
            }
        }
    }

    private void mostrarMenu() {
        System.out.println("1- Buscar libro por título");
        System.out.println("2- Listar libros registrados");
        System.out.println("3- Listar autores registrados");
        System.out.println("4- Listar autores vivos en un determinado año");
        System.out.println("5- Listar libros por idioma");
        System.out.println("0- Salir");
    }

    private void ejecutarOpcion(int option) {
        switch (option) {
            case 1:
                buscarLibroPorTitulo();
                break;
            case 2:
                listarLibrosRegistrados();
                break;
            case 3:
                listarAutoresRegistrados();
                break;
            case 4:
                listarAutoresVivosEnAno();
                break;
            case 5:
                listarLibrosPorIdioma();
                break;
            case 0:
                System.out.println("Saliendo...");
                break;
            default:
                System.out.println("Opción no válida. Intenta de nuevo.");
                break;
        }
    }

    private void buscarLibroPorTitulo() {
        try {
            buscarLibro();
        } catch (IOException e) {
            System.out.println("Error al buscar el libro: " + e.getMessage());
            logger.error("Error al buscar el libro: ", e);
        }
    }

    private void buscarLibro() throws IOException {
        System.out.println("Ingrese el título del libro: ");
        var nombreLibro = teclado.nextLine();
        var nombreLibroEncoded = URLEncoder.encode(nombreLibro, StandardCharsets.UTF_8.toString());
        var json = consumoApi.obtenerDatos(URL_BASE + nombreLibroEncoded.replace(" ", "+"));
        List<DatosLibro> datosLibro = conversor.obtenerDatos(json, Datos.class).results();
        Optional<DatosLibro> libroBuscado = datosLibro.stream()
                .filter(l -> l.titulo().toLowerCase().contains(nombreLibro.toLowerCase()))
                .findFirst();

        if (libroBuscado.isPresent()) {
            var libroEncontrado = libroBuscado.get();

            // Verificación si el libro ya está en la base de datos
            Libro libroExistente = libroRepository.findByTituloIgnoreCase(libroEncontrado.titulo());

            if (libroExistente != null) {
                System.out.println("El libro " + libroExistente.getTitulo() + " ya está registrado");
            } else {
                var libro = new Libro(libroEncontrado);
                libroRepository.save(libro);
                System.out.println(libro);
            }
        } else {
            System.out.println("Libro no encontrado");
        }
    }

    private <T> void listarElementos(List<T> elementos, String tipoElemento) {
        if (elementos.isEmpty()) {
            System.out.println("No se han encontrado " + tipoElemento + " registrados");
        } else {
            for (T elemento : elementos) {
                System.out.println(elemento.toString());
            }
        }
    }

    private void listarLibrosRegistrados() {
        try {
            listarElementos(libroRepository.findAll(), "libros");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            logger.error("Error al listar los libros: ", e);
        }
    }

    private void listarAutoresRegistrados() {
        try {
            listarElementos(autorRepository.findAll(), "autores");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            logger.error("Error al listar los autores: ", e);
        }
    }

    private void listarAutoresVivosEnAno() {
        System.out.println("Ingrese el año en el que estuvo vivo su autor: ");
        var ano = teclado.nextInt();
        teclado.nextLine(); // Consumir el salto de línea
        try {
            List<Autor> autores = autorRepository.findByFechaNacimientoLessThanEqualAndFechaFallecimientoGreaterThanEqual(ano, ano);
            listarElementos(autores, "autores vivos en el año " + ano);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            logger.error("Error al listar autores vivos en el año " + ano + ": ", e);
        }
    }

    private void listarLibrosPorIdioma() {
        System.out.println("""
                Ingrese el idioma para buscar los libros
                es - español
                en - inglés
                fr - francés
                pt - portugués
                """);
        String idioma = teclado.nextLine();
        try {
            List<Libro> libros = libroRepository.findByIdioma(idioma);
            if (libros.isEmpty()) {
                System.out.println("No se han encontrado libros en idioma " + idioma);
            } else {
                listarElementos(libros, "libros en idioma " + idioma);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            logger.error("Error al listar libros por idioma: ", e);
        }
    }
}