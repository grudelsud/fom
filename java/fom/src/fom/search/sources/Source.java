package fom.search.sources;

import java.util.Date;
import java.util.List;

import fom.model.SearchResult;


public interface Source {
	public List<SearchResult> searchPosts(List<String> terms, Date startTime, Date endTime);
}
