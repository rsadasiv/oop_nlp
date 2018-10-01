package com.outofprintmagazine.nlp.scorers.categorical;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.outofprintmagazine.nlp.Score;
import com.outofprintmagazine.nlp.Ta;
import com.outofprintmagazine.nlp.scorers.PosScorerImpl;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;

public class Prepositions extends PosScorerImpl implements DocumentCategoricalScorer {
	
	
//	public Pronouns() {
//		super();
//	}
	
	public Prepositions(Ta ta) throws IOException {
		this(ta, Arrays.asList("IN", "CC"));
	}
	
	public Prepositions(Ta ta, List<String> tags) throws IOException {
		super(ta, tags);
	}
	
	@Override
	public List<Score> scoreDocument(CoreDocument document) {
		ArrayList<String> rawScores = new ArrayList<String>();
		for (CoreSentence sentence : document.sentences()) {
			List<CoreLabel> tokens = sentence.tokens();
			for (int i = 0; i < tokens.size(); i++) {
				String score = scoreLemma(tokens.get(i));
				if (score != null) {
					rawScores.add(tokens.get(i).tag());
				}
			}
		}
		return(rawScoresToScoreList(rawScores, document));
	}

}