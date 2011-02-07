package fom.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class BlacklistChecker {

	private static Set<String> blacklist;
	
	public static boolean isBlacklisted(String uri){
		if(blacklist == null){
			try {
				blacklist = new HashSet<String>();
				File blacklistFile = new File("data/blacklist.txt");
				BufferedReader bufRead = new BufferedReader(new FileReader(blacklistFile));
				String blacklistedUri = null;
				if(bufRead.ready()){
					while((blacklistedUri=bufRead.readLine())!=null){
						blacklist.add(blacklistedUri);
					}
					bufRead.close();
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for(String blacklistedUri : blacklist){
			if(uri.contains(blacklistedUri)){
				return true;
			}
		}
		return false;
	}
	
}
