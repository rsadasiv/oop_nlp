package com.outofprintmagazine.nlp;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map.Entry;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreEntityMention;
import edu.stanford.nlp.pipeline.CoreSentence;

public class PosLexicalGroupScorer {
	
	IDictionary dict = null;

	public PosLexicalGroupScorer() throws IOException {
		super();
		dict = new Dictionary(new URL("file", null, "c:/users/rsada/eclipse-workspace/NLP/src/main/resources/wn3.1.dict/dict"));
		dict.open();
	}
	
	public ArrayList<Score> score(CoreDocument document, List<String> tags, List<String> stopWords, List<String> matchWords) throws IOException {
		ArrayList<Score> retval = new ArrayList<Score>();
		ArrayList<String> rawScores = new ArrayList<String>();
		for (CoreSentence sentence : document.sentences()) {
			List<CoreLabel> tokens = sentence.tokens();
			for (int i = 0; i < tokens.size(); i++) {
				CoreLabel token = tokens.get(i);
				if (tags.contains(token.tag())) {
					if (stopWords == null || !stopWords.contains(token.lemma())) {
						if (matchWords == null || matchWords.contains(token.lemma())) {
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
								wnPos = POS.ADJECTIVE;
							}
							try {
								IIndexWord idxWord = dict.getIndexWord (token.lemma(), wnPos);
								if (idxWord != null && idxWord.getWordIDs().size() > 0) {
									IWordID wordID = idxWord.getWordIDs().get(0);
									IWord word = dict.getWord(wordID);
									rawScores.add(word.getSynset().getLexicalFile().getName());
								}
							}
							catch (Exception e) {
								System.err.println(token.toString());
								e.printStackTrace();
							}
						}
					}
				}
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
