package com.example.GestionRando.repositories;

import com.example.GestionRando.Entities.Rando;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository pour la gestion des randonnée du club sur une application WEB
 * @author Emma/Hugo/Marie
 */
public interface RandoRepo extends CrudRepository<Rando, String> {
    
}
