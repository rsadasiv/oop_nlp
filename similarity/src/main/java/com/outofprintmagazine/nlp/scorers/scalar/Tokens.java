package com.outofprintmagazine.nlp.scorers.scalar;

import java.io.IOException;
import java.util.Arrays;

import com.outofprintmagazine.nlp.Ta;
import com.outofprintmagazine.nlp.scorers.categorical.DocumentLength;

public class Tokens extends DocumentScalarScorerImpl implements DocumentScalarScorer {

	
	public Tokens() {
		super();
	}
	
	public Tokens(Ta ta) throws IOException {
		super(ta);
		setScorer(new DocumentLength(ta));
		setAnalysisName("Tokens");
		setScoreNames(Arrays.asList("tokenCount"));
	}

}
