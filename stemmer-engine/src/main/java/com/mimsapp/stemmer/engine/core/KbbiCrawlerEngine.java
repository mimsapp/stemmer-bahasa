package com.mimsapp.stemmer.engine.core;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.mimsapp.stemmer.engine.domain.KbbiWordInfo;
import com.mimsapp.stemmer.engine.util.CommonUtil;
import com.mimsapp.stemmer.engine.util.Constants;
import com.mimsapp.stemmer.engine.util.ScraperUtil;

public class KbbiCrawlerEngine implements DictionaryCrawlerEngine {
	
	private String getDetailText(String desc) {
		
		Document descDetail = Jsoup.parse(desc);
		descDetail.select("br").append("#");
		String detailText = null;
		Element sup = descDetail.getElementsByTag("sup").first();
		String headerNote = sup != null? sup.text() : null;
	
		try {
			String detailHtml = headerNote == null ? descDetail.html() : 
				descDetail.html().replace("<sup>"+headerNote+"</sup>", "");
			detailText = new String(detailHtml.getBytes(), "UTF-8");
		} catch(UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return detailText;
	}
	
	private Map<String, KbbiWordInfo> getKbbiWordInfoMap(List<String> wordDetails, String rootWord, Integer wordId) {
		
		Map<String, KbbiWordInfo> tmpMap = ScraperUtil.getWordInfoMap(wordDetails, wordId);
		String separator = String.valueOf((char) 183);
		Map<String, KbbiWordInfo> resultMap = new HashMap<String, KbbiWordInfo>();
		
		for(Map.Entry<String, KbbiWordInfo> entry : tmpMap.entrySet()) {
			KbbiWordInfo e = entry.getValue();
			if(e.getSeparatedWord().length() > 1) {
				Map<String, List<String>> prefixSuffixMap = CommonUtil.
						getAffixes(e.getSeparatedWord().split(separator), 
						rootWord, 0, 1);
				
				e.setPrefixes(CommonUtil.getListAsString(
						prefixSuffixMap.get(Constants.PREFIX)));
				e.setSuffixes(CommonUtil.getListAsString(
						prefixSuffixMap.get(Constants.SUFFIX)));
				e.setRootWord(rootWord);
				e.setWordId(wordId);
				e.setWord(e.getWord().replaceAll(separator, ""));
				resultMap.put(entry.getKey(), e);
			}
		}
		
		return resultMap;
	}
	
	public List<Map<String, KbbiWordInfo>> getWordInfo(String word) {
		
		List<Map<String, KbbiWordInfo>> list = new ArrayList<Map<String,KbbiWordInfo>>();
		
		Document doc = ScraperUtil.getDocument(Constants.KBBI_URL+word);
		if(doc != null) {
			Element jsdata = doc.getElementById("jsdata");

			if(jsdata != null) {
				if (jsdata.val() != null && !jsdata.val().isEmpty()) {
					JSONArray array = new JSONArray(jsdata.val());
					int index = 0;
					for (int i = 0; i < array.length(); ++i) {
						JSONObject o = array.getJSONObject(i);
						String rootWord = o.getString("w");
						rootWord = rootWord.split("<")[0];
						if (word.contains(rootWord.substring(1))) {
							++index;
							String detailText = getDetailText(o.getString("d"));
							if (detailText != null) {
								String[] arrayOfDetail = detailText.split("#");
								List<String> wordDetails = new ArrayList<String>();
								for (String detail : arrayOfDetail) {
									if (detail.trim().length() > 0) {
										wordDetails.add(detail);
									}
								}
								Map<String, KbbiWordInfo> map = getKbbiWordInfoMap(wordDetails, rootWord, index);
								list.add(map);
							}
						}
					}
				}
			}
		}
		
		return list;
	}	
}
