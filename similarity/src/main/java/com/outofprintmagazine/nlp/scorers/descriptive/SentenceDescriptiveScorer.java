package com.outofprintmagazine.nlp.scorers.descriptive;

import java.io.IOException;
import java.util.List;

import edu.stanford.nlp.pipeline.CoreSentence;

public interface SentenceDescriptiveScorer {
	
	public List<Double> scoreSentence(CoreSentence sentence) throws IOException;

}
