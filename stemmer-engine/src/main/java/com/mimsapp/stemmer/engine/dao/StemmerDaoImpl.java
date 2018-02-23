package com.mimsapp.stemmer.engine.dao;

import java.util.List;
import java.util.Map;

import com.mimsapp.stemmer.engine.domain.KbbiWordAndMeans;
import com.mimsapp.stemmer.engine.domain.WordInfo;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.mimsapp.stemmer.engine.domain.KbbiWordInfo;

public class StemmerDaoImpl implements StemmerDao {
	
	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		
		this.sessionFactory = sessionFactory;
	}

	public void insertKbbiWordInfo(Map<String, KbbiWordInfo> kbbiWordInfoMap) {
		
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
	
		for(Map.Entry<String, KbbiWordInfo> entry : kbbiWordInfoMap.entrySet()) {		
			KbbiWordInfo kbbiWordInfo = entry.getValue();
			if(kbbiWordInfo.getWordType() != null) {
				List<KbbiWordAndMeans> wordAndMeans = kbbiWordInfo.getKbbiWordAndMeans();
				if(wordAndMeans != null) {
					for(KbbiWordAndMeans wordAndMean : wordAndMeans) {
						session.save(wordAndMean);
					}
				}
				session.save(kbbiWordInfo);
			}
		}
		tx.commit();
		session.close();
	}

	public boolean insertWordInfo(WordInfo wordInfo) {
		
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		String id = (String) session.save(wordInfo);
		
		tx.commit();
		session.close();
		
		return id != null;
	}

	@SuppressWarnings("unchecked")
	public List<KbbiWordInfo> getKbbiWordInfo(String word) {
		
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		List<KbbiWordInfo> list = (List<KbbiWordInfo>) session.createQuery("from KbbiWordInfo "
				+ "where word = :word")
				.setString("word", word).list();
		for(KbbiWordInfo kbbiWordInfo : list) {
			if(kbbiWordInfo != null) {
				List<KbbiWordAndMeans> kbbiWordAndMeans = (List<KbbiWordAndMeans>) session
						.createQuery("from KbbiWordAndMeans where word = :word and word_id = :word_id")
						.setString("word", kbbiWordInfo.getWord())
						.setInteger("word_id", kbbiWordInfo.getWordId())
						.list();
				kbbiWordInfo.setKbbiWordAndMeans(kbbiWordAndMeans);
				
				List<String> relatedWords = (List<String>) session.
						createQuery("select word from KbbiWordInfo where word <> :word " +
								"and root_word = :root_word and word_id = :word_id")
								.setString("word", word)
								.setString("root_word", kbbiWordInfo.getRootWord())
								.setInteger("word_id", kbbiWordInfo.getWordId())
								.list();
				kbbiWordInfo.setRelatedWords(relatedWords);
			}
		}
		
		tx.commit();
		session.close();
				
		return list;
	}

	public WordInfo getWordInfo(String word) {
		
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		WordInfo wordInfo = (WordInfo) session.get(WordInfo.class, word);
		
		tx.commit();
		session.close();
		
		return wordInfo;
	}

	public boolean isRootWord(String word) {
		
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		Long count = (Long) session.createQuery("select count(1) from RootWord where word = :word").
				setString("word", word).uniqueResult();
		
		tx.commit();
		session.close();
		
		return count > 0;
	}

}
