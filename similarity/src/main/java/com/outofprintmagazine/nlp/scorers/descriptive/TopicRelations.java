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
import edu.stanford.nlp.pipeline.CoreSentence;

public class TopicRelations extends ScorerImpl implements DocumentCategoricalScorer {
	
	private String topic = null;
	
	public TopicRelations(Ta ta) throws IOException {
		super(ta);
	}

	public String getTopic() {
		return topic;
	}
	
	public void setTopic(String topic) {
		this.topic = topic;
	}
	
	public List<Score> scoreDocument(CoreDocument document) throws IOException {
		ArrayList<String> rawScores = new ArrayList<String>();
		Pattern pattern = Pattern.compile("(.*)\\s?("+Pattern.quote(getTopic())+")\\s?(.*)");
		for (CoreSentence sentence : document.sentences()) {
			Collection<RelationTriple> triples = sentence.coreMap().get(NaturalLogicAnnotations.RelationTriplesAnnotation.class);
			for (RelationTriple triple : triples) {
				if (triple.confidence > .5) {
					Matcher subjectMatcher = pattern.matcher(triple.subjectGloss());
					if (subjectMatcher.find()) {
						if (subjectMatcher.group(2) != null && subjectMatcher.group(2).length() > 0) {
							//add this to topic attributes

							if (subjectMatcher.group(1) != null && subjectMatcher.group(1).length() > 0) {
								rawScores.addAll(Arrays.asList(subjectMatcher.group(1).split(" ")));
							}
	
							if (subjectMatcher.group(3) != null && subjectMatcher.group(3).length() > 0) {
								rawScores.addAll(Arrays.asList(subjectMatcher.group(3).split(" ")));
							}
						}
						// add this to topic attribute description
						rawScores.addAll(Arrays.asList(triple.objectGloss()));
						// add this to topic attribute action
						//triple.relationGloss()
					}
				}
			}
		}
		return(rawScoresToScoreList(rawScores, document));
    }

}
