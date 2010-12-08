package fom.queryexpansion.engines;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.wikipedia.miner.model.Article;
import org.wikipedia.miner.model.Redirect;
import org.wikipedia.miner.model.Wikipedia;
import org.wikipedia.miner.model.Article.AnchorText;
import org.wikipedia.miner.util.SortedVector;

import fom.properties.PropertyHandler;

public class Wikiminer implements ExpansionEngine {

	@Override
	public List<String> expandQuery(String query) {
		List<String> expandedQuery = new ArrayList<String>();
		if(query.trim().equalsIgnoreCase("")){
			return expandedQuery;
		}
		try {
			String dbServer = PropertyHandler.getInstance().getProperties().getProperty("WikiminerDBServer");
			String dbName = PropertyHandler.getInstance().getProperties().getProperty("WikiminerDBName");
			String dbUser = PropertyHandler.getInstance().getProperties().getProperty("WikiminerDBUser"); 
			String dbPass = PropertyHandler.getInstance().getProperties().getProperty("WikiminerDBPass");
			Wikipedia wiki = new Wikipedia(dbServer, dbName, dbUser, dbPass);
			Article article = wiki.getMostLikelyArticle(query, null);
			if(article==null){
				expandedQuery.add(query);
				return expandedQuery;
			}
			
			Set<String> relatedWords = new HashSet<String>();
			relatedWords.add(query);
			
	//		
			System.out.println("Getting redirects");
			SortedVector<Redirect> redirects = article.getRedirects();
			for(int i=0; i<redirects.size(); i++){
				relatedWords.add(redirects.elementAt(i).getTarget().getTitleWithoutScope());
			}
			
	//		System.out.println("Getting anchors");
			SortedVector<AnchorText> anchorTexts = article.getAnchorTexts();
			for(int i=0; i<anchorTexts.size(); i++){
				relatedWords.add(anchorTexts.elementAt(i).getText());
			}
			
			/*
			System.out.println("Getting linksIn");
			SortedVector<Article> linksIn = article.getLinksIn();
			for(int i=0; i<linksIn.size(); i++){
				relatedWords.add(linksIn.elementAt(i).getTitleWithoutScope());
			}
			
			System.out.println("Getting linksOut");
			SortedVector<Article> linksOut = article.getLinksOut();
			for(int i=0; i<linksOut.size(); i++){
				relatedWords.add(linksOut.elementAt(i).getTitleWithoutScope());
			}
			*/
			
			expandedQuery.addAll(relatedWords);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return expandedQuery;
	}

}
