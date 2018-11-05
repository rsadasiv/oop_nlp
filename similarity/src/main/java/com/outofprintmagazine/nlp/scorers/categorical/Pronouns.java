package com.outofprintmagazine.nlp.scorers.categorical;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.outofprintmagazine.nlp.Ta;
import com.outofprintmagazine.nlp.scorers.PosScorerImpl;
import com.outofprintmagazine.nlp.scores.Score;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;

public class Pronouns extends PosScorerImpl implements DocumentCategoricalScorer, DocumentRankedCategoricalScorer {
	
	
//	public Pronouns() {
//		super();
//	}
	
	public Pronouns(Ta ta) throws IOException {
		this(ta, Arrays.asList("PRP"));
	}
	
	public Pronouns(Ta ta, List<String> tags) throws IOException {
		super(ta, tags);
	}
	
	@Override
	public List<Score> scoreDocument(CoreDocument document) {
		ArrayList<String> rawScores = new ArrayList<String>();
		for (CoreSentence sentence : document.sentences()) {
			List<CoreLabel> tokens = sentence.tokens();
			for (int i = 0; i < tokens.size(); i++) {
				if (!tokens.get(i).lemma().endsWith("self") && !tokens.get(i).lemma().endsWith("selves")) {
					String score = scoreLemma(tokens.get(i));
					if (score != null) {
						rawScores.add(score);
					}
				}
			}
		}
		return(rawScoresToScoreList(rawScores, document));
	}
	
	@Override
	public List<Score> scoreDocumentRanked(CoreDocument document) throws IOException {
		return super.scoreDocumentRanked(scoreDocument(document));
	}
	
}