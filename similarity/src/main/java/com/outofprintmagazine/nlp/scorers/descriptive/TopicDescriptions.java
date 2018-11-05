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
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.trees.GrammaticalRelation;
import edu.stanford.nlp.trees.TypedDependency;

public class TopicDescriptions extends ScorerImpl implements DocumentCategoricalScorer {
	
	private String topic = null;
	
	public TopicDescriptions(Ta ta) throws IOException {
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
		for (CoreSentence sentence : document.sentences()) {
			SemanticGraph dependencyParse = sentence.dependencyParse();
			Collection<TypedDependency> deps = dependencyParse.typedDependencies();
			for (TypedDependency dependency : deps) {
				GrammaticalRelation rn = dependency.reln();
				if (rn.getShortName().equals("amod")) {
					if (dependency.gov().lemma().equals(getTopic()));
						rawScores.add(dependency.dep().originalText());
				}
			}
		}
		return(rawScoresToScoreList(rawScores, document));
    }

}
