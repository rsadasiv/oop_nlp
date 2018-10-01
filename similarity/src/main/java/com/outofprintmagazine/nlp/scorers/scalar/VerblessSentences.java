package com.outofprintmagazine.nlp.scorers.scalar;

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

public class VerblessSentences extends PosScorerImpl implements DocumentScalarScorer {
		
//	public Actions() {
//		super();
//	}
//	
	public VerblessSentences(Ta ta) throws IOException {
		this(ta, Arrays.asList("VB","VBD","VBG","VBN","VBP","VBZ"));
	}
	
	public VerblessSentences(Ta ta, List<String> tags) throws IOException {
		super(ta, tags);
	}

	
	@Override
	public Score scoreDocument(CoreDocument document) {
		int verblessCount = 0;
		for (CoreSentence sentence : document.sentences()) {
			List<CoreLabel> tokens = sentence.tokens();
			boolean shouldScore = true;
			for (int i = 0; i < tokens.size(); i++) {
				String score = scoreLemma(tokens.get(i));
				if (score != null) {
					shouldScore = false;
					break;
				}
			}
			if (shouldScore) {
				verblessCount++;
			}
		}
        return new Score("VerblessSentences", verblessCount);
	}

	@Override
	public Score scoreDocument(List<Score> scores) throws IOException {
		return scoreListToScalar(scores, new ArrayList<String>());
	}
	
	protected Score scoreListToScalar(List<Score> scores, List<String> scoreNames) {
		int total = 0;
		for (Score score : scores) {
			if (scoreNames.size() == 0 || scoreNames.contains(score.getName())) {
				total += score.getScore();
			}
		}
		return new Score("VerblessSentences", total);
	}
	
}