package com.foogle.model.dao;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.dao.BasicDAO;

import com.foogle.model.entities.TextEntry;
import com.mongodb.MongoClient;

public class DAOTextEntry extends BasicDAO<TextEntry, ObjectId> {
	public DAOTextEntry( Morphia morphia, MongoClient mongo ) {
        super(mongo, morphia, "myDb");
    }

	
}
