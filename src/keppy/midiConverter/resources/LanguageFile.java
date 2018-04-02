package keppy.midiConverter.resources;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import keppy.midiConverter.GUI.ErrorMessage;
import keppy.midiConverter.main.KeppysMidiConverter;

public class LanguageFile {
	
	public static LanguageFile CURRENT;
	public static LanguageFile ENGLISH;
	public static LanguageFile GERMAN;
	//I need more languages
	
	public String languageName;
	
	private List<String> languageFile;
	
	public String menuActions;
	public String menuActionsRemove;
	public String menuActionsClearMIDIsList;
	public String menuActionsOpenSfManager;
	public String menuActionsRenderWav;
	public String menuActionsRenderOgg;
	public String menuActionsPreview;
	public String menuActionsAbort;
	public String menuActionsExit;
	public String menuActionsImport;
	public String menuOptions;
	public String menuOptionsEnabled;
	public String menuOptionsDisabled;
	public String menuOptionsCheckUpdates;
	public String menuOptionsShutdown;
	public String menuOptionsClearAfterRendering;
	public String menuOptionsShowConversionPosition;
	public String menuOptionsCrash;
	public String menuHelp;
	public String menuHelpInformation;
	public String menuHelpSupport;
	public String menuHelpBlackMidi;
	public String menuHelpBlackMidiKeppsYoutube;
	public String menuHelpBlackMidiKeppsGitHub;
	public String menuHelpBlackMidiTgmsYoutube;
	public String menuHelpBlackMidiWiki;
	public String menuHelpBlackMidiGPlus;
	public String menuHelpBlackMidiWikipedia;
	public String mainWindowVoices;
	public String mainWindowVolume;
	public String mainWindowSettings;
	public String mainWindowBtnAdvancedSettings;
	public String mainWindowVoiceLimit;
	public String mainWindowMidiListPopupUp;
	public String mainWindowMidiListPopupDown;
	public String mainWindowStatusIdle1;
	public String mainWindowStatusIdle2;
	public String fileSelectorImportMidiTitle;
	public String fileSelectSelectSaveLocationTitle;
	
	public LanguageFile(String languageName){
		this.languageName = languageName;
	}
	
	public void loadLanguage(){
			languageFile = new ArrayList<String>();
			try {
			BufferedReader br = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/language/" + languageName + ".txt")));
			String line = "lol";
			while(line != null){
				line = br.readLine();
				if(line != null){
					languageFile.add(line);
				}
			}
			br.close();
			menuActions = getLanguageFileEntry("menu.actions");
			menuActionsRemove = getLanguageFileEntry("menu.actions.remove");
			menuActionsClearMIDIsList = getLanguageFileEntry("menu.actions.clearMIDIList");
			menuActionsOpenSfManager = getLanguageFileEntry("menu.actions.openSfManager");
			menuActionsRenderWav = getLanguageFileEntry("menu.actions.renderWav");
			menuActionsRenderOgg = getLanguageFileEntry("menu.actions.renderOgg");
			menuActionsPreview = getLanguageFileEntry("menu.actions.preview");
			menuActionsAbort = getLanguageFileEntry("menu.actions.abort");
			menuActionsExit = getLanguageFileEntry("menu.actions.exit");
			menuActionsImport = getLanguageFileEntry("menu.actions.import");
			menuOptions = getLanguageFileEntry("menu.options");
			menuOptionsEnabled = getLanguageFileEntry("menu.options.enabled");
			menuOptionsDisabled = getLanguageFileEntry("menu.options.disabled");
			menuOptionsCheckUpdates = getLanguageFileEntry("menu.options.checkUpdates");
			menuOptionsShutdown = getLanguageFileEntry("menu.options.shutdown");
			menuOptionsClearAfterRendering = getLanguageFileEntry("menu.options.clearAfterRendering");
			menuOptionsShowConversionPosition = getLanguageFileEntry("menu.options.showConversionPosition");
			menuOptionsCrash = getLanguageFileEntry("menu.options.crash");
			menuHelp = getLanguageFileEntry("menu.help");
			menuHelpInformation = getLanguageFileEntry("menu.help.information");
			menuHelpSupport = getLanguageFileEntry("menu.help.support");
			menuHelpBlackMidi = getLanguageFileEntry("menu.help.blackMidi");
			menuHelpBlackMidiKeppsYoutube = getLanguageFileEntry("menu.help.blackMidi.keppsYoutube");
			menuHelpBlackMidiKeppsGitHub = getLanguageFileEntry("menu.help.blackMidi.keppsGithub");
			menuHelpBlackMidiTgmsYoutube = getLanguageFileEntry("menu.help.blackMidi.tgmsYoutube");
			menuHelpBlackMidiWiki = getLanguageFileEntry("menu.help.blackMidi.wiki");
			menuHelpBlackMidiGPlus = getLanguageFileEntry("menu.help.blackMidi.gPlus");
			menuHelpBlackMidiWikipedia = getLanguageFileEntry("menu.help.blackMidi.wikipedia");
			mainWindowVoices = getLanguageFileEntry("mainWindow.voices");
			mainWindowVolume = getLanguageFileEntry("mainWindow.volume");
			mainWindowSettings = getLanguageFileEntry("mainWindow.settings");
			mainWindowBtnAdvancedSettings = getLanguageFileEntry("mainWindow.btnAdvSettings");
			mainWindowVoiceLimit = getLanguageFileEntry("mainWindow.voiceLimit");
			mainWindowMidiListPopupUp = getLanguageFileEntry("mainWindow.midiListPopup.up");
			mainWindowMidiListPopupDown = getLanguageFileEntry("mainWindow.midiListPopup.down");
			mainWindowStatusIdle1 = getLanguageFileEntry("mainWindow.status.idle.1");
			mainWindowStatusIdle2 = getLanguageFileEntry("mainWindow.status.idle.2");
			fileSelectorImportMidiTitle = getLanguageFileEntry("fileSelector.importMidi.title");
			fileSelectSelectSaveLocationTitle = getLanguageFileEntry("fileSelector.selectSaveLocation.title");
			
		} catch(Exception e){
			e.printStackTrace();
			ErrorMessage.showErrorMessage(KeppysMidiConverter.frame, "Error loading language file", e, false);
			defaultLanguage();
		}
	}
	
	private String getLanguageFileEntry(String entryName) throws Exception {
		for(String entry : languageFile){
			String s = entry;
			if(entry.startsWith(entryName)){
				s = s.replaceAll(entryName + "=", "");
				return s.replace("[newline]", "\n");
			}
		}
		return "[error]";
	}
	
	private void defaultLanguage(){
		menuActions = "Actions";
		menuActionsRemove = "Remove selected MIDI";
		menuActionsClearMIDIsList = "Clear MIDIs list";
		menuActionsOpenSfManager = "Open soundfonts manager";
		menuActionsRenderWav = "Render files to Wave (.WAV)";
		menuActionsRenderOgg = "Render files to Vorbis (.OGG)";
		menuActionsPreview = "Preview files (Real-time playback)";
		menuActionsAbort = "Abort rendering";
		menuActionsExit = "Exit";
		menuActionsImport = "Import MIDI";
		menuOptions = "Options";
		menuOptionsEnabled = "Enabled";
		menuOptionsDisabled = "Disabled";
		menuOptionsCheckUpdates = "Automatically check for updates when starting the converter";
		menuOptionsShutdown = "Automatic shutdown after rendering";
		menuOptionsClearAfterRendering = "Clear MIDIs list after rendering";
		menuOptionsShowConversionPosition = "Show conversion position instead of time left";
		menuOptionsCrash = "Crash the application";
		menuHelp = "Help";
		menuHelpInformation = "Information about the program";
		menuHelpSupport = "Support the developer with a donation";
		menuHelpBlackMidi = "Black MIDI stuff";
		menuHelpBlackMidiKeppsYoutube = "KaleidonKep99's YouTube Channel";
		menuHelpBlackMidiKeppsGitHub = "KaleidonKep99's GitHub page";
		menuHelpBlackMidiTgmsYoutube = "TheGhastModding's Youtube Channel";
		menuHelpBlackMidiWiki = "Official Black MIDI Wikia";
		menuHelpBlackMidiGPlus = "Official Black MIDI Community (Google+)";
		menuHelpBlackMidiWikipedia = "Wikipedia's page";
		mainWindowVoices = "Voices:";
		mainWindowVolume = "Volume:";
		mainWindowSettings = "Settings";
		mainWindowBtnAdvancedSettings = "Advanced Settings";
		mainWindowVoiceLimit = "Voice limiter:";
		mainWindowMidiListPopupUp = "Move up";
		mainWindowMidiListPopupDown = "Move down";
		mainWindowStatusIdle1 = "Idle.";
		mainWindowStatusIdle2 = "Select a MIDI, and load your soundonts to start the conversion/playback";
		fileSelectorImportMidiTitle = "Import a MIDI";
		fileSelectSelectSaveLocationTitle = "Select location to save files in";
		
	}
	
	public static void loadCurrentLanguage(int language){
		if(language == 0){
			CURRENT = new EnglishLanguage();
			return;
		}
		
		CURRENT = new EnglishLanguage();
	}
	
	public static class EnglishLanguage extends LanguageFile {
		
		public EnglishLanguage() {
			super("english");
			loadLanguage();
		}
		
	}
	
}
