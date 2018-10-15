package com.outofprintmagazine.nlp.scorers.scalar;

import java.io.IOException;
import java.util.ArrayList;

import com.outofprintmagazine.nlp.Ta;
import com.outofprintmagazine.nlp.scorers.Adjectives;

public class Vividness extends DocumentScalarScorerImpl implements DocumentScalarScorer {

	
	public Vividness() {
		super();
	}
	
	public Vividness(Ta ta) throws IOException {
		super(ta);
		setScorer(new Adjectives(ta));
		setAnalysisName("Vividness");
		setScoreNames(new ArrayList<String>());
	}

}
