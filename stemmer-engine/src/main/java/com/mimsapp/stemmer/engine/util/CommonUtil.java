package com.mimsapp.stemmer.engine.util;

import com.mimsapp.stemmer.engine.domain.WordInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommonUtil {
	
	public static WordInfo getCombinedDuplicateWordInfo(
			WordInfo wordInfoFront, WordInfo wordInfoRear, 
			String word) {
		
		WordInfo wordInfo = new WordInfo();
		wordInfo.setWord(word);
		
		if(wordInfoFront.getRootWord().equals(wordInfoRear.getRootWord())) {
			wordInfo.setRootWord(wordInfoFront.getRootWord());
		} else {
			wordInfo.setRootWord(wordInfoFront.getRootWord()+
					"-"+wordInfoRear.getRootWord());
		}
		wordInfo.setPrefixes(wordInfoFront.getPrefixes()+","+
				wordInfoRear.getPrefixes());
		wordInfo.setSuffixes(wordInfoFront.getSuffixes()+","+
				wordInfoRear.getSuffixes());
		
		return wordInfo;
	}
	
	public static boolean isStringFoundOnList(List<String> list, String element) {
		
		boolean value = false;
		
		for(String e : list) {
			if(e.equals(element)) {
				value = true;
				break;
			}
		}
		
		return value;
	}
	
	public static String getCleanWord(String separatedWord) {
		
		char[] wordChars = separatedWord.toCharArray();
		StringBuilder builder = new StringBuilder();
		
		for(int i = 0; i < wordChars.length; ++i) {
			int charAsInteger = Integer.valueOf(wordChars[i]);
			if(charAsInteger != 65533) {
				builder.append(wordChars[i]);
			}
		}
		
		return builder.toString();
	}
	
	public static Map<String, List<String>> getAffixes(String[] arrayOfWord, 
			String root, int start, int end) {
		
		String combinedWord = "";
		Map<String, List<String>> affixMap = new HashMap<String, List<String>>();
		
		if(end == arrayOfWord.length) {
			++start;
			end = start + 1;
		}
		if(start < arrayOfWord.length - 1) {
			for(int i = start; i <= end; ++i) {
				combinedWord += arrayOfWord[i];
			}
			if(combinedWord.startsWith("ng") || combinedWord.startsWith("ny")) {
				combinedWord = combinedWord.substring(2);
			} else {
				combinedWord = combinedWord.substring(1);
			}
			if(!combinedWord.equals(root.substring(1)+"i")) {
				if(!combinedWord.equals(root.substring(1))) {
					affixMap = getAffixes(arrayOfWord, root, start, ++end);
				} else {
					affixMap = getAffixesMap(arrayOfWord, root, start, end, false);
				}
			} else {
				affixMap = getAffixesMap(arrayOfWord, root, start, end, true);
			}
		}
		
		return affixMap;
	}
	
	private static Map<String, List<String>> getAffixesMap(String[] arrayOfWord, 
			String root, int start, int end, boolean iSuffix) {
		
		List<String> prefixes = new ArrayList<String>();
		List<String> suffixes = new ArrayList<String>();
		
		for(int i = 0; i < start; ++i) {
			String prefix = arrayOfWord[i];
			if(i == start - 1) {
				if(!root.startsWith(arrayOfWord[start]) && 
						(arrayOfWord[start].startsWith("ng") || 
						arrayOfWord[start].startsWith("ny"))) {
					prefix = prefix + arrayOfWord[start].substring(0, 2);
				}
			}
			prefixes.add(prefix);
		}
		for(int i = end + 1; i < arrayOfWord.length; ++i) {
			suffixes.add(arrayOfWord[i]);
		}
		if(iSuffix) {
			suffixes.add("i");
		}
		Map<String, List<String>> affixMap = new HashMap<String, List<String>>();
		
		affixMap.put(Constants.PREFIX, prefixes);
		affixMap.put(Constants.SUFFIX, suffixes);
		
		return affixMap;
	}
	
	public static boolean isSeparatedWord(String word) {
		
		return word.contains(String.valueOf((char) 65533));
	}
	
	public static String getListAsString(List<String> list) {
		
		String text = "";
		if(list != null) {
			for(int i = 0; i < list.size(); ++i) {
				text += list.get(i);
				if(i < list.size() - 1) {
					text += ",";
				}
			}
		}
		
		return text;
	}
	
	public static String getStreamText(InputStream in) {
		
		BufferedReader reader = null;
		String text = "";
		
		try {
			reader = new BufferedReader(new InputStreamReader(in));
			String line;
			
			while((line = reader.readLine()) != null) {
				text += line;
			}
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		
		return text;
	}
}
