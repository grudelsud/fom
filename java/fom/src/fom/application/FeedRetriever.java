package fom.application;

/*
 * Workflow:
 *	1	Start
 *	2	Read Feed List
 *	3	For each Feed:
 *		3.1	Download feedItems
 *		3.2	For each feedItem:
 *			3.2.1	Check if new and save
 *	4	Wait a predefined time
 *	5 	Goto 2
*/

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.sun.syndication.feed.synd.SyndCategory;
import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

import fom.model.Feed;
import fom.model.FeedItem;
import fom.model.Term;
import fom.model.Vocabulary;
import fom.model.dao.interfaces.DAOFactory;
import fom.properties.PropertyHandler;
import fom.rss.FeedListParser;

public class FeedRetriever extends Thread{
	
	private FeedListParser feedListParser;
	private Vocabulary rssVoc;
	
	public FeedRetriever(){
		super();
		this.feedListParser = new FeedListParser();
		this.rssVoc = new Vocabulary("RSS", null);
	}
	
	public void run(){
		while(true){
			System.out.println(new Date());
			System.out.println("Reading feed list...");
			List<Feed> feeds = feedListParser.readFeedList();
			for(Feed feed : feeds){
				System.out.print("\nSearching for new items in the channel: ");
				System.out.println(feed.toString());				
				SyndFeedInput feedInput = new SyndFeedInput();
				try {
					SyndFeed syndFeed = feedInput.build(new XmlReader(new URL(feed.getUrl())));
					for(Object entry : syndFeed.getEntries()){
						FeedItem currItem = parseSyndEntry((SyndEntry) entry, feed);
						DAOFactory.getFactory().getFeedItemDAO().create(currItem);
					}
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (FeedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}			
			
			try {
				System.out.println("\nDone! The thread will sleep for " + PropertyHandler.getIntProperty("FeedRefreshMinutes") + " minutes.");
				Thread.sleep(PropertyHandler.getIntProperty("FeedRefreshMinutes")*60*1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private FeedItem parseSyndEntry(SyndEntry entry, Feed parentFeed){
		String title = entry.getTitle();
		String uri = entry.getUri();
		String description = null;
		SyndContent entryDesc = entry.getDescription();
		if(entry.getDescription()!=null){
			if(entry.getDescription().getType()!=null && entry.getDescription().getType().equalsIgnoreCase("text/html")){
				Document htmlDesc = Jsoup.parseBodyFragment(entry.getDescription().getValue());
				description = htmlDesc.text();
			} else {				
				description = entryDesc.getValue();
			}
		}
		DateTime date = new DateTime(entry.getPublishedDate());
		
		/*
		System.out.println("Entry:");
		System.out.println("\tTitle: " + title);
		System.out.println("\tDescription: " + description);
		System.out.println("\tDate: " + date);
		System.out.println("\tUri: " + uri);
		*/
		
		FeedItem result = new FeedItem(parentFeed ,title, description, uri, date);
		for(Object category : entry.getCategories()){
			SyndCategory syndCategory = (SyndCategory) category;
			result.addCategory(new Term(syndCategory.getName(), syndCategory.getTaxonomyUri(), null, null, rssVoc));
		}
		return result;
	}
}
