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

import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreEntityMention;
import edu.stanford.nlp.pipeline.CoreSentence;

public class MentionTypeScorer {

	public MentionTypeScorer() {
		super();
	}
	
	public ArrayList<Score> score(CoreDocument document, List<String> mentionTypes, List<String> stopWords, List<String> matchWords) throws IOException {
	
		ArrayList<Score> retval = new ArrayList<Score>();
		ArrayList<String> rawScores = new ArrayList<String>();
		for (CoreSentence sentence : document.sentences()) {
			List<CoreEntityMention> entityMentions = sentence.entityMentions();
			for (CoreEntityMention mention : entityMentions) {
				if (mentionTypes.contains(mention.entityType())) {
					if (stopWords == null || !stopWords.contains(mention.canonicalEntityMention().get().text())) {
						rawScores.add(mention.canonicalEntityMention().get().text());
					}
				}
			}
		}

		//Consolidate substrings
		rawScores.sort(null);
		ListIterator<String> iter = rawScores.listIterator(rawScores.size());
		String prev = "";
		while (iter.hasPrevious()) {
			String cur = iter.previous();
			if (prev.contains(cur)) {
				iter.set(prev);
			}
			else {
				prev = cur;
			}		
		}
		
		//Consolidate mentions
		HashMap<String, Integer> scoreMap = new HashMap<String, Integer>();
		for (String rawScore : rawScores) {
			Integer existingScore = scoreMap.get(rawScore);
			if (existingScore == null) {
				scoreMap.put(rawScore, new Integer(1));
			}
			else {
				scoreMap.put(rawScore, new Integer(existingScore.intValue()+1));
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
