package com.outofprintmagazine.nlp;

import java.util.Properties;

import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.paragraphs.ParagraphAnnotator;

public class Ta {

	private StanfordCoreNLP pipeline;
	
	public Ta() {
		super();
		// set up pipeline properties
		Properties props = new Properties();
		// set the list of annotators to run
	    // adding our own annotator property
	    props.put("customAnnotatorClass.paragraphs",
	            "com.outofprintmagazine.nlp.annotators.ParagraphAnnotator");
	    props.put("customAnnotatorClass.gender",
	            "com.outofprintmagazine.nlp.annotators.GenderAnnotator");	    
	    props.put("paragraphs.paragraphBreak", "two");

	    // configure pipeline
	//    edu.stanford.nlp.paragraphs.ParagraphAnnotator x = new edu.stanford.nlp.paragraphs.ParagraphAnnotator(props, verbose)
		props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse,sentiment,paragraphs,gender");
		props.setProperty("ner.applyFineGrained", "false");

		
		// build pipeline
		pipeline = new StanfordCoreNLP(props);		
	}
	
	public CoreDocument annotate(String text) {
		// create a document object
		CoreDocument document = new CoreDocument(text);
		// annnotate the document
		pipeline.annotate(document);
		return document;
	}
}
