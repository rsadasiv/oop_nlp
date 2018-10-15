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
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
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


	}

}
