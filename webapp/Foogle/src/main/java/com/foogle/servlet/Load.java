package com.foogle.servlet;

import java.awt.List;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.mongodb.morphia.Morphia;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.foogle.model.dao.DAOMongoSingleton;
import com.foogle.model.dao.DAOTextEntry;
import com.foogle.model.entities.TextEntry;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

/**
 * Servlet implementation class Load
 */
public class Load extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private final Logger logger = LoggerFactory.getLogger(getClass());
	/**
	 * Default constructor. 
	 */
	public Load() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


		MongoClient mongo = DAOMongoSingleton.getMongo();
		Morphia morphia = new Morphia();
		morphia.map(TextEntry.class);
		DAOTextEntry daoTextEntry = new DAOTextEntry(morphia, mongo);

		DBCollection coll = daoTextEntry.getCollection();
		coll.drop();
		coll.dropIndexes();


		// any string value in the data of every field of every document of the collection are indexed
		//mongo.getDB("mydb").getCollection("data").createIndex(new BasicDBObject("$**", "text")); 
		daoTextEntry.getCollection().createIndex(new BasicDBObject("$**", "text")); 

			
		//Get file from resources folder
		ClassLoader classLoader = getClass().getClassLoader();

		// Insert some documents	
		String filesTab[] = {"Brazil.txt","France.txt","Germany.txt","Netherlands.txt","Spain.txt","Uruguay.txt"};
		for(String fileName : filesTab){
			String text = null;
			InputStream inputStream = classLoader.getResourceAsStream("teams"+File.separator+fileName);
		    try {
		    	//text = new String(IOUtils.toString(inputStream, "windows-1252").getBytes(), "UTF-8");
		    	text = new String(IOUtils.toString(inputStream).getBytes());
		    	
		    } finally {
		        inputStream.close();
		    }
		      
		    String title = text.split("\r\n")[0];
		    String source = text.split("\r\n")[1];
		    int startContent = text.indexOf("\r\n", text.indexOf("\r\n", text.indexOf("\r\n")+1)+1);
		    String content = text.substring(startContent, text.length());
		    
			coll.insert(new BasicDBObject("title", title).append("source", source).append("content", content));
		}
		
		//DBObject myDoc2 = coll.findOne();
		//System.out.println("test: "+myDoc2);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
