package com.outofprintmagazine.nlp.scorers;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map.Entry;

import com.outofprintmagazine.nlp.Ta;
import com.outofprintmagazine.nlp.scorers.categorical.DocumentCategoricalScorer;
import com.outofprintmagazine.nlp.scorers.descriptive.SentenceDescriptiveScorer;
import com.outofprintmagazine.nlp.scorers.scalar.DocumentScalarScorer;
import com.outofprintmagazine.nlp.scores.Score;

import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.ISenseKey;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;

public class UncommonSenses extends PosLexicalGroupScorerImpl implements DocumentCategoricalScorer, SentenceDescriptiveScorer, DocumentScalarScorer {
		

//		public ActionGroups() throws IOException {
//			super();
//		}
		
		public UncommonSenses(Ta ta) throws IOException {
			this(ta, Arrays.asList());
		}
		
		public UncommonSenses(Ta ta, List<String> tags) throws IOException {
			super(ta, tags);
			setAnalysisName("UncommonSenses");
		}	

		@Override
		public List<Score> scoreDocument(CoreDocument document) {
			ArrayList<String> rawScores = new ArrayList<String>();
			for (CoreSentence sentence : document.sentences()) {
				List<CoreLabel> tokens = sentence.tokens();
				for (int i = 0; i < tokens.size(); i++) {
					try {
						String score = scoreToken(tokens.get(i), sentence);
						if (score != null) {
							rawScores.add(tokens.get(i).lemma());
						}
					}
					catch (Exception e) {
						//TODO logging
						e.printStackTrace();
					}
				}
			}

			return(rawScoresToScoreList(rawScores, document));
		}
		
		public String scoreToken(CoreLabel token, CoreSentence sentence) {
			String score = null;
			
			try {
				POS pos = tagToPOS(token);
				if (pos != null) {
					IIndexWord idxWord = getTa().getWordnet().getIndexWord(token.lemma(), tagToPOS(token));
					if (idxWord != null && idxWord.getWordIDs().size() > 0) {
						//TODO WTF?
						ISenseKey lesk = simplifiedLesk(token, sentence);
//						System.err.println("first sense: " + getTa().getWordnet().getWord(idxWord.getWordIDs().get(0)).getSenseKey().toString());
//						System.err.println("leskSense: " + lesk.toString());
						if (lesk != null) {
							if (!getTa().getWordnet().getWord(idxWord.getWordIDs().get(0)).getSenseKey().toString().equals(lesk.toString())) {
								score = token.lemma();
							}
						}
					}
				}
			}
			catch (Exception e) {
				System.err.println(token.toString());
				e.printStackTrace();
			}

			return score;
		}

		@Override
		public List<Double> scoreSentence(CoreSentence sentence) {
			ArrayList<Double> retval = new ArrayList<Double>();
			List<CoreLabel> tokens = sentence.tokens();
			for (int i = 0; i < tokens.size(); i++) {
				String score = scoreToken(tokens.get(i), sentence);
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
