package com.outofprintmagazine.nlp.scorers.scalar;

import java.io.IOException;
import java.util.ArrayList;

import com.outofprintmagazine.nlp.Ta;
import com.outofprintmagazine.nlp.scorers.Adverbs;

public class Verbly extends DocumentScalarScorerImpl implements DocumentScalarScorer {
	
	public Verbly() {
		super();
	}
	
	public Verbly(Ta ta) throws IOException {
		super(ta);
		setScorer(new Adverbs(ta));
		setAnalysisName("Verbly");
		setScoreNames(new ArrayList<String>());
	}

}
