package fom.geocoding;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.util.Version;

import fom.utils.StringOperations;


public class LocalGeonamesGeocoder implements Geocoder {

	IndexSearcher idxSearcher;
	QueryParser qParser;
	
	public LocalGeonamesGeocoder(){
		try {
			idxSearcher = new IndexSearcher(new NIOFSDirectory(new File("data/geonamesIdx")));
			qParser = new QueryParser(Version.LUCENE_30, "names", new StandardAnalyzer(Version.LUCENE_30));
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override
	public double[] geocode(String location) {
		if(location!=null){
			location = StringOperations.removeNonLettersFromString(location);
			if(location.trim()!=""){
				try {
					TopDocs topDocs = idxSearcher.search(qParser.parse(location), 1);
					if(topDocs.scoreDocs.length>0){
						double lat = Double.parseDouble(idxSearcher.doc(topDocs.scoreDocs[0].doc).getField("lat").stringValue());
						double lon = Double.parseDouble(idxSearcher.doc(topDocs.scoreDocs[0].doc).getField("lon").stringValue());
						return new double[] {lat, lon};
					}
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ParseException e) {
				}
			}
		}
		return new double[] {0,0};	
	}

}
