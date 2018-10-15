package com.outofprintmagazine.nlp.scorers.categorical;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.outofprintmagazine.nlp.Score;
import com.outofprintmagazine.nlp.Ta;
import com.outofprintmagazine.nlp.scorers.ScorerImpl;

import edu.stanford.nlp.ling.CoreAnnotations.GenderAnnotation;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreEntityMention;
import edu.stanford.nlp.pipeline.CoreSentence;

public class Genders extends ScorerImpl implements DocumentCategoricalScorer,  DocumentRankedCategoricalScorer {
	
//	public Genders() {
//		super();
//	}
	
	public Genders(Ta ta) throws IOException {
		super(ta);
	}

	@Override
	public List<Score> scoreDocument(CoreDocument document) {
		ArrayList<String> rawScores = new ArrayList<String>();
		for (CoreSentence sentence : document.sentences()) {
			List<CoreEntityMention> entityMentions = sentence.entityMentions();
			for (CoreEntityMention mention : entityMentions) {
				if (mention.entityType().equals("PERSON")) {
					if (mention.coreMap().containsKey(GenderAnnotation.class)) {
						if (mention.coreMap().get(GenderAnnotation.class) != null) {
							//TODO WTF? No Match on FemaleNames.txt
							rawScores.add(mention.coreMap().get(GenderAnnotation.class));
						}
					}
				}
			}
		}
		return(rawScoresToScoreList(rawScores, document));
	}
	
	@Override
	public List<Score> scoreDocumentRanked(CoreDocument document) throws IOException {
		return super.scoreDocumentRanked(scoreDocument(document));
	}


}
