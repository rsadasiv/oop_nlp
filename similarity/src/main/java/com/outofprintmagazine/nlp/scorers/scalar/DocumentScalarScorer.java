package com.outofprintmagazine.nlp.scorers.scalar;

import java.io.IOException;
import java.util.List;

import com.outofprintmagazine.nlp.Score;

import edu.stanford.nlp.pipeline.CoreDocument;

public interface DocumentScalarScorer {
	
	public Score scoreDocument(CoreDocument document) throws IOException;
	
	public Score scoreDocument(List<Score> scores) throws IOException;

}
