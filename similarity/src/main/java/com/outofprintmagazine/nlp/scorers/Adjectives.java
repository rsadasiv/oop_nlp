package com.outofprintmagazine.nlp.scorers;

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

import com.outofprintmagazine.nlp.Score;
import com.outofprintmagazine.nlp.Ta;
import com.outofprintmagazine.nlp.scorers.categorical.DocumentCategoricalScorer;
import com.outofprintmagazine.nlp.scorers.descriptive.SentenceDescriptiveScorer;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;

public class Adjectives extends PosScorerImpl implements DocumentCategoricalScorer, SentenceDescriptiveScorer {

	List<String> exclude = new ArrayList<String>();
	
//	public Adjectives() {
//		super();
//	}
//	
		
	public Adjectives(Ta ta) throws IOException {
		this(ta, Arrays.asList("JJ","JJR","JJS"));
		//exclude = Files.readAllLines(new File("C:\\Users\\rsada\\git\\oop_nlp\\similarity\\resources\\Adjectives.txt").toPath(), Charset.defaultCharset());
	}
	
	public Adjectives(Ta ta, List<String> tags) throws IOException {
		super(ta, tags);
		//exclude = Files.readAllLines(new File("C:\\Users\\rsada\\git\\oop_nlp\\similarity\\resources\\Adjectives.txt").toPath(), Charset.defaultCharset());
	}

	
	@Override
	public List<Score> scoreDocument(CoreDocument document) {
		ArrayList<String> rawScores = new ArrayList<String>();
		for (CoreSentence sentence : document.sentences()) {
			List<CoreLabel> tokens = sentence.tokens();
			for (int i = 0; i < tokens.size(); i++) {
				String score = scoreLemma(tokens.get(i));
				if (score != null && !exclude.contains(score)) {
					rawScores.add(score);
				}
			}
		}
		return(rawScoresToScoreList(rawScores, document));
	}
	
	@Override
	public List<Integer> scoreSentence(CoreSentence sentence) {
		ArrayList<Integer> retval = new ArrayList<Integer>();
		List<CoreLabel> tokens = sentence.tokens();
		for (int i = 0; i < tokens.size(); i++) {

			String score = scoreLemma(tokens.get(i));
			if (score != null) {
				retval.add(new Integer(1));
			}
			else {
				retval.add(new Integer(0));
			}
		}
		return retval;
	}

}