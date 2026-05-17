/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.project.absensi.rfid.object;

import com.mongodb.client.MongoDatabase;
import org.bson.Document;

/**
 *
 * @author ACER
 */

public class TesKoneksi {
    public static void main(String[] args) {
        try {
            System.out.println("Sedang mencoba menghubungkan ke database...");
            
            // Memanggil koneksi melalui MongoManager
            MongoDatabase database = MongoManager.getDatabase();
            
            // ini itu untuk melakukan pengecekan dengan perintah ping 1 untuk memastikan database aktid
            Document ping = new Document("ping", 1);
            database.runCommand(ping);
            
            System.out.println("=========================================");
            System.out.println("STATUS: KONEKSI BERHASIL!");
            System.out.println("Terhubung ke Database: " + database.getName());
            System.out.println("=========================================");
            
            // 3. Opsional: Menampilkan daftar koleksi yang tersedia
            System.out.println("Daftar Koleksi di " + database.getName() + ":");
            for (String name : database.listCollectionNames()) {
                System.out.println("- " + name);
            }

        } catch (Exception e) {
            System.err.println("=========================================");
            System.err.println("STATUS: KONEKSI GAGAL!");
            System.err.println("Pesan Error: " + e.getMessage());
            System.err.println("=========================================");
        }
    }
}