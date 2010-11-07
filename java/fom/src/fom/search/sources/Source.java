package fom.search.sources;

import java.util.List;

import org.joda.time.DateTime;

import fom.model.Post;


public interface Source {
	public List<Post> searchPosts(List<String> terms, DateTime startTime, DateTime endTime);
}
