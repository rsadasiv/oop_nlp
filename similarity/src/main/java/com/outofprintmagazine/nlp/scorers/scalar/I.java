package com.outofprintmagazine.nlp.scorers.scalar;

import java.io.IOException;
import java.util.Arrays;

import com.outofprintmagazine.nlp.Ta;
import com.outofprintmagazine.nlp.scorers.categorical.Pronouns;

public class I extends DocumentScalarScorerImpl implements DocumentScalarScorer {
	
	public I() {
		super();
	}
	
	public I(Ta ta) throws IOException {
		super(ta);
		setScorer(new Pronouns(ta));
		setAnalysisName("I");
		setScoreNames(Arrays.asList("I"));
	}
	

}
