package com.outofprintmagazine.nlp.scorers.scalar;

import java.io.IOException;
import java.util.Arrays;

import com.outofprintmagazine.nlp.Ta;
import com.outofprintmagazine.nlp.scorers.categorical.Pronouns;

public class We extends DocumentScalarScorerImpl implements DocumentScalarScorer {
	
	public We() {
		super();
	}
	
	public We(Ta ta) throws IOException {
		super(ta);
		setScorer(new Pronouns(ta));
		setAnalysisName("We");
		setScoreNames(Arrays.asList("we"));
	}
	
}
