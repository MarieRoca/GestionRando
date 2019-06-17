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
 * Rest Controller pour la gestion des randonnée du club sur une application WEB
 * @author Emma/Hugo/Marie
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
    public boolean clotSondage (@PathVariable String idr, @PathVariable String idd){
        return gr.cloturerSondage(idr,idd);
    }
    
    //RANDO /ok
    @RequestMapping(value="/{idr}/inscription", method = RequestMethod.PATCH)
    public boolean inscription (@RequestBody Long membre, @PathVariable String idr){
        return gr.inscrire(idr, membre);
    }
    
    //RANDO / ok
    @RequestMapping(value="/randoAChercher/{idm}/{switche}", method = RequestMethod.GET)
    public ArrayList<Rando> randoAChercher (@PathVariable Long idm, @PathVariable String switche ){
       return gr.randoAChercher(idm, switche);
    }
    
    //RANDO / ok
    @RequestMapping(value="/randoAnnulee", method = RequestMethod.GET)
    public ArrayList<Rando> randoAnnulee (){
       return gr.randoAnnulee();
    }
    
    //RANDO / get rando où on est TL / OK
    @RequestMapping(value="/teamleader/{idm}", method = RequestMethod.GET)
    public ArrayList<Rando> randoTL (@PathVariable Long idm){
        return gr.randoTL(idm);
    }
    
    //RANDO / get une rando / ok
    @RequestMapping(value="/{idr}", method = RequestMethod.GET)
    public Rando rando (@PathVariable String idr){
        return gr.getRando(idr);
    }
    
    //RANDO /ok
    @RequestMapping(value="/{idr}/cloturer", method = RequestMethod.PATCH)
    public boolean cloturer (@PathVariable String idr){
        return gr.cloturer(idr);
    }
    
}
