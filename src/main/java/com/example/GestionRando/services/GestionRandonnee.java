/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.GestionRando.services;

import com.example.GestionRando.Entities.Membre;
import com.example.GestionRando.Entities.Rando;
import com.example.GestionRando.Entities.Rando.Statut;
import com.example.GestionRando.Entities.Vote;
import com.example.GestionRando.repositories.MembreRepo;
import com.example.GestionRando.repositories.RandoRepo;
import com.example.GestionRando.repositories.VoteRepo;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author marieroca
 */
@Service
public class GestionRandonnee {
    @Autowired
    MembreRepo mr;
    @Autowired
    VoteRepo vr;
    @Autowired
    RandoRepo rr;
    
    int nbPlacesRando = 15;
    
    //Créer rando
    public void creerRando(String titre, float niveau, Date date1, Date date2, Date date3, Membre teamLeader, String lieu, float dist, float cf, float cv){
        //Récupérer niveau du membre TL
        //Vérifier niveau 1,5x supérieur à distance Rando
        if(estTeamLeaderApte(dist, teamLeader) && estCoutValide(cf)){
            //je créé ma rando
            Rando r = new Rando(titre, niveau, date1, date2, date3, teamLeader, lieu, dist, cf, cv);
            rr.save(r);
        }
    }
    
    //Voter
    //Normalement test avec RandoDispo()
    public void voter(Membre jeanClaude, long idDate, long idRando){
        Rando r = (Rando) rr.findById(idRando).get();
        Vote v = (Vote) vr.findById(idDate).get();
        ArrayList<Membre> votants = v.getVotants();
        votants.add(jeanClaude);
        //je teste si jean claude a pas déjà voté pour cette rando
        if(!r.getVote()[0].getVotants().contains(jeanClaude) && !r.getVote()[1].getVotants().contains(jeanClaude) && !r.getVote()[2].getVotants().contains(jeanClaude))
            v.setVotants(votants);
        vr.save(v);
    }
    
    //Confirmer
    //Le TL choisis la date, les membres l'ayant choisi sont de facto inscris
    //Statut clos
    public void cloturerSondage(long idRando, long idDate){
        Rando r = (Rando) rr.findById(idRando).get();
        Vote v = (Vote) vr.findById(idDate).get();
        Vote[] votes = r.getVote();
        int i = 0;
        while(!votes[i].equals(v) && i < votes.length){
            i++;
        }
        v = votes[i];
        r.setParticipants(v.getVotants());
        votes = new Vote[0];
        votes[0] = v;
        r.setStatut(Statut.SONDAGE_CLOS);
        rr.save(r);
    }
    
    //S'inscrire lorsqu'une date est déjà chosisie par le TL + statut clos
    //Normalement test avec RandoDispo()
    public void inscrire(long idRando, long idMembre){
        Membre m = (Membre) mr.findById(idMembre).get();
        Rando r = (Rando) rr.findById(idRando).get();
        //test rando statut
        if(r.getStatut() == Rando.Statut.SONDAGE_CLOS){
            //test niveau participant
            ArrayList<Membre> p = r.getParticipants();
            p.add(m);
            r.setParticipants(p);
        }
        rr.save(r);
    }
    
    //Rando dispo
    public ArrayList<Rando> RandoDispo(Membre m){
        //lister les randos dispo pour un membre en fonction de son niveau, 
        //du nb de place restantes, et du fait qu'elle soit dispo et pas 
        //passée => statut pas ORGA_CLOS, pas PASSEE
        
        float niveau = getMembreNiveau(m);
        ArrayList<Rando> randoDispo = new ArrayList<Rando>();
        Iterator randos = rr.findAll().iterator();
        Rando rCourant = new Rando();
        while (randos.hasNext()){
            if(rCourant.getNiveau() <= niveau && rCourant.getParticipants().size() <= this.nbPlacesRando && rCourant.getStatut() != Statut.ORGA_CLOS && rCourant.getStatut() != Statut.ANNULEE)
                randoDispo.add(rCourant);  
            rCourant = (Rando) randos.next();
        }
        return randoDispo;
    }
    
    //Cloturer l'orga
    //du coup on peut plus s'inscrire
    //il faut que la date soit pas passée
    //tester budget cf + nb membre * cv < tresorerie
    //oui debit non annulée
    public void cloturer(Long idRando){
        Rando r = (Rando) rr.findById(idRando).get();
        float coutRando = r.getCf() + r.getParticipants().size() * r.getCv();
        if(r.getStatut() == Statut.SONDAGE_CLOS){
            if(r.getVote()[0].getDate().after(new Date()) && estCoutValide(coutRando)){
                r.setStatut(Statut.ORGA_CLOS);
                debitTresorerie(coutRando);
            }else
                r.setStatut(Statut.ANNULEE);
        }   
    }
    
    //Stats
    //Total d’en-cours budgétaire
    //Total du coût des randonnées
    
    
    public boolean estCoutValide(float coutFixe){
        //tester si le budget de l'asso est bien supérieur au cout de la rando
        //on récupère le budget de l'asso
        //Une randonnée ne peut être créée que si son coût fixe est inférieur au budget de l’association.
        
        // URI locale
        String uri = "http://127.0.0.1:5050/";
        
        Client client = ClientBuilder.newClient();
        //WebTarget wt = client.target(uri + "&q=" + contrat);
        WebTarget wt = client.target(uri);

        //Invocation.Builder invocationBuilder = wt.request(MediaType.TEXT_PLAIN);
        Invocation.Builder test = wt.request();
        Response response = test.get();
        
        //String reponse = response.readEntity(String.class);
        float reponse = response.readEntity(Float.class);
        
        //tester supérieur au cout
        return reponse >= coutFixe;
    }
    
    public boolean estTeamLeaderApte(float distanceRando, Membre jeanClaude){
        //tester si le niveau du TL est bien au moins 1,5x supérieur à la distance de la rando
        //on récupère le niveau du Team Leader
        
        //Une randonnée ne peut être créé que par un Team Leader « apte ». Il doit disposer d’un niveau 1,5 fois supérieur à la distance de la randonnée.
        // URI locale
        String uri = "http://127.0.0.1:5050/";
        
        Client client = ClientBuilder.newClient();
        WebTarget wt = client.target(uri + "&q=" + jeanClaude.getIdMembre());
        //WebTarget wt = client.target(uri);

        //Invocation.Builder invocationBuilder = wt.request(MediaType.TEXT_PLAIN);
        Invocation.Builder test = wt.request();
        Response response = test.get();
        
        //String reponse = response.readEntity(String.class);
        float reponse = response.readEntity(Float.class);
        
        //tester 1,5 * supérieur à la distance
        return reponse*(1.5) >= distanceRando;
    }
    
    public float getMembreNiveau(Membre jeanClaude){
        //retourne le niveau du membre
        // URI locale
        String uri = "http://127.0.0.1:5050/";
        
        Client client = ClientBuilder.newClient();
        WebTarget wt = client.target(uri + "&q=" + jeanClaude.getIdMembre());
        //WebTarget wt = client.target(uri);

        //Invocation.Builder invocationBuilder = wt.request(MediaType.TEXT_PLAIN);
        Invocation.Builder test = wt.request();
        Response response = test.get();
        
        //String reponse = response.readEntity(String.class);
        return response.readEntity(Float.class);
    }
    
    public boolean debitTresorerie(float coutRando){
        //débite le cout de la rando de la trésorerie
        // URI locale
        String uri = "http://127.0.0.1:5050/";
        
        Client client = ClientBuilder.newClient();
        WebTarget wt = client.target(uri + "&q=" + coutRando);
        //WebTarget wt = client.target(uri);

        //Invocation.Builder invocationBuilder = wt.request(MediaType.TEXT_PLAIN);
        Invocation.Builder test = wt.request();
        Response response = test.get();
        
        //String reponse = response.readEntity(String.class);
        return response.readEntity(Boolean.class);
    }
    
}
