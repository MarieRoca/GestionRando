/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.GestionRando.controller;

import com.example.GestionRando.Entities.Rando;
import com.example.GestionRando.Entities.Vote;
import com.example.GestionRando.services.GestionRandonnee;
import java.util.ArrayList;
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
public class RestController {
    @Autowired
    GestionRandonnee gr;
    
    //RANDO / ok
    @RequestMapping(value="/create/{idm}", method = RequestMethod.POST)
    public boolean createRando (@RequestBody Rando randoACree, @PathVariable Long idm){
        Iterator votes = randoACree.getVote().iterator();
        return gr.creerRando(randoACree.getTitre(), randoACree.getNiveau(), ((Vote) votes.next()).getDate(), ((Vote) votes.next()).getDate(), ((Vote) votes.next()).getDate(),idm, randoACree.getLieu(), randoACree.getCf(), randoACree.getCv(), randoACree.getDist());
    }
    
    //RANDO / ok
    @RequestMapping(value="/{idr}/voter/{idd}", method = RequestMethod.PATCH)
    public boolean voter (@RequestBody Long membre, @PathVariable String idd, @PathVariable String idr){
        return gr.voter(membre, idd, idr);
    }
    
    //RANDO / ok
    @RequestMapping(value="/{idr}/cloturer/{idd}", method = RequestMethod.PATCH)
    public void clotSondage (@PathVariable String idr, @PathVariable String idd){
        gr.cloturerSondage(idr,idd);
    }
    
    //RANDO /ok
    @RequestMapping(value="/{idr}/inscription", method = RequestMethod.PATCH)
    public boolean inscription (@RequestBody Long membre, @PathVariable String idr){
        return gr.inscrire(idr, membre);
    }
    
    //RANDO / get des rando où l'on peut s'incrire : ouverte à l'inscription et à notre niveau / OK
    @RequestMapping(value="/dispo/{idm}", method = RequestMethod.GET)
    public ArrayList<Rando> randoDispo (@PathVariable Long idm){
       return gr.randoDispo(idm);
    }
    
    //RANDO / get rando où on est TL / OK
    @RequestMapping(value="/teamleader/{idm}", method = RequestMethod.GET)
    public ArrayList<Rando> randoTL (@PathVariable Long idm){
        return gr.randoTL(idm);
    }
    
    //RANDO / get rando où on a voté  / ok
    @RequestMapping(value="/vote/{idm}", method = RequestMethod.GET)
    public ArrayList<Rando> randoVote (@PathVariable Long idm){
        return gr.randoVote(idm);
    }
    
    //RANDO / get une rando / ok
    @RequestMapping(value="/{idr}", method = RequestMethod.GET)
    public Rando rando (@PathVariable String idr){
        return gr.getRando(idr);
    }
    
    //RANDO /ok
    @RequestMapping(value="/{idr}/cloturer", method = RequestMethod.PATCH)
    public void cloturer (@PathVariable String idr){
        gr.cloturer(idr);
    }
    
}
