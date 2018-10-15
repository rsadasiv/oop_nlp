package com.outofprintmagazine.nlp.scorers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map.Entry;

import com.outofprintmagazine.nlp.Score;
import com.outofprintmagazine.nlp.Ta;
import com.outofprintmagazine.nlp.scorers.categorical.DocumentCategoricalScorer;
import com.outofprintmagazine.nlp.scorers.descriptive.SentenceDescriptiveScorer;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;

public class Lemmas extends PosScorerImpl implements DocumentCategoricalScorer, SentenceDescriptiveScorer {
	
	
//	public Pronouns() {
//		super();
//	}
	
	public Lemmas(Ta ta) throws IOException {
		this(ta, Arrays.asList("CD", "LS", "SYM", "POS", "#", "$", "``", "''", ",",  "(", "LRB", ")", "RRB", "[", "LSB", "]", "RSB", ".", "?", "??", "!", ":", ";" ));
	}
	
	public Lemmas(Ta ta, List<String> tags) throws IOException {
		super(ta, tags);
	}
	
	@Override
	public List<Score> scoreDocument(CoreDocument document) {
		ArrayList<String> rawScores = new ArrayList<String>();
		for (CoreSentence sentence : document.sentences()) {
			List<CoreLabel> tokens = sentence.tokens();
			for (int i = 0; i < tokens.size(); i++) {
				if (scoreLemma(tokens.get(i)) == null) {
					rawScores.add(tokens.get(i).lemma());
				}
			}
		}
		return(rawScoresToScoreList(rawScores, document));
	}
	
	@Override
	public List<Double> scoreSentence(CoreSentence sentence) {
		ArrayList<Double> retval = new ArrayList<Double>();
		List<CoreLabel> tokens = sentence.tokens();
		for (int i = 0; i < tokens.size(); i++) {
			retval.add(new Double(1));
		}
		return retval;
	}

}