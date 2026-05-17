package com.project.absensi.rfid.dao;

import com.mongodb.client.MongoCollection;
import com.project.absensi.rfid.object.MongoManager;
import org.bson.conversions.Bson;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ACER
 */

/**
 * Implementasi Generic DAO untuk MongoDB yang efisien dan reusable.
 * @param <T>
 */
public class GenericDAO<T> implements BaseDAO<T> { //implementasi CRUD MongoDB yang reusable
    private final MongoCollection<T> collection;
    private final Class<T> clazz;

    // Konstruktor menerima nama koleksi dan kelas entitas untuk mapping otomatis
    public GenericDAO(String collectionName, Class<T> clazz) {
        this.clazz = clazz;
        this.collection = MongoManager.getDatabase().getCollection(collectionName, clazz);
    } //menghubngkan generik dan mongodb

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
    }// mengambil semuda data dan di masuian ke array list

    @Override
    public T findOne(Bson filter) {
        return collection.find(filter).first();
    }//mengambil data pertama yang cocok

    @Override
    public List<T> findMany(Bson filter) {
        return collection.find(filter).into(new ArrayList<>());
    }
}//mengambil banyuak data dari hasil pencaharian