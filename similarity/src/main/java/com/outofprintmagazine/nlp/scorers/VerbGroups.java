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
import com.outofprintmagazine.nlp.scorers.categorical.DocumentRankedCategoricalScorer;
import com.outofprintmagazine.nlp.scorers.descriptive.SentenceDescriptiveScorer;
import com.outofprintmagazine.nlp.scores.Score;

import edu.mit.jwi.item.ISenseKey;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;

public class VerbGroups extends PosLexicalGroupScorerImpl implements DocumentCategoricalScorer, SentenceDescriptiveScorer, DocumentRankedCategoricalScorer {
		

//		public ActionGroups() throws IOException {
//			super();
//		}
		
		public VerbGroups(Ta ta) throws IOException {
			this(ta, Arrays.asList("VB","VBD","VBG","VBN","VBP","VBZ"));
		}
		
		public VerbGroups(Ta ta, List<String> tags) throws IOException {
			super(ta, tags);
		}	

		@Override
		public List<Score> scoreDocument(CoreDocument document) {
			ArrayList<String> rawScores = new ArrayList<String>();
			for (CoreSentence sentence : document.sentences()) {
				List<CoreLabel> tokens = sentence.tokens();
				for (int i = 0; i < tokens.size(); i++) {
					try {
						//TODO WTF?
						ISenseKey senseKey = simplifiedLesk(tokens.get(i), sentence);
						if (senseKey != null) {
							List<String> senses = getTa().getVerbnet().get(senseKey.toString().substring(0, senseKey.toString().length()-2));
							if (senses != null) {
								for (String sense : senses) {
									rawScores.add(sense.split("-")[0]);
								}
							}
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
		public List<Score> scoreDocumentRanked(CoreDocument document) throws IOException {
			return scoreDocumentRanked(scoreDocument(document));
		}
	
}
