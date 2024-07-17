package com.pacientesimulado.application.services;

import com.pacientesimulado.application.data.Materia;
import com.pacientesimulado.application.repository.MateriaRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MateriaService {

    @Autowired
    private MateriaRepository materiaRepository;

    public List<Materia> obtenerMateriasPorCarrera(String carrera) {
        return materiaRepository.findByCarrera(carrera);
    }

    public Materia guardarMateria(Materia materia) {
        return materiaRepository.save(materia);
    }

    public void eliminarMateria(String id) {
        materiaRepository.deleteById(id);
    }

    public List<String> obtenerTodasLasCarreras() {
        return materiaRepository.findAll().stream()
                .map(Materia::getCarrera)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<Materia> obtenerTodasLasMaterias() {
        return materiaRepository.findAll();
    }
}
