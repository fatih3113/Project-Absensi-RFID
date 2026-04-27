/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.project.absensi.rfid;

import com.project.absensi.rfid.object.Siswa;

/**
 *
 * @author ACER
 */
public class MainApp {
    public static void main(String[] args) {
        // Membuat objek Siswa baru
        Siswa s = new Siswa();
        
        // Pengecekan tipe objek
        if(s instanceof Siswa){
            System.out.println("Objek adalah Siswa");
        } else {
            System.out.println("Bukan objek Siswa");
        }
    }
}
