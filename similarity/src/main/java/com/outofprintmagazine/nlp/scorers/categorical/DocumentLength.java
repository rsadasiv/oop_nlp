package com.outofprintmagazine.nlp.scorers.categorical;

import java.util.ArrayList;
import java.util.List;

import com.outofprintmagazine.nlp.Score;
import com.outofprintmagazine.nlp.Ta;
import com.outofprintmagazine.nlp.scorers.ScorerImpl;

import edu.stanford.nlp.ling.CoreAnnotations.ParagraphIndexAnnotation;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;

public class DocumentLength extends ScorerImpl implements DocumentCategoricalScorer {
		
//	public DocumentLength() {
//		super();
//	}
		
	public DocumentLength(Ta ta) {
		super(ta);
	}

	@Override
	public List<Score> scoreDocument(CoreDocument document) {
		ArrayList<Score> retval = new ArrayList<Score>();
		double tokenCount = 0;
		double sentenceCount = 0;
		double paragraphCount = 0;
		
		for (CoreSentence sentence : document.sentences()) {
			sentenceCount++;
			paragraphCount = sentence.coreMap().get(ParagraphIndexAnnotation.class);
			tokenCount += sentence.tokens().size();
		}
        retval.add(new Score("tokenCount", tokenCount));
        retval.add(new Score("sentenceCount", sentenceCount));
        retval.add(new Score("paragraphCount", paragraphCount));
			
		return retval;
	}
}
