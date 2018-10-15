package com.outofprintmagazine.nlp.scorers;

import com.outofprintmagazine.nlp.Ta;

import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.POS;
import edu.stanford.nlp.ling.CoreLabel;

public abstract class WordnetScorerImpl extends ScorerImpl {
	
	public WordnetScorerImpl() {
		super();
	}
	
	public WordnetScorerImpl(Ta ta) {
		this();
		setTa(ta);
	}
	
	
	public Double scoreToken(CoreLabel token) {
		Double score = new Double(0);

		POS wnPos = null;
		if (token.tag().startsWith("N")) {
			wnPos = POS.NOUN;
		}
		else if (token.tag().startsWith("V")) {
			wnPos = POS.VERB;
		}
		else if (token.tag().startsWith("J")) {
			wnPos = POS.ADJECTIVE;
		}
		else if (token.tag().startsWith("R")) {
			wnPos = POS.ADVERB;
		}
		
		try {
			if (wnPos != null) {
				IIndexWord idxWord = getTa().getWordnet().getIndexWord(token.lemma(), wnPos);
				if (idxWord == null || idxWord.getWordIDs().size() == 0) {
					score = new Double(1);
				}
			}
			
		}
		catch (Throwable e) {
			System.err.println(token.toString());
			e.printStackTrace();
			score = new Double(1);
		}
		return score;
	}

}
