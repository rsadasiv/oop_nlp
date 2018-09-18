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

	public Analysis score(Story story) throws IOException {
		Analysis analysis = new Analysis();
		Ta ta = new Ta();
		CoreDocument document = ta.annotate(story.getBody());
		CountScorer countScorer = new CountScorer();		
		MentionTypeScorer mentionTypeScorer = new MentionTypeScorer();
		SentenceQuestionScorer sentenceQuestionScorer = new SentenceQuestionScorer();
		PosScorer posScorer = new PosScorer();
		PosLexicalGroupScorer posLexicalGroupScorer = new PosLexicalGroupScorer();		
		SentimentScorer sentimentScorer = new SentimentScorer();

		analysis.setCountScores( 
				countScorer.score(
					document, 
					null,
					null,
					null)
				);
		
		analysis.setPeopleScores( 
				mentionTypeScorer.score(
					document, 
					Arrays.asList("PERSON"),
					Files.readAllLines(new File("C:\\Users\\rsada\\git\\oop_nlp\\similarity\\resources\\Pronouns.txt").toPath(), Charset.defaultCharset()),
					null)
				);

		analysis.setMaleScores( 
				mentionTypeScorer.score(
					document, 
					Arrays.asList("PERSON"),
					null,
					Arrays.asList("MALE"))
				);
	
		analysis.setFemaleScores( 
				mentionTypeScorer.score(
					document, 
					Arrays.asList("PERSON"),
					null,
					Arrays.asList("FEMALE"))
				);
		
		analysis.setLocationScores(
				mentionTypeScorer.score(
					document, 
					Arrays.asList("LOCATION"), 
					null,
					null)
				);

		analysis.setQuestionScores( 
				sentenceQuestionScorer.score(
					document,
					Arrays.asList("?"),
					null,
					null)
				);
		
		analysis.setTopicScores( 
				posScorer.score(
					document, 
					Arrays.asList("NN","NNS","NNP","NNPS"),
					null, 
					null)
				);
		
		analysis.setTopicLexicalGroupScores( 
				posLexicalGroupScorer.score(
					document, 
					Arrays.asList("NN","NNS","NNP","NNPS"),
					null, 
					null)
				);
		
		analysis.setActionScores( 
				posScorer.score(
					document, 
					Arrays.asList("VB","VBD","VBG","VBN","VBP","VBZ"), 
					Files.readAllLines(new File("C:\\Users\\rsada\\git\\oop_nlp\\similarity\\resources\\StativeVerbs.txt").toPath(), Charset.defaultCharset()), 
					null)
				);

		analysis.setActionLexicalGroupScores( 
				posLexicalGroupScorer.score(
					document, 
					Arrays.asList("VB","VBD","VBG","VBN","VBP","VBZ"),
					Files.readAllLines(new File("C:\\Users\\rsada\\git\\oop_nlp\\similarity\\resources\\StativeVerbs.txt").toPath(), Charset.defaultCharset()),
					null)
				);		
		
		analysis.setSentimentScores( 
				sentimentScorer.score(
					document, 
					null,
					null, 
					null)
				);		
		
		analysis.setAdjectiveScores( 
				posScorer.score(
					document, 
					Arrays.asList("JJ","JJR","JJS"), 
					Files.readAllLines(new File("C:\\Users\\rsada\\git\\oop_nlp\\similarity\\resources\\Adjectives.txt").toPath(), Charset.defaultCharset()), 
					null)
				);

		analysis.setAdverbScores(
				posScorer.score(
					document, 
					Arrays.asList("RB","RBR","RBS"), 
					Files.readAllLines(new File("C:\\Users\\rsada\\git\\oop_nlp\\similarity\\resources\\Adverbs.txt").toPath(), Charset.defaultCharset()), 
					null)
				);
		
		
		analysis.setColorScores(
				posScorer.score(
					document, 
					Arrays.asList("JJ","JJR","JJS"), 
					null, 
					Files.readAllLines(new File("C:\\Users\\rsada\\git\\oop_nlp\\similarity\\resources\\Colors.txt").toPath(), Charset.defaultCharset()))
				);		
		
		analysis.setFlavorScores(
				posScorer.score(
					document, 
					Arrays.asList("JJ","JJR","JJS"), 
					null, 
					Files.readAllLines(new File("C:\\Users\\rsada\\git\\oop_nlp\\similarity\\resources\\Flavors.txt").toPath(), Charset.defaultCharset()))
				);	

		return analysis;
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
					System.out.println(me.score(story));
					System.out.println("-----------------------------------------");
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
