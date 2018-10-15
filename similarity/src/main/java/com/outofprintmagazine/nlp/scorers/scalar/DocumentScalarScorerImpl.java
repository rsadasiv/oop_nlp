package com.outofprintmagazine.nlp.scorers.scalar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.outofprintmagazine.nlp.Score;
import com.outofprintmagazine.nlp.Ta;
import com.outofprintmagazine.nlp.scorers.ScorerImpl;
import com.outofprintmagazine.nlp.scorers.categorical.DocumentCategoricalScorer;

import edu.stanford.nlp.pipeline.CoreDocument;

public class DocumentScalarScorerImpl extends ScorerImpl implements DocumentScalarScorer {

	
	DocumentCategoricalScorer scorer = null;

	List<String> scoreNames = new ArrayList<String>();
	
	public DocumentScalarScorerImpl() {
		super();
	}
	
	public DocumentScalarScorerImpl(Ta ta) throws IOException {
		this();
		setTa(ta);
	}
	
	public DocumentCategoricalScorer getScorer() {
		return scorer;
	}

	public void setScorer(DocumentCategoricalScorer scorer) {
		this.scorer = scorer;
	}

	public List<String> getScoreNames() {
		return scoreNames;
	}

	public void setScoreNames(List<String> scoreNames) {
		this.scoreNames = scoreNames;
	}

	@Override
	public Score scoreDocumentScalar(CoreDocument document) throws IOException {
		return scoreDocumentScalar(scorer.scoreDocument(document));
	}

	@Override
	public Score scoreDocumentScalar(List<Score> scores) throws IOException {
		return scoreListToScalar(scores, getScoreNames());
	}

	@Override
	protected Score scoreListToScalar(List<Score> scores, List<String> scoreNames) {
		double total = 0;
		for (Score score : scores) {
			if (scoreNames.size() == 0 || scoreNames.contains(score.getName())) {
				total += score.getScore();
			}
		}
		return new Score(getAnalysisName(), total);
	}
	
}
