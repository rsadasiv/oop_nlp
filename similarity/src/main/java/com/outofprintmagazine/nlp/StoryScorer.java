package com.outofprintmagazine.nlp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;

import com.outofprintmagazine.nlp.scorers.WordlessWords;
import com.outofprintmagazine.nlp.scorers.categorical.*;
import com.outofprintmagazine.nlp.scorers.descriptive.*;
import com.outofprintmagazine.nlp.scorers.scalar.*;

import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;

public class StoryScorer {
	
	List<DocumentCategoricalScorer> categoricalScorers = new ArrayList<DocumentCategoricalScorer>();
	List<SentenceDescriptiveScorer> descriptiveScorers = new ArrayList<SentenceDescriptiveScorer>();
	List<DocumentScalarScorer> scalarScorers = new ArrayList<DocumentScalarScorer>();
	List<DocumentCategoricalScorer> preScalarScorers = new ArrayList<DocumentCategoricalScorer>();
	
	public StoryScorer() throws IOException {
		super();
	}

	public void setScorers(Ta ta) throws IOException {

		scalarScorers.add(new Tokens(ta));
		scalarScorers.add(new Sentences(ta));
		scalarScorers.add(new Phrases(ta));		
		scalarScorers.add(new Paragraphs(ta));

		scalarScorers.add(new VerblessSentences(ta));
		scalarScorers.add(new Readability(ta));

		scalarScorers.add(new Dialect(ta));
		scalarScorers.add(new Vocabulary(ta));
		scalarScorers.add(new Ambiguity(ta));
		scalarScorers.add(new Verbly(ta));
		scalarScorers.add(new Vividness(ta));	

				
		//Document Metrics
		categoricalScorers.add(new com.outofprintmagazine.nlp.scorers.VerbGroups(ta));
//		categoricalScorers.add(new com.outofprintmagazine.nlp.scorers.ActionGroups(ta));
//		categoricalScorers.add(new com.outofprintmagazine.nlp.scorers.TopicGroups(ta));
//		categoricalScorers.add(new com.outofprintmagazine.nlp.scorers.Actions(ta));
		categoricalScorers.add(new com.outofprintmagazine.nlp.scorers.Topics(ta));
//		categoricalScorers.add(new com.outofprintmagazine.nlp.scorers.Lemmas(ta));
//		categoricalScorers.add(new com.outofprintmagazine.nlp.scorers.Locations(ta));
//		categoricalScorers.add(new com.outofprintmagazine.nlp.scorers.categorical.Pronouns(ta));
//		categoricalScorers.add(new com.outofprintmagazine.nlp.scorers.categorical.Genders(ta));
		//categoricalScorers.add(new com.outofprintmagazine.nlp.scorers.categorical.Verbs(ta));
//		categoricalScorers.add(new com.outofprintmagazine.nlp.scorers.categorical.Possessives(ta));
//		categoricalScorers.add(new com.outofprintmagazine.nlp.scorers.categorical.Sentiments(ta));
		categoricalScorers.add(new com.outofprintmagazine.nlp.scorers.categorical.Punctuation(ta));

//		
//		//Document Specifics
//		QualitativeScorers.add(new com.outofprintmagazine.nlp.scorers.People(ta));
//
//		
//		//Sentence Metrics
//		QuantitativeScorers.add(new com.outofprintmagazine.nlp.scorers.Actions(ta));
//		QuantitativeScorers.add(new com.outofprintmagazine.nlp.scorers.Topics(ta));
//		QuantitativeScorers.add(new com.outofprintmagazine.nlp.scorers.Adjectives(ta));
//		QuantitativeScorers.add(new com.outofprintmagazine.nlp.scorers.descriptive.Questions(ta));	
//		categoricalScorers.add(new com.outofprintmagazine.nlp.scorers.UncommonWords(ta));
		//categoricalScorers.add(new com.outofprintmagazine.nlp.scorers.UncommonSenses(ta));
//		QuantitativeScorers.add(new com.outofprintmagazine.nlp.scorers.descriptive.VaderSentiments(ta));			
	}
	
	
	public Analyses score(Story story) throws IOException {
		Analyses analyses = new Analyses();
		analyses.setCorpus("Submissions");
		analyses.setId("1");
		Ta ta = new Ta();
		setScorers(ta);
		CoreDocument document = ta.annotate(story.getBody());
		//CoreDocument document = ta.annotate("I hit the ball with my bat. I ran from the spotlight. me good.");
		for (DocumentScalarScorer documentScalarScorer : scalarScorers) {
			analyses.putScalarScore(documentScalarScorer.getClass().getSimpleName(), documentScalarScorer.scoreDocument(document));
		}
		
		for (DocumentCategoricalScorer documentScorer : categoricalScorers) {
			analyses.putCategoricalScore(documentScorer.getClass().getSimpleName(), documentScorer.scoreDocument(document));
		}
		for (CoreSentence sentence : document.sentences()) {
			for (SentenceDescriptiveScorer sentenceScorer : descriptiveScorers) {
				List<Integer> scores = analyses.getDescriptiveScore(sentenceScorer.getClass().getSimpleName());
				if (scores == null) {
					scores = new ArrayList<Integer>();
				}
				scores.addAll(sentenceScorer.scoreSentence(sentence));
				analyses.putDescriptiveScore(sentenceScorer.getClass().getSimpleName(), scores);
			}
		}
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
	    //File file = new File(dropboxFolder + "Prashant Bhagat.Accomplishment.mod.docx");

	    //dropboxFolder = "C:\\Users\\rsada\\Dropbox\\Out of Print Reading\\Potential Stories Issue 31\\Stories\\";
	    //File file = new File(dropboxFolder + "Salvatore Difalco.Time of the Djinns.docx");
	    
	    //File file = new File("C:\\Users\\rsada\\git\\oop_nlp\\similarity\\resources\\Dickens\\BOOK_1_CHAPTER_10.txt");
	    //File file = new File("C:\\Users\\rsada\\git\\oop_nlp\\similarity\\resources\\Austen\\CHAPTER_10.txt");
	    File file = new File("C:\\Users\\rsada\\git\\oop_nlp\\similarity\\resources\\blogger/1555267.male.35.Engineering.Aquarius.xml.txt");
	    //File file = new File("C:\\Users\\rsada\\git\\oop_nlp\\similarity\\resources\\wiki/Raymond_Chandler.txt");
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
