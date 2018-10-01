package com.outofprintmagazine.nlp.scorers.scalar;

import java.io.IOException;
import java.util.Arrays;

import com.outofprintmagazine.nlp.Ta;
import com.outofprintmagazine.nlp.scorers.categorical.Verbs;

public class Past extends DocumentScalarScorerImpl implements DocumentScalarScorer {
	
	public Past() {
		super();
	}
	
	public Past(Ta ta) throws IOException {
		super(ta);
		setScorer(new Verbs(ta));
		setAnalysisName("Past");
		setScoreNames(Arrays.asList("VBD","VBN"));
	}
}
