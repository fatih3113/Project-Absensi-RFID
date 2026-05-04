/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.project.absensi.rfid.object;

import java.util.List;
import org.bson.conversions.Bson;

/**
 *
 * @author ACER
 */
public interface BaseDAO<T> {
    void save(T entity);
    void update(Bson filter, T entity);
    void delete(Bson filter);
    
    List<T> findAll();
    T findOne(Bson filter);

    List<T> findMany(Bson filter);
}