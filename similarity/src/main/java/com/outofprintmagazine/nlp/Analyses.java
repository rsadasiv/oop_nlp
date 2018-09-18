package com.outofprintmagazine.nlp;


import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Analysis {
	
	public Analysis() {
		super();
	}
	

	public int getTokens() {
		for (Score score : countScores) {
			if (score.getName().equals("tokenCount")) {
				return score.getScore();
			}
		}
		return 0;
	}

	public int getSentences() {
		for (Score score : countScores) {
			if (score.getName().equals("sentenceCount")) {
				return score.getScore();
			}
		}
		return 0;
	}
	public int getParagraphs() {
		for (Score score : countScores) {
			if (score.getName().equals("paragraphCount")) {
				return score.getScore();
			}
		}
		return 0;
	}

	public ArrayList<Score> getMaleScores() {
		return maleScores;
	}


	public void setMaleScores(ArrayList<Score> maleScores) {
		this.maleScores = maleScores;
	}


	public ArrayList<Score> getFemaleScores() {
		return femaleScores;
	}


	public void setFemaleScores(ArrayList<Score> femaleScores) {
		this.femaleScores = femaleScores;
	}

	ArrayList<Score> maleScores = new ArrayList<Score>();
	ArrayList<Score> femaleScores = new ArrayList<Score>();
	ArrayList<Score> countScores = new ArrayList<Score>();
	ArrayList<Score> peopleScores = new ArrayList<Score>();
	ArrayList<Score> locationScores = new ArrayList<Score>();
	ArrayList<Score> questionScores = new ArrayList<Score>();
	ArrayList<Score> topicScores = new ArrayList<Score>();
	ArrayList<Score> topicLexicalGroupScores = new ArrayList<Score>();
	ArrayList<Score> actionScores = new ArrayList<Score>();
	ArrayList<Score> actionLexicalGroupScores = new ArrayList<Score>();
	ArrayList<Score> sentimentScores = new ArrayList<Score>();
	ArrayList<Score> adjectiveScores = new ArrayList<Score>();
	ArrayList<Score> adverbScores = new ArrayList<Score>();
	ArrayList<Score> colorScores = new ArrayList<Score>();
	ArrayList<Score> flavorScores = new ArrayList<Score>();
	
	public ArrayList<Score> getCountScores() {
		return countScores;
	}
	public void setCountScores(ArrayList<Score> countScores) {
		this.countScores = countScores;
	}
	public ArrayList<Score> getPeopleScores() {
		return peopleScores;
	}
	public void setPeopleScores(ArrayList<Score> peopleScores) {
		this.peopleScores = peopleScores;
	}
	public ArrayList<Score> getLocationScores() {
		return locationScores;
	}
	public void setLocationScores(ArrayList<Score> locationScores) {
		this.locationScores = locationScores;
	}
	public ArrayList<Score> getQuestionScores() {
		return questionScores;
	}
	public void setQuestionScores(ArrayList<Score> questionScores) {
		this.questionScores = questionScores;
	}
	public ArrayList<Score> getTopicScores() {
		return topicScores;
	}
	public void setTopicScores(ArrayList<Score> topicScores) {
		this.topicScores = topicScores;
	}
	public ArrayList<Score> getTopicLexicalGroupScores() {
		return topicLexicalGroupScores;
	}
	public void setTopicLexicalGroupScores(ArrayList<Score> topicLexicalGroupScores) {
		this.topicLexicalGroupScores = topicLexicalGroupScores;
	}
	public ArrayList<Score> getActionScores() {
		return actionScores;
	}
	public void setActionScores(ArrayList<Score> actionScores) {
		this.actionScores = actionScores;
	}
	public ArrayList<Score> getActionLexicalGroupScores() {
		return actionLexicalGroupScores;
	}
	public void setActionLexicalGroupScores(ArrayList<Score> actionLexicalGroupScores) {
		this.actionLexicalGroupScores = actionLexicalGroupScores;
	}
	public ArrayList<Score> getSentimentScores() {
		return sentimentScores;
	}
	public void setSentimentScores(ArrayList<Score> sentimentScores) {
		this.sentimentScores = sentimentScores;
	}
	public ArrayList<Score> getAdjectiveScores() {
		return adjectiveScores;
	}
	public void setAdjectiveScores(ArrayList<Score> adjectiveScores) {
		this.adjectiveScores = adjectiveScores;
	}
	public ArrayList<Score> getAdverbScores() {
		return adverbScores;
	}
	public void setAdverbScores(ArrayList<Score> adverbScores) {
		this.adverbScores = adverbScores;
	}
	public ArrayList<Score> getColorScores() {
		return colorScores;
	}
	public void setColorScores(ArrayList<Score> colorScores) {
		this.colorScores = colorScores;
	}
	public ArrayList<Score> getFlavorScores() {
		return flavorScores;
	}
	public void setFlavorScores(ArrayList<Score> flavorScores) {
		this.flavorScores = flavorScores;
	}
	
	public String toString() {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(os);

		ps.println("Tokens: " + getTokens());
		ps.println("Sentences: " + getSentences());
		ps.println("Paragraphs: " + getParagraphs());
		
		ps.println("People:");
		for (int i=0;i<peopleScores.size() && i < 10; i++) {
			Score score = peopleScores.get(i);
			if (score.getScore() > 1) {
				ps.println(score);
			}
		}
		ps.println();
		
		ps.println("Male:");
		for (int i=0;i<maleScores.size() && i < 10; i++) {
			Score score = maleScores.get(i);
			if (score.getScore() > 1) {
				ps.println(score);
			}
		}
		ps.println();
		
		ps.println("Female:");
		for (int i=0;i<femaleScores.size() && i < 10; i++) {
			Score score = femaleScores.get(i);
			if (score.getScore() > 1) {
				ps.println(score);
			}
		}
		ps.println();		
		
		ps.println("Places:");
		for (int i=0;i<locationScores.size() && i < 20; i++) {
			Score score = locationScores.get(i);
			boolean shouldAdd = true;
			for (Score person : peopleScores) {
				if (person.getName().equals(score.getName())) {
					shouldAdd = false;
					break;
				}
			}
			if (shouldAdd) {
				if (score.getScore() > 1) {
					ps.println(score);
				}
			}
		}
		ps.println();
		
		ps.println("Topics:");
		for (int i=0;i<topicScores.size() && i < 20; i++) {
			Score score = topicScores.get(i);
			if (!Character.isUpperCase(score.getName().charAt(0))) {
				if (score.getScore() > 1) {
					ps.println(score);
				}
			}
		}
		ps.println();
		
		ps.println("TopicLexicalGroups:");
		for (int i=0;i<topicLexicalGroupScores.size() && i < 20; i++) {
			Score score = topicLexicalGroupScores.get(i);
			if (!Character.isUpperCase(score.getName().charAt(0))) {
				if (score.getScore() > 1) {
					ps.println(score);
				}
			}
		}
		ps.println();
		
		ps.println("Adjectives:");
		for (int i=0;i<adjectiveScores.size() && i < 20; i++) {
			Score score = adjectiveScores.get(i);
			if (score.getScore() > 1) {
				ps.println(score);
			}
		}
		ps.println();
		
		ps.println("Actions:");
		for (int i=0;i<actionScores.size() && i < 20; i++) {
			Score score = actionScores.get(i);
			if (score.getScore() > 1) {
				ps.println(score);
			}
		}
		ps.println();
		
		ps.println("ActionLexicalGroups:");
		for (int i=0;i<actionLexicalGroupScores.size() && i < 20; i++) {
			Score score = actionLexicalGroupScores.get(i);
			if (score.getScore() > 1) {
				ps.println(score);
			}
		}
		ps.println();		
		
		ps.println("Adverbs:");
		for (int i=0;i<adverbScores.size() && i < 20; i++) {
			Score score = adverbScores.get(i);
			if (score.getScore() > 1) {
				ps.println(score);
			}
		}
		ps.println();
		
		ps.println("Colors:");
		for (int i=0;i<colorScores.size() && i < 10; i++) {
			Score score = colorScores.get(i);
			ps.println(score);
		}
		ps.println();
		
		ps.println("Flavors:");
		for (int i=0;i<flavorScores.size() && i < 10; i++) {
			Score score = flavorScores.get(i);
			ps.println(score);
		}
		ps.println();
		
		
		ps.println("Questions:");
		for (int i=0;i<questionScores.size() && i < 1; i++) {
			Score score = questionScores.get(i);
			ps.println(score.getName());
		}
		ps.println();
		
		ps.println("Sentiment:");
		double documentSentimentSum = 0.00;
		for (int i=0;i<sentimentScores.size(); i++) {
			int score = sentimentScores.get(i).getScore();
			documentSentimentSum += score;
		}
		if (sentimentScores.size() == 0) {
			ps.println(0);
		}
		else {
			DecimalFormat df = new DecimalFormat("0.00"); 
			df.setRoundingMode(RoundingMode.CEILING);
			ps.println(df.format(documentSentimentSum/sentimentScores.size()));	
		}
		ps.println();		
		

		try {
			return os.toString("UTF8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return (e.toString());
		}

		
	}



	
	

}
