package com.mimsapp.stemmer.engine.dao;

import java.util.List;
import java.util.Map;

import com.mimsapp.stemmer.engine.domain.KbbiWordInfo;
import com.mimsapp.stemmer.engine.domain.WordInfo;

public interface StemmerDao {
	
	void insertKbbiWordInfo(Map<String, KbbiWordInfo> kbbiWordInfoMap);
	
	boolean insertWordInfo(WordInfo wordInfo);
	
	List<KbbiWordInfo> getKbbiWordInfo(String word);
	
	WordInfo getWordInfo(String word);
	
	boolean isRootWord(String word);
}
