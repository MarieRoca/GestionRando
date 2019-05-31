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
public class Rando {
    @Id
    Long id;
    String titre, lieu;
    float niveau, cv, cf, dist;
    Vote[] vote;
    ArrayList<Membre> participants;
    Membre teamLeader;

    public enum Statut{
        PLAN, //en cours de vote
        SONDAGE_CLOS, //Cloturee le TL a choisi une date
        ORGA_CLOS, //Cloturee plus personne peut s'inscrire
        PASSEE
    };
    
    private Statut statut;
    
    public Rando(){
        
    }

    public Rando(String titre, float niveau, Date date1, Date date2, Date date3, Membre teamLeader, String lieu, float dist, float cf, float cv) {
        this.titre = titre;
        this.lieu = lieu;
        this.dist = dist;
        this.cv = cv;
        this.cf = cf;
        this.niveau = niveau;
        this.vote = new Vote[3];
        this.vote[0] = new Vote(date1);
        this.vote[1] = new Vote(date2);
        this.vote[2] = new Vote(date3);
        this.statut = Statut.PLAN;
        this.participants = new ArrayList<Membre>();
        this.teamLeader = teamLeader;
    }

    public Long getId() {
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

    public Vote[] getVote() {
        return vote;
    }

    public void setVote(Vote[] vote) {
        this.vote = vote;
    }

    public ArrayList<Membre> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<Membre> participants) {
        this.participants = participants;
    }

    public Statut getStatut() {
        return statut;
    }

    public void setStatut(Statut statut) {
        this.statut = statut;
    }
}
