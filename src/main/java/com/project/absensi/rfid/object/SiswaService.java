package com.project.absensi.rfid.object;

import com.mongodb.client.model.Filters;
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
 */
public class SiswaService {
    private final GenericDAO<Siswa> siswaRepo;
    
    public SiswaService() {
        this.siswaRepo = new GenericDAO<>("siswa", Siswa.class);
    }
    
    public void tambahSiswa(Siswa siswaBaru) {
        siswaRepo.save(siswaBaru); 
    }
    
    public void tambahSiswa(String uidRfid, String nis, String namaLengkap, String program) {
        Siswa siswaBaru = new Siswa(uidRfid, nis, namaLengkap, program);
        siswaRepo.save(siswaBaru);
    }
    
    public void tampilkanDaftarSiswa() {
        List<Siswa> daftar = siswaRepo.findAll();
        System.out.println("--- Daftar Siswa ---");
        for (Siswa s : daftar) {
            System.out.println(s.toString()); 
        }
    }
    
    /**
     * Menampilkan daftar siswa ke dalam panel UI dalam bentuk Card.
     * Menggunakan "Program" sebagai label informasi.
     */
    public void tampilkanDaftarSiswa(JPanel panelTarget) {
        List<Siswa> daftarSiswa = siswaRepo.findAll();

        // 1. Bersihkan panel lama
        panelTarget.removeAll();
        
        // 2. Buat Grid untuk menampung card (3 kolom)
        // Kita tidak mengubah layout panelTarget secara radikal agar tidak merusak JFrame utama
        JPanel gridPanel = new JPanel(new GridLayout(0, 3, 15, 15));
        gridPanel.setOpaque(false); 
        gridPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); 
        
        for (Siswa s : daftarSiswa) {
            // Card Panel (Warna Orange)
            JPanel cardPanel = new JPanel(new GridLayout(3, 1, 0, 5)); 
            cardPanel.setBackground(new Color(237, 125, 49)); 
            cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 1, true),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));

            JLabel lblNama = new JLabel("Nama: " + s.getNamaLengkap());
            lblNama.setForeground(Color.WHITE);
            lblNama.setFont(new java.awt.Font("Tahoma", 1, 12));
            
            // Menggunakan getProgram()
            JLabel lblProgram = new JLabel("Program: " + s.getProgram());
            lblProgram.setForeground(Color.WHITE);
            
            JButton tombolDetail = new JButton("Detail");
            tombolDetail.setFocusPainted(false);
            tombolDetail.setBackground(Color.WHITE);
            tombolDetail.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Dialog informasi detail menggunakan kata "Program"
                    JOptionPane.showMessageDialog(null, 
                        "UID RFID: " + s.getUidRfid() + 
                        "\nNIS: " + s.getNis() + 
                        "\nNama: " + s.getNamaLengkap() + 
                        "\nProgram: " + s.getProgram());
                }
            });
            
            cardPanel.add(lblNama);
            cardPanel.add(lblProgram);
            cardPanel.add(tombolDetail);

            gridPanel.add(cardPanel);
        }

        // Tambahkan grid ke panel target
        panelTarget.add(gridPanel);

        // Paksa refresh UI agar tidak berantakan
        panelTarget.revalidate();
        panelTarget.repaint();
    }

    public Siswa cariSiswaByRFID(String uid) {
        Bson filter = Filters.eq("uidRfid", uid);
        return siswaRepo.findOne(filter);
    }

    public void perbaruiProgram(String nis, String programBaru) {
        Bson filter = Filters.eq("nis", nis);
        Siswa siswa = siswaRepo.findOne(filter);
        
        if (siswa != null) {
            siswa.setProgram(programBaru); 
            siswaRepo.update(filter, siswa);
        }
    }

    public void hapusSiswa(String nis) {
        Bson filter = Filters.eq("nis", nis);
        siswaRepo.delete(filter);
    }
}