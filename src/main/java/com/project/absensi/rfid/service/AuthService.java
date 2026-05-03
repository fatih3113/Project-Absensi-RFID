/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.project.absensi.rfid.service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.project.absensi.rfid.object.MongoManager;
import org.bson.Document;

/**
 *
 * @author ACER
 */


public class AuthService {
    
    public boolean login(String username, String password) {
        try {
            // Mengambil koneksi database dari MongoManager
            MongoDatabase database = MongoManager.getDatabase();
            
            // Mengakses koleksi admin_users (sesuai di MongoDB Compass kamu)
            MongoCollection<Document> col = database.getCollection("admin_users");
            
            // Mencari user dengan username dan password yang cocok
            Document query = new Document("username", username)
                                .append("password", password);
            
            Document userFound = col.find(query).first();
            
            // Jika userFound tidak null, berarti login berhasil
            return userFound != null;
            
        } catch (Exception e) {
            System.err.println("Error saat login: " + e.getMessage());
            return false;
        }
    }
}