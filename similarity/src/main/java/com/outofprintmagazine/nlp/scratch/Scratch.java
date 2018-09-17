package com.outofprintmagazine.nlp.scratch;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.Bucket;

import java.io.IOException;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

public class Scratch {

	/*
	 * http://www.outofprintmagazine.co.in/archives.html
	 * 
	 */
	
	
	public static void main(String[] args) throws IOException {
		Document doc = Jsoup.connect("http://www.outofprintmagazine.co.in/archives.html").get();
		Elements links = doc.select("a[href^=archive]");
		for (Element link : links) {
			String ref = link.parent().childNode(0).attr("href").trim();
			if (ref.length()>0 && ref.startsWith("archive")) {
				String issuelink = "http://www.outofprintmagazine.co.in/" + ref;
				System.out.println(issuelink);				
				String issuedt = ref.substring("archive/".length(), ref.length()-"_issue/index.html".length());
				System.out.println(issuedt);
				Document issue = Jsoup.connect(issuelink).get();
				Elements storylinks = issue.select("area");
				if (storylinks.isEmpty()) {
					storylinks = issue.select("a");
				}
				for (Element storylink : storylinks) {
					String story = storylink.attr("href").trim();
					if (!story.startsWith(".") && !story.startsWith("index.html")) {
						String refstory = issuelink.substring(0, issuelink.length()-"index.html".length()) + story;
						System.out.println("Url: " + refstory);
						if (!story.equals("editors-note.html")) {
							try {
								Document storytext = Jsoup.connect(refstory).get();
								Elements titles = storytext.select("h5");
								for (Element title : titles) {
									Node titleNode = null;
									if (title.childNode(0).nodeName() == "strong") {
										titleNode = title.childNode(0).childNode(0);
									}
									else {
										titleNode = title.childNode(0);
									}
									if (titleNode.nodeName() == "span") {
										System.out.println("Title: " + title.childNode(0).childNode(0).outerHtml());
									}
									System.out.println("Author: " + title.textNodes().get(0).getWholeText().trim().substring("by ".length()));
	
								}
							}
							catch (Exception e) {
								System.out.println(e.toString());
							}
						}
					}
				}

				System.out.println();
				
			}
		}
	}

}
