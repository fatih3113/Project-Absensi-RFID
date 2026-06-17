/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.project.absensi.rfid.object;

/**
 *
 * @author ACER
 */

public class Siswa {

    private String uidRfid;
    private String nis;
    private String namaLengkap;
    private String program;
    private String nomorHp;
    private String foto;
    private int umur;

    public Siswa() {
    }//onstructor kosong agar bisa membuat object otomatis.

    public Siswa(String uidRfid, String nis, String namaLengkap,
            String program, String nomorHp,String foto, int umur) { //membuatt objek siswaa sklaligus datanya

        this.uidRfid = uidRfid;
        this.nis = nis;
        this.namaLengkap = namaLengkap;
        this.program = program;
        this.nomorHp = nomorHp;
        this.umur = umur;
        this.foto = foto;
    }

    @Override
    public String toString() {
        return "Siswa{" +
                "uidRfid=" + uidRfid +
                ", nis=" + nis +
                ", namaLengkap=" + namaLengkap +
                ", program=" + program +
                ", nomorHp=" + nomorHp +
                ", foto="+ foto +
                ", umur=" + umur +
                '}';
    }// mengubah objek mnjadi teks

    public String getUidRfid() {
        return uidRfid;
    }

    public void setUidRfid(String uidRfid) {
        this.uidRfid = uidRfid;
    }

    public String getNis() {
        return nis;
    }

    public void setNis(String nis) {
        this.nis = nis;
    }

    public String getNamaLengkap() {
        return namaLengkap;
    }

    public void setNamaLengkap(String namaLengkap) {
        this.namaLengkap = namaLengkap;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getNomorHp() {
        return nomorHp;
    }

    public void setNomorHp(String nomorHp) {
        this.nomorHp = nomorHp;
    }
    

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto; 
    }

    public int getUmur() {
        return umur;
    }

    public void setUmur(int umur) {
        this.umur = umur;
    }
}