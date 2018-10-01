package com.outofprintmagazine.nlp.scratch;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;

import com.outofprintmagazine.nlp.Analyses;
import com.outofprintmagazine.nlp.Extractor;
import com.outofprintmagazine.nlp.Issue;
import com.outofprintmagazine.nlp.Story;
import com.outofprintmagazine.nlp.Ta;

import edu.stanford.nlp.pipeline.CoreDocument;

public class StoryScorer {
	
	
	public StoryScorer() {
		super();
	}

	public Analyses score(Story story) throws IOException {
		Analyses analyses = new Analyses();
		Ta ta = new Ta();
		CoreDocument document = ta.annotate(story.getBody());
		//DocumentLength countScorer = new DocumentLength();		
//		MentionTypeScorer mentionTypeScorer = new MentionTypeScorer();
//		SentenceQuestionScorer sentenceQuestionScorer = new SentenceQuestionScorer();
//		PosScorer posScorer = new PosScorer();
//		PosLexicalGroupScorer posLexicalGroupScorer = new PosLexicalGroupScorer();		
//		SentimentScorer sentimentScorer = new SentimentScorer();
//		VaderSentimentScorer vaderSentimentScorer = new VaderSentimentScorer();		
//		UncommonScorer uncommonScorer = new UncommonScorer();
//		Readability readablityScorer = new Readability();		

//		analyses.putAnalysisScore("CountScores",  
//				countScorer.score(
//					document, 
//					null,
//					null,
//					null)
//				);
//		
//		analyses.putAnalysisScore("PeopleScores", 
//				mentionTypeScorer.score(
//					document, 
//					Arrays.asList("PERSON"),
//					Files.readAllLines(new File("C:\\Users\\rsada\\git\\oop_nlp\\similarity\\resources\\Pronouns.txt").toPath(), Charset.defaultCharset()),
//					null)
//				);
//
//		analyses.putAnalysisScore("MaleScores", 
//				mentionTypeScorer.score(
//					document, 
//					Arrays.asList("PERSON"),
//					null,
//					Arrays.asList("MALE"))
//				);
//	
//		analyses.putAnalysisScore("FemaleScores",
//				mentionTypeScorer.score(
//					document, 
//					Arrays.asList("PERSON"),
//					null,
//					Arrays.asList("FEMALE"))
//				);
//		
//		analyses.putAnalysisScore("LocationScores",
//				mentionTypeScorer.score(
//					document, 
//					Arrays.asList("LOCATION"), 
//					null,
//					null)
//				);
//
//		analyses.putAnalysisScore("QuestionScores", 
//				sentenceQuestionScorer.score(
//					document,
//					Arrays.asList("?"),
//					null,
//					null)
//				);
//		
//		analyses.putAnalysisScore("TopicScores", 
//				posScorer.score(
//					document, 
//					Arrays.asList("NN","NNS","NNP","NNPS"),
//					null, 
//					null)
//				);
//		
//		analyses.putAnalysisScore("TopicLexicalGroupScores",
//				posLexicalGroupScorer.score(
//					document, 
//					Arrays.asList("NN","NNS","NNP","NNPS"),
//					null, 
//					null)
//				);
//		
//		analyses.putAnalysisScore("ActionScores",
//				posScorer.score(
//					document, 
//					Arrays.asList("VB","VBD","VBG","VBN","VBP","VBZ"), 
//					Files.readAllLines(new File("C:\\Users\\rsada\\git\\oop_nlp\\similarity\\resources\\StativeVerbs.txt").toPath(), Charset.defaultCharset()), 
//					null)
//				);
//
//		analyses.putAnalysisScore("ActionLexicalGroupScores",
//				posLexicalGroupScorer.score(
//					document, 
//					Arrays.asList("VB","VBD","VBG","VBN","VBP","VBZ"),
//					Files.readAllLines(new File("C:\\Users\\rsada\\git\\oop_nlp\\similarity\\resources\\StativeVerbs.txt").toPath(), Charset.defaultCharset()),
//					null)
//				);		
//		
//		analyses.putAnalysisScore("SentimentScores", 
//				sentimentScorer.score(
//					document, 
//					null,
//					null, 
//					null)
//				);		
//
//		analyses.putAnalysisScore("VaderSentimentScores", 
//				vaderSentimentScorer.score(
//					document, 
//					null,
//					null, 
//					null)
//				);	
//		
////		analyses.putAnalysisScore("UncommonScores", 
////				uncommonScorer.score(
////					document, 
////					null,
////					null, 
////					null)
////				);	
//		
//		analyses.putAnalysisScore("AdjectiveScores", 
//				posScorer.score(
//					document, 
//					Arrays.asList("JJ","JJR","JJS"), 
//					Files.readAllLines(new File("C:\\Users\\rsada\\git\\oop_nlp\\similarity\\resources\\Adjectives.txt").toPath(), Charset.defaultCharset()), 
//					null)
//				);
//
////		analyses.putAnalysisScore("ReadabilityScores",
////				readablityScorer.score(
////					document, 
////					null, 
////					null, 
////					null)
////				);
//		
//		analyses.putAnalysisScore("AdverbScores",
//				posScorer.score(
//					document, 
//					Arrays.asList("RB","RBR","RBS"), 
//					Files.readAllLines(new File("C:\\Users\\rsada\\git\\oop_nlp\\similarity\\resources\\Adverbs.txt").toPath(), Charset.defaultCharset()), 
//					null)
//				);
//		
//		
//		analyses.putAnalysisScore("ColorScores",
//				posScorer.score(
//					document, 
//					Arrays.asList("JJ","JJR","JJS"), 
//					null, 
//					Files.readAllLines(new File("C:\\Users\\rsada\\git\\oop_nlp\\similarity\\resources\\Colors.txt").toPath(), Charset.defaultCharset()))
//				);		
//		
//		analyses.putAnalysisScore("FlavorScores",
//				posScorer.score(
//					document, 
//					Arrays.asList("JJ","JJR","JJS"), 
//					null, 
//					Files.readAllLines(new File("C:\\Users\\rsada\\git\\oop_nlp\\similarity\\resources\\Flavors.txt").toPath(), Charset.defaultCharset()))
//				);	

		return analyses;
	}
	
	public static void main(String[] args) throws IOException, TikaException, ParseException {
		StoryScorer.mainLocal(args);
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

	    //dropboxFolder = "C:\\Users\\rsada\\Dropbox\\Out of Print Reading\\Potential Stories Issue 31\\Stories\\";
	    //File file = new File(dropboxFolder + "Salvatore Difalco.Time of the Djinns.docx");
	    FileInputStream fis = new FileInputStream(file);
	    story.setBody(tika.parseToString(fis));

		//story.setBody(IOUtils.slurpFile("c:/users/rsada/eclipse-workspace/NLP/src/main/resources/Story.txt", "UTF-8"));
		System.out.println(me.score(story));
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
