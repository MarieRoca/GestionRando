/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.GestionRando.Entities;

import java.util.ArrayList;
import java.util.Date;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author marieroca
 */
@Document(collection="Rando")
public class Vote {
    @Id
    Long id;
    Date date;
    ArrayList<Membre> votants;
    
    public Vote() {
        
    }
    
    public Vote(Date date) {
        this.date = date;
        this.votants = new ArrayList<Membre>();
    }

    public Long getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public ArrayList<Membre> getVotants() {
        return votants;
    }

    public void setVotants(ArrayList<Membre> votants) {
        this.votants = votants;
    }
    
}
