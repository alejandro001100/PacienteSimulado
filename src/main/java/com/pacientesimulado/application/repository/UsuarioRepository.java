package com.pacientesimulado.application.repository;

import com.pacientesimulado.application.data.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.List;

public interface UsuarioRepository extends MongoRepository<Usuario, String> {
    Optional<Usuario> findByCorreo(String correo);
    List<Usuario> findByRol(String rol);
}
