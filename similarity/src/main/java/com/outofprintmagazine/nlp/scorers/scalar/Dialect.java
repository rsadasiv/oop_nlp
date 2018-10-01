package com.outofprintmagazine.nlp.scorers.scalar;

import java.io.IOException;
import java.util.ArrayList;

import com.outofprintmagazine.nlp.Ta;
import com.outofprintmagazine.nlp.scorers.WordlessWords;

public class Dialect extends DocumentScalarScorerImpl implements DocumentScalarScorer {

	
	public Dialect() {
		super();
	}
	
	public Dialect(Ta ta) throws IOException {
		super(ta);
		setScorer(new WordlessWords(ta));
		setAnalysisName("Dialect");
		setScoreNames(new ArrayList<String>());
	}

}
