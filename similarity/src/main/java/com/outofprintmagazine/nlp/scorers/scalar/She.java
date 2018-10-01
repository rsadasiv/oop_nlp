package com.outofprintmagazine.nlp.scorers.scalar;

import java.io.IOException;
import java.util.Arrays;

import com.outofprintmagazine.nlp.Ta;
import com.outofprintmagazine.nlp.scorers.categorical.Pronouns;

public class She extends DocumentScalarScorerImpl implements DocumentScalarScorer {
	
	public She() {
		super();
	}
	
	public She(Ta ta) throws IOException {
		super(ta);
		setScorer(new Pronouns(ta));
		setAnalysisName("She");
		setScoreNames(Arrays.asList("she"));
	}
	
}
