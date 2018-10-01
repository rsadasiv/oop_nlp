package com.outofprintmagazine.nlp.scorers.scalar;

import java.io.IOException;
import java.util.Arrays;

import com.outofprintmagazine.nlp.Ta;
import com.outofprintmagazine.nlp.scorers.categorical.Genders;

public class Male extends DocumentScalarScorerImpl implements DocumentScalarScorer {
	
	public Male() {
		super();
	}
	
	public Male(Ta ta) throws IOException {
		super(ta);
		setScorer(new Genders(ta));
		setAnalysisName("Male");
		setScoreNames(Arrays.asList("MALE"));
	}
	

}
