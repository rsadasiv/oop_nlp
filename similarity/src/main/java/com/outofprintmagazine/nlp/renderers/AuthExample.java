package com.outofprintmagazine.nlp.renderers;
import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.AuthInterface;
import com.flickr4java.flickr.auth.Permission;
import com.flickr4java.flickr.util.IOUtilities;

import org.scribe.model.Token;
import org.scribe.model.Verifier;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;

/*
token: Token[72157701211381324-634921aa9028a98d , 77682dbb8b4a0bf0]
Follow this URL to authorise yourself on Flickr
http://www.flickr.com/services/oauth/authorize?oauth_token=72157701211381324-634921aa9028a98d&perms=read
Paste in the token it gives you:
>>275-679-797
Authentication success
Token: 72157698263200692-3842a663c0204e04
Secret: 7ca09372ae4ff278
nsid: 42748022@N00
Realname: 
Username: rsadasiv
Permission: 1
 */

public class AuthExample {

    public static void auth() throws IOException, FlickrException {
        Properties properties;
        FileReader in = null;
        try {
            in = new FileReader("C:\\Users\\rsada\\git\\oop_nlp\\similarity\\src\\main\\resources\\flickr_api_key.txt");
            properties = new Properties();
            properties.load(in);
        } finally {
            IOUtilities.close(in);
        }

        Flickr flickr = new Flickr(properties.getProperty("apiKey"), properties.getProperty("secret"), new REST());
        Flickr.debugStream = false;
        AuthInterface authInterface = flickr.getAuthInterface();

        Scanner scanner = new Scanner(System.in);

        Token token = authInterface.getRequestToken();
        System.out.println("token: " + token);

        String url = authInterface.getAuthorizationUrl(token, Permission.READ);
        System.out.println("Follow this URL to authorise yourself on Flickr");
        System.out.println(url);
        System.out.println("Paste in the token it gives you:");
        System.out.print(">>");

        String tokenKey = scanner.nextLine();
        scanner.close();

        Token requestToken = authInterface.getAccessToken(token, new Verifier(tokenKey));
        System.out.println("Authentication success");

        Auth auth = authInterface.checkToken(requestToken);

        // This token can be used until the user revokes it.
        System.out.println("Token: " + requestToken.getToken());
        System.out.println("Secret: " + requestToken.getSecret());
        System.out.println("nsid: " + auth.getUser().getId());
        System.out.println("Realname: " + auth.getUser().getRealName());
        System.out.println("Username: " + auth.getUser().getUsername());
        System.out.println("Permission: " + auth.getPermission().getType());
    }

    public static void main(String[] args) {
        try {
            AuthExample.auth();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(0);
    }
}
