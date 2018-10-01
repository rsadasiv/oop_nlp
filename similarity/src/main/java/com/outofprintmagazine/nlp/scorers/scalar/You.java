package com.outofprintmagazine.nlp.scorers.scalar;

import java.io.IOException;
import java.util.Arrays;

import com.outofprintmagazine.nlp.Ta;
import com.outofprintmagazine.nlp.scorers.categorical.Pronouns;

public class You extends DocumentScalarScorerImpl implements DocumentScalarScorer {
	
	public You() {
		super();
	}
	
	public You(Ta ta) throws IOException {
		super(ta);
		setScorer(new Pronouns(ta));
		setAnalysisName("You");
		setScoreNames(Arrays.asList("you"));
	}
	

}
