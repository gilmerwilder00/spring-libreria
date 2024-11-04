package com.egg.biblioteca.servicios;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.egg.biblioteca.entidades.Autor;
import com.egg.biblioteca.entidades.Editorial;
import com.egg.biblioteca.entidades.Libro;
import com.egg.biblioteca.excepciones.MiException;
import com.egg.biblioteca.repositorios.LibroRepositorio;
import com.egg.biblioteca.repositorios.AutorRepositorio;
import com.egg.biblioteca.repositorios.EditorialRepositorio;

@Service
public class LibroServicio {

    @Autowired
    private LibroRepositorio libroRepositorio;

    @Autowired
    private AutorRepositorio autorRepositorio;

    @Autowired
    private EditorialRepositorio editorialRepositorio;

    @Transactional
    public void crearLibro(Long isbn, String titulo, Integer ejemplares, String idAutor, String idEditorial)
            throws MiException {

        validar(isbn, titulo, ejemplares, idAutor, idEditorial);

        Autor autor = autorRepositorio.findById(idAutor).get();

        Editorial editorial = editorialRepositorio.findById(idEditorial).get();

        Libro libro = new Libro();

        libro.setIsbn(isbn);
        libro.setTitulo(titulo);
        libro.setEjemplares(ejemplares);
        libro.setAlta(new Date());
        libro.setAutor(autor);
        libro.setEditorial(editorial);

        libroRepositorio.save(libro);

    }

    @Transactional(readOnly = true)
    public List<Libro> listarLibros() {

        List<Libro> libros = new ArrayList<>();

        libros = libroRepositorio.findAll();
        return libros;
    }

    @Transactional
    public void modificarLibro(String nombre, Long isbn, Integer ejemplares, String idAutor, String idEditorial)
            throws MiException {

        validar(isbn, nombre, ejemplares, idAutor, idEditorial);

        Optional<Libro> respuesta = libroRepositorio.findById(isbn);

        if (respuesta.isPresent()) {
            Libro libro = respuesta.get();
            libro.setTitulo(nombre);

            Optional<Autor> respuestaAutor = autorRepositorio.findById(idAutor);
            if (respuestaAutor.isPresent()) {
                libro.setAutor(respuestaAutor.get());
            }

            Optional<Editorial> respuestaEditorial = editorialRepositorio.findById(idEditorial);
            if (respuestaEditorial.isPresent()) {
                libro.setEditorial(respuestaEditorial.get());
            }

            libro.setEjemplares(ejemplares);

            libroRepositorio.save(libro);
        }
    }

    private void validar(Long isbn, String titulo, Integer ejemplares, String idAutor, String idEditorial)
            throws MiException {
        if (isbn == null || isbn <= 0) {
            throw new MiException("El ISBN no puede ser nulo o negativo");
        }
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new MiException("El título no puede ser nulo o vacío");
        }
        if (ejemplares == null || ejemplares <= 0) {
            throw new MiException("La cantidad de ejemplares debe ser mayor que cero");
        }
        if (idAutor == null || idAutor.trim().isEmpty()) {
            throw new MiException("El ID del autor no puede ser nulo o vacío");
        }
        if (idEditorial == null || idEditorial.trim().isEmpty()) {
            throw new MiException("El ID de la editorial no puede ser nulo o vacío");
        }
    }

}
