package fom.model.dao.localdb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import org.joda.time.DateTime;

import fom.model.Media;
import fom.model.dao.interfaces.MediaDAO;

public class LocalDBMediaDAO implements MediaDAO {

		private PreparedStatement stm;
		private PreparedStatement saveMediaStm;
		
	public LocalDBMediaDAO(Connection conn) {
		try {
			stm = conn.prepareStatement("INSERT INTO fom_media(relpath,filename,filetype,description,created,modified) VALUES (?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
			saveMediaStm = conn.prepareStatement("SELECT * FROM fom_media WHERE id_media=?");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public long create(Media media) {
		long mediaId = 0;
		try {
			stm.setString(1, media.getRelPath());
			stm.setString(2, media.getFileName());
			stm.setString(3, media.getFileType());
			stm.setString(4, media.getDescription());
			stm.setTimestamp(5, new Timestamp(media.getCreated().toDate().getTime()));
			stm.setTimestamp(6, new Timestamp(media.getModified().toDate().getTime()));
			
			stm.executeUpdate();
			ResultSet generatedKeys = stm.getGeneratedKeys();
			if(generatedKeys.next()){
				mediaId=generatedKeys.getLong(1);
			} else {
				System.err.println("Error creating media");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return mediaId;
	}

	@Override
	public Media retrieve(long mediaId) {
		Media media = null;
		try{
			saveMediaStm.setLong(1, mediaId);
			ResultSet res = saveMediaStm.executeQuery();
			while(res.next()){
				String relPath = res.getString("relpath");
				String fileName = res.getString("filename");
				String fileType = res.getString("filetype");
				String description = res.getString("description");
				DateTime created = new DateTime(res.getString("created"));
				DateTime modified = new DateTime(res.getString("modified"));
				media = new Media(relPath, fileName, fileType, description, created, modified);
			}
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return media;
	}


}
