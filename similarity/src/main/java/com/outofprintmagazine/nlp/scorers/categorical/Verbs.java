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

public class Verbs extends PosScorerImpl implements DocumentCategoricalScorer, DocumentRankedCategoricalScorer {
	
	
//	public Verbs() {
//		super();
//	}
	
	public Verbs(Ta ta) throws IOException {
		this(ta, Arrays.asList("VB","VBD","VBG","VBN","VBP","VBZ"));
	}
	
	public Verbs(Ta ta, List<String> tags) throws IOException {
		super(ta, tags);
	}
	
	@Override
	public List<Score> scoreDocument(CoreDocument document) {
		ArrayList<String> rawScores = new ArrayList<String>();
		for (CoreSentence sentence : document.sentences()) {
			List<CoreLabel> tokens = sentence.tokens();
			for (int i = 0; i < tokens.size(); i++) {
				CoreLabel token = tokens.get(i);
				if (token.tag().equals("MD") && (token.lemma().equals("will") || token.lemma().equals("shall"))) {
					rawScores.add(token.tag());
				}
				else {
					String score = scoreLemma(tokens.get(i));
					if (score != null) {
						rawScores.add(tokens.get(i).tag());
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