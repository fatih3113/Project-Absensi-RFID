/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.project.absensi.rfid.service;

/**
 *
 * @author ACER
 */

public class AuthService {

    public boolean login(String username, String password) {
        return username.equals("admin") && password.equals("123");
    }
}