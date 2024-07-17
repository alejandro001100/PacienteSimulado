package com.pacientesimulado.application.services;

import com.pacientesimulado.application.data.Usuario;
import com.pacientesimulado.application.repository.UsuarioRepository;
import com.pacientesimulado.application.repository.ActorRepository;
import com.pacientesimulado.application.repository.DoctorRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ActorRepository actorRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    public Usuario registrarUsuario(Usuario usuario) {
        if (usuarioRepository.findByCorreo(usuario.getCorreo()).isPresent()) {
            throw new RuntimeException("El correo ya está en uso");
        }
        return usuarioRepository.save(usuario);
    }

    public Usuario actualizarUsuario(String id, Usuario usuarioActualizado) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            usuario.setNombre(usuarioActualizado.getNombre());
            usuario.setApellido(usuarioActualizado.getApellido());
            usuario.setCorreo(usuarioActualizado.getCorreo());
            usuario.setContraseña(usuarioActualizado.getContraseña());
            usuario.setRol(usuarioActualizado.getRol());
            return usuarioRepository.save(usuario);
        }
        throw new RuntimeException("Usuario no encontrado");
    }

    public Usuario obtenerUsuario(String id) {
        return usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    public List<Usuario> obtenerUsuariosPorRol(String rol) {
        return usuarioRepository.findByRol(rol);
    }

    public Optional<Usuario> obtenerUsuarioPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }

    public void eliminarUsuario(String id) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            if ("Doctor".equals(usuario.getRol())) {
                doctorRepository.deleteByCorreo(usuario.getCorreo());
            } else if ("Actor".equals(usuario.getRol())) {
                actorRepository.deleteByCorreo(usuario.getCorreo());
            }
            usuarioRepository.deleteById(id);
        }
    }

    public Usuario buscarPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo).orElse(null);
    }

    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> obtenerUsuarioPorCorreoOptional(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }
    @PostConstruct
    public void crearAdminUsuario() {
        String adminCorreo = "alejandropaqui00@gmail.com";
        if (!usuarioRepository.findByCorreo(adminCorreo).isPresent()) {
            Usuario admin = new Usuario();
            admin.setNombre("Justin");
            admin.setApellido("Paqui");
            admin.setCorreo(adminCorreo);
            admin.setContraseña("123");
            admin.setRol("Administrador");
            usuarioRepository.save(admin);
        }
    }
}
