package com.outofprintmagazine.nlp.scratch;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import edu.stanford.nlp.coref.CorefCoreAnnotations;
import edu.stanford.nlp.coref.data.CorefChain;
import edu.stanford.nlp.coref.data.CorefChain.CorefMention;
import edu.stanford.nlp.coref.data.Mention;
import edu.stanford.nlp.io.IOUtils;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreEntityMention;
import edu.stanford.nlp.pipeline.CoreQuote;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class CorefDemo {

	public static void main(String[] args) throws Exception {
		// set up pipeline properties
		Properties props = new Properties();
		// set the list of annotators to run
		props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse");
		// build pipeline
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		// create a document object
		CoreDocument document = new CoreDocument(
				IOUtils.slurpFile("c:/users/rsada/eclipse-workspace/NLP/src/main/resources/Story.txt"));
		// annnotate the document
		pipeline.annotate(document);
		for (CoreSentence sentence : document.sentences()) {
			System.out.println(sentence.text());
			// list of the part-of-speech tags for the second sentence
			List<CoreLabel> tokens = sentence.tokens();
			for (int i = 0; i < tokens.size(); i++) {
				CoreLabel token = tokens.get(i);
				System.out.println("token: " + token.originalText());
				System.out.println("lemma: " + token.lemma());
				System.out.println("pos: " + token.tag());
				System.out.println("ner: " + token.ner());
				if (i == tokens.size() - 1) {
					System.out.println("terminator: " + token.tag());
				}
			}

			List<CoreEntityMention> entityMentions = sentence.entityMentions();
			for (CoreEntityMention mention : entityMentions) {
				System.out.println(mention.entityType());
				System.out.println(mention.canonicalEntityMention().get());
			}
			System.out.println(entityMentions);
			System.out.println();
		}

	}
}
