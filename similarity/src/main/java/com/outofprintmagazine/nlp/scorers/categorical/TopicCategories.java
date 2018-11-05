package com.outofprintmagazine.nlp.scorers.categorical;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.outofprintmagazine.nlp.Ta;
import com.outofprintmagazine.nlp.scorers.PosScorerImpl;
import com.outofprintmagazine.nlp.scorers.Topics;
import com.outofprintmagazine.nlp.scores.Score;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;


public class TopicCategories extends PosScorerImpl implements DocumentCategoricalScorer, DocumentRankedCategoricalScorer  {

	public TopicCategories(Ta ta) throws IOException {
		//this(ta, Arrays.asList("NN", "NNS", "NNP", "NNPS"));
		this(ta, Arrays.asList("NN", "NNS"));
		setAnalysisName("TopicCategories");

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
		SimpleDateFormat fmt = new SimpleDateFormat("HH:mm:ss.S");
		List<Score> topics = super.scoreDocumentRanked(scoreDocumentTopics(document));
		HashMap<String, List<Score>> topicPageTopics = new HashMap<String, List<Score>>();
		HashMap<String, String> topicPages = new HashMap<String, String>();
		for (Score topic : topics) {
			try {
				ObjectMapper objectMapper = new ObjectMapper();
				JsonNode rootNode = objectMapper.readValue(new URL("https://en.wikipedia.org/w/api.php?action=query&redirects&format=json&formatversion=2&titles="+(URLEncoder.encode(topic.getName(), "UTF-8"))), JsonNode.class);
				//TODO - pagination?

				JsonNode pagesNode = rootNode.get("query").get("pages");
				if (pagesNode != null && pagesNode.isArray()) {
					for (final JsonNode pageNode : pagesNode) {
						//instead, download the wikipedia page, tokenize/pos, and calculate a tf/idf for all the topics in the text vs all the topics in the wikipedia pages.
						//TODO - ignore disambiguation pages?
						if (pageNode.get("missing") == null || !pageNode.get("missing").asBoolean()) {
							topicPages.put(topic.getName(), pageNode.get("title").asText());
							System.out.println(topic.getName() + " : " + pageNode.get("title").asText());
							StringWriter buf = new StringWriter();
							Document homepage = Jsoup.connect("https://en.wikipedia.org/wiki/" + pageNode.get("title").asText()).get();
							Elements authorParas = homepage.select("#mw-content-text > div > p");
							for (Element para : authorParas) {
								buf.write(para.wholeText());
								buf.write('\n');
							}
							System.out.println("Chars: " + buf.toString().length());
							System.out.println(fmt.format(new java.util.Date(System.currentTimeMillis())) + " started annotation");
							CoreDocument wikiDoc = getTa().annotate(getTa().getWikiPipeline(), buf.toString());
							List<Score> wikiTopics = scoreDocumentTopics(wikiDoc);
							topicPageTopics.put(pageNode.get("title").asText(), wikiTopics);
							System.out.println(fmt.format(new java.util.Date(System.currentTimeMillis())) + " finished annotation");
						}
				    }
				}
	        } 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		//calculate tf-idf per topic
		//calculate idf per topic
		HashMap<String, Double> topicIdf = new HashMap<String,Double>();
		for (Score topic : topics) {
			for (String pageName : topicPageTopics.keySet()) {
				int idf = 0;
				List<Score> wikiPageTopics = topicPageTopics.get(pageName);
				for (Score wikiPageTopic : wikiPageTopics) {
					if (wikiPageTopic.getName().equals(topic.getName())) {
						idf++;
						continue;
					}
				}
				topicIdf.put(topic.getName(), new Double(idf));
			}
		}
		
		//calculate tf per topic per wikiPage
		HashMap<String, HashMap<String,Double>> topicPageTf = new HashMap<String,HashMap<String,Double>>();
		for (Score topic : topics) {
			for (String pageName : topicPageTopics.keySet()) {
				List<Score> wikiPageTopics = topicPageTopics.get(pageName);
				for (Score wikiPageTopic : wikiPageTopics) {
					HashMap<String,Double> topicPageScore = topicPageTf.get(topic.getName());
					if (topicPageScore == null) {
						topicPageScore = new HashMap<String,Double>();
					}
					if (topicPageScore.get(pageName) == null) {
						topicPageScore.put(pageName, new Double(0.0));
						topicPageTf.put(topic.getName(), topicPageScore);
					}
		
					if (wikiPageTopic.getName().equals(topic.getName())) {
						topicPageTf.get(topic.getName()).put(pageName, new Double(wikiPageTopic.getScore()));						
						continue;
					}
				}
			}
		}
		
		//calculate similarity
		HashMap<String,Double> pageSimilarity = new HashMap<String,Double>();
		for (String pageName : topicPageTopics.keySet()) {		
			for (Score topic : topics) {
				double mainPageTopicScore = topic.getScore();
				Double topicIdfScore = topicIdf.get(topic.getName());
				if (topicPageTf.get(topic.getName()) != null) {
					Double topicPageTfScore = topicPageTf.get(topic.getName()).get(pageName);
					double answer = mainPageTopicScore*topicPageTfScore*(Math.log(topicPageTopics.size()/(1+Math.abs(topicIdfScore))));
					Double existingValue = pageSimilarity.get(pageName);
					if (existingValue == null) {
						existingValue = new Double(0.0);
					}
					existingValue = new Double(existingValue.doubleValue() + answer);
					pageSimilarity.put(pageName, existingValue);
				}
			}
		}
		
		return(sortDescending(normalizeScores(pageSimilarity,document)));
	}
	
	@Override
	public List<Score> scoreDocumentRanked(CoreDocument document) throws IOException {
		return super.scoreDocumentRanked(scoreDocument(document));
	}
	
//	if (pageNode.get("missing") == null || !pageNode.get("missing").asBoolean()) {
//		//System.out.println(pageNode.get("title").asText());
//		String clcontinue = "init";
//		while (clcontinue != null) {
//			JsonNode categoryRootNode = null;
//
//			if (clcontinue.equals("init")) {
//				categoryRootNode = objectMapper.readValue(new URL("https://en.wikipedia.org/w/api.php?format=json&action=query&prop=categories&utf8=1&titles="+(URLEncoder.encode(pageNode.get("title").asText(), "UTF-8"))), JsonNode.class);
//			}
//			else {
//				categoryRootNode = objectMapper.readValue(new URL("https://en.wikipedia.org/w/api.php?format=json&action=query&prop=categories&utf8=1&titles="+(URLEncoder.encode(pageNode.get("title").asText(), "UTF-8")+"&clcontinue="+clcontinue)), JsonNode.class);
//			}
//			if (categoryRootNode.get("continue") != null) {
//				clcontinue = categoryRootNode.get("continue").get("clcontinue").asText();
//			}
//			else {
//				clcontinue = null;
//			}
//			//System.out.println(categoryRootNode);
//			JsonNode pageCategoriesNode = categoryRootNode.get("query").get("pages");
//			Iterator<Entry<String, JsonNode>> fieldsIter = pageCategoriesNode.fields();
//			while (fieldsIter.hasNext()) {
//				JsonNode categoriesNode = fieldsIter.next().getValue().get("categories");
//				if (categoriesNode != null && categoriesNode.isArray()) {
//					for (final JsonNode categoryNode : categoriesNode) {
//						String categoryName = categoryNode.get("title").asText().substring("Category:".length());
//						if (!categoryName.startsWith("All") 
//								&& !categoryName.startsWith("Articles")
//								&& !categoryName.startsWith("Wikipedia") 
//								&& !categoryName.startsWith("CS1") 
//								&& !categoryName.startsWith("Pages") 
//								&& !categoryName.startsWith("Commons") 
//								&& !categoryName.startsWith("Disambiguation")
//								&& !categoryName.contains("disambiguation")
//								&& !categoryName.contains("wayback")
//								&& !categoryName.contains("dates")
//								&& !categoryName.contains("articles")
//								&& !categoryName.contains("inventions")
//								&& !categoryName.startsWith("Cleanup")
//								&& !categoryName.startsWith("Lists")
//								&& !categoryName.startsWith("Vague")
//								&& !categoryName.startsWith("Interlanguage")
//								&& !categoryName.startsWith("Webarchive")
//								&& !categoryName.startsWith("SI")
//								&& !categoryName.startsWith("Use")
//								&& !categoryName.startsWith("ISO")) {
//							//System.out.println(categoryName);
//							Double existingScore = scoreMap.get(categoryName);
//							if (existingScore == null) {
//								scoreMap.put(categoryName, new Double(topic.getScore()));
//							}
//							else {
//								scoreMap.put(categoryName, new Double(existingScore.doubleValue()+topic.getScore()));
//							}
//						}
//					}
//				}
//			}
//		}
//	}

}
