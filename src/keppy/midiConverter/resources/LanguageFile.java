package keppy.midiConverter.resources;

public class LanguageFile {
	
	public static LanguageFile CURRENT;
	public static LanguageFile ENGLISH;
	public static LanguageFile GERMAN;
	//I need more languages
	
	public String languageName;
	
	public LanguageFile(String languageName){
		this.languageName = languageName;
	}
	
	public void loadLanguage(){
		
	}
	
}
