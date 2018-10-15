package com.outofprintmagazine.nlp.scorers.scalar;

import java.io.IOException;
import java.util.ArrayList;

import com.outofprintmagazine.nlp.Ta;
import com.outofprintmagazine.nlp.scorers.UncommonWords;

public class Vocabulary extends DocumentScalarScorerImpl implements DocumentScalarScorer {

	
	public Vocabulary() {
		super();
	}
	
	public Vocabulary(Ta ta) throws IOException {
		super(ta);
		setScorer(new UncommonWords(ta));
		setAnalysisName("Vocabulary");
		setScoreNames(new ArrayList<String>());
	}

}
