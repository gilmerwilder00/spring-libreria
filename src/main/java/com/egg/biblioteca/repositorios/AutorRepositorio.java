package com.egg.biblioteca.repositorios;
import com.egg.biblioteca.entidades.Autor;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface AutorRepositorio extends JpaRepository<Autor, String> {
    
}
