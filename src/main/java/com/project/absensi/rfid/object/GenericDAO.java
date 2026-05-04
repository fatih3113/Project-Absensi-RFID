/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.project.absensi.rfid.object;

import com.mongodb.client.MongoCollection;
import org.bson.conversions.Bson;
import java.util.ArrayList;
import java.util.List;


/**
 * implementasi eneric DAO untuk MongoDB yang efisien dan reusable.
 * @param <T>
 * @author ACER
 */


// Implementasi Generic DAO untuk MongoDB (Pertemuan 5-7)
public class GenericDAO<T> implements BaseDAO<T> {
    private final MongoCollection<T> collection;
    private final Class<T> clazz; 

    public GenericDAO(String collectionName, Class<T> clazz) {
        this.clazz = clazz;
        this.collection = MongoManager.getDatabase().getCollection(collectionName, clazz);
    }

    @Override
    public void save(T entity) {
        collection.insertOne(entity);
    }

    @Override
    public void update(Bson filter, T entity) {
        collection.replaceOne(filter, entity);
    }

    @Override
    public void delete(Bson filter) {
        collection.deleteOne(filter);
    }

    @Override
    public List<T> findAll() {
        return collection.find().into(new ArrayList<>());
    }

    @Override
    public T findOne(Bson filter) {
        return collection.find(filter).first();
    }
    
    @Override
    public List<T> findMany(Bson filter) {
        return collection.find(filter).into(new ArrayList<>());
    }

}