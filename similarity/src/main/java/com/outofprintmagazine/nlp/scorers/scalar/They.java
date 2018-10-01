package com.outofprintmagazine.nlp.scorers.scalar;

import java.io.IOException;
import java.util.Arrays;

import com.outofprintmagazine.nlp.Ta;
import com.outofprintmagazine.nlp.scorers.categorical.Pronouns;

public class They extends DocumentScalarScorerImpl implements DocumentScalarScorer {
	
	public They() {
		super();
	}
	
	public They(Ta ta) throws IOException {
		super(ta);
		setScorer(new Pronouns(ta));
		setAnalysisName("They");
		setScoreNames(Arrays.asList("they"));
	}
	

}
