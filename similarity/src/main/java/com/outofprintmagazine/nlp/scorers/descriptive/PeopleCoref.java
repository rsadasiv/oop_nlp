package com.outofprintmagazine.nlp.scorers.descriptive;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.outofprintmagazine.nlp.Ta;
import com.outofprintmagazine.nlp.scorers.ScorerImpl;

import edu.stanford.nlp.coref.CorefCoreAnnotations;
import edu.stanford.nlp.coref.data.CorefChain;
import edu.stanford.nlp.coref.data.CorefChain.CorefMention;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;

public class PeopleCoref extends ScorerImpl implements SentenceDescriptiveScorer{
	
	private CoreDocument document = null;
	private String topic = null;
	int sentenceId = 1;
	// initialize with a Person name
	// get coref chains for document, cache
	// for each sentence
	// for each coref mention
	// if Person name matches mentionSpan of first mention
	// if sentence id equals sentNum
	//return 1
	
	public PeopleCoref(Ta ta) throws IOException {
		super(ta);
	}

	public String getTopic() {
		return topic;
	}
	
	public void setTopic(String topic) {
		this.topic = topic;
	}

	@Override
	public List<Double> scoreSentence(CoreSentence sentence) throws IOException {
		List<Double> retval = new ArrayList<Double>();
		if (document == null) {
			document = sentence.document();
		}
		boolean hasMention = false;
	    for (CorefChain cc : document.annotation().get(CorefCoreAnnotations.CorefChainAnnotation.class).values()) {
	    	boolean firstMention = true;
	    	for (CorefMention m : cc.getMentionsInTextualOrder()) {
	    		if (firstMention) {
	    			firstMention = false;
	    			if (!(m.mentionSpan.contains(getTopic()) || getTopic().contains(m.mentionSpan))) {
	    				break;
	    			}
	    		}
	    		if (m.sentNum == sentenceId) {
	    			hasMention = true;
	    			break;
	    		}
	    	}
	    }
	    for (int i=0;i<sentence.tokens().size();i++) {
	    	if (hasMention) {
	    		retval.add(new Double(1));
	    	}
	    	else {
	    		retval.add(new Double(0));
	    	}
	    }
	    sentenceId++;
	    return retval;
	}

}
