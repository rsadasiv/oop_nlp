package com.outofprintmagazine.nlp.scratch;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;

public class MatchTest {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String body = "Feels a bit like a school essay/story.\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"*Out of Print Magazine <http://www.outofprintmagazine.co.in>*   *The blog\r\n" + 
				"at Out of Print <http://outofprintmagazine.blogspot.com>*     *Facebook*\r\n" + 
				"<https://www.facebook.com/Outofprintmagazine>   *Twitter*\r\n" + 
				"<https://twitter.com/OutofPrintOnlne>\r\n" + 
				"\r\n" + 
				"2017 DNA-OUT of PRINT Short Fiction Special <http://bit.ly/2017DNA-OofP>\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"---------- Forwarded message ----------\r\n" + 
				"From: sheena dlima <sheenadlima@gmail.com>\r\n" + 
				"Date: 14 June 2018 at 12:22\r\n" + 
				"Subject: SUBMISSION\r\n" + 
				"To: outofprintmagazine@gmail.com\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"Dear Editors,\r\n" + 
				"\r\n" + 
				"My name is Sheena D’Lima.\r\n" + 
				"\r\n" + 
				"Please consider my submission ‘By the sea’ for the upcoming issue of Out of\r\n" + 
				"Print. I am a writer and researcher who works out of Pune. This story was\r\n" + 
				"born after a week-long Global Initiative of Academic Networks (GIAN) course\r\n" + 
				"on Gender and Sexualities in South Asia that I attended in January where I\r\n" + 
				"had a frank engagement with an issue that I am very passionate about.\r\n" + 
				"\r\n" + 
				"*Name*: Sheena D’Lima\r\n" + 
				"\r\n" + 
				"*Postal Address*: 53 Gitanjali Kunj, 7 Dr Ambedkar Road, Opposite Nehru\r\n" + 
				"Memorial Hall, Pune 411001.\r\n" + 
				"\r\n" + 
				"*Email*: sheenadlima@gmail.com\r\n" + 
				"\r\n" + 
				"*Telephone Number*: 7507664455\r\n" + 
				"\r\n" + 
				"*Biographical Sketch*: Sheena D’Lima is an amateur fiction writer who lives\r\n" + 
				"and works out of Pune. She has written non-fiction for digital imprints\r\n" + 
				"like Deep Dives: Sexing the interwebs\r\n" + 
				"<https://deepdives.in/what-schoolgirls-in-india-can-teach-us-about-social-media-9d9e61c45f55>and\r\n" + 
				"has been a freelance as well as full time journalist for publications like\r\n" + 
				"Time Out Pune, Better Interiors and Hindustan Times Brunch. She currently\r\n" + 
				"works with the Krantijyoti Savitribai Phule Women’s Studies Centre in Pune\r\n" + 
				"on exciting research projects that marry gender, caste, access and social\r\n" + 
				"location in Indian Higher Education. She has also conducted  workshops on\r\n" + 
				"Gender sensitivity, the declining child sex ratio and Women’s safety in\r\n" + 
				"Public spaces with undergraduate students both as part of the KSP Women’s\r\n" + 
				"Studies Centre and independently. In her free time, she reads like it’s\r\n" + 
				"going out of style and does jigsaw puzzles.\r\n" + 
				"\r\n"
				;
		
			List<String> stopWords = Files.readAllLines(
				new File("C:\\Users\\rsada\\git\\oop_nlp\\similarity\\resources\\Email.txt").toPath(),
				Charset.defaultCharset());
			
			String lines[] = body.split("\\r?\\n");
			for (String line : lines) {
				boolean shouldPrint = true;
				for (String stopWord : stopWords) {
					if (line.matches(stopWord)) {
						System.out.println("Matched: " + stopWord + "  " + line);
						shouldPrint = false;
					}
				}
				if (shouldPrint) {
					System.out.println("No Match: " + line);
				}
				
			}
		
	}

}
