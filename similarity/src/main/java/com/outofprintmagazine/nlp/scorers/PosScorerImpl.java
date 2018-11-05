package com.outofprintmagazine.nlp.scorers;

import java.util.ArrayList;
import java.util.List;

import com.outofprintmagazine.nlp.Ta;
import com.outofprintmagazine.nlp.scorers.categorical.DocumentCategoricalScorer;
import com.outofprintmagazine.nlp.scores.Score;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;

public abstract class PosScorerImpl extends ScorerImpl {

	private List<String> tags = new ArrayList<String>();
	
	public PosScorerImpl() {
		super();
	}
	
	public PosScorerImpl(Ta ta) {
		this();
		setTa(ta);
	}
	
	public PosScorerImpl(Ta ta, List<String> tags) {
		this(ta);
		setTags(tags);
	}
	

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	
	public String scoreToken(CoreLabel token) {
		String retval = null;
		if (getTags().contains(token.tag())) {
			retval = token.originalText();
		}
		return retval;
	}
	
	public String scoreLemma(CoreLabel token) {
		String retval = null;
		if (getTags().contains(token.tag())) {
			retval = token.lemma();
		}
		return retval;
	}
	
	public String scoreTag(CoreLabel token) {
		String retval = null;
		if (getTags().contains(token.tag())) {
			retval = token.tag();
		}
		return retval;
	}

}
