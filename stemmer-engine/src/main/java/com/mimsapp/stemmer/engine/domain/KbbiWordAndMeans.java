package com.mimsapp.stemmer.engine.domain;

public class KbbiWordAndMeans {
	
	private Integer id;
	
	private String word;
	
	private Integer wordId;
	
	private String means;
	
	private String usages;
	
	public KbbiWordAndMeans() {
		// Default
	}
	
	public KbbiWordAndMeans(Integer id, String word, Integer wordId, String mean, String usage) {
		
		setId(id);
		setWord(word);
		setWordId(wordId);
		setMeans(mean);
		setUsages(usage);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public Integer getWordId() {
		return wordId;
	}

	public void setWordId(Integer wordId) {
		this.wordId = wordId;
	}

	public String getMeans() {
		return means;
	}

	public void setMeans(String means) {
		this.means = means;
	}

	public String getUsages() {
		return usages;
	}

	public void setUsages(String usages) {
		this.usages = usages;
	}
}
