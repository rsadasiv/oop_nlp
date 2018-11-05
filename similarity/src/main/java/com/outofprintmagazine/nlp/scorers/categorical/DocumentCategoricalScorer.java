package com.outofprintmagazine.nlp.scorers.categorical;

import java.io.IOException;
import java.util.List;

import com.outofprintmagazine.nlp.scores.Score;

import edu.stanford.nlp.pipeline.CoreDocument;

public interface DocumentCategoricalScorer {
	
	public List<Score> scoreDocument(CoreDocument document) throws IOException;

}
