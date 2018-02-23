package com.mimsapp.stemmer.engine.domain;

public class RootWord {
	
	private int id;
	
	private String word;
	
	private String wordType;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getWordType() {
		return wordType;
	}

	public void setWordType(String wordType) {
		this.wordType = wordType;
	}
}
