package com.outofprintmagazine.nlp.scorers.scalar;

import java.io.IOException;
import java.util.Arrays;

import com.outofprintmagazine.nlp.Ta;
import com.outofprintmagazine.nlp.scorers.categorical.DocumentLength;

public class Sentences extends DocumentScalarScorerImpl implements DocumentScalarScorer {
	
	public Sentences() {
		super();
	}
	
	public Sentences(Ta ta) throws IOException {
		super(ta);
		setScorer(new DocumentLength(ta));
		setScoreNames(Arrays.asList("sentenceCount"));
		setAnalysisName("Sentences");
	}
	
}
