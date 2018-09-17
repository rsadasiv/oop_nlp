package com.outofprintmagazine.nlp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map.Entry;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;

public class SentenceQuestionScorer {
	
	public SentenceQuestionScorer() {
		super();
	}
	

	public ArrayList<Score> score(CoreDocument document, List<String> terminators, List<String> stopWords, List<String> matchWords) {
		ArrayList<Score> retval = new ArrayList<Score>();
		ArrayList<CoreSentence> rawScores = new ArrayList<CoreSentence>();
		for (CoreSentence sentence : document.sentences()) {
			List<CoreLabel> tokens = sentence.tokens();
			boolean shouldAdd = false;
			for (int i = 0; i < tokens.size(); i++) {
				CoreLabel token = tokens.get(i);
				if (matchWords == null || matchWords.contains(token.tag())) {
					//shouldAdd = true;
				}
			}
			if (terminators.contains(tokens.get(tokens.size()-1).lemma())) {
				shouldAdd = true;
			}
			if (shouldAdd) {
				rawScores.add(sentence);
			}
		}

		
		//Consolidate mentions
		HashMap<String, Integer> scoreMap = new HashMap<String, Integer>();
		for (CoreSentence rawScore : rawScores) {
			Integer existingScore = scoreMap.get(rawScore.text());
			if (existingScore == null) {
				scoreMap.put(rawScore.text(), new Integer(1));
			}
			else {
				scoreMap.put(rawScore.text(), new Integer(existingScore.intValue()+1));
			}
		}
		
		//Sort by score
        List<Entry<String, Integer>> list = new ArrayList<>(scoreMap.entrySet());
//        list.sort(Entry.comparingByValue());
      

        //Return in descending order
        ListIterator<Entry<String, Integer>> li = list.listIterator(list.size());
        while (li.hasPrevious()) {
        	Entry<String, Integer> entry = li.previous();
        	Score tmp = new Score(entry.getKey(), entry.getValue());
        	retval.add(tmp);
        }
		
		
		return retval;
	}
}
