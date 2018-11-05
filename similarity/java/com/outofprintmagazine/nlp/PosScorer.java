package com.outofprintmagazine.nlp;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map.Entry;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreEntityMention;
import edu.stanford.nlp.pipeline.CoreSentence;

public class PosScorer {
	
	
	public PosScorer() {
		super();
	}
	
	public ArrayList<Score> score(CoreDocument document, List<String> tags, List<String> stopWords, List<String> matchWords) throws IOException {
		ArrayList<Score> retval = new ArrayList<Score>();
		ArrayList<CoreLabel> rawScores = new ArrayList<CoreLabel>();
		for (CoreSentence sentence : document.sentences()) {
			List<CoreLabel> tokens = sentence.tokens();
			for (int i = 0; i < tokens.size(); i++) {
				CoreLabel token = tokens.get(i);
				if (tags.contains(token.tag())) {
					if (stopWords == null || !stopWords.contains(token.lemma())) {
						if (matchWords != null) {
							if (matchWords.contains(token.lemma())) {
								rawScores.add(token);
							}
						}
						else {
							rawScores.add(token);
						}
					}
				}
			}
		}

		
		//Consolidate mentions
		HashMap<String, Integer> scoreMap = new HashMap<String, Integer>();
		for (CoreLabel rawScore : rawScores) {
			Integer existingScore = scoreMap.get(rawScore.lemma());
			if (existingScore == null) {
				scoreMap.put(rawScore.lemma(), new Integer(1));
			}
			else {
				scoreMap.put(rawScore.lemma(), new Integer(existingScore.intValue()+1));
			}
		}
		
		//Sort by score
        List<Entry<String, Integer>> list = new ArrayList<>(scoreMap.entrySet());
        list.sort(Entry.comparingByValue());
      

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
