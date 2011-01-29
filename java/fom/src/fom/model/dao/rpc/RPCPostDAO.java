package fom.model.dao.rpc;

import java.util.List;

import org.joda.time.DateTime;

import fom.model.Post;
import fom.model.dao.interfaces.PostDAO;

public class RPCPostDAO implements PostDAO {

	@Override
	public long create(Post post) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Post retrieve(long postId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Post> retrieve(List<String> terms, DateTime fromDate,
			DateTime toDate, double lat, double lon, int radius) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Post> getAllPosts() {
		// TODO Auto-generated method stub
		return null;
	}


}
