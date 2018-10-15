package com.outofprintmagazine.nlp.scorers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.outofprintmagazine.nlp.Score;
import com.outofprintmagazine.nlp.Ta;
import com.outofprintmagazine.nlp.scorers.categorical.DocumentCategoricalScorer;
import com.outofprintmagazine.nlp.scorers.descriptive.SentenceDescriptiveScorer;
import com.outofprintmagazine.nlp.scorers.scalar.DocumentScalarScorer;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;

public class VerblessSentences extends PosScorerImpl implements DocumentCategoricalScorer, SentenceDescriptiveScorer, DocumentScalarScorer {
		
//	public Actions() {
//		super();
//	}
//	
	public VerblessSentences(Ta ta) throws IOException {
		this(ta, Arrays.asList("VB","VBD","VBG","VBN","VBP","VBZ"));
		setAnalysisName("VerblessSentences");
	}
	
	public VerblessSentences(Ta ta, List<String> tags) throws IOException {
		super(ta, tags);
	}

	
	@Override
	public Score scoreDocumentScalar(CoreDocument document) throws IOException {
		return scoreDocumentScalar(scoreDocument(document));
	}

	@Override
	public Score scoreDocumentScalar(List<Score> scores) throws IOException {
		return super.scoreDocumentScalar(scores);
	}

	@Override
	public List<Double> scoreSentence(CoreSentence sentence) throws IOException {
		List<CoreLabel> tokens = sentence.tokens();
		List<Double> retval = new ArrayList<Double>();
		boolean shouldScore = true;
		for (int i = 0; i < tokens.size(); i++) {
			String score = scoreLemma(tokens.get(i));
			if (score != null) {
				shouldScore = false;
				break;
			}
		}
		for (int i = 0; i < tokens.size(); i++) {
			if (shouldScore) {
				retval.add(new Double(1));
			}
			else {
				retval.add(new Double(0));
			}
		}
		return retval;
	}

	@Override
	public List<Score> scoreDocument(CoreDocument document) throws IOException {
		ArrayList<Score> retval = new ArrayList<Score>();
		for (CoreSentence sentence : document.sentences()) {
			List<CoreLabel> tokens = sentence.tokens();
			boolean shouldScore = true;
			for (int i = 0; i < tokens.size(); i++) {
				String score = scoreLemma(tokens.get(i));
				if (score != null) {
					shouldScore = false;
					break;
				}
			}
			if (shouldScore) {
				retval.add(new Score(getAnalysisName(), new Double(1)));
			}
			else {
				retval.add(new Score(getAnalysisName(), new Double(0)));
			}
		}
		return retval;
	}
	
}