/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.project.absensi.rfid.object;

import com.mongodb.client.model.Filters;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.bson.conversions.Bson;

/**
 * Service untuk menangani CRUD data Siswa ke MongoDB.
 * Disesuaikan dengan atribut: uidRfid, nis, namaLengkap, program.
 * 
 * @author ACER
 */
public class SiswaService {
    // Inisialisasi GenericDAO khusus untuk entitas Siswa
    private final GenericDAO<Siswa> siswaRepo;
    
    public SiswaService() {
        // Menggunakan koleksi "siswa" di MongoDB
        this.siswaRepo = new GenericDAO<>("siswa", Siswa.class);
    }
    
    /**
     * 1. CREATE: Menyimpan data siswa baru
     */
    public void tambahSiswa(Siswa siswaBaru) {
        siswaRepo.save(siswaBaru); 
    }
    
    public void tambahSiswa(String uidRfid, String nis, String namaLengkap, String program) {
        Siswa siswaBaru = new Siswa(uidRfid, nis, namaLengkap, program);
        siswaRepo.save(siswaBaru);
    }
    
    /**
     * 2. READ (Console): Mengambil semua data untuk log sistem
     */
    public void tampilkanDaftarSiswa() {
        List<Siswa> daftar = siswaRepo.findAll();
        System.out.println("--- Daftar Siswa ---");
        for (Siswa s : daftar) {
            System.out.println(s.toString()); 
        }
    }
    
    /**
     * 2. READ (GUI): Menampilkan daftar siswa ke dalam panel UI dalam bentuk Card
     */
    public void tampilkanDaftarSiswa(JPanel panelTarget) {
        List<Siswa> daftarSiswa = siswaRepo.findAll();

        panelTarget.removeAll();
        panelTarget.setLayout(new BorderLayout()); 
        panelTarget.setBackground(new Color(68, 114, 196)); 

        // Grid 3 kolom untuk menampung card siswa
        JPanel gridPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        gridPanel.setOpaque(false); 
        gridPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); 
        
        for (Siswa s : daftarSiswa) {
            // Card Panel (Box Orange)
            JPanel cardPanel = new JPanel(new GridLayout(3, 1, 0, 10)); 
            cardPanel.setBackground(new Color(237, 125, 49)); 
            cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY, 1, true),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
            ));

            JLabel lblNama = new JLabel("Nama: " + s.getNamaLengkap());
            lblNama.setForeground(Color.WHITE);
            
            // Menggunakan getProgram() sesuai class Siswa
            JLabel lblProgram = new JLabel("Program: " + s.getProgram());
            lblProgram.setForeground(Color.WHITE);
            
            JButton tombolEdit = new JButton("Detail");
            tombolEdit.setBackground(Color.ORANGE); 
            tombolEdit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JOptionPane.showMessageDialog(null, "NIS: " + s.getNis() + "\nNama: " + s.getNamaLengkap());
                }
            });
            
            cardPanel.add(lblNama);
            cardPanel.add(lblProgram);
            cardPanel.add(tombolEdit);

            gridPanel.add(cardPanel);
        }

        panelTarget.add(gridPanel, BorderLayout.NORTH);

        panelTarget.revalidate();
        panelTarget.repaint();
    }

    /**
     * 3. READ (One): Mencari satu siswa berdasarkan UID RFID
     */
    public Siswa cariSiswaByRFID(String uid) {
        Bson filter = Filters.eq("uidRfid", uid);
        return siswaRepo.findOne(filter);
    }

    /**
     * 4. UPDATE: Memperbarui data program/jurusan siswa
     */
    public void perbaruiProgram(String nis, String programBaru) {
        Bson filter = Filters.eq("nis", nis);
        Siswa siswa = siswaRepo.findOne(filter);
        
        if (siswa != null) {
            // Karena di class Siswa method set-nya bernama setKelas namun mengisi program
            // Kita gunakan setKelas agar sinkron dengan class model kamu
            siswa.setKelas(programBaru); 
            siswaRepo.update(filter, siswa);
            System.out.println("Data program siswa berhasil diperbarui.");
        }
    }

    /**
     * 5. DELETE: Menghapus siswa berdasarkan NIS
     */
    public void hapusSiswa(String nis) {
        Bson filter = Filters.eq("nis", nis);
        siswaRepo.delete(filter);
        System.out.println("Data siswa berhasil dihapus.");
    }
}