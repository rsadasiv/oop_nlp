package com.outofprintmagazine.nlp.scorers.scalar;

import java.io.IOException;
import java.util.ArrayList;

import com.outofprintmagazine.nlp.Ta;
import com.outofprintmagazine.nlp.scorers.categorical.Prepositions;

public class Phrases extends DocumentScalarScorerImpl implements DocumentScalarScorer {
	
	public Phrases() {
		super();
	}
	
	public Phrases(Ta ta) throws IOException {
		super(ta);
		setScorer(new Prepositions(ta));
		setAnalysisName("Phrases");
		setScoreNames(new ArrayList<String>());
	}
	
}
