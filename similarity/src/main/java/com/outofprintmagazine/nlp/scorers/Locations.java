package com.outofprintmagazine.nlp.scorers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map.Entry;

import com.outofprintmagazine.nlp.Ta;
import com.outofprintmagazine.nlp.scorers.categorical.DocumentCategoricalScorer;
import com.outofprintmagazine.nlp.scorers.categorical.DocumentRankedCategoricalScorer;
import com.outofprintmagazine.nlp.scores.Score;

import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreEntityMention;
import edu.stanford.nlp.pipeline.CoreSentence;

public class Locations extends ScorerImpl implements DocumentCategoricalScorer, DocumentRankedCategoricalScorer {
		
//	public Locations() {
//		super();
//	}
	
	public Locations(Ta ta) throws IOException {
		super(ta);
		setAnalysisName("Locations");
	}

	@Override
	public List<Score> scoreDocument(CoreDocument document) {
		ArrayList<String> rawScores = new ArrayList<String>();
		for (CoreSentence sentence : document.sentences()) {
			List<CoreEntityMention> entityMentions = sentence.entityMentions();
			for (CoreEntityMention mention : entityMentions) {
				if (mention.entityType().equals("LOCATION")) {
					rawScores.add(mention.canonicalEntityMention().get().text());
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
