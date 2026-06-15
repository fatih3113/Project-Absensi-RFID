package com.project.absensi.rfid.dao;

import java.util.List;
import org.bson.conversions.Bson;

/**
 *
 * @author ACER
 * sebagai tempat dasar standar oprasi CRUD 
 */

public interface BaseDAO<T> { //<t> generic type parameter, ini bisa di gunakan di entitas mana saja 
    
    // Operasi CRUD Dasar
    void save(T entity);
    void update(Bson filter, T entity);
    void delete(Bson filter);
    
    // Operasi Searching/Reading
    List<T> findAll();
    T findOne(Bson filter); // Mencari satu data spesifik
    List<T> findMany(Bson filter); // Mencari banyak data berdasarkan kriteria
}