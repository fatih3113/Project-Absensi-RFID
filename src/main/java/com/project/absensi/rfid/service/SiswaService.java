package com.project.absensi.rfid.service;

import com.project.absensi.rfid.GUI.panel.SiswaPanel;
import com.mongodb.client.model.Filters;
import com.project.absensi.rfid.dao.GenericDAO;
import com.project.absensi.rfid.object.Siswa;
import com.project.absensi.rfid.util.EncryptionUtils;
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

    private final GenericDAO<Siswa> DAO; //DAO khusus object Siswa.

    public SiswaService() {
        this.DAO = new GenericDAO<>("siswa", Siswa.class); // Menghubungkan service ke collection MongoDB bernama siswa
    }

    /**
     * 1. CREATE: Menyimpan data siswa baru ke MongoDB menggunakan Object
     */
    public void tambahSiswa(Siswa siswaBaru) {
        DAO.save(siswaBaru);
    }
    
    /**
     * 1. CREATE: Menyimpan data siswa baru menggunakan Parameter Langsung
     */
    public void tambahSiswa(String uidRfid, String nis, String namaLengkap, String program, String nomorhp, String foto, int umur) {
        Siswa siswaBaru = new Siswa(uidRfid, nis, namaLengkap, program, nomorhp, foto, umur);
        DAO.save(siswaBaru);
    }

    /**
     * 2. READ (Console): Menampilkan semua data siswa ke console/system out
     */
    public void tampilkanDaftarSiswa() {
        List<Siswa> daftar = DAO.findAll();
        System.out.println("--- Daftar Siswa ---");
        for (Siswa s : daftar) {
            System.out.println(s.toString());
        }
    }

    /**
     * 2. READ (GUI Card View): Menampilkan data siswa ke panel GUI berbentuk Kartu
     *
     * @param panelTarget Panel tujuan untuk menampilkan card siswa (jPanel4)
     * @param key         Kata kunci pencarian; kosong = tampilkan semua
     */
    public void tampilSiswa(JPanel panelTarget, String key) {
        List<Siswa> daftarSiswa;

        if (key.isEmpty()) { 
            daftarSiswa = DAO.findAll();
        } else {
            daftarSiswa = cariSiswa(key); 
        }

        panelTarget.removeAll(); // Menghapus isi panel lama sebelum di-refresh
        panelTarget.setLayout(new BorderLayout());
        panelTarget.setBackground(new Color(68, 114, 196));

        JPanel gridPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        gridPanel.setOpaque(false);
        gridPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        try {
            for (Siswa s : daftarSiswa) {

                // MEMBUAT KARTU MAHASISWA (Diset 7 baris untuk menampung komponen Foto + Info + Tombol)
                JPanel cardPanel = new JPanel(new GridLayout(7, 1, 5, 5));
                cardPanel.setBackground(Color.WHITE);
                cardPanel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                        BorderFactory.createEmptyBorder(15, 15, 15, 15)
                ));

                // =============================================================
                // KOMPONEN 1: RENDER FOTO SISWA DARI PATH/LOKASI COMPUTER
                // =============================================================
                JLabel lblFotoSiswa = new JLabel();
                lblFotoSiswa.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                
                if (s.getFoto() != null && !s.getFoto().isEmpty()) {
                    try {
                        java.io.File fileFoto = new java.io.File(s.getFoto());
                        if (fileFoto.exists()) {
                            // Baca file gambar asli dan scale ukurannya menjadi proporsional kotak (misal: 60x60)
                            java.awt.image.BufferedImage img = javax.imageio.ImageIO.read(fileFoto);
                            java.awt.Image resizedImg = img.getScaledInstance(60, 60, java.awt.Image.SCALE_SMOOTH);
                            lblFotoSiswa.setIcon(new javax.swing.ImageIcon(resizedImg));
                            lblFotoSiswa.setText(""); // Hapus teks jika gambar berhasil di-load
                        } else {
                            lblFotoSiswa.setText("📷 (Foto tidak ditemukan)");
                            lblFotoSiswa.setForeground(Color.RED);
                        }
                    } catch (Exception e) {
                        lblFotoSiswa.setText("📷 (Gagal memuat foto)");
                    }
                } else {
                    lblFotoSiswa.setText("👤 (Tidak ada foto)");
                    lblFotoSiswa.setForeground(Color.GRAY);
                }

                // KOMPONEN 2: Menampilkan Nama
                JLabel lblNama = new JLabel(s.getNamaLengkap().toUpperCase());
                lblNama.setFont(new java.awt.Font("Segoe UI", 1, 16));
                lblNama.setForeground(new Color(44, 62, 80));

                // KOMPONEN 3: Menampilkan ID / NIS (Sudah didekrip)
                JLabel lblNis = new JLabel("🆔 ID : " + EncryptionUtils.decrypt(s.getNis()));
                lblNis.setForeground(new Color(127, 140, 141));

                // KOMPONEN 4: Menampilkan Program Kelas
                JLabel lblProgram = new JLabel("🎓 Program : " + s.getProgram());
                lblProgram.setForeground(new Color(127, 140, 141));

                // KOMPONEN 5: Menampilkan Nomor HP
                JLabel lblHpText = new JLabel("📞 Nomor HP : " + s.getNomorHp());
                lblHpText.setForeground(new Color(127, 140, 141));
                
                // KOMPONEN 6: Menampilkan Umur
                JLabel lblUmurText = new JLabel("🎂 Umur : " + s.getUmur() + " Tahun");
                lblUmurText.setForeground(new Color(127, 140, 141));

                // KOMPONEN 7: Panel Tombol Aksi (Edit & Delete)
                JPanel controlPanel = new JPanel(new GridLayout(1, 2, 10, 0));
                controlPanel.setOpaque(false);

                // Tombol Edit
                JButton tombolEdit = new JButton("Edit");
                tombolEdit.setBackground(Color.ORANGE);
                tombolEdit.setCursor(new Cursor(Cursor.HAND_CURSOR));
                
                // Menaruh kembali data kartu ke Form Input ketika tombol edit ditekan
                tombolEdit.addActionListener((ActionEvent e) -> {
                    SiswaPanel.txtUID.setText(s.getUidRfid());
                    SiswaPanel.txtSiswaNIS.setText(EncryptionUtils.decrypt(s.getNis())); // Dekrip NIS saat diedit
                    SiswaPanel.txtSiswaNIS.setEnabled(false);
                    SiswaPanel.txtSiswaName.setText(s.getNamaLengkap());
                    SiswaPanel.txtSiswaProgram.setSelectedItem(s.getProgram());
                    SiswaPanel.lblHp.setText(s.getNomorHp());
                    SiswaPanel.lblUmur.setText(String.valueOf(s.getUmur()));

                    // Mengaktifkan status tombol kontrol di form utama
                    SiswaPanel.btnUpdate.setEnabled(true);
                    SiswaPanel.btnSave.setEnabled(false);
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

                // MENYUSUN KOMPONEN MASUK KE KARTU SISWA (Urutan Vertikal)
                cardPanel.add(lblFotoSiswa); // Baris 1 (Paling Atas)
                cardPanel.add(lblNama);      // Baris 2
                cardPanel.add(lblNis);       // Baris 3
                cardPanel.add(lblProgram);   // Baris 4
                cardPanel.add(lblHpText);    // Baris 5
                cardPanel.add(lblUmurText);  // Baris 6
                cardPanel.add(controlPanel); // Baris 7 (Paling Bawah)

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
     * 3. READ (Search): Mencari siswa berdasarkan kata kunci fleksibel (Regex)
     */
    public List<Siswa> cariSiswa(String key) {
        List<Bson> filters = new ArrayList<>();
        for (Field field : Siswa.class.getDeclaredFields()) { 
            if (field.getName().equals("uidRfid") || field.getName().equals("foto")) { 
                continue; // Melewati pencarian pada field sensitif dan file path gambar
            }
            filters.add(Filters.regex(field.getName(), key, "i")); 
        }
        return DAO.findMany(Filters.or(filters)); 
    }

    /**
     * 4. UPDATE: Memperbarui data siswa di MongoDB
     */
    public void updateSiswa(Siswa newS) {
        Bson filter = Filters.eq("nis", newS.getNis()); 
        Siswa s = DAO.findOne(filter);
        if (s != null) {
            DAO.update(filter, newS);
            SiswaPanel.showData(""); // Me-refresh UI visual panel
            JOptionPane.showMessageDialog(null, "Data siswa berhasil diperbarui!");
        }
    }

    /**
     * 5. DELETE: Menghapus data siswa berdasarkan NIS
     */
    public void hapusSiswa(String nis) {
        Bson filter = Filters.eq("nis", nis); 
        DAO.delete(filter);
        SiswaPanel.showData(""); // Memastikan table/card antarmuka langsung otomatis ter-refresh
        JOptionPane.showMessageDialog(null, "Data siswa berhasil dihapus.");
    }
}