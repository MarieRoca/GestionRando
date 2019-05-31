/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.GestionRando.repositories;

import com.example.GestionRando.Entities.Vote;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author marieroca
 */
public interface VoteRepo extends CrudRepository<Vote, Long> {
    
}
