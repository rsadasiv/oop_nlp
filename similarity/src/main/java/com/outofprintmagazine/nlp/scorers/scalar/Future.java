package com.outofprintmagazine.nlp.scorers.scalar;

import java.io.IOException;
import java.util.Arrays;

import com.outofprintmagazine.nlp.Ta;
import com.outofprintmagazine.nlp.scorers.categorical.Verbs;

public class Future extends DocumentScalarScorerImpl implements DocumentScalarScorer {
	
	public Future() {
		super();
	}
	
	public Future(Ta ta) throws IOException {
		super(ta);
		setScorer(new Verbs(ta));
		setAnalysisName("Future");
		setScoreNames(Arrays.asList("MD"));
	}
	
}
