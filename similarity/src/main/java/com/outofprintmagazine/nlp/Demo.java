package com.outofprintmagazine.nlp;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;
import edu.stanford.nlp.coref.CorefCoreAnnotations;
import edu.stanford.nlp.coref.data.Mention;
import edu.stanford.nlp.io.IOUtils;
import edu.stanford.nlp.ling.CoreAnnotations.GenderAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.ParagraphAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.ParagraphIndexAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreEntityMention;
import edu.stanford.nlp.pipeline.CoreNLPProtos.RelationTriple;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations.SentimentAnnotatedTree;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.TypesafeMap;

public class Demo {

	public static void main(String[] args) throws IOException {
//		String path = "c:/users/rsada/eclipse-workspace/NLP/src/main/resources/wn3.1.dict/dict";
//
//		URL url = new URL ("file", null, path) ;
//		
//		// construct the dictionary object and open it
//		IDictionary dict = new Dictionary (url) ;
//		dict.open () ;
//		
//		// look up first sense of the word "dog "
//		IIndexWord idxWord = dict.getIndexWord ("talk", POS.VERB );
//		IWordID wordID = idxWord.getWordIDs().get(0);
//		IWord word = dict.getWord(wordID) ;
//		System.out.println ("Id = " + wordID ) ;
//		System.out.println (" Lemma = " + word.getLemma()) ;
//		System.out.println (" Verb group = " + word.getSynset().getLexicalFile().getName()) ;
//		System.out.println (" Verb group = " + word.getSynset().getLexicalFile().getNumber()) ;		
//		System.out.println (" Verb group = " + word.getSynset().getLexicalFile().getDescription()) ;
//		//crud
		
		String text = "Who is running? When is the election?" + '\n' + '\n' + "Donald Trump is the worst. Barack Obama is the best candidate.";
		Ta ta = new Ta();
		CoreDocument document = ta.annotate(text);

		for (CoreSentence sentence : document.sentences()) {
			System.out.println(sentence.text());
			for (Object x : sentence.coreMap().keySet()) {
				System.out.println(x);
			}
			System.out.println("Sentiment: " + sentence.sentiment());
			PrintWriter pout = new PrintWriter(System.out);
			sentence.coreMap().get(SentimentAnnotatedTree.class).indentedListPrint(pout, true);
			pout.flush();
//			System.out.println("Score: " + RNNCoreAnnotations.getPredictedClass(sentence.sentimentTree()));
//			System.out.println("Paragraph: " + sentence.coreMap().get(ParagraphIndexAnnotation.class));


			List<CoreEntityMention> entityMentions = sentence.entityMentions();
			for (CoreEntityMention mention : entityMentions) {
				System.out.println("Entity: ");
				System.out.println(mention.coreMap().toShorterString());
				for (Object x : mention.coreMap().keySet()) {
					System.out.println(x);
				}
				//System.out.println(mention.text());
				System.out.println(mention.canonicalEntityMention().get());
				System.out.println(mention.entityType());
				if (mention.coreMap().containsKey(GenderAnnotation.class)) {
					System.out.println(mention.coreMap().get(GenderAnnotation.class));
				}
			}
			
			System.out.println("----------------------------");

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
		}

	}

}
