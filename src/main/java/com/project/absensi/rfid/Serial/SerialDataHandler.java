/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.project.absensi.rfid.Serial;

/**
 *
 * @author ACER
 */
public interface SerialDataHandler<T> {
    void onDataReceived(T data);
}
