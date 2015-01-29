package com.foogle.model.dao;

import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.MongoClient;
import com.mongodb.MongoException;

public class DAOMongoSingleton {
	

	private final static Logger logger = LoggerFactory.getLogger(DAOMongoSingleton.class);
	
	private static final int port = 27017;
	private static final String host = "localhost";
	private static MongoClient mongo = null;

	public static MongoClient getMongo() {
		if (mongo == null) {
			try {
				mongo = new MongoClient(host, port);
				logger.debug("New Mongo created with [" + host + "] and ["+ port + "]");
			} catch (MongoException e) {
				logger.error(e.getMessage());
			} catch (UnknownHostException e) {
				logger.error(e.getMessage());
			}
		}
		return mongo;
	}

}
