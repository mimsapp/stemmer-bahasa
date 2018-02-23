package com.mimsapp.stemmer.engine.core;

import java.util.List;
import java.util.Map;

import com.mimsapp.stemmer.engine.domain.KbbiWordInfo;

public interface DictionaryCrawlerEngine {
	
	List<Map<String, KbbiWordInfo>> getWordInfo(String word);
}
