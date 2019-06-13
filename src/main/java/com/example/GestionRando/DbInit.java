package com.example.GestionRando;

import com.example.GestionRando.Entities.Rando;
import com.example.GestionRando.repositories.RandoRepo;
import com.example.GestionRando.services.GestionRandonnee;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 *
 * @author Emma/Hugo/Marie
 */
@Component
@ConditionalOnProperty(name = "app.db-init", havingValue = "true")
public class DbInit implements CommandLineRunner {
    private RandoRepo rr;
    @Autowired
    GestionRandonnee gr;
    
    public DbInit(RandoRepo rr){
        this.rr = rr;
    }
    
    @Override
    public void run(String... strings) throws Exception {
        this.rr.deleteAll();
        float niveau = 10;
        float f = 2;
        
        Date v1 = new Date();
        Date v2 = new Date();
        Date v3 = new Date();
        Long m1 = new Long("5");
        Long m2 = new Long("6");
        Rando r1 = new Rando("Rando1", niveau,v1, v2, v3, m1,"", f,f, f);
        Rando r2 = new Rando("Rando2", niveau,v1, v2, v3, m1,"", f,f, f);
        Rando r3 = new Rando("caca", 5000,v1, v2, v3, m2,"", f,f, f);
        
        this.rr.save(r1);
        this.rr.save(r2);
        this.rr.save(r3);
        
        /*System.out.println("com.example.GestionRando.DbInit.run()"+gr.getMembreNiveau(m1));
        System.out.println("com.example.GestionRando.DbInit.run()"+gr.estCoutValide(100));
        System.out.println("com.example.GestionRando.DbInit.run()"+gr.estCoutValide(1002));*/
        //gr.debitTresorerie(100);
        
        System.out.println(" -- Database has been initialized");
    }
}
