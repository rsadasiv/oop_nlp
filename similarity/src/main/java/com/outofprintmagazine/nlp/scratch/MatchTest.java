package com.outofprintmagazine.nlp.scratch;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.*;

import org.apache.http.HttpHost;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import edu.mit.jverbnet.data.FrameType;
import edu.mit.jverbnet.data.IFrame;
import edu.mit.jverbnet.data.IMember;
import edu.mit.jverbnet.data.IVerbClass;
import edu.mit.jverbnet.data.IWordnetKey;
import edu.mit.jverbnet.index.IVerbIndex;
import edu.mit.jverbnet.index.VerbIndex;
import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.IPointer;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;
import edu.stanford.nlp.coref.CorefCoreAnnotations;
import edu.stanford.nlp.coref.data.CorefChain;
import edu.stanford.nlp.coref.data.Mention;
import edu.stanford.nlp.ie.util.RelationTriple;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.naturalli.NaturalLogicAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreEntityMention;
import edu.stanford.nlp.pipeline.CoreQuote;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.QuoteAttributionAnnotator;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.trees.GrammaticalRelation;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TypedDependency;
import edu.stanford.nlp.util.CoreMap;
import io.phrasefinder.Corpus;
import io.phrasefinder.Phrase;
import io.phrasefinder.Phrase.Token;
import io.phrasefinder.PhraseFinder;
import io.phrasefinder.SearchOptions;
import io.phrasefinder.SearchResult;
import io.phrasefinder.SearchResult.Status;

public class MatchTest {

	public static String toTitleCase(String input) {
	    StringBuilder titleCase = new StringBuilder();
	    boolean nextTitleCase = true;

	    for (char c : input.toCharArray()) {
	        if (Character.isSpaceChar(c)) {
	            nextTitleCase = true;
	        } else if (nextTitleCase) {
	            c = Character.toTitleCase(c);
	            titleCase.append(c);
	            nextTitleCase = false;
	        }
	        else {
	        	titleCase.append(Character.toLowerCase(c));
	        }
	    }

	    return titleCase.toString();
	}
	
	public static void main(String[] args) throws IOException, URISyntaxException {

		
//			List<String> stopWords = Files.readAllLines(
//				new File("C:\\Users\\rsada\\git\\oop_nlp\\similarity\\resources\\COCA\\en_full.txt").toPath(),
//				Charset.defaultCharset());
//			Pattern pattern = Pattern.compile("^([A-Z]*\\h)");
//			File submissionFile = new File("C:\\Users\\rsada\\git\\oop_nlp\\similarity\\resources\\COCA\\en_uncommon.txt");
////		      // creates the file
//			submissionFile.createNewFile();
//			PrintWriter fout = new PrintWriter(submissionFile);
//			FileReader fileReader = new FileReader("C:\\Users\\rsada\\git\\oop_nlp\\similarity\\resources\\COCA\\en_full.txt");
//			BufferedReader bufferedReader = new BufferedReader(fileReader);
//			String line;
//			while ((line = bufferedReader.readLine()) != null) {
//				String[] name_value = line.split(" ");
//				int value = Integer.parseInt(name_value[1]);
//				if (110 > value && value > 50) {
//					fout.println(line.toLowerCase());
//				}
////				Matcher matcher = pattern.matcher(line);
////				if (matcher.find()) {
////					fout.println(MatchTest.toTitleCase(matcher.group(1)));
////				}
//			}
//			fileReader.close();
//			fout.flush();
//			fout.close();
//		api.php?action=query&titles=picture&prop=categories
//		#mw-normal-catlinks > ul > li:nth-child(1) > a
		//String topic = "Photography";
//		String topic = "picture";
//		ObjectMapper objectMapper = new ObjectMapper();
//		JsonNode rootNode = objectMapper.readValue(new URL("https://en.wikipedia.org/w/api.php?action=query&redirects&format=json&utf-8=1&formatversion=2&titles="+topic), JsonNode.class);
//		System.out.println(rootNode);
//		JsonNode pagesNode = rootNode.get("query").get("pages");
//		if (pagesNode.isArray()) {
//			for (final JsonNode pageNode : pagesNode) {
//				if (pageNode.get("missing") == null || !pageNode.get("missing").asBoolean()) {
//					System.out.println(pageNode.get("title").asText());
//					JsonNode categoryRootNode = objectMapper.readValue(new URL("https://en.wikipedia.org/w/api.php?format=json&action=query&prop=categories&utf8=1&titles="+pageNode.get("title").asText()), JsonNode.class);
//					System.out.println(categoryRootNode);
//					JsonNode pageCategoriesNode = categoryRootNode.get("query").get("pages");
//					Iterator<Entry<String, JsonNode>> fieldsIter = pageCategoriesNode.fields();
//					while (fieldsIter.hasNext()) {
//						JsonNode categoriesNode = fieldsIter.next().getValue().get("categories");
//						if (categoriesNode.isArray()) {
//							for (final JsonNode categoryNode : categoriesNode) {
//								String categoryName = categoryNode.get("title").asText().substring("Category:".length());
//								if (!categoryName.startsWith("All") && !categoryName.startsWith("Articles")) {
//									System.out.println(categoryName);
//								}
//							}
//						}
//					}
//				}
//		    }
//		}
		
		
//for each topic
//	https://en.wikipedia.org/wiki/%topic%
//	until you dont get a redirect
//	https://en.wikipedia.org/wiki/%title%
//			
//https://en.wikipedia.org/w/api.php?format=json&action=query&prop=categories&utf8=1&titles=%title1|title2|title3%
//https://en.wikipedia.org/w/api.php?format=json&action=query&prop=categories&utf8=1&titles=%title1|title2|title3%&clcontinue=25080|Photographs
		
//		PrintWriter fout = new PrintWriter(new BufferedWriter(new FileWriter("C:\\Users\\rsada\\git\\oop_nlp\\similarity\\resources\\FemaleIndianNames.txt", true)));	
		//Document homepage = Jsoup.connect("https://en.wikipedia.org/wiki/Edward_Aveling").get();
		//Elements mwpages = homepage.select("#mw-content-text > div > p");
//		Document homepage = Jsoup.connect("https://en.wikipedia.org/wiki/List_of_English_writers_(A-C)").get();
//		Elements mwpages = homepage.select("#mw-content-text > div > div > ul > li > a");
//		for (Element body : mwpages) {
//			Writer fout = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("C:\\Users\\rsada\\git\\oop_nlp\\similarity\\resources" + body.attr("href") + ".txt"), "UTF-8"));
//			Document authorPage = Jsoup.connect("https://en.wikipedia.org" + body.attr("href")).get();
//			Elements authorParas = authorPage.select("#mw-content-text > div > p");
//			for (Element para : authorParas) {
//				fout.write(para.wholeText());
//				fout.write('\n');
//			}
//			fout.flush();
//			fout.close();
//		}
//		fout.flush();
//		fout.close();
		
		//Reader fin = new BufferedReader(new InputStreamReader(new FileInputStream("C:\\Users\\rsada\\git\\oop_nlp\\similarity\\resources\\LittleDorrit.txt"), "UTF-8"));
//		Writer fout = null;
//		BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\rsada\\git\\oop_nlp\\similarity\\resources\\Victorian\\Stendhal\\TheRedAndTheBlack.txt"));
//	    String line;
//	    int chapterCount = 0;
//	    int bookCount = 0;
//	    while ((line = br.readLine()) != null) {
//		   if (line.startsWith("BOOK")) {
//			   bookCount++;
//			   chapterCount = 0;
//		   }
//	       if (line.startsWith("CHAPTER")) {
//	    	   if (fout != null) {
//	    		   fout.flush();
//	    		   fout.close();
//	    	   };
//	    	   fout = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("C:\\Users\\rsada\\git\\oop_nlp\\similarity\\resources\\Victorian\\Stendhal\\CHAPTER_" + ++chapterCount +".txt"), "UTF-8"));   
//	    	   fout.write(line);
//	    	   fout.write('\n');
//	       }
//	       else {
//	    	   if (fout != null) {
//		    	   fout.write(line);
//		    	   fout.write('\n');
//	    	   }
//	       }
//	    }
//		br.close();
// 	   	fout.flush();
// 	   	fout.close();
		
	
//		File dir = new File("C:\\Users\\rsada\\git\\oop_nlp\\similarity\\resources\\blogs\\");
//		File[] files = dir.listFiles();
//		for (int i=0;i<files.length;i++) {
//			if ((i % 19) == 0) {
//				File input = files[i];
//
//				//File input = new File("C:\\Users\\rsada\\git\\oop_nlp\\similarity\\resources\\blogs\\1016738.male.26.Publishing.Libra.xml");
//				Writer fout = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("C:\\Users\\rsada\\git\\oop_nlp\\similarity\\resources\\blogger\\" + input.getName() + ".txt"), "UTF-8"));
//				Document doc = Jsoup.parse(input, "UTF-8", "");
//				Elements elements = doc.getElementsByTag("post");
//				for (Element element : elements) {
//					String txt = element.wholeText().trim();
//					fout.write(txt.replace("urlLink", ""));
//					fout.write('\n');
//					fout.write('\n');
//				}
//				fout.flush();
//				fout.close();
//			}
//		}
		
/*
 * function SIMPLIFIED LESK(word,sentence) returns best sense of word
best-sense <- most frequent sense for word
max-overlap <- 0
context <- set of words in sentence
for each sense in senses of word do
signature <- set of words in the gloss and examples of sense
overlap <- COMPUTEOVERLAP (signature,context)
if overlap > max-overlap then
max-overlap <- overlap
best-sense <- sense
end return (best-sense)		
 */
		
//	    public double similarity(Synset synset1, Synset synset2) { 
//	        String[] gloss1 = synset1.getGloss().split("\\s+"); 
//	        String[] gloss2 = synset2.getGloss().split("\\s+"); 
//	        return StringUtils.tokenOverlap(gloss1, gloss2); 
//	    }
//	    
//	    public static int tokenOverlap(String[] tokens1, String[] tokens2) { 
//	        Set<String> tokenSet2 = new HashSet<String>(Arrays.asList(tokens2)); 
//	 
//	        int score = 0; 
//	        for (String word : tokens1) 
//	            if (tokenSet2.contains(word)) 
//	                score++; 
//	        return score; 
//	    }
//	        
//		
//		
//		String lemma = "hit";
//		POS wnPos = POS.NOUN;
//		
//		IDictionary wordnet = new Dictionary(new URL("file", null, "C:\\Users\\rsada\\git\\oop_nlp\\similarity\\resources\\wn3.1.dict\\dict"));
//		wordnet.open();
//
//		IIndexWord idxWord = wordnet.getIndexWord (lemma, wnPos);
//		if (idxWord != null && idxWord.getWordIDs().size() > 0) {
//			for (IWordID wordID : idxWord.getWordIDs()) {
//				IWord word = wordnet.getWord(wordID);
//				System.out.println("Lemma: " + word.getLemma());
//				System.out.println("SenseKey: " + word.getSenseKey());
//				System.out.println("SenseNumber: " + wordnet.getSenseEntry(word.getSenseKey()).getSenseNumber());
//				System.out.println("SenseCount: " + wordnet.getSenseEntry(word.getSenseKey()).getTagCount());
//				String gloss = word.getSynset().getGloss();
//				String[] glossSplit = gloss.split(";");
//				System.out.println("Gloss: " + glossSplit[0]);
//				if (glossSplit.length > 0) {
//					StringBuffer examples = new StringBuffer();
//					for (int i=1;i<glossSplit.length;i++) {
//						examples.append(glossSplit[i]);
//						examples.append(" ");
//					}
//					System.out.println("Examples: " + examples.toString());
//				}
//				ISynset synset = word.getSynset();
//		        for (IWord w : synset.getWords()) {
//		            System.out.println("synset syn: " + w.getLemma());
//		        }
//				Map<IPointer, List<IWordID>> synsetMap = word.getRelatedMap();
//				for (IPointer ptr : synsetMap.keySet()) {
//					System.out.println("ptr: " + ptr.getName());
//					for (IWordID synId : synsetMap.get(ptr)) {
//						IWord syn = wordnet.getWord(synId);
//						System.out.println(syn.getLemma());
//					}
//				}
//				System.out.println("---------------------------------------");
//			}
//
//		}
//
//		String x = "This is a test; of nothing";
//		String[] split = x.split(";");
//		System.out.println(split[0]);
		
//		System.out.println("You: " + (double)1/22484400);
//		System.out.println("If: " + 1/2041724);
//		System.out.println("Dad: " + 1/292023);
//		System.out.println("Tree: " + 1/37829);

		 // make a url pointing to the Verbnet data
//		String pathToVerbnet = "C:\\Users\\rsada\\git\\oop_nlp\\similarity\\resources\\new_vn\\";
//		URL url = new URL("file", null, pathToVerbnet);
//		
//		// construct the index and open it
//		IVerbIndex index = new VerbIndex(url);
//		index.open();
//		// look up a verb class and print out some info
//
//		IVerbClass verb = index.getRootVerb("hit-18.1");
//		IMember member = verb.getMembers().get(0);
//		Set keys = member.getWordnetTypes().keySet();
//		IFrame frame = verb.getFrames().get(0);
//		FrameType type = frame.getPrimaryType();
//		String example = frame.getExamples().get(0);
//		System.out.println("id: " + verb.getID());
//		System.out.println("name: "+ verb.toString());
//		System.out.println("first wordnet keys: " + keys);
//		for (IWordnetKey senseKey : member.getWordnetTypes().keySet()) {
//			System.out.println("SenseKey: " + senseKey.toString());
//			
//		}
//		System.out.println("first frame type: " + type.getID());
//		System.out.println("first example: " + example);		

//not getting a space between period and next word		
		
//		String sectionName = "mental-illness";
//		for (int i=5;i<10;i++) {
////			Document doc = Jsoup.connect("https://www.dnaindia.com/" + sectionName + "?page="+i).get();
//			Document doc = Jsoup.connect("https://www.dnaindia.com/topic/" + sectionName + "?page="+i).get();
////			Elements links = doc.select("div.mrebolynwsrgtbx > div.bolyveralign > h3 > a");
//			Elements links = doc.select("div.mrebolynwsrgtbx > div > span > div.bolyveralign > h3 > a");
//			for (Element element : links) {
//				System.out.println(new Integer(i).toString() + " " + element.attr("href"));
//				PrintWriter fout = new PrintWriter(new BufferedWriter(new FileWriter("C:\\Users\\rsada\\git\\oop_nlp\\similarity\\resources\\dnaindia\\" + sectionName + "\\" + element.attr("href").substring(element.attr("href").lastIndexOf('/'), element.attr("href").length()) + ".txt", true)));	
//				Document article = Jsoup.connect("https://www.dnaindia.com"+element.attr("href")).get();
//				Elements articleboxes = article.select("div.articllftpbx");
//				for (Element articlebox : articleboxes) {
//					Elements paras = articlebox.select("p");
//					for (Element para : paras) {
//						fout.write(para.wholeText());
//						fout.write('\n');
//					}
//				}
//				fout.flush();
//				fout.close();
//			}
//		}
//		
//
//		String text = "John Smith walked to his car. He felt the breeze on his face. He thought about Susan. She was a mystery to him. In her apartment, Julie Andrews was thinking about John. Luciano Pavarotti sang the national anthem to a thousand people in Dodger stadium. Pavarotti had an angelic voice.";
//		Properties props = new Properties();
//		props.setProperty("annotators","tokenize,ssplit,pos,lemma,ner,parse,depparse,coref,entitylink");
//		props.setProperty("ner.applyFineGrained", "false");
//		props.setProperty("coref.algorithm", "statistical");
//		props.setProperty("coref.maxMentionDistance", "5");
//		props.setProperty("coref.maxMentionDistanceWithStringMatch", "300");
//		props.setProperty("coref.statisical.pairwiseScoreThresholds", ".5");
//		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
//		CoreDocument document = new CoreDocument(text);
//		pipeline.annotate(document);
////		System.out.println("coref chains");
////	    for (CorefChain cc : document.annotation().get(CorefCoreAnnotations.CorefChainAnnotation.class).values()) {
////	      System.out.println("\t" + cc);
////	    }
////	    List<CoreMap> sentenceAnnotationList = document.annotation().get(CoreAnnotations.SentencesAnnotation.class);
////	    Iterator<CoreMap> sentenceAnnotationIter = sentenceAnnotationList.iterator();
//	    List<CoreSentence> sentenceList = document.sentences();
//	    Iterator<CoreSentence> sentenceIter = sentenceList.iterator();
//	    while (sentenceIter.hasNext()) {
////	    	CoreMap sentenceMap = sentenceAnnotationIter.next();
//	    	CoreSentence sentence = sentenceIter.next();
//	    	System.out.println(sentence.toString());
////	        System.out.println("coref mentions");
////	        for (Mention m : sentenceMap.get(CorefCoreAnnotations.CorefMentionsAnnotation.class)) {
////	          System.out.println("\t" + m + " " + m.nerName());
////	          System.out.println("\t" + m);
////	        }
//	        System.out.println("ner mentions");
//			for (CoreEntityMention mention : sentence.entityMentions()) {
//				//if (mention.entityType().equals("PERSON")) {
//					if (mention.canonicalEntityMention().isPresent()) {
//						System.out.println("\t" + mention + " " + mention.canonicalEntityMention().get().toString());
//					}
//					else {
//						System.out.println("\t" + mention + " " + mention.entity());
//					}
//				//}
//			}
//		    System.out.println();
//		}
//	}
	
	/*
	 * w/o coref
	 * ner mentions
	John Smith John Smith
	his his

ner mentions
	He He
	his his

ner mentions
	Susan Susan
	He He

ner mentions
	She She
	him him

ner mentions
	Julie Andrews Julie Andrews
	John John
	her her

ner mentions
	Luciano Pavarotti Luciano Pavarotti

ner mentions
	Pavarotti Pavarotti
	 */
	
	/*
	 * w coref
	 * 
	 * ner mentions
	John Smith John Smith
	his John Smith

ner mentions
	He John Smith
	his John Smith

ner mentions
	Susan Julie Andrews
	He John Smith

ner mentions
	She Julie Andrews
	him John Smith

ner mentions
	Julie Andrews Julie Andrews
	John John Smith
	her Julie Andrews

ner mentions
	Luciano Pavarotti Luciano Pavarotti

ner mentions
	Pavarotti Luciano Pavarotti

	 */
		
		 Properties props = new Properties();
		    //props.setProperty("annotators", "tokenize,ssplit,pos,lemma,parse,depparse,ner,coref,quote");
		 	props.setProperty("annotators", "tokenize,ssplit,pos,lemma");
			props.setProperty("ner.applyFineGrained", "false");
			//props.setProperty("dcoref.maxdist", "2");
			props.setProperty("coref.algorithm", "statistical");
			props.setProperty("coref.maxMentionDistance", "15");
			props.setProperty("coref.maxMentionDistanceWithStringMatch", "300");
			props.setProperty("coref.statisical.pairwiseScoreThresholds", ".5");
			props.setProperty("openie.resolve_coref", "true");
			props.setProperty("parse.maxlen", "70");
			props.setProperty("pos.maxlen", "70");
			props.setProperty("ner.maxlen", "70");		
			props.setProperty("ner.useSUTime", "false");
			props.setProperty("ner.applyNumericClassifiers", "false");
			props.setProperty("quote.maxLength", "70");
		    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

		    // Annotate an example document.
//		    CoreDocument doc = new CoreDocument("I looked at the beige walls. John Smith grew up in Hawaii. Mr. John Smith is our president. He is wearing a green tie. Obama's shrinking hairline receeded each year of his presidency. When John Smith left office, his hair was completely gray. John Smith ran for office. The walls were painted yellow. Obama beat John Smith until he was black and blue. The conflict between Obama and John Smith quickly erupted into violence.");
//		    pipeline.annotate(doc);

//		    // Loop over sentences in the document
//		    //for (CoreMap sentence : doc.get(CoreAnnotations.SentencesAnnotation.class)) {
//			for (CoreSentence sentence : doc.sentences()) {
//		      // Get the OpenIE triples for the sentence
//		      Collection<RelationTriple> triples =
//			          sentence.coreMap().get(NaturalLogicAnnotations.RelationTriplesAnnotation.class);
//		      // Print the triples
//		      for (RelationTriple triple : triples) {
//		        System.out.println(triple.confidence + "\n" +
//		            triple.subjectGloss() + "\n" +
//		            triple.relationGloss() + "\n" +
//		            triple.objectGloss());
//		        System.out.println();
//		      }
//		    }

//			for (CoreSentence sentence : doc.sentences()) {
//				System.out.println(sentence.toString());
//				SemanticGraph dependencyParse = sentence.dependencyParse();
//			    System.out.println("Example: dependency parse");
//			    //System.out.println(dependencyParse.toString(SemanticGraph.OutputFormat.LIST));
//			    
//				Collection<TypedDependency> deps = dependencyParse.typedDependencies();
//
//				for (TypedDependency dependency : deps) {
//					GrammaticalRelation rn = dependency.reln();
//					if (rn.getShortName().equals("amod")) {
//						System.out.println("amod");
//						System.out.println("dep: " + dependency.dep().originalText());
//						System.out.println("gov: " + dependency.gov().originalText());
//						System.out.println("gov sent idx: " + dependency.gov().sentIndex());
//						System.out.println("gov idx: " + dependency.gov().index());
//					}
//					if (rn.getShortName().equals("nsubj")) {
//						System.out.println("nsubj");
//						System.out.println("dep: " + dependency.dep().originalText());
//						System.out.println("gov: " + dependency.gov().originalText());
//						System.out.println("gov sent idx: " + dependency.gov().sentIndex());
//						System.out.println("gov idx: " + dependency.gov().index());
//					}
//				}
//			    System.out.println();
//
//			}
		    
//		    CoreDocument doc = new CoreDocument("John Smith was sitting in his living room. \"What a lovely evening\" he said to himself. For a moment, all was quiet. Then, from the kitchen, Jane answered \"Why do you say that?\"");
//		    pipeline.annotate(doc);
//			//for (CoreSentence sentence : doc.sentences()) {
//			//	System.out.println(sentence.toString());
//		    List<CoreQuote> quotes = doc.quotes();
//			for (CoreQuote quote : quotes) {
//				//System.out.println("char offset: " + quote.quoteCharOffsets().first() + " to " + quote.quoteCharOffsets().second());
//		        if (quote.canonicalSpeaker().isPresent()) {
//		        	System.out.println(quote.canonicalSpeaker().get());
//		        	System.out.println(quote);
//					for (CoreSentence sentence : quote.sentences()) {
//						List<CoreSentence> allSentences = sentence.document().sentences();
//						for (int i=0;i<allSentences.size();i++) {
//							if (allSentences.get(i).equals(sentence)) {
//								System.out.println("sentence id: " + i);
//								break;
//							}
//						}
//
//					}
//		        }


		    // Optional: set the maximum number of phrases to return.
		    SearchOptions options = new SearchOptions();
		    options.setMaxResults(100);

			HashMap<String, String> dict = new HashMap<String, String>();
			String fileName = "C:\\Users\\rsada\\git\\oop_nlp\\similarity\\resources\\COCA\\en_100.txt";
			BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				String[] name_value = line.split(" ");
				dict.put(name_value[0], name_value[1]);
			}
			bufferedReader.close();

		  
			String text = "The quick black fox jumped over the lazy black dog.";
		    //text = "I looked at the beige walls. John Smith grew up in Hawaii. Mr. John Smith is our president. He is wearing a green tie. Obama's shrinking hairline receeded each year of his presidency. When John Smith left office, his hair was completely gray. John Smith ran for office. The walls were painted yellow. Obama beat John Smith until he was black and blue. The conflict between Obama and John Smith quickly erupted into violence.";
			//text = "Naomi Plumb looked at the ribbed kettle in her hands and felt cross.";
			text = "She walked over to the window and reflected on her urban surroundings. She had always loved creepy Exeter with its foolish, fluffy fields. It was a place that encouraged her tendency to feel cross.";
			//text = "She walked over to the window. She had always loved Exeter with its gentle river and green fields. It was a place that made her feel peaceful.";
		    CoreDocument document = new CoreDocument(text);
		    pipeline.annotate(document);
//			for (CoreSentence sentence : document.sentences()) {
//				System.out.println(sentence);
//				List<CoreLabel> tokens = sentence.tokens();
//				//exclude proper nouns? NNP NNPS
//				//break on conjunctions? CC
//				//exclude non-wordy CD FW LS UH SYM
//				//break on clauses? IN
//				for (int token_idx = 0;token_idx < tokens.size(); token_idx++) {
//					boolean not_nnp = true;
//					for (int ngram_idx=1;ngram_idx<5&&ngram_idx+token_idx<tokens.size()&&not_nnp;ngram_idx++) {
//					    StringBuffer query = new StringBuffer();
//					    for (int i=token_idx;i<ngram_idx+token_idx;i++) {
//					    	//if (tokens.get(i).tag().equals("NNP") || tokens.get(i).tag().equals("NNPS")) {
//					    		//|| dict.get(tokens.get(i).lemma()) != null) {
//					    	//	not_nnp = false;
//					    	//	break;
//					    	//}
//					    	//else {
//					    		query.append(tokens.get(i).originalText());
//					    		query.append(" ");
//					    	//}
//					    }
//					    try {
//					    	if (query.toString().trim().length()>0) {
//						    	System.out.println("Query: " + query.toString().trim()+" "+tokens.get(ngram_idx+token_idx).originalText());
//						    	SearchResult wildcardResult = PhraseFinder.search(Corpus.AMERICAN_ENGLISH, query.toString().trim() + " ?", options);
//						    	if (wildcardResult.getStatus() == Status.OK) {
//						    		double phraseScore = 0;
//						    		long matchCount = 0;
//						    		long volumeCount = 0;
//						    		long firstYear = 2018;
//						    		long lastYear = 0;
//						    		long totalMatchCount = 0;
//						    		long totalVolumeCount = 0;
//						    		int totalFirstYear = 2018;
//						    		int totalLastYear = 0;
//						    		for (Phrase phrase : wildcardResult.getPhrases()) {
//						    			Token[] phraseTokens = phrase.getTokens();
//						    			if (ngram_idx < phraseTokens.length) {
//							    			totalMatchCount+=phrase.getMatchCount();
//							    			totalVolumeCount+=phrase.getVolumeCount();
//							    			if (totalFirstYear>phrase.getFirstYear()) {
//							    				totalFirstYear = phrase.getFirstYear();
//							    			}
//							    			if (totalLastYear<phrase.getLastYear()) {
//							    				totalLastYear = phrase.getLastYear();
//							    			}
//						    				if (tokens.get(ngram_idx+token_idx).originalText().equalsIgnoreCase(phraseTokens[ngram_idx].getText())) {
//						    					phraseScore+=phrase.getScore();
//								    			matchCount+=phrase.getMatchCount();
//								    			volumeCount+=phrase.getVolumeCount();
//								    			if (firstYear>phrase.getFirstYear()) {
//								    				firstYear = phrase.getFirstYear();
//								    			}
//								    			if (lastYear<phrase.getLastYear()) {
//								    				lastYear = phrase.getLastYear();
//								    			}
//						    				}
//						    			}
//						    		}
//						    		if (totalMatchCount > 0) {
//						    			System.out.println("TotalMatchCount: " + totalMatchCount);
//						    			System.out.println("TotalVolumeCount: " + totalVolumeCount);					    			
//						    			System.out.println("TotalFirstYear: " + totalFirstYear);
//						    			System.out.println("TotalLastYear: " + totalLastYear);
//						    		}
//						    		else {
//						    			System.out.println("No total matches");
//						    		}
//							    	if (matchCount > 0) {
//						    			System.out.println("PhraseScore: " + phraseScore);
//						    			System.out.println("MatchCount: " + matchCount);
//						    			System.out.println("VolumeCount: " + volumeCount);					    			
//						    			System.out.println("FirstYear: " + firstYear);
//						    			System.out.println("LastYear: " + lastYear);
//						    		}
//						    		else if (totalMatchCount > 0) {
//								    	//System.out.println("Query: " + query.toString().trim()+" "+tokens.get(ngram_idx+token_idx).originalText());
//						    			SearchResult result = PhraseFinder.search(Corpus.AMERICAN_ENGLISH, query.toString().trim()+" "+tokens.get(ngram_idx+token_idx).originalText(), options);
//								    	if (result.getStatus() == Status.OK) {
//								    		long tMatchCount = 0;
//								    		long tVolumeCount = 0;
//								    		long tFirstYear = 2018;
//								    		long tLastYear = 0;
//								    		for (Phrase phrase : result.getPhrases()) {
//									    		tMatchCount+=phrase.getMatchCount();
//									    		tVolumeCount+=phrase.getVolumeCount();
//									    		if (tFirstYear>phrase.getFirstYear()) {
//									    			tFirstYear = phrase.getFirstYear();
//									    		}
//									    		if (tLastYear<phrase.getLastYear()) {
//									    			tLastYear = phrase.getLastYear();
//									    		}
//								    		}
//								    		if (tMatchCount>0) {
//								    			System.out.println("PhraseScore: " + new Long(tMatchCount/totalMatchCount).toString());
//								    			System.out.println("MatchCount: " + tMatchCount);
//								    			System.out.println("VolumeCount: " + tVolumeCount);					    			
//								    			System.out.println("FirstYear: " + tFirstYear);
//								    			System.out.println("LastYear: " + tLastYear);
//								    		}
//								    		else {
//								    			System.out.println("No Matches");
//								    		}
//								    	}
//						    		}
//					    			System.out.println();
//
//						    	}
//						    	else {
//						    		System.out.println("No results");
//					    			System.out.println();
//						    	}
//
//					    	}
//					    }
//					    catch (Throwable t) {
//					    	t.printStackTrace();
//					    }
//					}
//				}
//				System.out.println("------------------------");
//			}
	}
}
