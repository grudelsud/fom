package fom.langidentification;

import java.util.List;

import it.unimi.dsi.fastutil.bytes.ByteArrayList;
import net.olivo.lc4j.LanguageCategorization;

public class Lc4jLangIdentifier implements LanguageIdentifier {

	private LanguageCategorization lc;
	
	public Lc4jLangIdentifier(){
		this.lc = new LanguageCategorization();
		lc.setLanguageModelsDir("data/langmodels");
		lc.setMaxLanguages(1);
	}
	
	@Override
	public Language identifyLanguageOf(String text) {
		List res = lc.findLanguage(new ByteArrayList(text.getBytes()));
		if(res.size()>0){
			//return res.get(0).toString();
			return Language.unknown;
		} else {
			return Language.unknown;
		}
	}

}
