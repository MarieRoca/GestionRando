/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.GestionRando.services;

import com.example.GestionRando.Entities.Rando;
import com.example.GestionRando.Entities.Rando.Statut;
import com.example.GestionRando.Entities.Vote;
import com.example.GestionRando.repositories.RandoRepo;
import com.example.GestionRando.repositories.VoteRepo;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Classe permettant de gérer les randonnées de l'association
 *
 * @author Emma/Hugo/Marie
 */
@Service
public class GestionRandonnee {

    @Autowired
    VoteRepo vr;
    @Autowired
    RandoRepo rr;

    int nbPlacesRando = 15;
    String uri = "http://127.0.0.1:8182/EnMarcheMembre";

    //Créer rando
    /**
     * Création d'un randonnée au sein de l'application Va Marcher
     *
     * @param titre Titre de la randonnée
     * @param niveau Niveau cible de la randonnée
     * @param date1 Date proposée pour le vote 1
     * @param date2 Date proposée pour le vote 2
     * @param date3 Date proposée pour le vote 3
     * @param teamLeader Team Leader en charge de la randonnée
     * @param lieu Lieu de la randonnée
     * @param dist Distance de la randonnée
     * @param cf Coûts Fixes de la randonnée
     * @param cv Coûts Variables (correspond au coût par participant) de la
     * randonnée
     */
    public boolean creerRando(String titre, float niveau, Date date1, Date date2, Date date3, Long teamLeader, String lieu, float dist, float cf, float cv) {
        //Vérification TL niveau 1,5x supérieur à distance getRando & Coût fixe bien inférieur au budget de l'asso
        if (estMembreApte(dist, teamLeader, "TeamLeader") && estCoutValide(cf)) {
            //je créé ma rando
            Rando r = new Rando(titre, niveau, date1, date2, date3, teamLeader, lieu, dist, cf, cv);
            rr.save(r);
            return true;
        }else{
            return false;
        }

        //il a le niveau / assez d'argent
        //@TODO si c'est un TL / si il a le certificat
    }

    //Voter
    //Normalement test avec randoDispo()
    /**
     * Méthode permettant à un membre de voter pour une date pour une randonnée
     *
     * @param jeanClaude Membre qui vote pour une date pour la randonnée
     * @param idDate Date pour laquelle le membre vote pour la randonnée
     * @param idRando Randonnée pour laquelle le membre choisi une date
     */
    public boolean voter(Long jeanClaude, String idDate, String idRando) {
        Rando r = (Rando) rr.findById(idRando).get();
        if (estMembreApte(r.getDist(), jeanClaude, "Membre")) {
            Vote v = new Vote();
            ArrayList<Vote> votes = r.getVote();
            for (Vote vCourant : votes) {
                if (vCourant.getId().equals(idDate)) {
                    v = vCourant;
                }
            }

            //Est-ce que jean claude a déjà voté pour cette randonnée ?
            boolean aVote = false;
            for (Vote vCour : r.getVote()) {
                for (Long m : vCour.getVotants()) {
                    if (m.equals(jeanClaude)) {
                        aVote = true;
                    }
                }
            }
            //S'il a pas déjà voté, on prend son vote
            if (!aVote) {
                ArrayList<Long> votants = v.getVotants();
                votants.add(jeanClaude);

                votes.remove(v);
                v.setVotants(votants);
                votes.add(v);
                r.setVote(votes);
            }

            rr.save(r);
            return true;
        }else{
            return false;
        }

    }

    //Confirmer
    //Le TL choisis la date, les membres l'ayant choisi sont de facto inscris
    //Statut clos
    /**
     * Méthode permettant de cloturer un sondage : le statut de la randonnée
     * passe à "Sondage Clos" Aucun membre ne peut encore voter, les membres
     * ayant voté pour la date choisie sont inscris automatiquements
     *
     * @param idRando Identifiant de la randonnée pour laquelle le TeamLeader
     * cloture le vote
     * @param idDate Identifiant du vote (donc la date) que le TeamLeader a
     * choisi
     */
    public void cloturerSondage(String idRando, String idDate) {
        Rando r = (Rando) rr.findById(idRando).get();
        Vote v = new Vote();
        ArrayList<Vote> votetosave = new ArrayList<Vote>();
        for (Vote vCourant : r.getVote()) {
            if (vCourant.getId().equals(idDate)) {
                votetosave.add(vCourant);
                v = vCourant;
            }
        }

        r.setParticipants(v.getVotants());
        r.setVote(votetosave);

        r.setStatut(Statut.SONDAGE_CLOS);
        rr.save(r);
    }

    //S'inscrire lorsqu'une date est déjà chosisie par le TL + statut clos
    //Normalement test avec randoDispo()
    /**
     * Méthode permettant de s'inscrire. Cette action n'est possible que si le
     * Team Leader a cloturé le sondage et qu'il a donc choisi une date, et que
     * la randonnée n'est pas passée.
     *
     * @param idRando Identifiant de la randonnée auquel le membre veut
     * s'inscrire
     * @param idMembre Identifiant du membre souhaitant s'inscrire à la
     * randonnée
     */
    public boolean inscrire(String idRando, Long idMembre) {
        Rando r = (Rando) rr.findById(idRando).get();
        if (estMembreApte(r.getDist(), idMembre, "Membre")) {
            //test rando statut
            if (r.getStatut() == Rando.Statut.SONDAGE_CLOS) {
                //test niveau participant
                //@todo en regle et apte
                ArrayList<Long> p = r.getParticipants();
                p.add(idMembre);
                r.setParticipants(p);
            }
            rr.save(r);
            return true;
        } else {
            return false;
        }

    }

    //Rando dispo
    /**
     * Méthode permettant de lister les randonnées disponibles pour un membre.
     * Une randonnée est disponible si : * le membre a un niveau suffisant pour
     * participer * il reste des places * le statut de la randonnée est "en
     * planification" ou "sondage clos"
     *
     * @param idm id du Membre souhaitant avoir la liste des randos dispo
     * @return ArrayList des randonnées disponibles
     */
    public ArrayList<Rando> randoDispo(Long idm) {
        //lister les randos dispo pour un membre en fonction de son niveau, 
        //du nb de place restantes, et du fait qu'elle soit dispo et pas 
        //passée => statut pas ORGA_CLOS, pas ANNULEE
        float niveau = getMembreNiveau(idm);
        ArrayList<Rando> randoDispo = new ArrayList<Rando>();
        Iterator randos = rr.findAll().iterator();
        Rando rCourant;
        while (randos.hasNext()) {
            rCourant = (Rando) randos.next();
            if (rCourant.getNiveau() <= niveau && rCourant.getParticipants().size() <= this.nbPlacesRando && rCourant.getStatut() != Statut.ORGA_CLOS && rCourant.getStatut() != Statut.ANNULEE) {
                randoDispo.add(rCourant);
            }
        }
        return randoDispo;
    }

    /**
     * Méthode permettant de retourner une randonnée à partir de son identifiant
     *
     * @param id Identifiant de la randonnée
     * @return La randonnée
     */
    public Rando getRando(String id) {
        return (Rando) rr.findById(id).get();
    }

    //Rando où on est TL peu importe le statut de la rando
    /**
     * Méthode permettant de lister les randonnées pour lesquelles un membre est
     * team leader.
     *
     * @param idm id du Membre souhaitant avoir la liste des randonnées pour
     * lesquelles il est team leader
     * @return ArrayList des randonnées pour lesquelles le membre est team
     * leader
     */
    public ArrayList<Rando> randoTL(Long idm) {
        ArrayList<Rando> randoTL = new ArrayList<Rando>();
        Iterator randos = rr.findAll().iterator();
        Rando rCourant;
        while (randos.hasNext()) {
            rCourant = (Rando) randos.next();
            if (rCourant.getTeamLeader().equals(idm)) {
                randoTL.add(rCourant);
            }
        }
        return randoTL;
    }

    //Rando où on a voté peu importe le statut de la rando
    /**
     * Méthode permettant de lister les randonnées pour lesquelles le membre a
     * voté.
     *
     * @param idm id du Membre souhaitant avoir la liste des randonnées pour
     * lesquelles il a voté
     * @return ArrayList des randonnées pour lesquelles le membre a voté
     */
    public ArrayList<Rando> randoVote(Long idm) {
        ArrayList<Rando> randoVote = new ArrayList<Rando>();
        Iterator randos = rr.findAll().iterator();
        Rando rCourant;
        while (randos.hasNext()) {
            rCourant = (Rando) randos.next();
            for (Vote v : rCourant.getVote()) {
                for (Long m : v.getVotants()) {
                    if (m.equals(idm)) {
                        randoVote.add(rCourant);
                        break;
                    }
                }
            }
        }
        return randoVote;
    }

    //Cloturer l'orga
    //du coup on peut plus s'inscrire
    //il faut que la date soit pas passée
    //tester budget cf + nb membre * cv < tresorerie
    //oui debit non annulée
    /**
     * Méthode permettant de cloturer l'organisation de la randonnée. La cloture
     * n'est possible que si le sondage a été cloturée. Si la date de la
     * randonnée est passée ou si son coût total (coût fixe + coûts variables)
     * est supérieur à la trésorerie de l'association, la randonnée est annulée.
     * Une fois l'organisation close, les membres ne peuvent plus s'inscrire
     *
     * @param idRando Identifiant de la randonnée à cloturer
     */
    public void cloturer(String idRando) {
        Rando r = (Rando) rr.findById(idRando).get();
        float coutRando = r.getCf() + r.getParticipants().size() * r.getCv();

        Vote v = new Vote();
        for (Vote vCourant : r.getVote()) {
            v = vCourant;
        }

        if (r.getStatut() == Statut.SONDAGE_CLOS) {
            if (v.getDate().before(new Date()) && estCoutValide(coutRando)) {
                r.setStatut(Statut.ORGA_CLOS);
                //debitTresorerie(coutRando);
            } else {
                r.setStatut(Statut.ANNULEE);
            }
        }
        System.out.println("coutvalide" + estCoutValide(coutRando));
        System.out.println("date" + v.getDate().after(new Date()));
        rr.save(r);
    }

    //Stats
    //Total d’en-cours budgétaire
    //Total du coût des randonnées
    /**
     * Appel REST qui nous permet de savoir si le coût d'une randonnée rentre
     * bien dans le budget de l'association
     *
     * @param cout Coût d'une randonnée (Coût fixe à la création et Coût fixe +
     * coûts variables à la cloture)
     * @return True si le coût rentre dans le budget de l'association
     */
    public boolean estCoutValide(float cout) {
        //tester si le budget de l'asso est bien supérieur au cout de la rando
        //on récupère le budget de l'asso
        //Une randonnée ne peut être créée que si son coût fixe est inférieur au budget de l’association.

        String param = "/treso";

        RestTemplate restTemplate = new RestTemplate();

        Float response = restTemplate.getForObject(uri + param, Float.class);

        //tester supérieur au cout
        return response >= cout;

    }

    /**
     * Méthode qui nous permet de savoir si un membre est un Team Leader apte
     * pour une randonnée Le team leader d'une randonnée doit avoir un niveau à
     * minima 1,5* supérieur à la distance de la randonnée
     *
     * @param distanceRando Distance de la randonnée
     * @param jeanClaude Membre pour qui il faut vérifier s'il est apte à être
     * @param roleBon le role souhaité pour effectuer l'action team leader
     * @return
     */
    public boolean estMembreApte(float distanceRando, Long jeanClaude, String role) {
        //tester si le niveau du TL est bien au moins 1,5x supérieur à la distance de la rando
        boolean niveau = getMembreNiveau(jeanClaude) * (1.5) >= distanceRando;
        boolean certificat = getMembreCertificat(jeanClaude);
        boolean aRole = getMembreRole(jeanClaude, role);

        if (niveau && certificat && aRole) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Appel REST qui nous retourne le niveau d'un membre
     *
     * @param jeanClaude Membre pour qui on souhaite connaitre le niveau
     * @return Un décimal représentant le niveau du membre
     */
    public float getMembreNiveau(Long jeanClaude) {
        //retourne le niveau du membre
        // URI locale
        String param = "/niveau/";

        RestTemplate restTemplate = new RestTemplate();

        Float response = restTemplate.getForObject(uri + param + jeanClaude, Float.class);
        return response;
    }

    /**
     * Appel REST permettant de débiter la trésorerie de l'association
     *
     * @param coutRando Montant à débiter (coût de la randonnée)
     */
    //peut etre changer en void on s'en ballek du retour nan ??
    public void debitTresorerie(float coutRando) {
        //débite le cout de la rando de la trésorerie
        // URI locale treso
        String param = "/treso";

        RestTemplate restTemplate = new RestTemplate();

        restTemplate.put(uri + param, coutRando);
    }

    /**
     * Appel rest qui retoune un boolean en fonction de la validité du
     * certificat
     *
     * @param jeanClaude
     * @return
     */
    private boolean getMembreCertificat(Long jeanClaude) {
        //retourne le niveau du membre
        // URI locale
        String param = "/apte/";

        RestTemplate restTemplate = new RestTemplate();

        boolean response = restTemplate.getForObject(uri + param + jeanClaude, boolean.class);
        
        return response;
    }

    /**
     * *
     * Appel rest qui retroune le niveau le plus élevé d'un membre
     *
     * @param jeanClaude
     * @return
     */
    private boolean getMembreRole(Long jeanClaude, String role) {
        String param = "/role/";

        RestTemplate restTemplate = new RestTemplate();

        boolean response = restTemplate.getForObject(uri + param + jeanClaude + "/" + role, boolean.class);

        return response;
    }

}
