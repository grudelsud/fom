package fom.search.sources;

import java.util.List;

import org.joda.time.DateTime;

import fom.model.Post;
import fom.model.dao.interfaces.DAOFactory;

public class WholeDB implements Source{

	@Override
	public List<Post> searchPosts(List<String> terms, DateTime startTime, DateTime endTime, double lat, double lon, int radius) {
		return DAOFactory.getFactory().getPostDAO().getAllPosts();
	}

}
