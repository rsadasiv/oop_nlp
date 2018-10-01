package com.outofprintmagazine.nlp.scorers.categorical;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.outofprintmagazine.nlp.Score;
import com.outofprintmagazine.nlp.Ta;
import com.outofprintmagazine.nlp.scorers.ScorerImpl;
import com.outofprintmagazine.nlp.scorers.descriptive.SentenceDescriptiveScorer;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;

public class Sentiments extends ScorerImpl implements DocumentCategoricalScorer, SentenceDescriptiveScorer {

//	public Sentiments() {
//		super();
//	}
	
	public Sentiments(Ta ta) {
		super(ta);
	}
	
	@Override
	public List<Integer> scoreSentence(CoreSentence sentence) {
		ArrayList<Integer> retval = new ArrayList<Integer>();
		String sentiment = sentence.sentiment();
		List<CoreLabel> tokens = sentence.tokens();
		for (int i = 0; i < tokens.size(); i++) {
			if (sentiment.equals("Negative")) {
				retval.add(new Integer(0));
			}
			else if (sentiment.equals("Neutral")) {
				retval.add(new Integer(50));
			}
			else if (sentiment.equals("Positive")) {
				retval.add(new Integer(100));
			}				
		}
		return retval;
	}
	
	@Override
	public List<Score> scoreDocument(CoreDocument document) throws IOException {
		ArrayList<String> rawScores = new ArrayList<String>();
		for (CoreSentence sentence : document.sentences()) {
			rawScores.add(sentence.sentiment());
		}
		return(rawScoresToScoreList(rawScores, document));
	}

}
