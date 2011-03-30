package fom.langidentification;

import java.util.List;

import it.unimi.dsi.fastutil.bytes.ByteArrayList;
import net.olivo.lc4j.LanguageCategorization;

public class Lc4jLangIdentifier implements LanguageIdentifier {

	private LanguageCategorization lc;
	
	public Lc4jLangIdentifier(){
		this.lc = new LanguageCategorization();
		lc.setLanguageModelsDir("data/langmodels");
		lc.setMaxLanguages(4);
	}
	
	@Override
	public Language identifyLanguageOf(String text) {
		List res = lc.findLanguage(new ByteArrayList(text.getBytes()));
		if(res.size()>0){
			String lang = res.get(0).toString();
			System.out.println(lang);
			int barPosition = lang.indexOf("-");
			if(barPosition == -1){
				int dotPosition = lang.indexOf(".");
				if(dotPosition == -1){
					return Language.unknown;
				}
				else {
					lang = lang.substring(0, dotPosition);					
				}
			} else {
				lang = lang.substring(0, barPosition);
			}
			try{
				Language language = Enum.valueOf(Language.class, lang);
				return language;
			} catch (IllegalArgumentException ex) {
				return Language.unknown;
			}
			
		} else {
			return Language.unknown;
		}
	}

}
