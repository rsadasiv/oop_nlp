package com.outofprintmagazine.nlp.scorers.descriptive;

import java.util.ArrayList;
import java.util.List;

import com.outofprintmagazine.nlp.Ta;
import com.outofprintmagazine.nlp.scorers.ScorerImpl;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreSentence;

public class Questions extends ScorerImpl implements SentenceDescriptiveScorer {

//	public Questions() {
//		super();
//	}
	
	public Questions(Ta ta) {
		super(ta);
	}
	
	@Override
	public List<Integer> scoreSentence(CoreSentence sentence) {
		ArrayList<Integer> retval = new ArrayList<Integer>();
		List<CoreLabel> tokens = sentence.tokens();
		boolean shouldAdd = tokens.get(tokens.size()-1).lemma().equals("?");
		for (int i = 0; i < tokens.size(); i++) {
			if (shouldAdd) {
				retval.add(new Integer(1));
			}
			else {
				retval.add(new Integer(0));
			}				
		}
		return retval;
	}

}
