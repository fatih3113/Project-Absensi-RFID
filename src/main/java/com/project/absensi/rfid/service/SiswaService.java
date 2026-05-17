package com.project.absensi.rfid.service;

import com.project.absensi.rfid.GUI.AdminPage;
import com.mongodb.client.model.Filters;
import com.project.absensi.rfid.dao.GenericDAO;
import com.project.absensi.rfid.object.Siswa;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.lang.reflect.Field;
import org.bson.conversions.Bson;

/**
 *
 * @author ACER
 */

public class SiswaService {

    private final GenericDAO<Siswa> DAO;

    public SiswaService() {
        this.DAO = new GenericDAO<>("siswa", Siswa.class);
    }

    /**
     * 1. CREATE: Menyimpan data siswa baru ke MongoDB
     */
    public void tambahSiswa(Siswa siswaBaru) {
        DAO.save(siswaBaru);
    }

    public void tambahSiswa(String uidRfid, String nis, String namaLengkap, String program, String nomorhp, int umur) {
        Siswa siswaBaru = new Siswa(uidRfid, nis, namaLengkap, program, nomorhp, umur);
        DAO.save(siswaBaru);
    }

    /**
     * 2. READ (All): Menampilkan semua data siswa ke console
     */
    public void tampilkanDaftarSiswa() {
        List<Siswa> daftar = DAO.findAll();
        System.out.println("--- Daftar Siswa ---");
        
        for (Siswa s : daftar) {
            System.out.println(s.toString());
        }
    }

    /**
     * 2. READ (All/Search): Menampilkan data siswa ke panel GUI
     *
     * @param panelTarget Panel tujuan untuk menampilkan card siswa
     * @param key         Kata kunci pencarian; kosong = tampilkan semua
     */
    
    public void tampilSiswa(JPanel panelTarget, String key) {
        List<Siswa> daftarSiswa;

        if (key.isEmpty()) { //kalau search kue kosong
            daftarSiswa = DAO.findAll();
        } else {
            daftarSiswa = cariSiswa(key); // kalau ada ketword
        }

        panelTarget.removeAll(); // menghapus p[anel lamansebelum di refresh
        panelTarget.setLayout(new BorderLayout());
        panelTarget.setBackground(new Color(68, 114, 196));

        JPanel gridPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        gridPanel.setOpaque(false);
        gridPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        try {
            for (Siswa s : daftarSiswa) {

                JPanel cardPanel = new JPanel(new GridLayout(6, 1, 5, 5));

                cardPanel.setBackground(Color.WHITE);
                cardPanel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                        BorderFactory.createEmptyBorder(15, 15, 15, 15)
                ));

                // Nama
                JLabel lblNama = new JLabel(s.getNamaLengkap().toUpperCase());
                lblNama.setFont(new java.awt.Font("Segoe UI", 1, 16));
                lblNama.setForeground(new Color(44, 62, 80));

                // NIS
                JLabel lblNis = new JLabel("🆔 ID : " + s.getNis());
                lblNis.setForeground(new Color(127, 140, 141));

                // Program
                JLabel lblProgram = new JLabel("🎓 Program : " + s.getProgram());
                lblProgram.setForeground(new Color(127, 140, 141));

                // Nomor HP
                JLabel lblHp = new JLabel("📞 HP : " + s.getNomorHp());
                lblHp.setForeground(new Color(127, 140, 141));

                // Umur
                JLabel lblUmur = new JLabel("🎂 Umur : " + s.getUmur() + " Tahun");
                lblUmur.setForeground(new Color(127, 140, 141));

                // Panel tombol
                JPanel controlPanel = new JPanel(new GridLayout(1, 2, 10, 0));
                controlPanel.setOpaque(false);

                // Tombol Edit
                JButton tombolEdit = new JButton("Edit");
                tombolEdit.setBackground(Color.ORANGE);
                tombolEdit.setCursor(new Cursor(Cursor.HAND_CURSOR));

                tombolEdit.addActionListener((ActionEvent e) -> {

                    AdminPage.txtUID.setText(s.getUidRfid());
                    AdminPage.txtSiswaNIS.setText(s.getNis());
                    AdminPage.txtSiswaNIS.setEnabled(false);
                    AdminPage.txtSiswaName.setText(s.getNamaLengkap());
                    AdminPage.txtSiswaProgram.setSelectedItem(s.getProgram());
                    AdminPage.lblHp.setText(s.getNomorHp());
                    AdminPage.lblUmur.setText(String.valueOf(s.getUmur()));

                    AdminPage.btnUpdate.setEnabled(true);
                    AdminPage.btnSave.setEnabled(false);
                });

                // Tombol Delete
                JButton tombolDelete = new JButton("Delete");

                tombolDelete.setBackground(Color.RED);
                tombolDelete.setForeground(Color.WHITE);

                tombolDelete.setCursor(new Cursor(Cursor.HAND_CURSOR));

                tombolDelete.addActionListener((ActionEvent e) -> {

                    Object[] options = {"Ya, Hapus", "Batal"};

                    int choice = JOptionPane.showOptionDialog(
                            null,
                            "Apakah Anda ingin menghapus data "
                                    + s.getNamaLengkap() + "?",
                            "Konfirmasi Penghapusan",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            options,
                            options[0]
                    );

                    switch (choice) {

                        case JOptionPane.YES_OPTION:
                            hapusSiswa(s.getNis());
                            break;

                        case JOptionPane.NO_OPTION:
                            System.out.println("User memilih: Batal");
                            break;

                        default:
                            break;
                    }
                });

                controlPanel.add(tombolEdit);
                controlPanel.add(tombolDelete);

                cardPanel.add(lblNama);
                cardPanel.add(lblNis);
                cardPanel.add(lblProgram);
                cardPanel.add(lblHp);
                cardPanel.add(lblUmur);
                cardPanel.add(controlPanel);

                gridPanel.add(cardPanel);
            }

            panelTarget.add(gridPanel, BorderLayout.NORTH);
            panelTarget.revalidate();
            panelTarget.repaint();

            
            // Eror Hendling
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 3. READ (Search): Mencari siswa berdasarkan kata kunci
     *
     * @param key Kata kunci pencarian
     * @return List siswa yang cocok
     */
    public List<Siswa> cariSiswa(String key) {
        List<Bson> filters = new ArrayList<>();

        for (Field field : Siswa.class.getDeclaredFields()) {
            if (field.getName().equals("uidRfid")) {
                continue;
            }
            filters.add(Filters.regex(field.getName(), key, "i"));
        }

        return DAO.findMany(Filters.or(filters));
    }

    /**
     * 4. UPDATE: Memperbarui data siswa
     *
     * @param newS Objek Siswa dengan data terbaru
     */
    public void updateSiswa(Siswa newS) {
        Bson filter = Filters.eq("nis", newS.getNis());
        Siswa s = DAO.findOne(filter);
        if (s != null) {
            DAO.update(filter, newS);
            AdminPage.showData("");
            JOptionPane.showMessageDialog(null, "Data siswa berhasil diperbarui!");
        }
    }

    /**
     * 5. DELETE: Menghapus data siswa berdasarkan NIS
     *
     * @param nis NIS siswa yang akan dihapus
     */
    
    //Method Hapus Data Siswa
    public void hapusSiswa(String nis) {
        Bson filter = Filters.eq("nis", nis);
        DAO.delete(filter);
        AdminPage.showData("");
        JOptionPane.showMessageDialog(null, "Data siswa berhasil dihapus.");
    }
}