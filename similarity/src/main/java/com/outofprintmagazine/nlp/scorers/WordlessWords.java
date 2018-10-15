package com.outofprintmagazine.nlp.scorers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map.Entry;

import com.outofprintmagazine.nlp.Score;
import com.outofprintmagazine.nlp.Ta;
import com.outofprintmagazine.nlp.scorers.categorical.DocumentCategoricalScorer;
import com.outofprintmagazine.nlp.scorers.descriptive.SentenceDescriptiveScorer;
import com.outofprintmagazine.nlp.scorers.scalar.DocumentScalarScorer;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;

public class WordlessWords extends WordnetScorerImpl implements DocumentCategoricalScorer, SentenceDescriptiveScorer, DocumentScalarScorer {

//		public ActionGroups() throws IOException {
//			super();
//		}

	public WordlessWords(Ta ta) throws IOException {
		super(ta);
		setAnalysisName("WordlessWords");
	}

	@Override
	public List<Score> scoreDocument(CoreDocument document) throws IOException {
		ArrayList<String> rawScores = new ArrayList<String>();
		Double noScore = new Double(1);
		for (CoreSentence sentence : document.sentences()) {
			List<CoreLabel> tokens = sentence.tokens();
			for (int i = 0; i < tokens.size(); i++) {
				CoreLabel token = tokens.get(i);
				if (token.tag().equals("FW")) {
					rawScores.add(tokens.get(i).originalText());
				}
				else if(token.tag().equals("NNP") || token.tag().equals("NNPS")) {
					//pass
				}
				else {
					Double score = scoreToken(tokens.get(i));
					if (score.equals(noScore)) {
						rawScores.add(tokens.get(i).originalText());
					}
				}
			}
		}
		return(rawScoresToScoreList(rawScores, document));
	}
	
	@Override
	public List<Double> scoreSentence(CoreSentence sentence) throws IOException {
		ArrayList<Double> retval = new ArrayList<Double>();
		List<CoreLabel> tokens = sentence.tokens();
		for (int i = 0; i < tokens.size(); i++) {
			retval.add(scoreToken(tokens.get(i)));
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
