package com.mimsapp.stemmer.engine.service;

import java.util.List;

import com.mimsapp.stemmer.engine.domain.KbbiWordInfo;
import com.mimsapp.stemmer.engine.domain.WordInfo;

public interface StemmerService {
	
	List<KbbiWordInfo> getWordInfoFromKbbi(String word);
	
	WordInfo getWordInfoByAlgorithm(String word);
}
