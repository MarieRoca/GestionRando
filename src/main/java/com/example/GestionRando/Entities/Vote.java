package com.example.GestionRando.Entities;

import java.util.ArrayList;
import java.util.Date;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Entity représentant notre objet Vote présent au sein d'une Randonnée.
 * 
 * @author Emma/Hugo/Marie
 */
@Document(collection="Rando")
public class Vote {
    @Id
    private String id;
    
    Date date;
    
    ArrayList<Long> votants;
    
    /**
     * Constructeur vide
     */
    public Vote() {
        
    }
    
    /**
     * Constructeur de vote
     * 
     * @param date Date proposée dans le sondage
     */
    public Vote(Date date) {
        this.id = ""+new ObjectId();
        this.date = date;
        this.votants = new ArrayList<Long>();
    }

    /**
     * Getter sur l'identifiant du vote
     * @return Identifiant du vote
     */
    public String getId() {
        return id;
    }

    /**
     * Getter sur la date du vote
     * @return Date du vote
     */
    public Date getDate() {
        return date;
    }

    /**
     * Getter sur la liste des votants sur le vote
     * @return Liste des votants sur le vote
     */
    public ArrayList<Long> getVotants() {
        return votants;
    }

    /**
     * Setter sur la liste des votants sur le vote
     * @param votants Nouvelle liste des votants sur le vote
     */
    public void setVotants(ArrayList<Long> votants) {
        this.votants = votants;
    }
    
}
