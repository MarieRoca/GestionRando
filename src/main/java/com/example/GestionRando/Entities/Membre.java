/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.GestionRando.Entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author marieroca
 */
@Document(collection="Membre")
public class Membre {
    @Id
    String idMembre;

    public Membre(String id) {
        this.idMembre = id;
    }

    public Membre() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getIdMembre() {
        return idMembre;
    }
    
    
    
    
}
