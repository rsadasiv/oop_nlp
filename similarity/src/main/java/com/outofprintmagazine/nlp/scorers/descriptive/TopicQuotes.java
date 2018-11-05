package com.outofprintmagazine.nlp.scorers.descriptive;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.outofprintmagazine.nlp.Ta;
import com.outofprintmagazine.nlp.scorers.ScorerImpl;
import com.outofprintmagazine.nlp.scorers.categorical.DocumentCategoricalScorer;
import com.outofprintmagazine.nlp.scores.Score;

import edu.stanford.nlp.coref.CorefCoreAnnotations;
import edu.stanford.nlp.coref.data.CorefChain;
import edu.stanford.nlp.coref.data.CorefChain.CorefMention;
import edu.stanford.nlp.ie.util.RelationTriple;
import edu.stanford.nlp.naturalli.NaturalLogicAnnotations;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreQuote;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.trees.GrammaticalRelation;
import edu.stanford.nlp.trees.TypedDependency;

public class TopicQuotes extends ScorerImpl implements DocumentCategoricalScorer {
	
	private String topic = null;
	
	public TopicQuotes(Ta ta) throws IOException {
		super(ta);
	}

	public String getTopic() {
		return topic;
	}
	
	public void setTopic(String topic) {
		this.topic = topic;
	}
	
	public List<Score> scoreDocument(CoreDocument document) throws IOException {
		List<Score> retval = new ArrayList<Score>();
	    List<CoreQuote> quotes = document.quotes();
		for (CoreQuote quote : quotes) {
			//System.out.println("char offset: " + quote.quoteCharOffsets().first() + " to " + quote.quoteCharOffsets().second());
	        if (quote.canonicalSpeaker().isPresent()) {
	        	quote.canonicalSpeaker().get().equals(getTopic());
	        	Score score = new Score();
	        	score.setName(quote.text());
	        	score.setScore(new Double(-1));
				for (CoreSentence sentence : quote.sentences()) {
					List<CoreSentence> allSentences = sentence.document().sentences();
					for (int i=0;i<allSentences.size();i++) {
						if (allSentences.get(i).equals(sentence)) {
							score.setScore(new Double(i));
							break;
						}
					}
				}
				retval.add(score);
	        }
		}
		return retval;
    }

}
