package com.outofprintmagazine.nlp.scorers.descriptive;

import java.util.ArrayList;
import java.util.List;

import com.outofprintmagazine.nlp.Ta;
import com.outofprintmagazine.nlp.scorers.ScorerImpl;
import com.vader.sentiment.analyzer.SentimentAnalyzer;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreSentence;

public class VaderSentiments extends ScorerImpl implements SentenceDescriptiveScorer {

//	public VaderSentiments() {
//		super();
//	}
	private SentimentAnalyzer sentimentAnalyzer = new SentimentAnalyzer();

	public VaderSentiments(Ta ta) {
		super(ta);
	}
		
	public SentimentAnalyzer getSentimentAnalyzer() {
		return sentimentAnalyzer;
	}

	public void setSentimentAnalyzer(SentimentAnalyzer sentimentAnalyzer) {
		this.sentimentAnalyzer = sentimentAnalyzer;
	}

	@Override
	public List<Integer> scoreSentence(CoreSentence sentence) {
		ArrayList<Integer> retval = new ArrayList<Integer>();
		List<CoreLabel> tokens = sentence.tokens();
		for (int i = 0; i < tokens.size(); i++) {
			try {
				getSentimentAnalyzer().setInputString(sentence.text());
				getSentimentAnalyzer().analyze();
				retval.add(new Integer(new Float(getSentimentAnalyzer().getPolarity().get("compound")*100).intValue()));
			}
			catch (Exception e) {
				//TODO logging
				e.printStackTrace();
				retval.add(new Integer(50));
			}
		}
		return retval;
	}



}
