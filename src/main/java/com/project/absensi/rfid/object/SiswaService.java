package com.project.absensi.rfid.object;

import com.project.absensi.rfid.GUI.AdminPage;
import com.mongodb.client.model.Filters;
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

    public void tambahSiswa(String uidRfid, String nis, String namaLengkap, String program) {
        Siswa siswaBaru = new Siswa(uidRfid, nis, namaLengkap, program);
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

        if (key.isEmpty()) {
            daftarSiswa = DAO.findAll();
        } else {
            daftarSiswa = cariSiswa(key);
        }

        panelTarget.removeAll();
        panelTarget.setLayout(new BorderLayout());
        panelTarget.setBackground(new Color(68, 114, 196));

        JPanel gridPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        gridPanel.setOpaque(false);
        gridPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        try {
            for (Siswa s : daftarSiswa) {
                JPanel cardPanel = new JPanel(new GridLayout(4, 1, 0, 0));
                cardPanel.setBackground(new Color(237, 125, 49));
                cardPanel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.MAGENTA, 1, true),
                        BorderFactory.createEmptyBorder(15, 15, 15, 15)
                ));

                JLabel lblNama = new JLabel("Nama: " + s.getNamaLengkap());
                lblNama.setForeground(Color.WHITE);

                JLabel lblNis = new JLabel("NIS: " + s.getNis());
                lblNis.setForeground(Color.WHITE);

                JLabel lblProgram = new JLabel("Program: " + s.getProgram());
                lblProgram.setForeground(Color.WHITE);

                JPanel controlPanel = new JPanel(new GridLayout(1, 2, 20, 15));
                controlPanel.setBackground(new Color(237, 125, 49));

                JButton tombolEdit = new JButton("Edit");
                tombolEdit.setBackground(Color.ORANGE);
                tombolEdit.setCursor(new Cursor(Cursor.HAND_CURSOR));
                tombolEdit.addActionListener((ActionEvent e) -> {
                    AdminPage.txtUID.setText(s.getUidRfid());
                    AdminPage.txtSiswaNIS.setText(s.getNis());
                    AdminPage.txtSiswaNIS.setEnabled(false);
                    AdminPage.txtSiswaName.setText(s.getNamaLengkap());
                    AdminPage.txtSiswaProgram.setSelectedItem(s.getProgram());
                    AdminPage.btnUpdate.setEnabled(true);
                    AdminPage.btnSave.setEnabled(false);
                });

                JButton tombolDelete = new JButton("Delete");
                tombolDelete.setBackground(Color.RED);
                tombolDelete.setForeground(Color.WHITE);
                tombolDelete.setCursor(new Cursor(Cursor.HAND_CURSOR));
                tombolDelete.addActionListener((ActionEvent e) -> {
                    Object[] options = {"Ya, Hapus", "Batal"};
                    int choice = JOptionPane.showOptionDialog(
                            null,
                            "Apakah Anda ingin menghapus data " + s.getNamaLengkap() + "?",
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
                cardPanel.add(controlPanel);

                gridPanel.add(cardPanel);
            }

            panelTarget.add(gridPanel, BorderLayout.NORTH);
            panelTarget.revalidate();
            panelTarget.repaint();

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
    public void hapusSiswa(String nis) {
        Bson filter = Filters.eq("nis", nis);
        DAO.delete(filter);
        AdminPage.showData("");
        JOptionPane.showMessageDialog(null, "Data siswa berhasil dihapus.");
    }
}