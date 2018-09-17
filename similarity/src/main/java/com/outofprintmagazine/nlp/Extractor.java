package com.outofprintmagazine.nlp;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

public class Extractor {

	
	public Extractor() {
		super();
	}
	
	public List<Issue> extractAllIssues() throws IOException, ParseException {
		ArrayList<Issue> retval = new ArrayList<Issue>();
		Document doc = Jsoup.connect("http://www.outofprintmagazine.co.in/archives.html").get();
		Elements links = doc.select("a[href^=archive]");
		for (Element link : links) {
			String ref = link.parent().childNode(0).attr("href").trim();
			if (ref.length()>0 && ref.startsWith("archive")) {
				Issue issue = new Issue();
				issue.setUrl("http://www.outofprintmagazine.co.in/" + ref);
				issue.setIssueDate(ref.substring("archive/".length(), ref.length()-"_issue/index.html".length()));
				retval.add(issue);
			}
		}
		return retval;
	}
	
	public void extractIssueData(Issue issue) throws IOException, ParseException {
		if (issue.getIssueDate() == null) {
			issue.setIssueDate(issue.getUrl().substring("http://www.outofprintmagazine.co.in/archive/".length(), issue.getUrl().length()-"_issue/index.html".length()));
		}
		Document homepage = Jsoup.connect(issue.getUrl()).get();
		Elements imgs = homepage.select("img[src^=images/cover_pic]");
		for (Element img : imgs) {
			issue.setCoverArtUrl(img.attr("src").trim());
		}
		Elements storylinks = homepage.select("area");
		if (storylinks.isEmpty()) {
			storylinks = homepage.select("a");
		}
		for (Element storylink : storylinks) {
			String storyHref = storylink.attr("href").trim();
			if (!storyHref.startsWith(".") && !storyHref.startsWith("index.html")) {
				if (storyHref.equals("editors-note.html")) {
					issue.setEditorsNoteUrl(storyHref);
				}
				else {
					Story story = new Story();
					story.setUrl(issue.getHrefBase() + storyHref);
					try {
						Document storytext = Jsoup.connect(story.getUrl()).get();
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
								story.setTitle(title.childNode(0).childNode(0).outerHtml());
							}
							story.setAuthor(title.textNodes().get(0).getWholeText().trim().substring("by ".length()));

						}
						Elements mainText = storytext.select("div#main-text-cont2");
						for (Element body : mainText ) {
							//TODO
							Elements paragraphs = body.select("p");
							for (Element paragraph: paragraphs) {
								if (paragraph.attr("class").equals("writersintro")) {
									story.setBio(story.getBio() + '\n' + paragraph.text());
								}
								else {
									story.setBody(story.getBody() + '\n' + paragraph.text());
								}
							}
						}
						issue.getStories().add(story);
					}
					catch (Exception e) {
						System.out.println(e.toString());
					}
				}
			}
		}		
		
	}
	
	public static void main(String[] args) throws IOException, ParseException {
		Extractor me = new Extractor();
		Issue issue = new Issue();
		issue.setUrl("http://www.outofprintmagazine.co.in/archive/dec_2016-issue/index.html");
		me.extractIssueData(issue);
		System.out.println(issue);
		System.out.println(issue.getStories().get(0).getUrl());
		System.out.println(issue.getStories().get(0).getAuthor());
		System.out.println(issue.getStories().get(0).getTitle());
		System.out.println(issue.getStories().get(0).getBio());
		System.out.println("-----------------------------------------");
		System.out.println(issue.getStories().get(0).getBody());
		System.out.println("-----------------------------------------");		
	}
	
	
}
