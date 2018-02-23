package com.mimsapp.stemmer.engine.util;

import java.util.List;

public class AffixUtil {
	
	public static final String[] INFLEXION_SUFFIXES = new String[] {
		"lah", "kah", "ku", "mu", "nya"
	};

	public static final String[] DERIVATION_SUFFIXES = new String[] {
		"i", "an"
	};
	
	public static final String[] PREFIXES = new String[] {
		"di", "me", "ke", "te", "be", "se", "pe"
	};
	
	public static boolean isProhibitedAffix(String word, String lastFoundSuffix) {
		
		boolean value = false;
		
		if(word.startsWith("be") && lastFoundSuffix.equals("i")) {
			value = true;
		} else if(word.startsWith("di") && lastFoundSuffix.equals("an")) {
			value = true;
		} else if(word.startsWith("ke") && (lastFoundSuffix.equals("i") || 
				lastFoundSuffix.equals("kan"))) {
			value = true;
		} else if(word.startsWith("me") && lastFoundSuffix.equals("an")) {
			value = true;
		} else if(word.startsWith("se") && (lastFoundSuffix.equals("i") || 
				lastFoundSuffix.equals("kan"))) {
			value = true;
		}
		
		return value;
	}
	
	public static String removePrefix(String word, List<String> removedPrefixes) {
		
		String cleanedWord = word;
		
		if(word.startsWith("be")) {
			cleanedWord = removePrefixBe(cleanedWord, removedPrefixes);
		} else if(word.startsWith("te")) {
			cleanedWord = removePrefixTe(cleanedWord, removedPrefixes);
		} else if(word.startsWith("pe")) {
			cleanedWord = removePrefixPe(cleanedWord, removedPrefixes);
		} else if(word.startsWith("me")) {
			cleanedWord = removePrefixMe(cleanedWord, removedPrefixes);
		}
		
		return cleanedWord;
	}
	
	private static String removePrefixBe(String word, List<String> removedPrefixes) {
		
		String cleanedWord = word;
		char[] alphabets = word.toCharArray();
		
		if(word.length() >= 4 && word.startsWith("ber") && 
				isVocal(alphabets[3])) {
			cleanedWord = word.substring(3);
			removedPrefixes.add("ber");
			
		} else if(word.length() >= 7 && word.startsWith("ber") &&
				!isVocal(alphabets[3]) && alphabets[3] != 'r' &&
				(alphabets[5] != 'e' || alphabets[6] != 'r')) {
			cleanedWord = word.substring(3);
			removedPrefixes.add("ber");
			
		} else if(word.length() >= 8 && word.startsWith("ber") &&
				!isVocal(alphabets[3]) && alphabets[3] != 'r' &&
				alphabets[5] == 'e' && alphabets[6] == 'r' &&
				isVocal(alphabets[7])) {
			cleanedWord = word.substring(3);
			removedPrefixes.add("ber");
		} else if(word.equals("belajar")) {
			cleanedWord = word.substring(3);
			removedPrefixes.add("bel");
		} else if(word.length() >= 6 && word.startsWith("be") && 
				!isVocal(alphabets[2]) && alphabets[2] != 'r' &&
				alphabets[2] != 'l' && alphabets[3] == 'e' &&
				alphabets[4] == 'r' && !isVocal(alphabets[5])) {
			cleanedWord = word.substring(2);
			removedPrefixes.add("be");
		}
		
		return cleanedWord;
	}
	
	private static String removePrefixTe(String word, List<String> removedPrefixes) {
		
		String cleanedWord = word;
		char[] alphabets = word.toCharArray();
		
		if(word.length() >= 4 && word.startsWith("ter") && isVocal(alphabets[3])) {
			cleanedWord = word.substring(3);
			removedPrefixes.add("ter");
		} else if(word.length() >= 7 && word.startsWith("ter") && !isVocal(alphabets[3]) 
				&& alphabets[3] != 'r' && alphabets[4] == 'e' && alphabets[5] == 'r' &&
				isVocal(alphabets[6])) {
			cleanedWord = word.substring(3);
			removedPrefixes.add("ter");
		} else if(word.length() >= 6 && word.startsWith("ter") && !isVocal(alphabets[3]) 
				&& alphabets[3] != 'r' && (alphabets[4] != 'e' || alphabets[5] != 'r')) {
			cleanedWord = word.substring(3);
			removedPrefixes.add("ter");
		} else if(word.length() >= 6 && word.startsWith("te") && !isVocal(alphabets[2]) 
				&& alphabets[2] != 'r' && alphabets[3] == 'e' && alphabets[4] == 'r' &&
				isVocal(alphabets[5])) {
			cleanedWord = word.substring(2);
			removedPrefixes.add("te");
		} 	
		
		return cleanedWord;
	}
	
	private static String removePrefixPe(String word, List<String> removedPrefixes) {
		
		String cleanedWord = word;
		char[] alphabets = word.toCharArray();
		
		if(word.length() >= 4 && word.startsWith("pe") && (alphabets[2] == 'w' 
				|| alphabets[2] == 'y') && isVocal(alphabets[3])) {
			cleanedWord = word.substring(2);
			removedPrefixes.add("pe");
		} else if(word.length() >= 4 && word.startsWith("per") &&
				isVocal(alphabets[3])) {
			cleanedWord = word.substring(3);
			removedPrefixes.add("per");
		} else if(word.length() >= 7 && word.startsWith("per") && 
				!isVocal(alphabets[3]) && alphabets[3] != 'r' &&
				(alphabets[5] != 'e' || alphabets[6] != 'r')) {
			cleanedWord = word.substring(3);
			removedPrefixes.add("per");
		} else if(word.length() >= 4 && word.startsWith("pem") && 
				(alphabets[3] == 'b' || alphabets[3] == 'f' || 
				alphabets[3] == 'v')) {
			cleanedWord = word.substring(3);
			removedPrefixes.add("pem");
		} else if(word.length() >= 4 && word.startsWith("pem") &&
				((alphabets[3] == 'r' && isVocal(alphabets[4])) || 
						isVocal(alphabets[3]))) {
			cleanedWord = word.substring(2);
			removedPrefixes.add("pem");
		} else if(word.length() >= 4 && word.startsWith("pen") && 
				(alphabets[3] == 'c' || alphabets[3] == 'd' || 
				alphabets[3] == 'j' || alphabets[3] == 'z')) {
			cleanedWord = word.substring(3);
			removedPrefixes.add("pen");
		} else if(word.length() >= 4 && word.startsWith("pen") &&
				isVocal(alphabets[3])) {
			cleanedWord = word.substring(3);
			removedPrefixes.add("pen");
		} else if(word.length() >= 5 && word.startsWith("peng") &&
				(alphabets[4] == 'g' || alphabets[4] == 'h' || 
				alphabets[4] == 'q')) {
			cleanedWord = word.substring(4);
			removedPrefixes.add("peng");
		} else if(word.length() >= 5 && word.startsWith("peng") &&
				isVocal(alphabets[4])) {
			cleanedWord = word.substring(4);
			removedPrefixes.add("peng");
		} else if(word.length() >= 5 && word.startsWith("peny") &&
				isVocal(alphabets[4])) {
			cleanedWord = "s"+word.substring(4);
			removedPrefixes.add("peny");
		} else if(word.equals("pelajar")) {
			cleanedWord = "ajar";
			removedPrefixes.add("pel");
		} else if(word.length() >= 4 && word.startsWith("pel") &&
				isVocal(alphabets[3])) {
			cleanedWord = word.substring(3);
			removedPrefixes.add("pel");
		} else if(word.length() >= 5 && word.startsWith("pe") &&
				!isVocal(alphabets[2]) && (alphabets[2] != 'r' ||
				alphabets[2] != 'w' || alphabets[2] != 'y' || 
				alphabets[2] != 'l' || alphabets[2] != 'm' ||
				alphabets[2] != 'n') && (alphabets[3] != 'e' || 
				alphabets[4] !='r')) {
			cleanedWord = word.substring(2);
			removedPrefixes.add("pe");
		} else if(word.length() >= 6 && word.startsWith("pe") && 
				!isVocal(alphabets[2]) && (alphabets[2] != 'r' ||
				alphabets[2] != 'w' || alphabets[2] != 'y' || 
				alphabets[2] != 'l' || alphabets[2] != 'm' ||
				alphabets[2] != 'n') && alphabets[3] == 'e' && 
				alphabets[4] =='r' && isVocal(alphabets[5])) {
			cleanedWord = word.substring(2);
			removedPrefixes.add("pe");
		} else if(word.equals("pekerja")) {
			cleanedWord = word.substring(2);
			removedPrefixes.add("pe");
		}
		
		return cleanedWord;
	
	}
	
	private static String removePrefixMe(String word, List<String> removedPrefixes) {
		String cleanedWord = word;
		
		char[] alphabets = word.toCharArray();
		
		if(word.length() >= 4 && word.startsWith("me") && 
				(alphabets[2] == 'l' || alphabets[2] == 'r' || 
				alphabets[2] == 'w' || alphabets[2] == 'y') &&
				isVocal(alphabets[3])) {
			cleanedWord = word.substring(2);
			removedPrefixes.add("me");
		} else if(word.length() >= 4 && word.startsWith("mem") &&
				(alphabets[3] == 'b' || alphabets[3] == 'f' || 
				alphabets[3] == 'v')) {
			cleanedWord = word.substring(3);
			removedPrefixes.add("mem");
		} else if(word.length() >= 6 && word.startsWith("mempe") &&
				(alphabets[5] == 'r' || alphabets[5] == 'l')) {
			cleanedWord = word.substring(3);
			removedPrefixes.add("mem");
		} else if(word.length() >= 4 && word.startsWith("mem") &&
				((alphabets[3] == 'r' && isVocal(alphabets[4])) || 
						isVocal(alphabets[3]))) {
			cleanedWord = word.substring(2);
			removedPrefixes.add("me");
		} else if(word.length() >= 4 && word.startsWith("men") &&
				(alphabets[3] == 'c' || alphabets[3] == 'd' ||
				alphabets[3] == 'j' || alphabets[3] == 'z')) {
			cleanedWord = word.substring(3);
			removedPrefixes.add("men");
		} else if(word.length() >= 4 && word.startsWith("men") &&
				isVocal(alphabets[3])) {
			cleanedWord = word.substring(2);
			removedPrefixes.add("me");
		} else if(word.length() >= 5 && word.startsWith("meng") &&
				(alphabets[4] == 'g' || alphabets[4] == 'h' || 
				alphabets[4] == 'q' || alphabets[4] == 'k')) {
			cleanedWord = word.substring(4);
			removedPrefixes.add("meng");
		} else if(word.length() >= 5 && word.startsWith("meng") &&
				isVocal(alphabets[4])) {
			cleanedWord = word.substring(4);
			removedPrefixes.add("meng");
		} else if(word.length() >= 5 && word.startsWith("meny") &&
				isVocal(alphabets[4])) {
			cleanedWord = "s"+word.substring(4);
			removedPrefixes.add("meny");
		} else if(word.length() >= 5 && word.startsWith("memp") &&
				isVocal(alphabets[4]) && alphabets[4] != 'e') {
			cleanedWord = word.substring(3);
			removedPrefixes.add("mem");
		} else {
			if(word.startsWith("memp")) {
				cleanedWord = word.substring(3);
				removedPrefixes.add("mem");
			}
		}
		
		return cleanedWord;
	}
	
	public static String recoding(String word, List<String> removedPrefixes) {
		
		String rootWord = word;
		
		char[] alphabets = word.toCharArray();
		
		if(word.length() >= 4 && (word.startsWith("ber") || 
				word.startsWith("ter") || word.startsWith("per")) &&
				isVocal(alphabets[3])) {
			rootWord = word.substring(2);
			removedPrefixes.add(word.substring(0, 2));
		} else if(word.length() >= 4 && (word.startsWith("pem") || 
				word.startsWith("mem")) && 
				alphabets[3] == 'r' && isVocal(alphabets[4])) {
			rootWord = "p"+word.substring(3);
			removedPrefixes.add("mem");
		} else if(word.length() >= 3 && (word.startsWith("pem") || 
				word.startsWith("mem")) &&
				isVocal(alphabets[3])) {
			rootWord = "p"+word.substring(3);
			removedPrefixes.add(word.substring(0, 3));
		} else if(word.length() >= 4 && (word.startsWith("pen") || 
				word.startsWith("men")) && isVocal(alphabets[3])) {
			rootWord = "t"+word.substring(3);
			removedPrefixes.add(word.substring(0, 3));
		} else if(word.length() >= 5 && (word.startsWith("peng") || 
				word.startsWith("meng")) && isVocal(alphabets[4])) {
			rootWord = "k"+word.substring(4);
			removedPrefixes.add(word.substring(0, 4));
		}
		
		return rootWord;
	}
	
	private static boolean isVocal(char alphabet) {
		
		return alphabet == 'a' || alphabet == 'i' || alphabet == 'u' || 
				alphabet == 'e' || alphabet == 'o';
	}
}
