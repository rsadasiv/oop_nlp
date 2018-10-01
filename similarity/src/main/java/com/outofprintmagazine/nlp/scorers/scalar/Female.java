package com.outofprintmagazine.nlp.scorers.scalar;

import java.io.IOException;
import java.util.Arrays;

import com.outofprintmagazine.nlp.Ta;
import com.outofprintmagazine.nlp.scorers.categorical.Genders;

public class Female extends DocumentScalarScorerImpl implements DocumentScalarScorer {

	
	public Female() {
		super();
	}
	
	public Female(Ta ta) throws IOException {
		super(ta);
		setScorer(new Genders(ta));
		setAnalysisName("Female");
		setScoreNames(Arrays.asList("FEMALE"));;
	}

}
