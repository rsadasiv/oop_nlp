package com.outofprintmagazine.nlp.scorers.scalar;

import java.io.IOException;
import java.util.ArrayList;

import com.outofprintmagazine.nlp.Ta;
import com.outofprintmagazine.nlp.scorers.categorical.Possessives;

public class Ownership extends DocumentScalarScorerImpl implements DocumentScalarScorer {
	
	public Ownership() {
		super();
	}
	
	public Ownership(Ta ta) throws IOException {
		super(ta);
		setScorer(new Possessives(ta));
		setAnalysisName("Ownership");
		setScoreNames(new ArrayList<String>());
	}
	
}
