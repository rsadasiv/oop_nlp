package com.outofprintmagazine.nlp.scorers.scalar;

import java.io.IOException;
import java.util.Arrays;

import com.outofprintmagazine.nlp.Ta;
import com.outofprintmagazine.nlp.scorers.categorical.Verbs;

public class Present extends DocumentScalarScorerImpl implements DocumentScalarScorer {
	
	public Present() {
		super();
	}
	
	public Present(Ta ta) throws IOException {
		super(ta);
		setScorer(new Verbs(ta));
		setAnalysisName("Present");
		setScoreNames(Arrays.asList("VB","VBG","VBP","VBZ"));
	}
	
}
