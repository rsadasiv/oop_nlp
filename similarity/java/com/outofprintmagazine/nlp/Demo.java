package com.outofprintmagazine.nlp;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;
import edu.stanford.nlp.io.IOUtils;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;

public class Demo {

	public static void main(String[] args) throws IOException {
		String path = "c:/users/rsada/eclipse-workspace/NLP/src/main/resources/wn3.1.dict/dict";

		URL url = new URL ("file", null, path) ;
		
		// construct the dictionary object and open it
		IDictionary dict = new Dictionary (url) ;
		dict.open () ;
		
		// look up first sense of the word "dog "
		IIndexWord idxWord = dict.getIndexWord ("talk", POS.VERB );
		IWordID wordID = idxWord.getWordIDs().get(0);
		IWord word = dict.getWord(wordID) ;
		System.out.println ("Id = " + wordID ) ;
		System.out.println (" Lemma = " + word.getLemma()) ;
		System.out.println (" Verb group = " + word.getSynset().getLexicalFile().getName()) ;
		System.out.println (" Verb group = " + word.getSynset().getLexicalFile().getNumber()) ;		
		System.out.println (" Verb group = " + word.getSynset().getLexicalFile().getDescription()) ;
		
//		String text = "Who is running? When is the election? Trump is the worst. Obama is the best candidate.";
//		Ta ta = new Ta();
//		CoreDocument document = ta.annotate(text);
//		for (CoreSentence sentence : document.sentences()) {
//			System.out.println(sentence.text());
//			System.out.println("Sentiment: " + sentence.sentiment());
//			System.out.println("Score: " + RNNCoreAnnotations.getPredictedClass(sentence.sentimentTree()));


//			List<CoreLabel> tokens = sentence.tokens();
//			for (int i = 0; i < tokens.size(); i++) {
//				CoreLabel token = tokens.get(i);
//				System.out.println("token: " + token.originalText());
//				System.out.println("lemma: " + token.lemma());
//				System.out.println("pos: " + token.tag());
//				System.out.println("ner: " + token.ner());
//				if (i == tokens.size() - 1) {
//					System.out.println("terminator: " + token.tag());
//				}
//			}
//		}
	}

}
