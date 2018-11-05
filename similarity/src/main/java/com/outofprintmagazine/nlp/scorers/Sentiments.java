package com.outofprintmagazine.nlp.scorers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.outofprintmagazine.nlp.Ta;
import com.outofprintmagazine.nlp.scorers.categorical.DocumentCategoricalScorer;
import com.outofprintmagazine.nlp.scorers.categorical.DocumentRankedCategoricalScorer;
import com.outofprintmagazine.nlp.scorers.descriptive.SentenceDescriptiveScorer;
import com.outofprintmagazine.nlp.scorers.scalar.DocumentScalarScorer;
import com.outofprintmagazine.nlp.scores.Score;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;

public class Sentiments extends ScorerImpl implements DocumentCategoricalScorer, SentenceDescriptiveScorer, DocumentScalarScorer, DocumentRankedCategoricalScorer {

//	public Sentiments() {
//		super();
//	}
	
	public Sentiments(Ta ta) {
		super(ta);
		setAnalysisName("Sentiments");
	}
	
	@Override
	public List<Double> scoreSentence(CoreSentence sentence) {
		ArrayList<Double> retval = new ArrayList<Double>();
		String sentiment = sentence.sentiment();
		List<CoreLabel> tokens = sentence.tokens();
		for (int i = 0; i < tokens.size(); i++) {
			if (sentiment.equals("Very negative")) {
				retval.add(new Double(0));
			}
			else if (sentiment.equals("Negative")) {
				retval.add(new Double(.25));
			}
			else if (sentiment.equals("Neutral")) {
				retval.add(new Double(.5));
			}
			else if (sentiment.equals("Positive")) {
				retval.add(new Double(.75));
			}
			else if (sentiment.equals("Very positive")) {
				retval.add(new Double(1));
			}
		}
		return retval;
	}
	
	@Override
	public List<Score> scoreDocument(CoreDocument document) throws IOException {
		ArrayList<String> rawScores = new ArrayList<String>();
		for (CoreSentence sentence : document.sentences()) {
			rawScores.add(sentence.sentiment());
		}
		return(rawScoresToScoreList(rawScores, document));
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
	public List<Score> scoreDocumentRanked(CoreDocument document) throws IOException {
		return super.scoreDocumentRanked(scoreDocument(document));
	}
	
}
