package com.mimsapp.stemmer.engine.core;

import com.mimsapp.stemmer.engine.dao.StemmerDao;
import com.mimsapp.stemmer.engine.domain.WordInfo;

public interface AlgorithmEngine {

	void setDao(StemmerDao dao);
	
	WordInfo getWordInfo(String word);
}
