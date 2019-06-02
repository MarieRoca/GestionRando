/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.GestionRando.controller;

import com.example.GestionRando.Entities.Membre;
import com.example.GestionRando.Entities.Rando;
import com.example.GestionRando.services.GestionRandonnee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author marieroca
 */
@org.springframework.web.bind.annotation.RestController
@RequestMapping("/url")
public class RestController {
    @Autowired
    GestionRandonnee gr;
    
    //MEMBRE
    @RequestMapping(value="/subscribe", method = RequestMethod.POST)
    public void createRando (@RequestBody Rando randoACree ){
        gr.creerRando("caca", 0, null, null, null,new Membre(), "", 0, 0, 0);
    }
    
}
