package com.outofprintmagazine.nlp.scorers.scalar;

import java.io.IOException;
import java.util.Arrays;

import com.outofprintmagazine.nlp.Ta;
import com.outofprintmagazine.nlp.scorers.categorical.DocumentLength;

public class Paragraphs extends DocumentScalarScorerImpl implements DocumentScalarScorer {

	
	public Paragraphs() {
		super();
	}
	
	public Paragraphs(Ta ta) throws IOException {
		super(ta);
		setScorer(new DocumentLength(ta));
		setAnalysisName("Paragraphs");
		setScoreNames(Arrays.asList("paragraphCount"));
	}
	

}
