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
import edu.stanford.nlp.ling.CoreAnnotations.ParagraphIndexAnnotation;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreEntityMention;
import edu.stanford.nlp.pipeline.CoreSentence;

public class CountScorer {
	
	
	public CountScorer() {
		super();
	}
	
	public ArrayList<Score> score(CoreDocument document, List<String> tags, List<String> stopWords, List<String> matchWords) throws IOException {
		ArrayList<Score> retval = new ArrayList<Score>();
		int tokenCount = 0;
		int sentenceCount = 0;
		int paragraphCount = 0;
		
		for (CoreSentence sentence : document.sentences()) {
			sentenceCount++;
			paragraphCount = sentence.coreMap().get(ParagraphIndexAnnotation.class);
			tokenCount += sentence.tokens().size();
		}
        retval.add(new Score("tokenCount", tokenCount));
        retval.add(new Score("sentenceCount", sentenceCount));
        retval.add(new Score("paragraphCount", paragraphCount));
		
		
		return retval;
	}
}
