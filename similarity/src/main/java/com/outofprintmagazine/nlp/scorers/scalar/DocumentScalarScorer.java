package com.outofprintmagazine.nlp.scorers.scalar;

import java.io.IOException;
import java.util.List;

import com.outofprintmagazine.nlp.scores.Score;

import edu.stanford.nlp.pipeline.CoreDocument;

public interface DocumentScalarScorer {
	
	public Score scoreDocumentScalar(CoreDocument document) throws IOException;
	
	public Score scoreDocumentScalar(List<Score> scores) throws IOException;

}
