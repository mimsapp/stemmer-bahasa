package com.mimsapp.stemmer.engine.util;

import com.mimsapp.stemmer.engine.domain.KbbiWordAndMeans;
import com.mimsapp.stemmer.engine.domain.KbbiWordInfo;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

public class ScraperUtil {

	private static final String separator = String.valueOf((char) 183);


	public static Document getDocument(String url) { 
		
		Document doc = null;
		
		try {
			URI uri = URI.create(url);
	        doc = Jsoup.connect(url).get();
	        
	        for (Element refresh : doc.select("html head meta[http-equiv=refresh]")) {
	
	            Matcher m = Pattern.compile("(?si)\\d+;\\s+url=(.+)|\\d+")
	                               .matcher(refresh.attr("content"));
	
	            if (m.matches()) {
	                if (m.group(1) != null) {
	                    doc = Jsoup.connect(uri.resolve(m.group(1)).toString()).
	                    	header("Accept-Encoding", "gzip, deflate")
	        	            .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
	        	            .maxBodySize(0)
	        	            .get();
	                }
	                break;
	            }
	        }
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		return doc;
	}
	
	public static String getHtmlText(String url) {
		
		HttpClient httpclient = new HttpClient();
        
        httpclient.getHttpConnectionManager().getParams().setConnectionTimeout(3000);
        httpclient.getHttpConnectionManager().getParams().setSoTimeout(3000);
        httpclient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
        
        GetMethod method = new GetMethod(url);
        String htmlText = null;
        
        try {
        	int responseCode = httpclient.executeMethod(method);
        	
	        if (responseCode >= 200 && responseCode < 300) {
	        	htmlText = CommonUtil.getStreamText(method.getResponseBodyAsStream());
	        } else {
	        	System.out.println("Unsuccessfull Request with status code : "
	        			+responseCode+" "+CommonUtil.getStreamText(
	        			method.getResponseBodyAsStream()));
	        }
    	} catch (Exception e) {
    		e.printStackTrace();
    	} finally {
    		method.releaseConnection();
    	}
        
        return htmlText;
	}
	
	public static String getRootWordText(Element content) {
		
		Element word = content.select("div#word").first();
		
		Element rootWordDiv = word.select("div#w2").first();
		Element ul = rootWordDiv.select("ul#u2").first();
		Element li = ul.getElementsByTag("li").first();
		Element a = li.getElementsByTag("a").first();
		Element sup = a.getElementsByTag("sup").first();
		String headerNote = sup != null ? sup.text() : null;
		
		return headerNote != null ? a.text().replace(headerNote, "") : a.text();
	}
	
	public static int getTotalHomographWord(Element content) {
		
		int total = 0;
		
		Element word = content.select("div#word").first();
		Element rootWordDiv = word.select("div#w1").first();
		if(rootWordDiv != null) {
			Element ul = rootWordDiv.select("ul#u1").first();
			if(ul != null) {
				Elements listOfli = ul.getElementsByTag("li");
				total = listOfli.size();
			}
		}
		
		return total;
	}
	
	public static boolean isWordFoundOnKbbi(Element content) {
		
		boolean found = true;
		
		Element word = content.select("div#word").first();
		Element rootWordDiv = word.select("div#w2").first();
		if(rootWordDiv != null) {
			Element ul = rootWordDiv.select("ul#ul2").first();
			if(ul != null) {
				Elements listOfli = ul.getElementsByTag("li");
				if(listOfli == null || listOfli.isEmpty()) {
					found = false;
				}
			}
		}
		
		return found;
	}
	
	public static Map<String, KbbiWordInfo> getWordInfoMap(List<String> wordDetails, int wordId) {
		
		Map<String, KbbiWordInfo> map = new LinkedHashMap<String, KbbiWordInfo>();
		
		for(String wordDetail : wordDetails) {
			Document detDoc = Jsoup.parse("<div id='parse'>"+wordDetail+"</div>");
			Element test = detDoc.select("div#parse").first();
			int index = 0;
			String currentWord = null;
			String wordMeans = null;
			
			for(Node node : test.childNodes()) {
				KbbiWordInfo info = new KbbiWordInfo();
				if(currentWord != null) {
					info = map.get(currentWord.replaceAll(separator, ""));
				}
				
				if(index < 2) {
					Map<Integer, String> resultMap = updateWordInfoHeader(node, 
							currentWord, info, index);
					index = Integer.parseInt(resultMap.get(1));
					currentWord = resultMap.get(2);
					
					if(info.getWord() != null) {
						map.put(info.getWord().replaceAll(separator, ""), info);
					}
				} else {
					if(node instanceof TextNode) {
						TextNode text = (TextNode) node;
						if(text.toString() != null && text.toString().length() > 0) {
							wordMeans = text.toString();
						}
					} else if(node instanceof Element) {
						Element usage = (Element) node;
						if(usage.select("em").first() != null && 
								wordMeans != null) {
							info.addKbbiWordAndMeans(new KbbiWordAndMeans(0, null, wordId, wordMeans,
									usage.select("em").first().text()));
							wordMeans = null;
						} else if(wordMeans != null) {
							info.addKbbiWordAndMeans(new KbbiWordAndMeans(0, null, wordId,
									wordMeans, ""));
						}
					}
				}
			}
		}
		
		return map;
	}
	
	private static Map<Integer, String> updateWordInfoHeader(Node node, 
			String currentWord, KbbiWordInfo info, int index) {
		
		if(node instanceof Element) {
			Element w = (Element) node;
			if(w.select("b").first() != null) {
				if(!w.text().startsWith("--")) {
					currentWord = CommonUtil.getCleanWord(w.text());
					info.setWord(currentWord);
					info.setSeparatedWord(w.text());
					++index;
				}
			} else {
				if(index == 1) {
					if(w.select("em").first() != null) {
						info.setWordType(w.text());
						++index;
					}
				}
			}
		}
		Map<Integer, String> map = new HashMap<Integer, String>();
		map.put(1, String.valueOf(index));
		map.put(2, currentWord);
		
		return map;
	}
}
