package fom.model;

import java.util.HashMap;
import java.util.Map;

import fom.langidentification.LanguageIdentifier.Language;

public class TopicCluster extends Cluster {
			
	private double score;
	private Language language;
	
	public TopicCluster(Query originatingQuery, Cluster parent, double score, Language language) {
		super(originatingQuery, parent);
		this.score = score;
		this.language = language;
	}

	@Override
	public Map<String, String> getMeta() {
		Map<String, String> meta = new HashMap<String, String>();
		meta.put("type", "topic");
		meta.put("score", Double.toString(score));
		meta.put("language", language.toString());
		return meta;
	}

	@Override
	public int getTypeId() {
		return 3;
	}

}
