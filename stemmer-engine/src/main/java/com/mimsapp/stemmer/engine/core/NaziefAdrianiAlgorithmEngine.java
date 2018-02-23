package com.mimsapp.stemmer.engine.core;

import java.util.ArrayList;
import java.util.List;

import com.mimsapp.stemmer.engine.dao.StemmerDao;
import com.mimsapp.stemmer.engine.domain.WordInfo;
import com.mimsapp.stemmer.engine.util.AffixUtil;
import com.mimsapp.stemmer.engine.util.CommonUtil;

public class NaziefAdrianiAlgorithmEngine implements AlgorithmEngine {
	
	private StemmerDao dao;
	
	public void setDao(StemmerDao dao) {
		
		this.dao = dao;
	}
	
	public WordInfo getWordInfo(String word) {
		
		String rootWord = word;
		
		List<String> removedPrefixes = new ArrayList<String>();
		List<String> removedSuffixes = new ArrayList<String>();
		
		if(dao.isRootWord(word)) {
			rootWord = word;
		} else {	
			rootWord = removeInfexionSuffixes(word, removedSuffixes);
			rootWord = removeDerivationSuffixes(rootWord, removedSuffixes);
			if(!dao.isRootWord(rootWord)) {
				String lastSuffixRemoved = removedSuffixes.isEmpty() ? "" : 
					removedSuffixes.get(removedSuffixes.size() - 1);
				
				rootWord = removeDerivationPrefix(rootWord, removedPrefixes, 
						lastSuffixRemoved, 0);
				
				if(!dao.isRootWord(rootWord)) {
					String recodedWord = rootWord;
					
					if(!removedPrefixes.isEmpty()) {
						recodedWord = removedPrefixes.remove(
							removedPrefixes.size() - 1) + rootWord;
					}
					
					rootWord = AffixUtil.recoding(recodedWord, removedPrefixes);
					if(!dao.isRootWord(rootWord)) {
						if(!removedPrefixes.isEmpty()) {
							removedPrefixes.remove(removedPrefixes.size() - 1);
						}
						
						if(!removedSuffixes.isEmpty()) {
							rootWord = recodedWord + removedSuffixes.remove(
									removedSuffixes.size() - 1);
							rootWord = removeDerivationPrefix(rootWord, removedPrefixes, "", 0);
						}
						
						if(!dao.isRootWord(rootWord)) {
							if(!removedPrefixes.isEmpty()) {
								recodedWord = removedPrefixes.remove(
										removedPrefixes.size() - 1) + rootWord;
								rootWord = AffixUtil.recoding(recodedWord, removedPrefixes);
							}
						}
					}
				}
			}
		}
	
		return getWordInfo(word, rootWord, removedPrefixes, removedSuffixes);
	}
	
	private WordInfo getWordInfo(String word, String rootWord, 
			List<String> prefixes, List<String> suffixes) {
		
		WordInfo info = new WordInfo();
		info.setWord(word);
		info.setRootWord(rootWord);
		String prefixesStr = "";
		String suffixesStr = "";
		
		for(int i = 0; i < prefixes.size(); ++i) {
			prefixesStr += prefixes.get(i);
			if(i < prefixes.size() - 1) {
				prefixesStr += ",";
			}
		}
		for(int i = suffixes.size() - 1; i >= 0; --i) {
			suffixesStr += suffixes.get(i);
			if(i > 0) {
				suffixesStr += ",";
			}
		}
		info.setPrefixes(prefixesStr);
		info.setSuffixes(suffixesStr);
		
		return info;
	}
	
	private String removeInfexionSuffixes(String word, List<String> removedSuffixes) {
		
		String updatedWord = word;
		
		for(String inflexionSuffix : AffixUtil.INFLEXION_SUFFIXES) {
			if(word.endsWith(inflexionSuffix)) {
				updatedWord = word.substring(0, word.length() - inflexionSuffix.length());
				removedSuffixes.add(inflexionSuffix);
				
				if(inflexionSuffix.equals("lah") || inflexionSuffix.equals("kah")) {
					updatedWord = removeInfexionSuffixes(updatedWord, removedSuffixes);
				}
				break;
			}
		}
		
		return updatedWord;
	}
	
	private String removeDerivationSuffixes(String word, List<String> removedSuffixes) {
		
		String updatedWord = word;
		
		for(String derivationSuffix : AffixUtil.DERIVATION_SUFFIXES) {
			if(word.endsWith(derivationSuffix)) {
				
				removedSuffixes.add(derivationSuffix);
				updatedWord = word.substring(0, word.length() - derivationSuffix.length());
				
				if(dao.isRootWord(updatedWord)) {
					break;
				}
				if(derivationSuffix.equals("an") && updatedWord.endsWith("k")) {
					updatedWord = updatedWord.substring(0, updatedWord.length() - 1);
					int lastIndex = removedSuffixes.size() - 1;
					removedSuffixes.remove(lastIndex);
					removedSuffixes.add("kan");
				}
				if(dao.isRootWord(updatedWord)) {
					break;
				}
			}
		}
		
		return updatedWord;
	}
	
	private String removeDerivationPrefix(String word, 
			List<String> removedPrefixes, String lastSuffixRemoved, 
			int iteration) {
		
		String updatedWord = word;
		
		if(removedPrefixes.size() < 3 && !AffixUtil.isProhibitedAffix(
				word, lastSuffixRemoved) && !dao.isRootWord(updatedWord) && 
				iteration < 3) {
			
			for(String prefix : AffixUtil.PREFIXES) {
				if(word.startsWith(prefix)) {
					if(!CommonUtil.isStringFoundOnList(removedPrefixes, prefix)) {
						if(prefix.equals("di") || prefix.equals("ke") || 
								prefix.equals("se")) {
							updatedWord = updatedWord.substring(prefix.length());
							removedPrefixes.add(prefix);
						} else {
							updatedWord = AffixUtil.removePrefix(word, removedPrefixes);
						}
					}
					break;
				}
			}
			++iteration;
			updatedWord = removeDerivationPrefix(updatedWord, removedPrefixes, 
					lastSuffixRemoved, iteration);
		}
			
		return updatedWord;
	}
}
