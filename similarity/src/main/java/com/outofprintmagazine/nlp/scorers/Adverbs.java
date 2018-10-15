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

public class Adverbs extends PosScorerImpl implements DocumentCategoricalScorer, SentenceDescriptiveScorer, DocumentScalarScorer {
	
	
//	public Adverbs() {
//		super();
//	}
	
	public Adverbs(Ta ta) throws IOException {
		this(ta, Arrays.asList("RB", "RBR", "RBS"));
	}
	
	public Adverbs(Ta ta, List<String> tags) throws IOException {
		super(ta, tags);
		setAnalysisName("Adverbs");
	}
	
	@Override
	public List<Score> scoreDocument(CoreDocument document) {
		ArrayList<String> rawScores = new ArrayList<String>();
		for (CoreSentence sentence : document.sentences()) {
			List<CoreLabel> tokens = sentence.tokens();
			for (int i = 0; i < tokens.size(); i++) {
				String score = scoreLemma(tokens.get(i));
				if (score != null) {
					rawScores.add(score);
				}
			}
		}
		return(rawScoresToScoreList(rawScores, document));
	}
	
	@Override
	public List<Double> scoreSentence(CoreSentence sentence) {
		ArrayList<Double> retval = new ArrayList<Double>();
		List<CoreLabel> tokens = sentence.tokens();
		for (int i = 0; i < tokens.size(); i++) {

			String score = scoreLemma(tokens.get(i));
			if (score != null) {
				retval.add(new Double(1));
			}
			else {
				retval.add(new Double(0));
			}
		}
		return retval;
	}
	
	@Override
	public Score scoreDocumentScalar(CoreDocument document) throws IOException {
		return scoreDocumentScalar(scoreDocument(document));
	}

	@Override
	public Score scoreDocumentScalar(List<Score> scores) throws IOException {
		return super.scoreDocumentScalar(scores);
	}

}