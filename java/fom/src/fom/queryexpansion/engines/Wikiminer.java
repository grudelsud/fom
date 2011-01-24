package fom.queryexpansion.engines;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.wikipedia.miner.model.Article;
import org.wikipedia.miner.model.Wikipedia;

import fom.properties.PropertyHandler;

public class Wikiminer implements ExpansionEngine {

	@Override
	public List<String> expandQuery(String query) {
		List<String> expandedQuery = new ArrayList<String>();
		if(query.trim().equalsIgnoreCase("")){
			return expandedQuery;
		}
		try {
			String dbServer = PropertyHandler.getStringProperty("WikiminerDBServer");
			String dbName = PropertyHandler.getStringProperty("WikiminerDBName");
			String dbUser = PropertyHandler.getStringProperty("WikiminerDBUser"); 
			String dbPass = PropertyHandler.getStringProperty("WikiminerDBPass");
			Wikipedia wiki = new Wikipedia(dbServer, dbName, dbUser, dbPass);
			final Article article = wiki.getMostLikelyArticle(query, null);
			expandedQuery.add(query);
			if(article==null){
				return expandedQuery;
			}
			
			ArrayList<Article> expArticles = new ArrayList<Article>();
			expArticles.addAll(article.getLinksIn());
			expArticles.addAll(article.getLinksOut());
			Comparator<Article> articleRelatednessComparator = new Comparator<Article>() {
				@Override
				public int compare(Article o1, Article o2) {
					double rel1 = 0;
					double rel2 = 0;
					try {
						rel1 = o1.getRelatednessTo(article);
						rel2 = o2.getRelatednessTo(article);
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					if(rel1>rel2){
						return -1;
					} else if(rel1<rel2) {
						return 1;
					}
					return 0;
				}
			};
			Collections.sort(expArticles, articleRelatednessComparator);
			for(Article art : expArticles.subList(0, expArticles.size()>20?20:expArticles.size())){
				expandedQuery.add(art.getTitleWithoutScope());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return expandedQuery;
	}

}
