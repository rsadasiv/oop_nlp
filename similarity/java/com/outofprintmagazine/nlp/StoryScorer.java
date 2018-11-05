package com.outofprintmagazine.nlp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;

import edu.stanford.nlp.io.IOUtils;
import edu.stanford.nlp.pipeline.CoreDocument;

public class StoryScorer {
	
	
	public StoryScorer() {
		super();
	}

	public void score(Story story) throws IOException {
		Ta ta = new Ta();
		CoreDocument document = ta.annotate(story.getBody());
		
		MentionTypeScorer mentionTypeScorer = new MentionTypeScorer();
		SentenceQuestionScorer sentenceQuestionScorer = new SentenceQuestionScorer();
		PosScorer posScorer = new PosScorer();
		PosLexicalGroupScorer posLexicalGroupScorer = new PosLexicalGroupScorer();		
		SentimentScorer sentimentScorer = new SentimentScorer();
		
		ArrayList<Score> peopleScores = 
				mentionTypeScorer.score(
					document, 
					Arrays.asList("PERSON"),
					Files.readAllLines(new File("c:/users/rsada/eclipse-workspace/NLP/src/main/resources/Pronouns.txt").toPath(), Charset.defaultCharset()),
					null);
		
		ArrayList<Score> locationScores = 
				mentionTypeScorer.score(
					document, 
					Arrays.asList("LOCATION"), 
					null,
					null);

		ArrayList<Score> questionScores = 
				sentenceQuestionScorer.score(
					document,
					Arrays.asList("?"),
					null,
					null);
		
		ArrayList<Score> topicScores = 
				posScorer.score(
					document, 
					Arrays.asList("NN","NNS","NNP","NNPS"),
					null, 
					null);
		
		ArrayList<Score> topicLexicalGroupScores = 
				posLexicalGroupScorer.score(
					document, 
					Arrays.asList("NN","NNS","NNP","NNPS"),
					null, 
					null);
		
		ArrayList<Score> actionScores = 
				posScorer.score(
					document, 
					Arrays.asList("VB","VBD","VBG","VBN","VBP","VBZ"), 
					Files.readAllLines(new File("c:/users/rsada/eclipse-workspace/NLP/src/main/resources/StativeVerbs.txt").toPath(), Charset.defaultCharset()), 
					null);

		ArrayList<Score> actionLexicalGroupScores = 
				posLexicalGroupScorer.score(
					document, 
					Arrays.asList("VB","VBD","VBG","VBN","VBP","VBZ"),
					Files.readAllLines(new File("c:/users/rsada/eclipse-workspace/NLP/src/main/resources/StativeVerbs.txt").toPath(), Charset.defaultCharset()),
					null);		
		
		ArrayList<Score> sentimentScores = 
				sentimentScorer.score(
					document, 
					null,
					null, 
					null);		
		
		ArrayList<Score> adjectiveScores = 
				posScorer.score(
					document, 
					Arrays.asList("JJ","JJR","JJS"), 
					Files.readAllLines(new File("c:/users/rsada/eclipse-workspace/NLP/src/main/resources/Adjectives.txt").toPath(), Charset.defaultCharset()), 
					null);

		ArrayList<Score> adverbScores = 
				posScorer.score(
					document, 
					Arrays.asList("RB","RBR","RBS"), 
					Files.readAllLines(new File("c:/users/rsada/eclipse-workspace/NLP/src/main/resources/Adverbs.txt").toPath(), Charset.defaultCharset()), 
					null);
		
		
		ArrayList<Score> colorScores = 
				posScorer.score(
					document, 
					Arrays.asList("JJ","JJR","JJS"), 
					null, 
					Files.readAllLines(new File("c:/users/rsada/eclipse-workspace/NLP/src/main/resources/Colors.txt").toPath(), Charset.defaultCharset()));		
		
		ArrayList<Score> flavorScores = 
				posScorer.score(
					document, 
					Arrays.asList("JJ","JJR","JJS"), 
					null, 
					Files.readAllLines(new File("c:/users/rsada/eclipse-workspace/NLP/src/main/resources/Flavors.txt").toPath(), Charset.defaultCharset()));	

		
		System.out.println("People:");
		for (int i=0;i<peopleScores.size() && i < 10; i++) {
			Score score = peopleScores.get(i);
			if (score.getScore() > 1) {
				System.out.println(score);
			}
		}
		System.out.println();
		
		System.out.println("Places:");
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
					System.out.println(score);
				}
			}
		}
		System.out.println();
		
		System.out.println("Topics:");
		for (int i=0;i<topicScores.size() && i < 20; i++) {
			Score score = topicScores.get(i);
			if (!Character.isUpperCase(score.getName().charAt(0))) {
				if (score.getScore() > 1) {
					System.out.println(score);
				}
			}
		}
		System.out.println();
		
		System.out.println("TopicLexicalGroups:");
		for (int i=0;i<topicLexicalGroupScores.size() && i < 20; i++) {
			Score score = topicLexicalGroupScores.get(i);
			if (!Character.isUpperCase(score.getName().charAt(0))) {
				if (score.getScore() > 1) {
					System.out.println(score);
				}
			}
		}
		System.out.println();
		
		System.out.println("Adjectives:");
		for (int i=0;i<adjectiveScores.size() && i < 20; i++) {
			Score score = adjectiveScores.get(i);
			if (score.getScore() > 1) {
				System.out.println(score);
			}
		}
		System.out.println();
		
		System.out.println("Actions:");
		for (int i=0;i<actionScores.size() && i < 20; i++) {
			Score score = actionScores.get(i);
			if (score.getScore() > 1) {
				System.out.println(score);
			}
		}
		System.out.println();
		
		System.out.println("ActionLexicalGroups:");
		for (int i=0;i<actionLexicalGroupScores.size() && i < 20; i++) {
			Score score = actionLexicalGroupScores.get(i);
			if (score.getScore() > 1) {
				System.out.println(score);
			}
		}
		System.out.println();		
		
		System.out.println("Adverbs:");
		for (int i=0;i<adverbScores.size() && i < 20; i++) {
			Score score = adverbScores.get(i);
			if (score.getScore() > 1) {
				System.out.println(score);
			}
		}
		System.out.println();
		
		System.out.println("Colors:");
		for (int i=0;i<colorScores.size() && i < 10; i++) {
			Score score = colorScores.get(i);
			System.out.println(score);
		}
		System.out.println();
		
		System.out.println("Flavors:");
		for (int i=0;i<flavorScores.size() && i < 10; i++) {
			Score score = flavorScores.get(i);
			System.out.println(score);
		}
		System.out.println();
		
		
		System.out.println("Questions:");
		for (int i=0;i<questionScores.size() && i < 1; i++) {
			Score score = questionScores.get(i);
			System.out.println(score.getName());
		}
		System.out.println();
		
		System.out.println("Sentiment:");
		double documentSentimentSum = 0.00;
		for (int i=0;i<sentimentScores.size(); i++) {
			int score = sentimentScores.get(i).getScore();
			documentSentimentSum += score;
		}
		if (sentimentScores.size() == 0) {
			System.out.println(0);
		}
		else {
			DecimalFormat df = new DecimalFormat("0.00"); 
			df.setRoundingMode(RoundingMode.CEILING);
			System.out.println(df.format(documentSentimentSum/sentimentScores.size()));	
		}
		System.out.println();
	}
	
	public static void main(String[] args) throws IOException, TikaException, ParseException {
		StoryScorer.mainIssue(args);
	}
	
	public static void mainLocal(String[] args)  throws IOException, TikaException {
		StoryScorer me = new StoryScorer();
		Story story = new Story();
	    Tika tika = new Tika();
	    String dropboxFolder = "C:\\Users\\rsada\\Dropbox\\Out of Print Reading\\Potential Stories Issue 32\\Stories\\";
	    //File file = new File(dropboxFolder + "Giorgia Stavropoulou.Once Upon a Time in Hindustan.mod1.rs.docx");
	    //File file = new File(dropboxFolder + "Nidhi.Arora It is in the eyes.docx");
	    //File file = new File(dropboxFolder + "Alina Gufran.After Hours.mod.docx");
	    File file = new File(dropboxFolder + "Prashant Bhagat.Accomplishment.mod.docx");

	    dropboxFolder = "C:\\Users\\rsada\\Dropbox\\Out of Print Reading\\Potential Stories Issue 31\\Stories\\";
	    //File file = new File(dropboxFolder + "Salvatore Difalco.Time of the Djinns.docx");
	    FileInputStream fis = new FileInputStream(file);
	    story.setBody(tika.parseToString(fis));

		//story.setBody(IOUtils.slurpFile("c:/users/rsada/eclipse-workspace/NLP/src/main/resources/Story.txt", "UTF-8"));
		me.score(story);
	}
	
	public static void mainIssue(String[] args) throws IOException, ParseException {
		StoryScorer me = new StoryScorer();
		Extractor extractor = new Extractor();
//		List<Issue> issues = extractor.extractAllIssues();
		ArrayList<Issue> issues = new ArrayList<Issue>();
		Issue iss = new Issue();
		iss.setUrl("http://www.outofprintmagazine.co.in/archive/dec_2016-issue/index.html");
		issues.add(iss);
		for (Issue issue : issues) {
			try {
				extractor.extractIssueData(issue);
				System.out.println("Issue:");
				System.out.println(issue);
				for (Story story : issue.getStories()) {
					System.out.println("Story:");
					System.out.println(story);
					me.score(story);
					System.out.println("-----------------------------------------");
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
