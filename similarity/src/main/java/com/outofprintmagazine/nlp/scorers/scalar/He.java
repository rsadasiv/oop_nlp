package com.outofprintmagazine.nlp.scorers.scalar;

import java.io.IOException;
import java.util.Arrays;

import com.outofprintmagazine.nlp.Ta;
import com.outofprintmagazine.nlp.scorers.categorical.Pronouns;

public class He extends DocumentScalarScorerImpl implements DocumentScalarScorer {
	
	public He() {
		super();
	}
	
	public He(Ta ta) throws IOException {
		super(ta);
		setScorer(new Pronouns(ta));
		setAnalysisName("He");
		setScoreNames(Arrays.asList("he"));
	}
	

}
