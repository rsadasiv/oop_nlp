package com.outofprintmagazine.nlp.scorers.scalar;

import java.io.IOException;
import java.util.ArrayList;

import com.outofprintmagazine.nlp.Ta;
import com.outofprintmagazine.nlp.scorers.UncommonSenses;

public class Ambiguity extends DocumentScalarScorerImpl implements DocumentScalarScorer {
	
	public Ambiguity() {
		super();
	}
	
	public Ambiguity(Ta ta) throws IOException {
		super(ta);
		setScorer(new UncommonSenses(ta));
		setAnalysisName("UncommonSenses");
		setScoreNames(new ArrayList<String>());
	}

}
