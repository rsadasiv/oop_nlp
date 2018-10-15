package com.outofprintmagazine.nlp.scorers.categorical;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.outofprintmagazine.nlp.Score;
import com.outofprintmagazine.nlp.Ta;
import com.outofprintmagazine.nlp.scorers.PosScorerImpl;
import com.outofprintmagazine.nlp.scorers.Topics;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;


public class TopicCategories extends PosScorerImpl implements DocumentCategoricalScorer, DocumentRankedCategoricalScorer  {

	public TopicCategories(Ta ta) throws IOException {
		//this(ta, Arrays.asList("NN", "NNS", "NNP", "NNPS"));
		this(ta, Arrays.asList("NN", "NNS"));

	}

	public TopicCategories(Ta ta, List<String> tags) throws IOException {
		super(ta, tags);
	}
	
	private List<Score> scoreDocumentTopics(CoreDocument document) {
		ArrayList<String> rawScores = new ArrayList<String>();
		for (CoreSentence sentence : document.sentences()) {
			List<CoreLabel> tokens = sentence.tokens();
			for (int i = 0; i < tokens.size(); i++) {
				String score = scoreLemma(tokens.get(i));
				if (score != null) {
					rawScores.add(score);
				}
			}
		}
		return(rawScoresToScoreList(rawScores, document));
	}
	
	@Override
	public List<Score> scoreDocument(CoreDocument document) throws IOException {
		List<Score> topics = super.scoreDocumentRanked(scoreDocumentTopics(document));
		HashMap<String, Double> scoreMap = new HashMap<String, Double>();

		for (Score topic : topics) {
			try {
				ObjectMapper objectMapper = new ObjectMapper();
				JsonNode rootNode = objectMapper.readValue(new URL("https://en.wikipedia.org/w/api.php?action=query&redirects&format=json&formatversion=2&titles="+(URLEncoder.encode(topic.getName(), "UTF-8"))), JsonNode.class);
				//System.out.println(rootNode);
				JsonNode pagesNode = rootNode.get("query").get("pages");
				if (pagesNode != null && pagesNode.isArray()) {
					for (final JsonNode pageNode : pagesNode) {
						if (pageNode.get("missing") == null || !pageNode.get("missing").asBoolean()) {
							//System.out.println(pageNode.get("title").asText());
							String clcontinue = "init";
							while (clcontinue != null) {
								JsonNode categoryRootNode = null;

								if (clcontinue.equals("init")) {
									categoryRootNode = objectMapper.readValue(new URL("https://en.wikipedia.org/w/api.php?format=json&action=query&prop=categories&utf8=1&titles="+(URLEncoder.encode(pageNode.get("title").asText(), "UTF-8"))), JsonNode.class);
								}
								else {
									categoryRootNode = objectMapper.readValue(new URL("https://en.wikipedia.org/w/api.php?format=json&action=query&prop=categories&utf8=1&titles="+(URLEncoder.encode(pageNode.get("title").asText(), "UTF-8")+"&clcontinue="+clcontinue)), JsonNode.class);
								}
								if (categoryRootNode.get("continue") != null) {
									clcontinue = categoryRootNode.get("continue").get("clcontinue").asText();
								}
								else {
									clcontinue = null;
								}
								//System.out.println(categoryRootNode);
								JsonNode pageCategoriesNode = categoryRootNode.get("query").get("pages");
								Iterator<Entry<String, JsonNode>> fieldsIter = pageCategoriesNode.fields();
								while (fieldsIter.hasNext()) {
									JsonNode categoriesNode = fieldsIter.next().getValue().get("categories");
									if (categoriesNode != null && categoriesNode.isArray()) {
										for (final JsonNode categoryNode : categoriesNode) {
											String categoryName = categoryNode.get("title").asText().substring("Category:".length());
											if (!categoryName.startsWith("All") 
													&& !categoryName.startsWith("Articles")
													&& !categoryName.startsWith("Wikipedia") 
													&& !categoryName.startsWith("CS1") 
													&& !categoryName.startsWith("Pages") 
													&& !categoryName.startsWith("Commons") 
													&& !categoryName.startsWith("Disambiguation")
													&& !categoryName.contains("disambiguation")
													&& !categoryName.contains("wayback")
													&& !categoryName.contains("dates")
													&& !categoryName.contains("articles")
													&& !categoryName.contains("inventions")
													&& !categoryName.startsWith("Cleanup")
													&& !categoryName.startsWith("Lists")
													&& !categoryName.startsWith("Vague")
													&& !categoryName.startsWith("Interlanguage")
													&& !categoryName.startsWith("Webarchive")
													&& !categoryName.startsWith("SI")
													&& !categoryName.startsWith("Use")
													&& !categoryName.startsWith("ISO")) {
												//System.out.println(categoryName);
												Double existingScore = scoreMap.get(categoryName);
												if (existingScore == null) {
													scoreMap.put(categoryName, new Double(topic.getScore()));
												}
												else {
													scoreMap.put(categoryName, new Double(existingScore.doubleValue()+topic.getScore()));
												}
											}
										}
									}
								}
							}
						}
				    }
				}
	        } 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return(sortDescending(normalizeScores(scoreMap,document)));
	}
	
	@Override
	public List<Score> scoreDocumentRanked(CoreDocument document) throws IOException {
		return super.scoreDocumentRanked(scoreDocument(document));
	}

}
