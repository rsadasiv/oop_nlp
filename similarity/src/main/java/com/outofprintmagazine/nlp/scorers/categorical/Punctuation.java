package com.outofprintmagazine.nlp.scorers.categorical;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.outofprintmagazine.nlp.Score;
import com.outofprintmagazine.nlp.Ta;
import com.outofprintmagazine.nlp.scorers.PosScorerImpl;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;

public class Punctuation extends PosScorerImpl implements DocumentCategoricalScorer {
	
	HashMap<String,String> scoreLabelMap = new HashMap<String,String>();
	
	public Punctuation(Ta ta) throws IOException {
		this(ta, Arrays.asList("``", "''","?", "??", "!", ":", ";", ","));
	}
	
	public Punctuation(Ta ta, List<String> tags) throws IOException {
		super(ta, tags);
		initScoreLabelMap();
	}
	
	protected void initScoreLabelMap() {
		scoreLabelMap.put("``", "Quote");
		scoreLabelMap.put("''", "Quote");
		scoreLabelMap.put("?", "Question");
		scoreLabelMap.put("??", "Question");
		scoreLabelMap.put("!", "Exclamation");
		scoreLabelMap.put(":", "Colon");
		scoreLabelMap.put(";", "Semicolon");
		scoreLabelMap.put(",", "Comma");		
	}
	
	@Override
	public List<Score> scoreDocument(CoreDocument document) {
		ArrayList<String> rawScores = new ArrayList<String>();
		for (CoreSentence sentence : document.sentences()) {
			List<CoreLabel> tokens = sentence.tokens();
			for (int i = 0; i < tokens.size(); i++) {
				String score = scoreTag(tokens.get(i));
				if (score != null) {
					rawScores.add(scoreLabelMap.get(score));
				}
			}
		}
		return(rawScoresToScoreList(rawScores, document));
	}
		
}