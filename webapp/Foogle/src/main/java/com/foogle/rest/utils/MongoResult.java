package com.foogle.rest.utils;

public class MongoResult implements Comparable<MongoResult>{

	String title;
	String source;
	String content;
	Double scoring;
	public MongoResult(String title, String source, String content,
			String scoring) {
		super();
		this.title = title;
		this.source = source;
		this.content = content;
		this.scoring = Double.parseDouble(scoring);
	}
	public String getTitle() {
		return title;
	}
	public String getSource() {
		return source;
	}
	public String getContent() {
		return content;
	}
	public Double getScoring() {
		return scoring;
	}
	@Override
	public int compareTo(MongoResult o) {
		Double nombre1 =  o.getScoring(); 
		Double nombre2 = this.getScoring(); 
		if (nombre1 > nombre2)  return -1; 
		else if(nombre1 == nombre2) return 0; 
		else return 1; 
	}

}
