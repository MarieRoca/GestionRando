/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.GestionRando.Entities;

import java.util.ArrayList;
import java.util.Date;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author marieroca
 */
@Document(collection="Rando")
public class Vote {
    @Id
    private String id;
    
    Date date;
    
    ArrayList<Membre> votants;
    
    public Vote() {
        
    }
    
    public Vote(Date date) {
        this.id = ""+new ObjectId();
        this.date = date;
        this.votants = new ArrayList<Membre>();
    }

    public String getId() {
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
