package com.example.GestionRando;


import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.server.Session;

@SpringBootApplication
public class GestionRandoApplication {
    /*private static Session session;
    private static MongoDatabase db = null;
    private static MongoCollection<Document> collec = null;*/

	public static void main(String[] args) {
		SpringApplication.run(GestionRandoApplication.class, args);
                /*MongoClient mongoClient = new MongoClient("127.0.0.1");
                //Connexion à la bd et à la collection mongoDB
                db = mongoClient.getDatabase("dbDocuments");
                collec = db.getCollection("index");
                mongoClient.close();*/
                
	}
        
       
}
