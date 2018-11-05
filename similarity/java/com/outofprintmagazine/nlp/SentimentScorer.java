package com.outofprintmagazine.nlp;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map.Entry;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreEntityMention;
import edu.stanford.nlp.pipeline.CoreSentence;

public class SentimentScorer {

	public SentimentScorer() {
		super();
	}
	
	public ArrayList<Score> score(CoreDocument document, List<String> tags, List<String> stopWords, List<String> matchWords) throws IOException {
		ArrayList<Score> retval = new ArrayList<Score>();
		HashMap<String, Integer> scoreMap = new HashMap<String, Integer>();
		for (CoreSentence sentence : document.sentences()) {
			String sentiment = sentence.sentiment();
			boolean shouldAdd = true;
			List<CoreLabel> tokens = sentence.tokens();
			for (int i = 0; i < tokens.size(); i++) {
				CoreLabel token = tokens.get(i);
				if (stopWords != null && stopWords.contains(token.lemma())) {
					shouldAdd = false;
					break;
				}
				if (matchWords == null || matchWords.contains(token.lemma())) {
					shouldAdd = true;
				}
			}
			if (shouldAdd) {
				if (sentiment.equals("Negative")) {
					scoreMap.put(sentence.text(), new Integer(-1));
				}
				else if (sentiment.equals("Neutral")) {
					scoreMap.put(sentence.text(), new Integer(0));
				}
				else if (sentiment.equals("Positive")) {
					scoreMap.put(sentence.text(), new Integer(1));
				}				
			}
		}

		
		//Sort by score
        List<Entry<String, Integer>> list = new ArrayList<>(scoreMap.entrySet());
        //list.sort(Entry.comparingByValue());
      

        //Return in descending order
        Iterator<Entry<String, Integer>> li = list.iterator();
        while (li.hasNext()) {
        	Entry<String, Integer> entry = li.next();
        	Score tmp = new Score(entry.getKey(), entry.getValue());
        	retval.add(tmp);
        }
		
		
		return retval;
	}
}
