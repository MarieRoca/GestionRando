package com.example.GestionRando.Entities;

import java.util.ArrayList;
import java.util.Date;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Entity représentant notre objet Randonnée du club.
 * 
 * @author Emma/Hugo/Marie
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

    /**
     * Enumération de statuts de la randonnée
     * 4 éléments la composent : 
     * Ø PLAN : Randonnée en cours de planification, votes en cours
     * Ø SONDAGE_CLOS : Sondage de la randonnée cloturé, le Team Leader a choisi 
     * une date (un des éléments "Vote"), les membres peuvent s'inscrire
     * Ø ORGA_CLOS : Organisation de la randonnée cloturée, plus personne ne peut s'inscrire
     * Ø ANNULEE : Si la date de la randonnée est dépassée ou si la randonnée est trop chère
     * (coût fixe de la randonnée et coût variable par participant supérieurs à la trésorerie)
     * au moment où le Team Leader essaie de cloturée l'organisation de la randonnée
     */
    public enum Statut{
        PLAN, //en cours de vote
        SONDAGE_CLOS, //Cloturee le TL a choisi une date
        ORGA_CLOS, //Cloturee plus personne peut s'inscrire
        ANNULEE //Si date passée avant cloture ou si trop chère
    };
    
    private Statut statut;
    
    /**
     * Contructeur vide
     */
    public Rando(){
        
    }

    /**
     * Constructeur de Randonnée
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

    /**
     * Getter sur l'identifiant de la randonnée
     * @return Identifiant de la randonnée
     */
    public String getId() {
        return id;
    }

    /**
     * Getter sur le titre de la randonnée
     * @return Titre de la randonnée
     */
    public String getTitre() {
        return titre;
    }

    /**
     * Setter sur le titre de la randonnée
     * @param titre Nouveau titre de la randonnée
     */
    public void setTitre(String titre) {
        this.titre = titre;
    }

    /**
     * Getter sur le lieu de la randonnée
     * @return Lieu de la randonnée
     */
    public String getLieu() {
        return lieu;
    }

    /**
     * Setter sur le lieu de la randonnée
     * @param lieu Nouveau lieu de la randonnée
     */
    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    /**
     * Getter sur la distance de la randonnée
     * @return Distance de la randonnée
     */
    public float getDist() {
        return dist;
    }

    /**
     * Setter sur la distance de la randonnée
     * @param dist Nouvelle distance de la randonnée
     */
    public void setDist(float dist) {
        this.dist = dist;
    }

    /**
     * Getter sur les coûts variables de la randonnée
     * @return Coûts variables de la randonnée
     */
    public float getCv() {
        return cv;
    }

    /**
     * Setter sur les coûts variables de la randonnée
     * @param cv Nouveaux coûts variables de la randonnée
     */
    public void setCv(float cv) {
        this.cv = cv;
    }

    /**
     * Getter sur les coûts fixes de la randonnée
     * @return Coûts fixes de la randonnée
     */
    public float getCf() {
        return cf;
    }

    /**
     * Setter sur les coûts fixes de la randonnée
     * @param cv Nouveaux coûts fixes de la randonnée
     */
    public void setCf(float cf) {
        this.cf = cf;
    }

    /**
     * Getter sur le niveau de la randonnée
     * @return Niveau de la randonnée
     */
    public float getNiveau() {
        return niveau;
    }

    /**
     * Setter sur le niveau de la randonnée
     * @param niveau Niveau de la randonnée
     */
    public void setNiveau(float niveau) {
        this.niveau = niveau;
    }

    /**
     * Getter sur les votes de la randonnée.
     * Ils sont 3 tant que l'organisation n'est pas cloturée
     * Les votes sont constitués d'une date et d'une liste de votants
     * @see Vote
     * @return Liste des votes de la randonnées
     */
    public ArrayList<Vote> getVote() {
        return vote;
    }

    /**
     * Setter sur les votes de la randonnée.
     * Ils sont 3 tant que l'organisation n'est pas cloturée
     * Les votes sont constitués d'une date et d'une liste de votants
     * @see Vote
     * @param vote Nouvelle liste des votes de la randonnées
     */
    public void setVote(ArrayList<Vote> vote) {
        this.vote = vote;
    }

    /**
     * Getter sur les participants de la randonnée.
     * Cette liste est vide tant que l'organisation n'est pas cloturée
     * Lorsque le Team Leader choisi une date (un vote), elle est initialisée
     * avec les votants sur cette date
     * @return Liste des participants de la randonnée
     */
    public ArrayList<Long> getParticipants() {
        return participants;
    }

    /**
     * Setter sur les participants de la randonnée.
     * Cette liste est vide tant que l'organisation n'est pas cloturée
     * Lorsque le Team Leader choisi une date (un vote), elle est initialisée
     * avec les votants sur cette date
     * @param participants Nouvelle liste des participants de la randonnée
     */
    public void setParticipants(ArrayList<Long> participants) {
        this.participants = participants;
    }

    /**
     * Getter sur le statut de la randonnée.
     * 4 valeurs possibles : 
     * Ø PLAN : Randonnée en cours de planification, votes en cours
     * Ø SONDAGE_CLOS : Sondage de la randonnée cloturé, le Team Leader a choisi 
     * une date (un des éléments "Vote"), les membres peuvent s'inscrire
     * Ø ORGA_CLOS : Organisation de la randonnée cloturée, plus personne ne peut s'inscrire
     * Ø ANNULEE : Si la date de la randonnée est dépassée ou si la randonnée est trop chère
     * (coût fixe de la randonnée et coût variable par participant supérieurs à la trésorerie)
     * au moment où le Team Leader essaie de cloturée l'organisation de la randonnée
     * @return Statut de la randonnée
     * @see Statut
     */
    public Statut getStatut() {
        return statut;
    }

    /**
     * Setter sur le statut de la randonnée.
     * 4 valeurs possibles : 
     * Ø PLAN : Randonnée en cours de planification, votes en cours
     * Ø SONDAGE_CLOS : Sondage de la randonnée cloturé, le Team Leader a choisi 
     * une date (un des éléments "Vote"), les membres peuvent s'inscrire
     * Ø ORGA_CLOS : Organisation de la randonnée cloturée, plus personne ne peut s'inscrire
     * Ø ANNULEE : Si la date de la randonnée est dépassée ou si la randonnée est trop chère
     * (coût fixe de la randonnée et coût variable par participant supérieurs à la trésorerie)
     * au moment où le Team Leader essaie de cloturée l'organisation de la randonnée
     * @param statut Nouveau statut de la randonnée
     * @see Statut
     */
    public void setStatut(Statut statut) {
        this.statut = statut;
    }

    /**
     * Getter sur le Team Leader de la randonnée
     * @return Team Leader de la randonnée
     */
    public Long getTeamLeader() {
        return teamLeader;
    }

    /**
     * Setter sur le Team Leader de la randonnée
     * @param teamLeader Nouveau Team Leader de la randonnée
     */
    public void setTeamLeader(Long teamLeader) {
        this.teamLeader = teamLeader;
    }
    
    
}
