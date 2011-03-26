package fom.langidentification;

import java.io.IOException;

public class JExSLILangIdentifier implements LanguageIdentifier {

	org.fbk.cit.hlt.langidentifier.LanguageIdentifier langIdentifier;
	
	public JExSLILangIdentifier(){
		try {
			this.langIdentifier = new org.fbk.cit.hlt.langidentifier.LanguageIdentifier(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public Language identifyLanguageOf(String text) {
		String lang = langIdentifier.identify(text);
		if(lang==null){
			return Language.unknown;
		} else {
		//	return lang;
			return Language.unknown;
		}
	}

}
