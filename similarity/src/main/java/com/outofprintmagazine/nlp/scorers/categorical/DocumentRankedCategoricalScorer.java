package com.outofprintmagazine.nlp.scorers.categorical;

import java.io.IOException;
import java.util.List;

import com.outofprintmagazine.nlp.Score;

import edu.stanford.nlp.pipeline.CoreDocument;

public interface DocumentRankedCategoricalScorer {
	
	public List<Score> scoreDocumentRanked(CoreDocument document) throws IOException;
	
}
