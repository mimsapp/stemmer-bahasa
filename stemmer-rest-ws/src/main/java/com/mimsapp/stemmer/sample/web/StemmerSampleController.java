package com.mimsapp.stemmer.sample.web;

import java.util.List;

import com.mimsapp.stemmer.sample.util.JsonMapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mimsapp.stemmer.engine.domain.KbbiWordInfo;
import com.mimsapp.stemmer.engine.domain.WordInfo;
import com.mimsapp.stemmer.engine.service.StemmerService;

@Controller
public class StemmerSampleController {
	
	@Autowired
	private StemmerService service;
	
	@RequestMapping(value = "/word/info/kbbi", method = RequestMethod.GET)
	public @ResponseBody String getKbbiWordInfo(
			@RequestParam("word") String word) {

		List<KbbiWordInfo> kbbiWordInfos = service.getWordInfoFromKbbi(word);

		return JsonMapperUtil.convertToJson(kbbiWordInfos);
	}
	
	@RequestMapping(value = "/word/info/algorithm", method = RequestMethod.GET)
	public @ResponseBody String getAlgorithmWordInfo(
			@RequestParam("word") String word) {

		WordInfo wordInfo = service.getWordInfoByAlgorithm(word);

		return JsonMapperUtil.convertToJson(wordInfo);
	}
}
