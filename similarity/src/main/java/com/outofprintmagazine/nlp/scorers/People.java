package com.outofprintmagazine.nlp.scorers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.outofprintmagazine.nlp.Score;
import com.outofprintmagazine.nlp.Ta;
import com.outofprintmagazine.nlp.scorers.categorical.DocumentCategoricalScorer;
import com.outofprintmagazine.nlp.scorers.categorical.DocumentRankedCategoricalScorer;

import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreEntityMention;
import edu.stanford.nlp.pipeline.CoreSentence;

public class People extends ScorerImpl implements DocumentCategoricalScorer, DocumentRankedCategoricalScorer {
	
	private String exclude = "C:\\Users\\rsada\\git\\oop_nlp\\similarity\\resources\\Pronouns.txt";
	
//	public Characters() {
//		super();
//	}
	
	public People(Ta ta) throws IOException {
		super(ta);
		if (getTa().getList(exclude) == null) {
			getTa().setList(exclude);
		}
		setAnalysisName("People");
	}


	public List<String> getExclude() {
		return getTa().getList(exclude);
	}


	@Override
	public List<Score> scoreDocument(CoreDocument document) {
		ArrayList<String> rawScores = new ArrayList<String>();
		for (CoreSentence sentence : document.sentences()) {
			List<CoreEntityMention> entityMentions = sentence.entityMentions();
			for (CoreEntityMention mention : entityMentions) {
				if (mention.entityType().equals("PERSON")) {
					if (exclude == null || getExclude() == null || !getExclude().contains(mention.canonicalEntityMention().get().text())) {
						rawScores.add(mention.canonicalEntityMention().get().text());
					}
				}
			}
		}
		return(rawScoresToScoreList(consolidateSubstrings(rawScores), document));
	}

	@Override
	public List<Score> scoreDocumentRanked(CoreDocument document) throws IOException {
		return super.scoreDocumentRanked(scoreDocument(document));
	}

}
