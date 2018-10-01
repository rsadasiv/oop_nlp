package com.outofprintmagazine.nlp.scorers.scalar;

import java.io.IOException;
import java.util.List;

import com.outofprintmagazine.nlp.Score;
import com.outofprintmagazine.nlp.Ta;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import eu.crydee.syllablecounter.SyllableCounter;

public class Readability extends DocumentScalarScorerImpl implements DocumentScalarScorer {

	private SyllableCounter syllableCounter = new SyllableCounter();
	
//	public Readability() {
//		super();
//	}
	
	public Readability(Ta ta) throws IOException {
		super(ta);
		setAnalysisName("Readability");
	}
		
	public SyllableCounter getSyllableCounter() {
		return syllableCounter;
	}

	public void setSyllableCounter(SyllableCounter syllableCounter) {
		this.syllableCounter = syllableCounter;
	}

	@Override
	public Score scoreDocument(CoreDocument document) {
		int wordCount = 0;
		int syllableCount = 0;
		int sentenceCount = 0;
		for (CoreSentence sentence : document.sentences()) {
			sentenceCount++;
			List<CoreLabel> tokens = sentence.tokens();
			wordCount += tokens.size();
			for (int i = 0; i < tokens.size(); i++) {
				CoreLabel token = tokens.get(i);
				syllableCount += getSyllableCounter().count(token.originalText());
			}
		}
		double fk = 206.835 - (1.015*wordCount/sentenceCount) - (84.6*syllableCount/wordCount);
		return new Score(getAnalysisName(), new Integer(new Double(fk).intValue()));
	}
}
