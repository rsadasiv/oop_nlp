package com.outofprintmagazine.nlp.scorers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.outofprintmagazine.nlp.Ta;
import com.outofprintmagazine.nlp.scorers.categorical.DocumentCategoricalScorer;
import com.outofprintmagazine.nlp.scores.Score;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;

public abstract class DictScorerImpl extends ScorerImpl {

	private String dictionaryFile;
	
	public DictScorerImpl() {
		super();
	}
	
	public DictScorerImpl(Ta ta) {
		super();
		setTa(ta);
	}
	
	public DictScorerImpl(Ta ta, String fileName) throws IOException {
		this(ta);
		setDictionaryFile(fileName); 
	}
	
	public String getDictionaryFile() {
		return dictionaryFile;
	}

	public void setDictionaryFile(String dictionaryFile) {
		this.dictionaryFile = dictionaryFile;
	}
	
	public HashMap<String, String> getDictionary() throws IOException {
		return getTa().getDictionary(getDictionaryFile());
	}
	
	public String scoreLemma(CoreLabel token) throws IOException {
		return getDictionary().get(token.lemma());
	}

}
