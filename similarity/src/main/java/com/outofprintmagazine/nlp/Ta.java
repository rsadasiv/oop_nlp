package com.outofprintmagazine.nlp;

import java.util.Properties;

import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

public class Ta {

	private StanfordCoreNLP pipeline;
	
	public Ta() {
		super();
		// set up pipeline properties
		Properties props = new Properties();
		// set the list of annotators to run
		props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse,sentiment");
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
