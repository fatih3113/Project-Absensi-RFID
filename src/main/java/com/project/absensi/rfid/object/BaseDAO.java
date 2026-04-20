/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.project.absensi.rfid.object;

import java.util.List;

/**
 *
 * @author ACER
 */
public interface BaseDAO<T> {
    void save(T entity);
    void update(int index, T entity);
    void delete(int index);
    List<T> findAll();
    T findByIndex(int index);    
}