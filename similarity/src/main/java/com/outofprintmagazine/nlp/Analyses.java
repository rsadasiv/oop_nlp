package com.outofprintmagazine.nlp;


import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.math.RoundingMode;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Analyses {
	
	/*
	 * Turn this into a map of ArrayList<Score>
	 * Add Mysql persistence
	 * Create table if not exists
	 */
	private HashMap<String, List<Score>> categoricalScores = new HashMap<String, List<Score>>();
	private HashMap<String, List<Integer>> descriptiveScores = new HashMap<String, List<Integer>>();
	private HashMap<String, Score> scalarScores = new HashMap<String, Score>();
	private String corpus = "default";
	private String id;
	
	
	public Analyses() {
		super();
	}
	
	public String getCorpus() {
		return corpus;
	}

	public void setCorpus(String corpus) {
		this.corpus = corpus;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setScalarScores(HashMap<String, Score> scalarScores) {
		this.scalarScores = scalarScores;
	}

	public HashMap<String, Score> getScalarScores() {
		return scalarScores;
	}
	
	public void putScalarScore(String analysisName, Score score) {
		getScalarScores().put(analysisName, score);
	}
	
	public Score getScalarScore(String analysisName) {
		return getScalarScores().get(analysisName);
	}
	
	public void setCategoricalScores(HashMap<String, List<Score>> categoricalScores) {
		this.categoricalScores = categoricalScores;
	}

	public HashMap<String, List<Score>> getCategoricalScores() {
		return categoricalScores;
	}
	
	public void putCategoricalScore(String analysisName, List<Score> scores) {
		getCategoricalScores().put(analysisName, scores);
	}
	
	public List<Score> getCategoricalScore(String analysisName) {
		return getCategoricalScores().get(analysisName);
	}
	
	public void setDescriptiveScores(HashMap<String, List<Integer>> descriptiveScores) {
		this.descriptiveScores = descriptiveScores;
	}

	public HashMap<String, List<Integer>> getDescriptiveScores() {
		return descriptiveScores;
	}
	
	public void putDescriptiveScore(String analysisName, List<Integer> scores) {
		getDescriptiveScores().put(analysisName, scores);
	}
	
	public List<Integer> getDescriptiveScore(String analysisName) {
		return getDescriptiveScores().get(analysisName);
	}

	
	public String toString() {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(os);
		ArrayList<String> sortedKeys = new ArrayList<String>();
		sortedKeys.addAll(getScalarScores().keySet());
		Collections.sort(sortedKeys);
		for (String analysisName : sortedKeys) {
			ps.println(String.format("%s :", analysisName));
			ps.println(getScalarScore(analysisName));
		}
		
		
		sortedKeys.clear();
		sortedKeys.addAll(getCategoricalScores().keySet());
		Collections.sort(sortedKeys);
		for (String analysisName : sortedKeys) {
			ps.println(String.format("%s :", analysisName));
			for (Score score : getCategoricalScore(analysisName)) {
				ps.println(score);
			}
		}
		sortedKeys.clear();
		sortedKeys.addAll(getDescriptiveScores().keySet());
		Collections.sort(sortedKeys);
		for (String analysisName : sortedKeys) {
			ps.print(String.format("%s,", analysisName));
			List<Integer> scores = getDescriptiveScore(analysisName);
			for (int i=0;i<scores.size();i++) {
				Integer score = scores.get(i);
				ps.print(score.toString());
				if (i < scores.size()) {
					ps.print(",");
				}
			}
			ps.println();
		}
		try {
			return os.toString("UTF8");
		} 
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return (e.toString());
		}
	}
	
	public void toMysql(Connection conn) {
		//TODO
	}
	
	
//	public String toStringFiltered() {
//		ByteArrayOutputStream os = new ByteArrayOutputStream();
//		PrintStream ps = new PrintStream(os);
//		
//		ps.println("Tokens: " + getTokens());
//		ps.println("Sentences: " + getSentences());
//		ps.println("Paragraphs: " + getParagraphs());
//		
//		ps.println("People:");
//		for (int i=0;i<peopleScores.size() && i < 10; i++) {
//			Score score = peopleScores.get(i);
//			if (score.getScore() > 1) {
//				ps.println(score);
//			}
//		}
//		ps.println();
//		
//		
//		ps.println("Male:");
//		for (int i=0;i<maleScores.size() && i < 10; i++) {
//			Score score = maleScores.get(i);
//			if (score.getScore() > 1) {
//				ps.println(score);
//			}
//		}
//		ps.println();
//		
//		ps.println("Female:");
//		for (int i=0;i<femaleScores.size() && i < 10; i++) {
//			Score score = femaleScores.get(i);
//			if (score.getScore() > 1) {
//				ps.println(score);
//			}
//		}
//		ps.println();		
//		
//		ps.println("Places:");
//		for (int i=0;i<locationScores.size() && i < 20; i++) {
//			Score score = locationScores.get(i);
//			boolean shouldAdd = true;
//			for (Score person : peopleScores) {
//				if (person.getName().equals(score.getName())) {
//					shouldAdd = false;
//					break;
//				}
//			}
//			if (shouldAdd) {
//				if (score.getScore() > 1) {
//					ps.println(score);
//				}
//			}
//		}
//		ps.println();
//		
//		ps.println("Topics:");
//		for (int i=0;i<topicScores.size() && i < 20; i++) {
//			Score score = topicScores.get(i);
//			if (!Character.isUpperCase(score.getName().charAt(0))) {
//				if (score.getScore() > 1) {
//					ps.println(score);
//				}
//			}
//		}
//		ps.println();
//		
//		ps.println("TopicLexicalGroups:");
//		for (int i=0;i<topicLexicalGroupScores.size() && i < 20; i++) {
//			Score score = topicLexicalGroupScores.get(i);
//			if (!Character.isUpperCase(score.getName().charAt(0))) {
//				if (score.getScore() > 1) {
//					ps.println(score);
//				}
//			}
//		}
//		ps.println();
//		
//		ps.println("Adjectives:");
//		for (int i=0;i<adjectiveScores.size() && i < 20; i++) {
//			Score score = adjectiveScores.get(i);
//			if (score.getScore() > 1) {
//				ps.println(score);
//			}
//		}
//		ps.println();
//		
//		ps.println("Actions:");
//		for (int i=0;i<actionScores.size() && i < 20; i++) {
//			Score score = actionScores.get(i);
//			if (score.getScore() > 1) {
//				ps.println(score);
//			}
//		}
//		ps.println();
//		
//		ps.println("ActionLexicalGroups:");
//		for (int i=0;i<actionLexicalGroupScores.size() && i < 20; i++) {
//			Score score = actionLexicalGroupScores.get(i);
//			if (score.getScore() > 1) {
//				ps.println(score);
//			}
//		}
//		ps.println();		
//		
//		ps.println("Adverbs:");
//		for (int i=0;i<adverbScores.size() && i < 20; i++) {
//			Score score = adverbScores.get(i);
//			if (score.getScore() > 1) {
//				ps.println(score);
//			}
//		}
//		ps.println();
//		
//		ps.println("Colors:");
//		for (int i=0;i<colorScores.size() && i < 10; i++) {
//			Score score = colorScores.get(i);
//			ps.println(score);
//		}
//		ps.println();
//		
//		ps.println("Flavors:");
//		for (int i=0;i<flavorScores.size() && i < 10; i++) {
//			Score score = flavorScores.get(i);
//			ps.println(score);
//		}
//		ps.println();
//		
//		
//		ps.println("Questions:");
//		for (int i=0;i<questionScores.size() && i < 1; i++) {
//			Score score = questionScores.get(i);
//			ps.println(score.getName());
//		}
//		ps.println();
//		
//		ps.println("Sentiment:");
//		double documentSentimentSum = 0.00;
//		for (int i=0;i<sentimentScores.size(); i++) {
//			int score = sentimentScores.get(i).getScore();
//			documentSentimentSum += score;
//		}
//		if (sentimentScores.size() == 0) {
//			ps.println(0);
//		}
//		else {
//			DecimalFormat df = new DecimalFormat("0.00"); 
//			df.setRoundingMode(RoundingMode.CEILING);
//			ps.println(df.format(documentSentimentSum/sentimentScores.size()));	
//		}
//		ps.println();		
//		
//
//		try {
//			return os.toString("UTF8");
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return (e.toString());
//		}

		
}
