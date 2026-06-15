/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.project.absensi.rfid.util;

import com.project.absensi.rfid.service.AuthService;

/**
 *
 * @author ACER
 */
public class UserInjector {

    public static void main(String[] args) {
        AuthService userService = new AuthService();
        userService.registerUser("Fatih mubarok", "fatih", "123"); 
    }
    
}
