package com.outofprintmagazine.nlp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;

import com.outofprintmagazine.nlp.scorers.categorical.DocumentCategoricalScorer;
import com.outofprintmagazine.nlp.scorers.categorical.DocumentRankedCategoricalScorer;
import com.outofprintmagazine.nlp.scorers.descriptive.SentenceDescriptiveScorer;
import com.outofprintmagazine.nlp.scorers.scalar.DocumentScalarScorer;

import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;


public class StoryScorer {
	List<DocumentScalarScorer> scalarScorers = new ArrayList<DocumentScalarScorer>();
	List<DocumentRankedCategoricalScorer> categoricalRankedScorers = new ArrayList<DocumentRankedCategoricalScorer>();
	List<DocumentCategoricalScorer> categoricalScorers = new ArrayList<DocumentCategoricalScorer>();

	
	List<SentenceDescriptiveScorer> descriptiveScorers = new ArrayList<SentenceDescriptiveScorer>();
	List<DocumentCategoricalScorer> preScalarScorers = new ArrayList<DocumentCategoricalScorer>();
	
	public StoryScorer() throws IOException {
		super();
	}
	
	public void setScorers(Ta ta) throws IOException {

		//not normalized
//		scalarScorers.add(new com.outofprintmagazine.nlp.scorers.scalar.Tokens(ta));
//		scalarScorers.add(new com.outofprintmagazine.nlp.scorers.scalar.Sentences(ta));	
//		scalarScorers.add(new com.outofprintmagazine.nlp.scorers.scalar.Paragraphs(ta));
//		scalarScorers.add(new com.outofprintmagazine.nlp.scorers.scalar.Readability(ta));
		
		//normalized per sentence
//		scalarScorers.add(new com.outofprintmagazine.nlp.scorers.VerblessSentences(ta));
//		scalarScorers.add(new com.outofprintmagazine.nlp.scorers.WordlessWords(ta));
//		scalarScorers.add(new com.outofprintmagazine.nlp.scorers.UncommonWords(ta));
//		scalarScorers.add(new com.outofprintmagazine.nlp.scorers.UncommonSenses(ta));
	
		//avg per sentence
//		scalarScorers.add(new com.outofprintmagazine.nlp.scorers.Sentiments(ta));
		
		//normalized per sentence
//		scalarScorers.add(new com.outofprintmagazine.nlp.scorers.Comparisons(ta));
//		scalarScorers.add(new com.outofprintmagazine.nlp.scorers.Adjectives(ta));
//		scalarScorers.add(new com.outofprintmagazine.nlp.scorers.Adverbs(ta));
//		scalarScorers.add(new com.outofprintmagazine.nlp.scorers.Prepositions(ta));
//		scalarScorers.add(new com.outofprintmagazine.nlp.scorers.Possessives(ta));

		
		//basically bounded categories, could pivot with scalar scorers
		//normalized by category per sentence
		//Female,Male
		//categoricalRankedScorers.add(new com.outofprintmagazine.nlp.scorers.categorical.Pronouns(ta));
		//I, You, He, She, We, They
		//categoricalRankedScorers.add(new com.outofprintmagazine.nlp.scorers.categorical.Genders(ta));
		//Past, Present, Future
		//categoricalRankedScorers.add(new com.outofprintmagazine.nlp.scorers.categorical.Verbs(ta));
		//Comma,Quote,Question,Colon,Exclamation,Semicolon,Hyphen	
		//categoricalRankedScorers.add(new com.outofprintmagazine.nlp.scorers.categorical.Punctuation(ta));

		//normalized by category per sentence
		//Negative,Neutral,Positive,Very negative,Very positive
		//categoricalRankedScorers.add(new com.outofprintmagazine.nlp.scorers.Sentiments(ta));
		
		
		//Bounded but unknown
		//categoricalRankedScorers.add(new com.outofprintmagazine.nlp.scorers.VerbGroups(ta));
		//categoricalRankedScorers.add(new com.outofprintmagazine.nlp.scorers.Topics(ta));
		//categoricalRankedScorers.add(new com.outofprintmagazine.nlp.scorers.Locations(ta));
//		categoricalRankedScorers.add(new com.outofprintmagazine.nlp.scorers.People(ta));

		//categoricalRankedScorers.add(new com.outofprintmagazine.nlp.scorers.categorical.TopicCategories(ta));
		
		//Unbounded and unknown - TF/IDF
//		categoricalScorers.add(new com.outofprintmagazine.nlp.scorers.ActionGroups(ta));
//		categoricalScorers.add(new com.outofprintmagazine.nlp.scorers.TopicGroups(ta));
//		categoricalScorers.add(new com.outofprintmagazine.nlp.scorers.Actions(ta));
//		categoricalScorers.add(new com.outofprintmagazine.nlp.scorers.Topics(ta));		
//		categoricalScorers.add(new com.outofprintmagazine.nlp.scorers.Lemmas(ta));
		categoricalScorers.add(new com.outofprintmagazine.nlp.scorers.categorical.NGrams(ta));			


//		//Sentence Metrics
		//depends on People
//		descriptiveScorers.add(new com.outofprintmagazine.nlp.scorers.descriptive.PeopleCoref(ta));
//		QuantitativeScorers.add(new com.outofprintmagazine.nlp.scorers.Actions(ta));
//		QuantitativeScorers.add(new com.outofprintmagazine.nlp.scorers.Topics(ta));
//		QuantitativeScorers.add(new com.outofprintmagazine.nlp.scorers.Adjectives(ta));
//		QuantitativeScorers.add(new com.outofprintmagazine.nlp.scorers.descriptive.Questions(ta));	
//		categoricalScorers.add(new com.outofprintmagazine.nlp.scorers.UncommonWords(ta));
//		categoricalScorers.add(new com.outofprintmagazine.nlp.scorers.UncommonSenses(ta));
//		QuantitativeScorers.add(new com.outofprintmagazine.nlp.scorers.descriptive.VaderSentiments(ta));			
	}
	
	
	public Analyses score(CoreDocument document) throws IOException {
		Analyses analyses = new Analyses();
		analyses.setCorpus("Submissions");
		analyses.setId("1");


		//CoreDocument document = ta.annotate("I hit the ball with my bat. I ran from the spotlight. me good.");
		for (DocumentScalarScorer documentScalarScorer : scalarScorers) {
			analyses.putScalarScore(documentScalarScorer.getClass().getSimpleName(), documentScalarScorer.scoreDocumentScalar(document));
		}
		for (DocumentRankedCategoricalScorer documentScorer : categoricalRankedScorers) {
			analyses.putCategoricalScore(documentScorer.getClass().getSimpleName(), documentScorer.scoreDocumentRanked(document));
		}		
		for (DocumentCategoricalScorer documentScorer : categoricalScorers) {
			analyses.putCategoricalScore(documentScorer.getClass().getSimpleName(), documentScorer.scoreDocument(document));
		}
		for (CoreSentence sentence : document.sentences()) {
			for (SentenceDescriptiveScorer sentenceScorer : descriptiveScorers) {
				List<Double> scores = analyses.getDescriptiveScore(sentenceScorer.getClass().getSimpleName());
				if (scores == null) {
					scores = new ArrayList<Double>();
				}
				scores.addAll(sentenceScorer.scoreSentence(sentence));
				analyses.putDescriptiveScore(sentenceScorer.getClass().getSimpleName(), scores);
			}
		}
		return analyses;
	}
	
	
	public static void main(String[] args) throws IOException, TikaException, ParseException {
		StoryScorer.mainTest(args);
	}
	
	public static void mainTest(String[] args)  throws IOException {
		String text = "The quick black fox jumped over the lazy black dog.";
	    //text = "I looked at the beige walls. John Smith grew up in Hawaii. Mr. John Smith is our president. He is wearing a green tie. Obama's shrinking hairline receeded each year of his presidency. When John Smith left office, his hair was completely gray. John Smith ran for office. The walls were painted yellow. Obama beat John Smith until he was black and blue. The conflict between Obama and John Smith quickly erupted into violence.";
		//text = "Naomi Plumb looked at the ribbed kettle in her hands and felt cross.";
		//text = "She walked over to the window and reflected on her urban surroundings. She had always loved creepy Exeter with its foolish, fluffy fields. It was a place that encouraged her tendency to feel cross with albraday - she would have to tweet about it";
		text = "She walked over to the window. She had always loved Exeter with its gentle river and green fields. It was a place that made her feel peaceful.";
		StoryScorer me = new StoryScorer();
		SimpleDateFormat fmt = new SimpleDateFormat("HH:mm:ss.S");
		Story story = new Story();
		story.setBody(text);
		Ta ta = new Ta();
		System.out.println(fmt.format(new java.util.Date(System.currentTimeMillis())) + " finished ta init");
		me.setScorers(ta);
		System.out.println(fmt.format(new java.util.Date(System.currentTimeMillis())) + " started annotation");
		CoreDocument document = ta.annotate(story.getBody());
		System.out.println(fmt.format(new java.util.Date(System.currentTimeMillis())) + " finished annotation");
		System.out.println(me.score(document));
		System.out.println(fmt.format(new java.util.Date(System.currentTimeMillis())) + " finished analysis");
	}
	
	
	public static void mainLocal(String[] args)  throws IOException, TikaException {
		
		StoryScorer me = new StoryScorer();
		SimpleDateFormat fmt = new SimpleDateFormat("HH:mm:ss.S");

		Ta ta = new Ta();
		System.out.println(fmt.format(new java.util.Date(System.currentTimeMillis())) + " finished ta init");
		me.setScorers(ta);
		
		Story story = new Story();
	    Tika tika = new Tika();
	    String dropboxFolder = "C:\\Users\\rsada\\Dropbox\\Out of Print Reading\\Potential Stories Issue 32\\Stories\\";
	    File file = new File(dropboxFolder + "Giorgia Stavropoulou.Once Upon a Time in Hindustan.mod1.rs.docx");
	    //File file = new File(dropboxFolder + "Nidhi.Arora It is in the eyes.docx");
	    //File file = new File(dropboxFolder + "Alina Gufran.After Hours.mod.docx");
	    //File file = new File(dropboxFolder + "Prashant Bhagat.Accomplishment.mod.docx");

	    //dropboxFolder = "C:\\Users\\rsada\\Dropbox\\Out of Print Reading\\Potential Stories Issue 31\\Stories\\";
	    //File file = new File(dropboxFolder + "Salvatore Difalco.Time of the Djinns.docx");
	    
	    //File file = new File("C:\\Users\\rsada\\git\\oop_nlp\\similarity\\resources\\Victorian\\Dickens\\BOOK_1_CHAPTER_10.txt");
	    //File file = new File("C:\\Users\\rsada\\git\\oop_nlp\\similarity\\resources\\Austen\\CHAPTER_10.txt");
	    //File file = new File("C:\\Users\\rsada\\git\\oop_nlp\\similarity\\resources\\blogger/1555267.male.35.Engineering.Aquarius.xml.txt");
	    //File file = new File("C:\\Users\\rsada\\git\\oop_nlp\\similarity\\resources\\wiki/Raymond_Chandler.txt");
	    FileInputStream fis = new FileInputStream(file);
	    story.setBody(tika.parseToString(fis));
	    fis.close();

		System.out.println(fmt.format(new java.util.Date(System.currentTimeMillis())) + " started annotation");
		CoreDocument document = ta.annotate(story.getBody());
		System.out.println(fmt.format(new java.util.Date(System.currentTimeMillis())) + " finished annotation");
		System.out.println(me.score(document));
		System.out.println(fmt.format(new java.util.Date(System.currentTimeMillis())) + " finished analysis");
		System.out.println("---------------------------------------------");
		dropboxFolder = "C:\\Users\\rsada\\Dropbox\\Out of Print Reading\\Potential Stories Issue 31\\Stories\\";
		file = new File(dropboxFolder + "Salvatore Difalco.Time of the Djinns.docx");
		fis = new FileInputStream(file);
	    story.setBody(tika.parseToString(fis));
	    fis.close();
		System.out.println(fmt.format(new java.util.Date(System.currentTimeMillis())) + " started annotation");
		document = ta.annotate(story.getBody());
		System.out.println(fmt.format(new java.util.Date(System.currentTimeMillis())) + " finished annotation");
		System.out.println(me.score(document));
		System.out.println(fmt.format(new java.util.Date(System.currentTimeMillis())) + " finished analysis");
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
					//System.out.println(me.score(story));
					System.out.println("-----------------------------------------");
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
