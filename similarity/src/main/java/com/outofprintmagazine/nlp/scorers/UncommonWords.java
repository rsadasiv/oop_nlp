package com.outofprintmagazine.nlp.scorers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.outofprintmagazine.nlp.Score;
import com.outofprintmagazine.nlp.Ta;
import com.outofprintmagazine.nlp.scorers.categorical.DocumentCategoricalScorer;
import com.outofprintmagazine.nlp.scorers.descriptive.SentenceDescriptiveScorer;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;

public class UncommonWords extends DictScorerImpl implements DocumentCategoricalScorer, SentenceDescriptiveScorer {
	
//	public Uncommon() {
//		super();
//	}
	
	public UncommonWords(Ta ta) throws IOException {
		super(ta, "C:\\Users\\rsada\\git\\oop_nlp\\similarity\\resources\\COCA\\en_uncommon.txt");
	}

	
	@Override
	public List<Score> scoreDocument(CoreDocument document) throws IOException {
		ArrayList<String> rawScores = new ArrayList<String>();
		for (CoreSentence sentence : document.sentences()) {
			List<CoreLabel> tokens = sentence.tokens();
			for (int i = 0; i < tokens.size(); i++) {
				String score = scoreLemma(tokens.get(i));
				if (score != null ) {
					rawScores.add(tokens.get(i).lemma());
				}
			}
		}

		return(rawScoresToScoreList(rawScores, document));
	}
	
	@Override
	public List<Integer> scoreSentence(CoreSentence sentence) throws IOException {
		ArrayList<Integer> retval = new ArrayList<Integer>();
		List<CoreLabel> tokens = sentence.tokens();
		for (int i = 0; i < tokens.size(); i++) {
			String score = scoreLemma(tokens.get(i));
			if (score != null) {
				retval.add(new Integer(1));
			}
			else {
				retval.add(new Integer(0));
			}
		}
		return retval;
	}

}