package com.mimsapp.stemmer.engine.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mimsapp.stemmer.engine.core.AlgorithmEngine;
import com.mimsapp.stemmer.engine.core.DictionaryCrawlerEngine;
import com.mimsapp.stemmer.engine.core.KbbiCrawlerEngine;
import com.mimsapp.stemmer.engine.core.NaziefAdrianiAlgorithmEngine;
import com.mimsapp.stemmer.engine.dao.StemmerDao;
import com.mimsapp.stemmer.engine.domain.WordInfo;
import com.mimsapp.stemmer.engine.domain.KbbiWordInfo;
import com.mimsapp.stemmer.engine.util.CommonUtil;

public class StemmerServiceImpl implements StemmerService {

	private StemmerDao dao;
	
	public void setDao(StemmerDao dao) {
		this.dao = dao;
	}

	public List<KbbiWordInfo> getWordInfoFromKbbi(String word) {
		
		List<KbbiWordInfo> kbbiWordInfoList = new ArrayList<KbbiWordInfo>();
		
		kbbiWordInfoList = dao.getKbbiWordInfo(word);
		
		if(kbbiWordInfoList.isEmpty()) {
			
			DictionaryCrawlerEngine engine = new KbbiCrawlerEngine();
			List<Map<String, KbbiWordInfo>> list = engine.getWordInfo(word);
			
			for(int i = 0; i < list.size(); ++i) {
				Map<String, KbbiWordInfo> map = list.get(i);
				if(i > 0) {
					KbbiWordInfo info = map.get(word);
					map.put(word, info);
				}
				
				dao.insertKbbiWordInfo(map);
				KbbiWordInfo info = map.get(word);
				List<String> relatedWords = new ArrayList<String>();
				
				for(Map.Entry<String, KbbiWordInfo> entry : map.entrySet()) {
					if(!entry.getKey().equals(word)) {
						relatedWords.add(entry.getKey());
					}
				}
				if(info != null) {
					info.setRelatedWords(relatedWords);
					kbbiWordInfoList.add(info);
				}			
			}
		}
		
		return kbbiWordInfoList;
	}

	public WordInfo getWordInfoByAlgorithm(String word) {
		
		WordInfo wordInfo = dao.getWordInfo(word);
		
		if(wordInfo == null) {
			AlgorithmEngine engine = new NaziefAdrianiAlgorithmEngine();
			engine.setDao(dao);
			String[] splittedWords = word.split("-");
			
			if(splittedWords.length == 1) {
				wordInfo = engine.getWordInfo(word);
			} else if(splittedWords.length == 2) {
				WordInfo wordInfoFront = engine.getWordInfo(splittedWords[0]);
				WordInfo wordInfoRear = engine.getWordInfo(splittedWords[1]);
				wordInfo = CommonUtil.getCombinedDuplicateWordInfo(
						wordInfoFront, wordInfoRear, word);
			}
			
			dao.insertWordInfo(wordInfo);
		}
		
		return wordInfo;
	}
}