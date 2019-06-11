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
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author marieroca
 */
@Document(collection="Rando")
public class Rando {
    @Id
    String id;
    String titre, lieu;
    float niveau, cv, cf, dist;
    
    ArrayList<Vote> vote;
    ArrayList<Long> participants;
    Long teamLeader;

    public enum Statut{
        PLAN, //en cours de vote
        SONDAGE_CLOS, //Cloturee le TL a choisi une date
        ORGA_CLOS, //Cloturee plus personne peut s'inscrire
        ANNULEE
    };
    
    private Statut statut;
    
    public Rando(){
        
    }

    public Rando(String titre, float niveau, Date date1, Date date2, Date date3, Long teamLeader, String lieu, float dist, float cf, float cv) {
        this.titre = titre;
        this.lieu = lieu;
        this.dist = dist;
        this.cv = cv;
        this.cf = cf;
        this.niveau = niveau;
        this.vote = new ArrayList<Vote>();
        this.vote.add(new Vote(date1));
        this.vote.add(new Vote(date2));
        this.vote.add(new Vote(date3));
        this.statut = Statut.PLAN;
        this.participants = new ArrayList<Long>();
        this.teamLeader = teamLeader;
    }

    public String getId() {
        return id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public float getDist() {
        return dist;
    }

    public void setDist(float dist) {
        this.dist = dist;
    }

    public float getCv() {
        return cv;
    }

    public void setCv(float cv) {
        this.cv = cv;
    }

    public float getCf() {
        return cf;
    }

    public void setCf(float cf) {
        this.cf = cf;
    }

    public float getNiveau() {
        return niveau;
    }

    public void setNiveau(float niveau) {
        this.niveau = niveau;
    }

    public ArrayList<Vote> getVote() {
        return vote;
    }

    public void setVote(ArrayList<Vote> vote) {
        this.vote = vote;
    }

    public ArrayList<Long> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<Long> participants) {
        this.participants = participants;
    }

    public Statut getStatut() {
        return statut;
    }

    public void setStatut(Statut statut) {
        this.statut = statut;
    }

    public Long getTeamLeader() {
        return teamLeader;
    }

    public void setTeamLeader(Long teamLeader) {
        this.teamLeader = teamLeader;
    }
    
    
}
