package com.mimsapp.stemmer.engine.domain;

import java.util.ArrayList;
import java.util.List;

public class KbbiWordInfo {
	
	private Integer id;
		
	private String word;
	
	private Integer wordId;
	
	private String separatedWord;
	
	private String wordType;
	
	private String rootWord;
	
	private String prefixes;
	
	private String suffixes;
	
	private List<KbbiWordAndMeans> kbbiWordAndMeans;
	
	private List<String> relatedWords;
	
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

	public String getSeparatedWord() {
		return separatedWord;
	}

	public void setSeparatedWord(String separatedWord) {
		this.separatedWord = separatedWord;
	}

	public String getWordType() {
		return wordType;
	}

	public void setWordType(String wordType) {
		this.wordType = wordType;
	}

	public String getRootWord() {
		return rootWord;
	}

	public void setRootWord(String rootWord) {
		this.rootWord = rootWord;
	}

	public List<KbbiWordAndMeans> getKbbiWordAndMeans() {
		return kbbiWordAndMeans;
	}

	public void addKbbiWordAndMeans(KbbiWordAndMeans wordAndMeans) {
		if(kbbiWordAndMeans == null) {
			kbbiWordAndMeans = new ArrayList<KbbiWordAndMeans>();
		}
		if(!(wordAndMeans.getMeans().trim().length() == 0 
				&& wordAndMeans.getUsages().trim().length() == 0) 
				&& (wordAndMeans.getMeans().matches(".*[a-zA-Z]+.*") 
				|| wordAndMeans.getUsages().matches(".*[a-zA-Z]+.*"))) {
			wordAndMeans.setId(kbbiWordAndMeans.size() + 1);
			wordAndMeans.setWord(word);
			kbbiWordAndMeans.add(wordAndMeans);
		}
	}
	
	public String getPrefixes() {
		return prefixes;
	}

	public void setPrefixes(String prefixes) {
		this.prefixes = prefixes;
	}

	public String getSuffixes() {
		return suffixes;
	}

	public void setSuffixes(String suffixes) {
		this.suffixes = suffixes;
	}

	public List<String> getRelatedWords() {
		return relatedWords;
	}

	public void setRelatedWords(List<String> relatedWords) {
		this.relatedWords = relatedWords;
	}

	public void setKbbiWordAndMeans(List<KbbiWordAndMeans> kbbiWordAndMeans) {
		this.kbbiWordAndMeans = kbbiWordAndMeans;
	}

	public String toString() {
		
		String text = word+" | "+wordType+" |"+rootWord+" | "+prefixes+" | "+suffixes+" : ";
		if(kbbiWordAndMeans != null) {
			int i = 1;
			for(KbbiWordAndMeans e : kbbiWordAndMeans) {
				text += i+"."+e.getMeans()+" -> "+e.getUsages()+" , ";
				++i;
			}
		}
		
		return text;
	}
}
