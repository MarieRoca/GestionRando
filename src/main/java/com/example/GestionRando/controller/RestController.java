/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.GestionRando.controller;

import com.example.GestionRando.Entities.Membre;
import com.example.GestionRando.Entities.Rando;
import com.example.GestionRando.Entities.Vote;
import com.example.GestionRando.services.GestionRandonnee;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author marieroca
 */
@org.springframework.web.bind.annotation.RestController
@RequestMapping("/rando")
public class RestController {
    @Autowired
    GestionRandonnee gr;
    
    //RANDO
    @RequestMapping(value="/create", method = RequestMethod.POST)
    public void createRando (@RequestBody Rando randoACree){
        Iterator votes = randoACree.getVote().iterator();
        gr.creerRando(randoACree.getTitre(), randoACree.getNiveau(), ((Vote) votes.next()).getDate(), ((Vote) votes.next()).getDate(), ((Vote) votes.next()).getDate(),randoACree.getTeamLeader(), randoACree.getLieu(), randoACree.getCf(), randoACree.getCv(), randoACree.getDist());
    }
    
    //RANDO
    @RequestMapping(value="/{idr}/voter/{idd}", method = RequestMethod.PATCH)
    public void voter (@RequestBody Membre membre, @PathVariable String idr, @PathVariable Long idd){
        gr.voter(membre, idd, idr);
    }
    
    //RANDO
    @RequestMapping(value="/{idr}/cloturer/{idd}", method = RequestMethod.PATCH)
    public void clotSondage (@PathVariable String idr, @PathVariable Long idd){
        gr.cloturerSondage(idr,idd);
    }
    
    //RANDO
    @RequestMapping(value="/{idr}/inscription", method = RequestMethod.PATCH)
    public void inscription (@RequestBody Membre membre, @PathVariable Long idr){
        gr.cloturerSondage(membre.getIdMembre(), idr);
    }
    
    //RANDO / get des rando où l'on peut s'incrire : ouverte à l'inscription et à notre niveau / OK
    @RequestMapping(value="/dispo", method = RequestMethod.GET)
    public ArrayList<Rando> randoDispo (@RequestBody Membre membre){
       return gr.randoDispo(membre);
    }
    
    //RANDO / get rando où on est TL / OK
    @RequestMapping(value="/teamleader", method = RequestMethod.GET)
    public ArrayList<Rando> randoTL (@RequestBody Membre membre){
        System.out.println("com.example.GestionRando.controller.RestController.randoTL()");
        return gr.randoTL(membre);
    }
    
    //RANDO / get rando où on a voté
    @RequestMapping(value="/vote", method = RequestMethod.GET)
    public ArrayList<Rando> randoVote (@RequestBody Membre membre){
        return gr.randoVote(membre);
    }
    
    //RANDO / get une rando
    @RequestMapping(value="/{idr}", method = RequestMethod.GET)
    public Rando rando (@PathVariable String idr){
        return gr.getRando(idr);
    }
    
    //RANDO 
    @RequestMapping(value="/{idr}/cloturer", method = RequestMethod.PATCH)
    public void cloturer (@PathVariable String idr){
        gr.cloturer(idr);
    }
    
}
