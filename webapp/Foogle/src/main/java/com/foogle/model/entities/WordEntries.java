package com.foogle.model.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "word_entries")
public class WordEntries extends AbstractEntity
{
	@Column(name = "word", nullable = false)
	private String word;

	public String getWord()
	{
		return word;
	}

	public void setWord(String word)
	{
		this.word = word;
	}
}