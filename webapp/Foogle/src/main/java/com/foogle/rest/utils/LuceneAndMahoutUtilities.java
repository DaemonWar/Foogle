package com.foogle.rest.utils;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLJDBCDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.foogle.model.datasource.MyDataSourceFactory;

public class LuceneAndMahoutUtilities
{
	private static final Logger logger = LoggerFactory.getLogger(LuceneAndMahoutUtilities.class);

	private static LuceneAndMahoutUtilities instance = null;
	
	private Analyzer analyzer1;
	private Analyzer analyzer2;
	
	public static LuceneAndMahoutUtilities getInstance()
	{
		if (instance == null)
		{
			instance = new LuceneAndMahoutUtilities();
		}
		
		return instance;
	}
	
	private LuceneAndMahoutUtilities()
	{   
		analyzer1 = new EnglishAnalyzer(StopAnalyzer.ENGLISH_STOP_WORDS_SET);
		analyzer2 = new KeywordAnalyzer();
	}
	
	public List<String> tokenizerAndStemmer(String entry)
	{
		try
		{
	        List<String> result = new ArrayList<String>();
	        
			TokenStream stream = analyzer1.tokenStream("", new StringReader(entry));
						
			stream.reset();
			
			while(stream.incrementToken())
			{
	            result.add(stream.getAttribute(CharTermAttribute.class).toString());
	        }
			
			stream.close();
			
			return result;
		} catch (Exception e)
		{
			logger.info(e.getMessage());
			
			return null;
		}
	}
	
	public List<String> tokenizer(String entry)
	{
		try
		{
	        List<String> result = new ArrayList<String>();
	        
			TokenStream stream = analyzer2.tokenStream("", new StringReader(entry));
						
			stream.reset();
			
			while(stream.incrementToken())
			{
	            result.add(stream.getAttribute(CharTermAttribute.class).toString());
	        }

			stream.close();
			
			return result;
		} catch (Exception e)
		{
			logger.info(e.getMessage());
			
			return null;
		}
	}

	public Recommender getRecommender()
	{
		try
		{
			DataModel model = new MySQLJDBCDataModel(MyDataSourceFactory.getMySQLDataSource(), "recommendation_entries", "session_id", "term_id", "count", null);
	
			double threshold = 0.1d;
			UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
			UserNeighborhood neighborhood = new ThresholdUserNeighborhood(threshold, similarity, model);
	
			return new GenericUserBasedRecommender(model, neighborhood, similarity);
		} catch (Exception e)
		{
			logger.info(e.getMessage());
			
			return null;
		}
	}
}
