package keppy.midiConverter.GUI;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.concurrent.TimeUnit;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import jouvieje.bass.Bass;
import jouvieje.bass.BassInit;
import jouvieje.bass.defines.BASS_ACTIVE;
import jouvieje.bass.defines.BASS_ATTRIB;
import jouvieje.bass.defines.BASS_CONFIG;
import jouvieje.bass.defines.BASS_CONFIG_MIDI;
import jouvieje.bass.defines.BASS_DEVICE;
import jouvieje.bass.defines.BASS_ENCODE;
import jouvieje.bass.defines.BASS_MIDI;
import jouvieje.bass.defines.BASS_SAMPLE;
import jouvieje.bass.defines.BASS_STREAM;
import jouvieje.bass.structures.BASS_INFO;
import jouvieje.bass.structures.BASS_MIDI_FONT;
import jouvieje.bass.structures.HENCODE;
import jouvieje.bass.structures.HSTREAM;
import jouvieje.bass.utils.BufferUtils;
import jouvieje.bass.utils.Pointer;
import keppy.midiConverter.main.KeppysMidiConverter;
import keppy.midiConverter.resources.JLabelGifPlayer;
import keppy.midiConverter.resources.LanguageFile;
import keppy.midiConverter.resources.Sounds;
import keppy.midiConverter.resources.Textures;
import javax.swing.JPopupMenu;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/*
 * Main Window content pane
 * 
 * This application uses NativeBass by Jérôme Jouvie
 */
@SuppressWarnings("serial")
public class KeppysMidiConverterPanel extends JPanel {
	
	public static boolean checkUpdatesOnStart = true;
	public static boolean shutdownAfterRendering = false;
	public static boolean clearMIDIsListAfterRendering = false;
	public static boolean showConversionPosition = true;
	public static int maxVoices = 500;
	private InformationDialog information;
	private JMenuItem abortRenderingMenuItem;
	private SoundfontManager sfManager;
	private JFileChooser midiChooser;
	public DefaultListModel<String> midiListModel;
	private JList<String> midiList;
	private AdvancedSettings advancedSettings;
	private boolean conversionAborting = false;
	private Thread converterThread;
	private JMenuItem startConversionMenuItem;
	private JMenuItem startOggConversionMenuItem;
	private JMenuItem previewFilesMenuItem;
	private JLabel lblLoadingpic;
	private int handle;
	private HSTREAM stream;
	private HENCODE encoder;
	private JLabel lblStatus;
	private JLabel lblVoices;
	private JLabel lblStatus2;
	private JLabel lblStatus3;
	private JProgressBar progressBar;
	private JMenuItem clearMidiListMenuItem;
	private JMenuItem importMidiMenuItem;
	private JMenuItem removeMidiMenuItem;
	private JFileChooser folderChooser;
	public static String font = "Arial";
	private JLabelGifPlayer convbusyPlayer;
	private JSlider volumeSlider;
	private JLabel lblVolume;
	private JSpinner spinner;
	
	/*
	 * Create GUI and stuff
	 */
	public KeppysMidiConverterPanel(){
		super();
		LanguageFile.loadCurrentLanguage(0);
		
		/*
		 * Load resources
		 */
		try {
			//Load textures
			Textures.load();
		}catch(Exception e){
			e.printStackTrace();
			ErrorMessage.showErrorMessage(KeppysMidiConverter.frame, "Error loading textures", e, true);
			System.exit(1);
		}
		try {
			//Load sounds
			Sounds.load();
		}catch(Exception e){
			e.printStackTrace();
			ErrorMessage.showErrorMessage(KeppysMidiConverter.frame, "Error loading sounds", e, true);
			System.exit(1);
		}
		
		//Unlike the other GUIs, this one has to be created first for the settings to load properly
		advancedSettings = new AdvancedSettings(KeppysMidiConverter.frame);
		/*
		 * Load Options
		 */
		loadOptions(advancedSettings);
		
		/*
		 * Init BASS
		 */
		BassInit.loadLibraries();
		
		setPreferredSize(new Dimension(652, 410));
		
		KeppysMidiConverter.frame.addWindowListener(new MainWindowListener());
		setFocusable(true);
		setLayout(null);
		
		midiList = new JList<String>();
		midiListModel = new DefaultListModel<String>();
		midiList.setModel(midiListModel);
		midiList.setFont(new Font(KeppysMidiConverterPanel.font, Font.PLAIN, 12));
		midiList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		midiList.setBounds(12, 12, 628, 258);
		
		JScrollPane listScroller = new JScrollPane(midiList);
		
		JPopupMenu popupMenu = new JPopupMenu();
		popupMenu.add(addMenuItem(LanguageFile.CURRENT.menuActionsImport, new ActionListener(){
			public void actionPerformed(ActionEvent event){
				importMidi();
			}}, null));
		popupMenu.add(addMenuItem(LanguageFile.CURRENT.menuActionsRemove, new ActionListener(){
			public void actionPerformed(ActionEvent event){
				if(converterThread != null){
					if(converterThread.isAlive()){
						JOptionPane.showMessageDialog(KeppysMidiConverter.frame, "Cant edit the MIDIs list during rendering", "Warning", JOptionPane.WARNING_MESSAGE);
						return;
					}
				}
				if(midiList.getSelectedIndex() >= 0 && midiList.getSelectedIndex() < midiListModel.size()){
					midiListModel.remove(midiList.getSelectedIndex());
				}
			}}, null));
		popupMenu.add(addMenuItem(LanguageFile.CURRENT.menuActionsClearMIDIsList, new ActionListener(){
			public void actionPerformed(ActionEvent event){
				if(converterThread != null){
					if(converterThread.isAlive()){
						JOptionPane.showMessageDialog(KeppysMidiConverter.frame, "Cant edit the MIDIs list during rendering", "Warning", JOptionPane.WARNING_MESSAGE);
						return;
					}
				}
				midiListModel.clear();
			}}, null));
		popupMenu.addSeparator();
		popupMenu.add(addMenuItem("Move up", new ActionListener(){
			public void actionPerformed(ActionEvent event){
	            if (midiListModel.size() > 0)
	            {
	                String selected = midiList.getSelectedValue();
	                int indx = midiList.getSelectedIndex();
	                int totl = midiListModel.size();
	                if (indx == 0)
	                {
	                	midiListModel.remove(indx);
	                	midiListModel.insertElementAt(selected, totl - 1);
	                	midiList.setSelectedIndex(totl - 1);
	                }
	                else
	                {
	                	midiListModel.remove(indx);
	                    midiListModel.insertElementAt(selected, indx - 1);
	                    midiList.setSelectedIndex(indx - 1);
	                }
	            }
			}}, null));
		popupMenu.add(addMenuItem("Move Down", new ActionListener(){
			public void actionPerformed(ActionEvent event){
	            if (midiListModel.size() > 0)
	            {
	                String selected = midiList.getSelectedValue();
	                int indx = midiList.getSelectedIndex();
	                int totl = midiListModel.size();
	                if (indx == totl - 1)
	                {
	                	midiListModel.remove(indx);
	                	midiListModel.insertElementAt(selected, 0);
	                	midiList.setSelectedIndex(0);
	                }
	                else
	                {
	                	midiListModel.remove(indx);
	                	midiListModel.insertElementAt(selected, indx + 1);
	                	midiList.setSelectedIndex(indx + 1);
	                }
	            }
			}}, null));
		popupMenu.setFont(new Font(KeppysMidiConverterPanel.font, Font.PLAIN, 12));
		addPopup(midiList, popupMenu);
		listScroller.setBounds(12, 12, 628, 258);
		listScroller.setBorder(new LineBorder(new Color(126, 180, 234)));
		listScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		add(listScroller);
		
		lblVoices = new JLabel("Voices: 0/" + maxVoices);
		lblVoices.setHorizontalAlignment(SwingConstants.RIGHT);
		lblVoices.setFont(new Font(KeppysMidiConverterPanel.font, Font.PLAIN, 12));
		lblVoices.setBounds(359, 283, 143, 16);
		add(lblVoices);
		
		progressBar = new JProgressBar();
		progressBar.setBorder(new LineBorder(new Color(0xBCBCBC)));
		progressBar.setMinimum(0);
		progressBar.setMaximum(100);
		progressBar.setForeground(new Color(0x06B025));
		progressBar.setBounds(0, 387, 652, 23);
		add(progressBar);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Settings", TitledBorder.LEADING, TitledBorder.TOP, new Font(KeppysMidiConverterPanel.font, Font.PLAIN, 12), null));
		panel.setBounds(510, 305, 130, 70);
		add(panel);
		panel.setLayout(null);
		
		JLabel lblVoiceLimit = new JLabel("Voice Limit:");
		lblVoiceLimit.setFont(new Font(KeppysMidiConverterPanel.font, Font.PLAIN, 11));
		lblVoiceLimit.setBounds(6, 18, 57, 16);
		panel.add(lblVoiceLimit);
		
		spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(maxVoices, 1, 100000, 1));
		spinner.setBounds(63, 16, 58, 21);
		spinner.setFont(new Font(KeppysMidiConverterPanel.font, Font.PLAIN, 11));
		spinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				int value = Integer.parseInt(spinner.getValue().toString());
				if(value > 100000){
					spinner.setValue(100000);
					value = 100000;
				}
				if(value < 1){
					spinner.setValue(1);
					value = 1;
				}
				maxVoices = value;
				saveOptions(advancedSettings);
				if(converterThread != null){
					if(converterThread.isAlive()){
						return;
					}
				}
				if(converterThread != null){
					if(!converterThread.isAlive()){
						lblVoices.setText("Voices: 0/" + value);
					}
				}else{
					lblVoices.setText("Voices: 0/" + value);
				}
			}
		});
		panel.add(spinner);
		
		JButton btnNewButton = new JButton("Advanced Settings");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				advancedSettings.setVisible(true);
			}
		});
		btnNewButton.setFont(new Font(KeppysMidiConverterPanel.font, Font.PLAIN, 9));
		btnNewButton.setBounds(6, 40, 118, 23);
		panel.add(btnNewButton);
		
		lblVolume = new JLabel("Volume:");
		lblVolume.setEnabled(false);
		lblVolume.setHorizontalAlignment(SwingConstants.CENTER);
		lblVolume.setFont(new Font(KeppysMidiConverterPanel.font, Font.PLAIN, 12));
		lblVolume.setBounds(510, 273, 130, 16);
		add(lblVolume);
		
		volumeSlider = new JSlider();
		volumeSlider.setEnabled(false);
		volumeSlider.setMaximum(10000);
		volumeSlider.setValue(10000);
		volumeSlider.setBounds(510, 283, 130, 20);
		add(volumeSlider);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel_1.setBackground(Color.WHITE);
		panel_1.setBounds(12, 311, 492, 64);
		add(panel_1);
		panel_1.setLayout(null);
		
		lblStatus2= new JLabel("Idle.");
		lblStatus2.setHorizontalAlignment(SwingConstants.CENTER);
		lblStatus2.setFont(new Font(KeppysMidiConverterPanel.font, Font.PLAIN, 12));
		lblStatus2.setBounds(0, 11, 488, 16);
		panel_1.add(lblStatus2);
		
		lblStatus = new JLabel("Select a MIDI, and load your soundfonts to start the conversion/playback!");
		lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
		lblStatus.setFont(new Font(KeppysMidiConverterPanel.font, Font.PLAIN, 12));
		lblStatus.setBounds(0, 27, 488, 16);
		panel_1.add(lblStatus);
		
		lblLoadingpic = new JLabel();
		convbusyPlayer = new JLabelGifPlayer(30, lblLoadingpic, "/convbusy.gif", true);
		lblLoadingpic.setVisible(false);
		lblLoadingpic.setBounds(425, 3, 63, 60);
		panel_1.add(lblLoadingpic);
		
		lblStatus3 = new JLabel("");
		lblStatus3.setHorizontalAlignment(SwingConstants.CENTER);
		lblStatus3.setFont(new Font(KeppysMidiConverterPanel.font, Font.PLAIN, 12));
		lblStatus3.setBounds(0, 44, 480, 16);
		panel_1.add(lblStatus3);
		
		//Create file choosers
		midiChooser = new JFileChooser();
		midiChooser.setDialogTitle("Import a MIDI");
		midiChooser.setAcceptAllFileFilterUsed(false);
		midiChooser.setFileFilter(new FileNameExtensionFilter("MIDI files", "mid", "MID", "midi"));
		
		folderChooser = new JFileChooser();
		folderChooser.setCurrentDirectory(new File(System.getProperty("user.home") + "/Desktop"));
		folderChooser.setDialogTitle("Select location to save files in");
		folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		requestFocus();
		createMenuBar();
		
		KeppysMidiConverter.frame.setIconImage(Textures.icon);
		/*
		 * Init GUIs
		 */
		information = new InformationDialog(KeppysMidiConverter.frame);
		sfManager = new SoundfontManager(KeppysMidiConverter.frame);
	}
	
	/*
	 * Subfunction to create the menu bar
	 */
	private void createMenuBar(){
		JMenuBar bar = new JMenuBar();
		KeppysMidiConverter.frame.setJMenuBar(bar);
		JMenu actionsMenu = new JMenu(LanguageFile.CURRENT.menuActions);
		actionsMenu.setFont(new Font(KeppysMidiConverterPanel.font, Font.PLAIN, 11));
		bar.add(actionsMenu);
		JMenu optionsMenu = new JMenu(LanguageFile.CURRENT.menuOptions);
		optionsMenu.setFont(new Font(KeppysMidiConverterPanel.font, Font.PLAIN, 11));
		bar.add(optionsMenu);
		JMenu helpMenu = new JMenu(LanguageFile.CURRENT.menuHelp);
		helpMenu.setFont(new Font(KeppysMidiConverterPanel.font, Font.PLAIN, 11));
		bar.add(helpMenu);
		importMidiMenuItem = addMenuItem(LanguageFile.CURRENT.menuActionsImport, new ActionListener(){
			public void actionPerformed(ActionEvent event){
				importMidi();
			}}, actionsMenu);
		removeMidiMenuItem = addMenuItem(LanguageFile.CURRENT.menuActionsRemove, new ActionListener(){
			public void actionPerformed(ActionEvent event){
				if(converterThread != null){
					if(converterThread.isAlive()){
						JOptionPane.showMessageDialog(KeppysMidiConverter.frame, "Cant edit the MIDIs list during rendering", "Warning", JOptionPane.WARNING_MESSAGE);
						return;
					}
				}
				if(midiList.getSelectedIndex() >= 0 && midiList.getSelectedIndex() < midiListModel.size()){
					midiListModel.remove(midiList.getSelectedIndex());
				}
			}}, actionsMenu);
		clearMidiListMenuItem = addMenuItem(LanguageFile.CURRENT.menuActionsClearMIDIsList, new ActionListener(){
			public void actionPerformed(ActionEvent event){
				if(converterThread != null){
					if(converterThread.isAlive()){
						JOptionPane.showMessageDialog(KeppysMidiConverter.frame, "Cant edit the MIDIs list during rendering", "Warning", JOptionPane.WARNING_MESSAGE);
						return;
					}
				}
				midiListModel.clear();
			}}, actionsMenu);
		actionsMenu.addSeparator();
		addMenuItem(LanguageFile.CURRENT.menuActionsOpenSfManager, new ActionListener(){
			public void actionPerformed(ActionEvent event){
				sfManager.setVisible(true);
			}}, actionsMenu);
		actionsMenu.addSeparator();
		startConversionMenuItem = addMenuItem(LanguageFile.CURRENT.menuActionsRenderWav, new ActionListener(){
			public void actionPerformed(ActionEvent event){
				startConversion(0);
			}}, actionsMenu);
		startOggConversionMenuItem = addMenuItem(LanguageFile.CURRENT.menuActionsRenderOgg, new ActionListener(){
			public void actionPerformed(ActionEvent event){
				startConversion(1);
			}}, actionsMenu);
		previewFilesMenuItem = addMenuItem(LanguageFile.CURRENT.menuActionsPreview, new ActionListener(){
			public void actionPerformed(ActionEvent event){
				startConversion(2);
			}}, actionsMenu);
		abortRenderingMenuItem = addMenuItem(LanguageFile.CURRENT.menuActionsAbort, new ActionListener(){
			public void actionPerformed(ActionEvent event){
				abortConversion();
			}}, actionsMenu);
		abortRenderingMenuItem.setEnabled(false);
		actionsMenu.addSeparator();
		addMenuItem(LanguageFile.CURRENT.menuActionsExit, new ActionListener(){
			public void actionPerformed(ActionEvent event){
				abortConversion();
				finishUp();
			}}, actionsMenu);
		addMenuItem(LanguageFile.CURRENT.menuHelpInformation, new ActionListener(){public void actionPerformed(ActionEvent event){information.setVisible(true);}}, helpMenu);
		addMenuItem(LanguageFile.CURRENT.menuHelpSupport, new ActionListener(){
			public void actionPerformed(ActionEvent event){
	            String url = "";

	            String business = "prapapappo1999@gmail.com";
	            String description = "Donation";
	            String country = "US";
	            String currency = "USD";

	            url += "https://www.paypal.com/cgi-bin/webscr" +
	                "?cmd=" + "_donations" +
	                "&business=" + business +
	                "&lc=" + country +
	                "&item_name=" + description +
	                "&currency_code=" + currency +
	                "&bn=" + "PP%2dDonationsBF";
	            openWebpage(url);
			}}, helpMenu);
		helpMenu.addSeparator();
		JMenu stuffMenu = new JMenu(LanguageFile.CURRENT.menuHelpBlackMidi);
		stuffMenu.setFont(new Font(KeppysMidiConverterPanel.font, Font.PLAIN, 11));
		helpMenu.add(stuffMenu);
		JMenuItem youtubeItem = new JMenuItem(LanguageFile.CURRENT.menuHelpBlackMidiKeppsYoutube);
		youtubeItem.setFont(new Font(KeppysMidiConverterPanel.font, Font.PLAIN, 11));
		youtubeItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				openWebpage("https://www.youtube.com/channel/UCJeqODojIv4TdeHcBfHJRnA");
			}});
		stuffMenu.add(youtubeItem);
		addMenuItem(LanguageFile.CURRENT.menuHelpBlackMidiTgmsYoutube, new ActionListener(){
			public void actionPerformed(ActionEvent event){
				openWebpage("https://www.youtube.com/channel/UCB8EPwooLFYoApIaXK3jpRg");
			}}, stuffMenu);
		stuffMenu.addSeparator();
		addMenuItem(LanguageFile.CURRENT.menuHelpBlackMidiWiki, new ActionListener(){
			public void actionPerformed(ActionEvent event){
				openWebpage("http://officialblackmidi.wikia.com/wiki/Official_Black_MIDI_Wikia");
			}}, stuffMenu);
		addMenuItem(LanguageFile.CURRENT.menuHelpBlackMidiGPlus, new ActionListener(){
			public void actionPerformed(ActionEvent event){
				openWebpage("https://plus.google.com/communities/105907289212970966669");
			}}, stuffMenu);
		stuffMenu.addSeparator();
		addMenuItem(LanguageFile.CURRENT.menuHelpBlackMidiWikipedia, new ActionListener(){
			public void actionPerformed(ActionEvent event){
				openWebpage("https://en.wikipedia.org/wiki/Black_MIDI");
			}}, stuffMenu);
		addOptionMenuItem(LanguageFile.CURRENT.menuOptionsCheckUpdates, optionsMenu, new ActionListener(){public void actionPerformed(ActionEvent event){checkUpdatesOnStart = true;saveOptions(advancedSettings);}},new ActionListener(){public void actionPerformed(ActionEvent event){checkUpdatesOnStart = false;saveOptions(advancedSettings);}}, checkUpdatesOnStart);
		addOptionMenuItem(LanguageFile.CURRENT.menuOptionsShutdown, optionsMenu, new ActionListener(){public void actionPerformed(ActionEvent event){shutdownAfterRendering = true;saveOptions(advancedSettings);}},new ActionListener(){public void actionPerformed(ActionEvent event){shutdownAfterRendering = false;saveOptions(advancedSettings);}}, shutdownAfterRendering);
		addOptionMenuItem(LanguageFile.CURRENT.menuOptionsClearAfterRendering, optionsMenu, new ActionListener(){public void actionPerformed(ActionEvent event){clearMIDIsListAfterRendering = true;saveOptions(advancedSettings);}},new ActionListener(){public void actionPerformed(ActionEvent event){clearMIDIsListAfterRendering = false;saveOptions(advancedSettings);}}, clearMIDIsListAfterRendering);
		addOptionMenuItem(LanguageFile.CURRENT.menuOptionsShowConversionPosition, optionsMenu, new ActionListener(){public void actionPerformed(ActionEvent event){showConversionPosition = true;saveOptions(advancedSettings);}},new ActionListener(){public void actionPerformed(ActionEvent event){showConversionPosition = false;saveOptions(advancedSettings);}}, showConversionPosition);
		addMenuItem(LanguageFile.CURRENT.menuOptionsCrash, new ActionListener(){
			public void actionPerformed(ActionEvent event){
				ErrorMessage.showErrorMessageNoStackTrace(KeppysMidiConverter.frame, "Application was forcefully crashed by user", true);
			}}, optionsMenu);
	}
	
	/*
	 * Select and add a MIDI to the list
	 */
	private void importMidi(){
		if(converterThread != null){
			if(converterThread.isAlive()){
				JOptionPane.showMessageDialog(KeppysMidiConverter.frame, "Cant edit the MIDIs list during rendering", "Warning", JOptionPane.WARNING_MESSAGE);
				return;
			}
		}
		int option = midiChooser.showOpenDialog(KeppysMidiConverter.frame);
		if(option == JFileChooser.APPROVE_OPTION){
			if(!midiChooser.getSelectedFile().exists()){
				ErrorMessage.showErrorMessageNoStackTrace(KeppysMidiConverter.frame, "The selected file doesnt exist", false);
				return;
			}
			if(midiChooser.accept(midiChooser.getSelectedFile())){
				midiListModel.addElement(midiChooser.getSelectedFile().getPath());
			}else{
				ErrorMessage.showErrorMessageNoStackTrace(KeppysMidiConverter.frame, "That isnt a MIDI!", false);
				return;
			}
		}
	}
	
	/*
	 * Create the conversion thread and start conversion
	 */
	private void startConversion(int mode){
		if(midiListModel.isEmpty()){
			ErrorMessage.showErrorMessageNoStackTrace(KeppysMidiConverter.frame, "Cant start conversion: add some MIDIs to convert first", false);
			return;
		}
		if(sfManager.listModel.isEmpty()){
			ErrorMessage.showErrorMessageNoStackTrace(KeppysMidiConverter.frame, "Cant start conversion: add some soundfonts to convert with first", false);
			return;
		}
		if(converterThread != null){ //to prevent crash with next if statement
			if(converterThread.isAlive()){ //check if its allready converting something
				ErrorMessage.showErrorMessageNoStackTrace(KeppysMidiConverter.frame, "Cant start conversion: the converter is allready converting MIDIs", false);
				return;
			}
		}
		if(mode != 2){
			if(folderChooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION){
				return;
			}
			if(!folderChooser.getSelectedFile().exists()){;
				ErrorMessage.showErrorMessageNoStackTrace(KeppysMidiConverter.frame, "The selected directory doesnt exist", false);
				return;
			}
		}
		convbusyPlayer.start();
		MidiConverter converter = new MidiConverter(mode, folderChooser.getSelectedFile().getPath());
		converterThread = new Thread(converter);
		converterThread.start();
		startConversionMenuItem.setEnabled(false);
		abortRenderingMenuItem.setEnabled(true);
		previewFilesMenuItem.setEnabled(false);
		midiList.setEnabled(false);
		lblLoadingpic.setVisible(true);
		advancedSettings.disableAll();
		startOggConversionMenuItem.setEnabled(false);
		clearMidiListMenuItem.setEnabled(false);
		importMidiMenuItem.setEnabled(false);
		removeMidiMenuItem.setEnabled(false);
		spinner.setEnabled(false);
	}
	
	/*
	 * Change some things back after the conversion is done
	 */
	private void endConversion(){
		convbusyPlayer.stop();
		startConversionMenuItem.setEnabled(true);
		abortRenderingMenuItem.setEnabled(false);
		previewFilesMenuItem.setEnabled(true);
		startOggConversionMenuItem.setEnabled(true);
		midiList.setEnabled(true);
		lblLoadingpic.setVisible(false);
		clearMidiListMenuItem.setEnabled(true);
		importMidiMenuItem.setEnabled(true);
		removeMidiMenuItem.setEnabled(true);
		spinner.setEnabled(true);
		Bass.BASS_StreamFree(stream);
		if(encoder != null){
			Bass.BASS_Encode_Stop(encoder.asInt());
		}
		advancedSettings.enableAll();
		lblStatus.setText("Select a MIDI, and load your soundfonts to start the conversion/playback!");
		lblStatus2.setText("Idle.");
		lblStatus3.setText("");
		lblVoices.setText("Voices: 0/" + maxVoices);
		setName(KeppysMidiConverter.NAME);
		if(clearMIDIsListAfterRendering){
			midiListModel.clear();
		}
		if(shutdownAfterRendering){
			try {
				Runtime.getRuntime().exec("shutdown /s /t 0");
			} catch(Exception e){
				e.printStackTrace();
				ErrorMessage.showErrorMessage(KeppysMidiConverter.frame, "Error shuting down computer after rendering",e, true);
			}
		}
		progressBar.setValue(0);
	}
	
	/*
	 * Force conversion to stop
	 */
	private void abortConversion(){
		abortRenderingMenuItem.setEnabled(false);
		conversionAborting = true;
	}
	
	/*
	 * Close BASS and all open streams before the application is closed
	 */
	private void finishUp(){
		Bass.BASS_Free();
		System.exit(0);
	}
	
	/*
	 * Add two menu items called "Enabled" and "Disabled" for options
	 */
	private void addOptionMenuItem(String title, JMenu menu, ActionListener enabledActionListener, ActionListener disabledActionListener, boolean defaultValue){
		JMenu updateOptionMenu = new JMenu(title);
		updateOptionMenu.setFont(new Font(KeppysMidiConverterPanel.font, Font.PLAIN, 11));
		menu.add(updateOptionMenu);
		JRadioButtonMenuItem updateOptionEnabledItem = new JRadioButtonMenuItem(LanguageFile.CURRENT.menuOptionsEnabled);
		updateOptionEnabledItem.addActionListener(enabledActionListener);
		updateOptionEnabledItem.setFont(new Font(KeppysMidiConverterPanel.font, Font.PLAIN, 11));
		updateOptionEnabledItem.setSelected(defaultValue);
		updateOptionMenu.add(updateOptionEnabledItem);
		JRadioButtonMenuItem updateOptionDisabledItem = new JRadioButtonMenuItem(LanguageFile.CURRENT.menuOptionsDisabled);
		updateOptionDisabledItem.addActionListener(disabledActionListener);
		updateOptionDisabledItem.setFont(new Font(KeppysMidiConverterPanel.font, Font.PLAIN, 11));
		updateOptionDisabledItem.setSelected(!defaultValue);
		updateOptionMenu.add(updateOptionDisabledItem);
		ButtonGroup g = new ButtonGroup();
		g.add(updateOptionDisabledItem);
		g.add(updateOptionEnabledItem);
	}
	
	/*
	 * Create a new menu item with action listener and add it to a menu if one is given
	 */
	public static JMenuItem addMenuItem(String title, ActionListener listener, JMenu menu){
		JMenuItem item = new JMenuItem(title);
		item.addActionListener(listener);
		item.setFont(new Font(KeppysMidiConverterPanel.font, Font.PLAIN, 11));
		if(menu != null){
			menu.add(item);
		}
		return item;
	}
	
	/*
	 * Opens a URL in the default webbrowser
	 */
	public static void openWebpage(String page){
		try {
			Desktop.getDesktop().browse(new URL(page).toURI());
		} catch(Exception e2){
			ErrorMessage.showErrorMessage(KeppysMidiConverter.frame, "Error opening a webpage", e2, false);
        	e2.printStackTrace();
        	return;
		}
	}
	
	/*
	 * Save all settings
	 */
	public static boolean saveOptions(AdvancedSettings advSettings){
		try {
			DataOutputStream dos = new DataOutputStream(new FileOutputStream(new File("settings.dat")));
			dos.writeBoolean(checkUpdatesOnStart);
			dos.writeBoolean(shutdownAfterRendering);
			dos.writeBoolean(clearMIDIsListAfterRendering);
			dos.writeBoolean(showConversionPosition);
			dos.writeInt(maxVoices);
			dos.writeBoolean(advSettings.chckbxOnlyRelease.isSelected());
			dos.writeBoolean(advSettings.chckbxDisableSoundEffects.isSelected());
			dos.writeBoolean(advSettings.chckbxOverrideMidiTempo.isSelected());
			dos.writeBoolean(advSettings.chckbxForceConstantBitrate.isSelected());
			dos.writeInt(advSettings.frequencyComboBox.getSelectedIndex());
			dos.writeInt(advSettings.bitrateComboBox.getSelectedIndex());
			dos.writeInt(Integer.parseInt(advSettings.tempoSpinner.getValue().toString()));
			dos.writeBoolean(advSettings.chckbxEnableTooltipGuide.isSelected());
			dos.close();
		}catch(Exception e){
			ErrorMessage.showErrorMessage(KeppysMidiConverter.frame, "Error saving settings", e, false);
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/*
	 * Load all settings
	 */
	public static boolean loadOptions(AdvancedSettings advSettings){
		if(!new File("settings.dat").exists()){
			return saveOptions(advSettings);
		}
		DataInputStream dis;
		try {
			dis = new DataInputStream(new FileInputStream(new File("settings.dat")));
			checkUpdatesOnStart = dis.readBoolean();
			shutdownAfterRendering = dis.readBoolean();
			clearMIDIsListAfterRendering = dis.readBoolean();
			showConversionPosition = dis.readBoolean();
			maxVoices = dis.readInt();
			boolean b;
			b = dis.readBoolean();
			advSettings.chckbxOnlyRelease.setSelected(b);
			b = dis.readBoolean();
			advSettings.chckbxDisableSoundEffects.setSelected(b);
			b = dis.readBoolean();
			advSettings.chckbxOverrideMidiTempo.setSelected(b);
			b = dis.readBoolean();
			advSettings.chckbxForceConstantBitrate.setSelected(b);
			advSettings.bitrateComboBox.setEnabled(advSettings.chckbxForceConstantBitrate.isSelected());
			advSettings.tempoSpinner.setEnabled(advSettings.chckbxOverrideMidiTempo.isSelected());
			int i;
			i = dis.readInt();
			advSettings.frequencyComboBox.setSelectedIndex(i);
			i = dis.readInt();
			advSettings.bitrateComboBox.setSelectedIndex(i);
			i = dis.readInt();
			advSettings.tempoSpinner.setValue(i);
			b = dis.readBoolean();
			advSettings.chckbxEnableTooltipGuide.setSelected(b);
			if(b){
				advSettings.enableTooltips();
			}
			dis.close();
		}catch(Exception e){
			ErrorMessage.showErrorMessage(KeppysMidiConverter.frame, "Error loading settings", e, false);
			e.printStackTrace();
			saveOptions(advSettings);
			return false;
		}
		return true;
	}
	
	/*
	 * Midi Converter Thread
	 */
	private class MidiConverter implements Runnable {
		
		private int mode;
		private String folder;
		private boolean conversionCrashed = false;
		
		private MidiConverter(int mode, String folder){
			Sounds.playClip(Sounds.convstart);
			this.mode = mode;
			this.folder = folder;
		}
		
		/*
		 * Main thread function
		 */
		@Override
		public void run() {
			if(mode == 2){
				volumeSlider.setEnabled(true);
				lblVolume.setEnabled(true);
				realtimePlayback();
				volumeSlider.setEnabled(false);
				lblVolume.setEnabled(false);
				return;
			}
			try {
				boolean keepLooping = true;
				while(keepLooping){
					for(int i = 0; i < midiListModel.size(); i++){
						String str = midiListModel.getElementAt(i);
						Bass.BASS_Init(-1, Integer.parseInt(advancedSettings.frequencyComboBox.getSelectedItem().toString()), BASS_DEVICE.BASS_DEVICE_NOSPEAKER, null, null);
						Bass.BASS_SetConfig(BASS_CONFIG_MIDI.BASS_CONFIG_MIDI_VOICES, 100000);
						BASSStreamSystem(str);
						BASSEffectSettings();
						BASSEncoderInit(new File(str).getName());
						long starttime = System.currentTimeMillis();
						if(conversionCrashed){
							return;
						}
				        while (Bass.BASS_ChannelIsActive(handle) == BASS_ACTIVE.BASS_ACTIVE_PLAYING)
				        {
				        	if(conversionAborting){
				        		lblStatus.setText("Conversion aborted.");
				        		lblVoices.setText("Voices: 0/" + maxVoices);
				        		Sounds.playClip(Sounds.convfail);
				        		JOptionPane.showMessageDialog(KeppysMidiConverter.frame, "Conversion aborted.", "Warning", JOptionPane.WARNING_MESSAGE);
				        		keepLooping = false;
				        		break;
				        	}else{
				        		BASSEncodingEngine(starttime);
				        	}
				        }
				        if(conversionAborting && keepLooping == false){
				        	break;
				        }else{
				        	continue;
				        }
					}
					if(conversionAborting){
		        		lblVoices.setText("Voices: 0/" + maxVoices);
		        		keepLooping = false;
		        		endConversion();
		        		conversionAborting = false;
					}else{
						conversionAborting = false;
						lblVoices.setText("Voices: 0/" + maxVoices);
						keepLooping = false;
						Sounds.playClip(Sounds.convfin);
						endConversion();
					}
				}
			}catch(Exception e){
				ErrorMessage.showErrorMessage(KeppysMidiConverter.frame, "Error converting MIDI", e, false);
				e.printStackTrace();
				endConversion();
				return;
			}
		}
		
		private void realtimePlayback() {
			try {
				boolean keepLooping = true;
				while(keepLooping){
					for(int i = 0; i < midiListModel.size(); i++){
						String str = midiListModel.getElementAt(i);
						Bass.BASS_Init(-1, Integer.parseInt(advancedSettings.frequencyComboBox.getSelectedItem().toString()), BASS_DEVICE.BASS_DEVICE_NOSPEAKER, null, null);
                        Bass.BASS_SetConfig(BASS_CONFIG.BASS_CONFIG_UPDATEPERIOD, 0);
                        Bass.BASS_SetConfig(BASS_CONFIG.BASS_CONFIG_UPDATETHREADS, 32);
                        BASS_INFO info = BASS_INFO.allocate();
                        Bass.BASS_GetInfo(info);
                        Bass.BASS_SetConfig(BASS_CONFIG.BASS_CONFIG_BUFFER, info.getMinBuffer() + 10);
                        Bass.BASS_ChannelSetAttribute(handle, 73731, maxVoices);
                        stream = Bass.BASS_MIDI_StreamCreateFile(false, pointerFromString(str), 0L, 0L, BASS_SAMPLE.BASS_SAMPLE_FLOAT | BASS_MIDI.BASS_MIDI_DECAYEND, Integer.parseInt(advancedSettings.frequencyComboBox.getSelectedItem().toString()));
                        handle = stream.asInt();
                        setName(KeppysMidiConverter.NAME + " | Playing \"" + str + "...\"...");
                        BASS_MIDI_FONT[] fonts = new BASS_MIDI_FONT[sfManager.listModel.size()];
                        for(int j = 0; j < sfManager.listModel.size(); j++){
                        	fonts[j] = BASS_MIDI_FONT.allocate();
                        	String s = sfManager.listModel.getElementAt(j);
                        	if(s == null || s.isEmpty()){
                        		continue;
                        	}
                            fonts[j].setFont(Bass.BASS_MIDI_FontInit(pointerFromString(s), 0));
                            fonts[j].setPreset(-1);
                            fonts[j].setPreset(0);
                            Bass.BASS_MIDI_StreamSetFonts(stream, fonts[j], j + 1);
                        }
                        Bass.BASS_MIDI_StreamLoadSamples(stream);
                        BASSEffectSettings();
                        Bass.BASS_ChannelPlay(handle, false);
				        while (Bass.BASS_ChannelIsActive(handle) == BASS_ACTIVE.BASS_ACTIVE_PLAYING)
				        {
				        	if(conversionAborting){
				        		lblStatus.setText("Conversion aborted.");
				        		lblVoices.setText("Voices: 0/" + maxVoices);
				        		Sounds.playClip(Sounds.convfail);
				        		JOptionPane.showMessageDialog(KeppysMidiConverter.frame, "Conversion aborted.", "Warning", JOptionPane.WARNING_MESSAGE);
				        		keepLooping = false;
				        		break;
				        	}else{
				        		BASSEffectSettings();
				                if(advancedSettings.chckbxOverrideMidiTempo.isSelected()){
				                	Bass.BASS_MIDI_StreamEvent(stream, 0, 1, Integer.parseInt(advancedSettings.tempoSpinner.getValue().toString()));
				                }
                                Bass.BASS_SetConfig(BASS_CONFIG.BASS_CONFIG_GVOL_STREAM, volumeSlider.getValue());
                                Bass.BASS_ChannelSetAttribute(handle, BASS_ATTRIB.BASS_ATTRIB_MIDI_VOICES, maxVoices);
                                long pos = Bass.BASS_ChannelGetLength(handle, 0);
                                long num6 = Bass.BASS_ChannelGetPosition(handle, 0);
                                float num7 = ((float)pos) / 1048576f;
                                float num8 = ((float)num6) / 1048576f;
                                double num9 = Bass.BASS_ChannelBytes2Seconds(handle, pos);
                                double num10 = Bass.BASS_ChannelBytes2Seconds(handle, num6);
                                String str4 = Long.toString(TimeUnit.SECONDS.toHours((long)num9)) + ":" + Long.toString(TimeUnit.SECONDS.toMinutes((long)num9) % 60) + ":" + Long.toString(TimeUnit.SECONDS.toSeconds((long)num9) % 60);
                                String str5 = Long.toString(TimeUnit.SECONDS.toHours((long)num10)) + ":" + Long.toString(TimeUnit.SECONDS.toMinutes((long)num10) % 60) + ":" + Long.toString(TimeUnit.SECONDS.toSeconds((long)num10) % 60);
                                float num11 = 0f;
                                float num12 = 0f;
                                FloatBuffer fbfr = BufferUtils.newFloatBuffer(1);
                                Bass.BASS_ChannelGetAttribute(handle, 73732, fbfr);
                                num11 = fbfr.get(0);
                                fbfr = BufferUtils.newFloatBuffer(1);
                                Bass.BASS_ChannelGetAttribute(handle, BASS_ATTRIB.BASS_ATTRIB_CPU, fbfr);
                                num12 = fbfr.get(0);
                                lblVoices.setText("Voices: " + (int)num11 + "/" + maxVoices);
                                float percentage = num8 / num7;
                                float percentagefinal;
                                if (percentage * 100 < 0){
                                    percentagefinal = 0.0f;
                                }
                                else if (percentage * 100 > 100){
                                    percentagefinal = 1.0f;
                                }
                                else{
                                    percentagefinal = percentage * 100;
                                }
                                String num8Str = Float.toString(num8);
                                if(num8Str.length() >= 4){
                                	num8Str = num8Str.substring(0, 4);
                                }
                                lblStatus.setText(num8Str + "MBs of RAW datas played.\nCurrent position: " + str5 + " - " + str4);
                                lblStatus3.setText("Rendering time: " + (int)num12 + "%");
                                if((int)percentagefinal > 100){
                                	progressBar.setValue(100);
                                }else{
                                	progressBar.setValue((int)percentagefinal);
                                }
                                Bass.BASS_ChannelUpdate(handle, 0);
				        	}
				        }
				        if(conversionAborting && keepLooping == false){
				        	break;
				        }else{
				        	continue;
				        }
					}
					if(conversionAborting){
		        		lblVoices.setText("Voices: 0/" + maxVoices);
		        		keepLooping = false;
		        		endConversion();
		        		conversionAborting = false;
					}else{
						conversionAborting = false;
						lblVoices.setText("Voices: 0/" + maxVoices);
						keepLooping = false;
						Sounds.playClip(Sounds.convfin);
						endConversion();
					}
				}
			}catch(Exception e){
				ErrorMessage.showErrorMessage(KeppysMidiConverter.frame, "Error converting MIDI", e, false);
				e.printStackTrace();
				endConversion();
				return;
			}
		}

		private void BASSEncodingEngine(long time){
			long timeSpent = System.currentTimeMillis() - time;
            int length = (int)Bass.BASS_ChannelSeconds2Bytes(handle, 0.02);
            long pos = Bass.BASS_ChannelGetLength(handle, 0);
            long num6 = Bass.BASS_ChannelGetPosition(handle, 0);
            float num7 = ((float)pos) / 1048576f;
            float num8 = ((float)num6) / 1048576f;
            double num9 = Bass.BASS_ChannelBytes2Seconds(handle, pos);
            double num10 = Bass.BASS_ChannelBytes2Seconds(handle, num6);
            String str4 = Long.toString(TimeUnit.SECONDS.toHours((long)num9)) + ":" + Long.toString(TimeUnit.SECONDS.toMinutes((long)num9) % 60) + ":" + Long.toString(TimeUnit.SECONDS.toSeconds((long)num9) % 60);
            String str5 = Long.toString(TimeUnit.SECONDS.toHours((long)num10)) + ":" + Long.toString(TimeUnit.SECONDS.toMinutes((long)num10) % 60) + ":" + Long.toString(TimeUnit.SECONDS.toSeconds((long)num10) % 60);
            float num11 = 0f;
            float num12 = 0f;
            FloatBuffer fbfr = BufferUtils.newFloatBuffer(1);
            Bass.BASS_ChannelGetAttribute(handle, 73732, fbfr);
            num11 = fbfr.get(0);
            fbfr = BufferUtils.newFloatBuffer(1);
            Bass.BASS_ChannelGetAttribute(handle, BASS_ATTRIB.BASS_ATTRIB_CPU, fbfr);
            num12 = fbfr.get(0);
            lblVoices.setText("Voices: " + (int)num11 + "/" + maxVoices);
            if(num6 == 1) num6 = 2;
            if(num6 == 0) num6 = 2;
            int secondsremaining = (int)((timeSpent / 1000) / (int)num6 * ((int)pos - (int)num6));
            String str6 = Long.toString(TimeUnit.SECONDS.toHours((long)secondsremaining)) + ":" + Long.toString(TimeUnit.SECONDS.toMinutes((long)secondsremaining) % 60) + ":" + Long.toString(TimeUnit.SECONDS.toSeconds((long)secondsremaining) % 60);
            String str7 = Long.toString(TimeUnit.MILLISECONDS.toHours((long)timeSpent)) + ":" + Long.toString(TimeUnit.MILLISECONDS.toMinutes((long)timeSpent) % 60) + ":" + Long.toString(TimeUnit.MILLISECONDS.toSeconds((long)timeSpent) % 60);
            float percentage = num8 / num7;
            float percentagefinal;
            if (percentage * 100 < 0){
                percentagefinal = 0.0f;
            }
            else if (percentage * 100 > 100){
                percentagefinal = 1.0f;
            }
            else{
                percentagefinal = percentage * 100;
            }
            ByteBuffer buffer = BufferUtils.newByteBuffer(length);
            if(advancedSettings.chckbxOverrideMidiTempo.isSelected()){
            	Bass.BASS_MIDI_StreamEvent(stream, 0, 1, Integer.parseInt(advancedSettings.tempoSpinner.getValue().toString()));
            }
            Bass.BASS_ChannelGetData(handle, buffer, length);
            String num8Str = Float.toString(num8);
            if(num8Str.length() >= 4){
            	num8Str = num8Str.substring(0, 4);
            }
            if (num12 < 100f)
            {
                if (showConversionPosition == false){
                    lblStatus2.setText(num8Str + "MBs of RAW samples converted. (" + (int)percentagefinal + "%)");
                    lblStatus.setText("Approximate time left: " + str6 + " - Time elapsed: " + str7);
                    lblStatus3.setText("Rendering time: " + (int)num12 + "% (" + (int)(100f / num12) + "x~ faster)");
                }else{
                    lblStatus2.setText(num8Str + "MBs of RAW samples converted. (" + (int)percentagefinal + "%)");
                    lblStatus.setText("Current position: " + str5 + " - " + str4);
                    lblStatus3.setText("Rendering time: " + (int)num12 + "% (" + (int)(100f / num12) + "x~ faster)");
                }
            }
            else if (num12 == 100f)
            {
                if (showConversionPosition == false){
	                lblStatus2.setText(num8Str + "MBs of RAW samples converted. (" + (int)percentagefinal + "%)");
	                lblStatus.setText("Approximate time left: " + str6 + " - Time elapsed: " + str7);
	                lblStatus3.setText("Rendering time: " + (int)num12 + "%");
                }else{
                    lblStatus2.setText(num8Str + "MBs of RAW samples converted. (" + (int)percentagefinal + "%)");
                    lblStatus.setText("Current position: " + str5 + " - " + str4);
                    lblStatus3.setText("Rendering time: " + (int)num12 + "%");
                }
            }
            else if (num12 > 100f)
            {
                if (showConversionPosition == false){
                    lblStatus2.setText(num8Str + "MBs of RAW samples converted. (" + (int)percentagefinal + "%)");
                    lblStatus.setText("Approximate time left: " + str6 + " - Time elapsed: " + str7);
                    lblStatus3.setText("Rendering time: " + (int)num12 + "% (" + (int)(num12 / 100f) + "x~ slower)");
                }else{
                    lblStatus2.setText(num8Str + "MBs of RAW samples converted. (" + (int)percentagefinal + "%)");
                    lblStatus.setText("Current position: " + str5 + " - " + str4);
                    lblStatus3.setText("Rendering time: " + (int)num12 + "% (" + (int)(num12 / 100f) + "x~ slower)");
                }
            }
            if((int)percentagefinal > 100){
            	progressBar.setValue(100);
            }else{
            	progressBar.setValue((int)percentagefinal);
            }
		}
		
		private void BASSStreamSystem(String str) throws Exception {
			stream = Bass.BASS_MIDI_StreamCreateFile(false, pointerFromString(str), 0L, 0L, BASS_STREAM.BASS_STREAM_DECODE | BASS_SAMPLE.BASS_SAMPLE_FLOAT | BASS_MIDI.BASS_MIDI_DECAYEND, Integer.parseInt(advancedSettings.frequencyComboBox.getSelectedItem().toString()));
			handle = stream.asInt();
            Bass.BASS_ChannelSetAttribute(handle, 73731, maxVoices);
            Bass.BASS_ChannelSetAttribute(handle, BASS_ATTRIB.BASS_ATTRIB_MIDI_CPU, 0);
            setName(KeppysMidiConverter.NAME + " | Exporting \"" + str + "...\"...");
            BASS_MIDI_FONT[] fonts = new BASS_MIDI_FONT[sfManager.listModel.size()];
            for(int j = 0; j < sfManager.listModel.size(); j++){
            	fonts[j] = BASS_MIDI_FONT.allocate();
            	String s = sfManager.listModel.getElementAt(j);
            	if(s == null || s.isEmpty()){
            		continue;
            	}
                fonts[j].setFont(Bass.BASS_MIDI_FontInit(pointerFromString(s), 0));
                fonts[j].setPreset(-1);
                fonts[j].setPreset(0);
                Bass.BASS_MIDI_StreamSetFonts(stream, fonts[j], j + 1);
            }
            Bass.BASS_MIDI_StreamLoadSamples(stream);
		}
		
		private void BASSEffectSettings(){
            if (advancedSettings.chckbxDisableSoundEffects.isSelected()){
                Bass.BASS_ChannelFlags(handle, BASS_MIDI.BASS_MIDI_NOFX, BASS_MIDI.BASS_MIDI_NOFX);
            }else{
                Bass.BASS_ChannelFlags(handle, 0, BASS_MIDI.BASS_MIDI_NOFX);
            }
            if (advancedSettings.chckbxOnlyRelease.isSelected()){
                Bass.BASS_ChannelFlags(handle, 65536, 65536);
            }else{
                Bass.BASS_ChannelFlags(handle, 0, 65536);
            }
		}
		
		private void BASSEncoderInit(String str){
			File f;
			String path;
			
			if(mode == 0){
				f = new File(folder + "\\" + str + ".wav");
				if(f.exists()){
					int num1 = 1;
					while(f.exists()){
						f = new File(folder + "\\" + str + "(Copy " + num1 + ").wav");
						num1++;
					}
				}
				encoder = Bass.BASS_Encode_Start(handle, f.getPath(),  BASS_ENCODE.BASS_ENCODE_AUTOFREE | BASS_ENCODE.BASS_ENCODE_PCM, null, null);
			}else if(mode == 1){
				f = new File(folder + "\\" + str + ".ogg");
				if(f.exists()){
					int num1 = 1;
					while(f.exists()){
						f = new File(folder + "\\" + str + "(Copy " + num1 + ").ogg");
						num1++;
					}
				}
				if(advancedSettings.chckbxForceConstantBitrate.isSelected()){
					path = "kmcogg -m" + advancedSettings.bitrateComboBox.getSelectedItem().toString() + " -M" + advancedSettings.bitrateComboBox.getSelectedItem().toString() + " - -o \"" + f.getPath() + "\"";
				}else{
					 path = "kmcogg - -o \"" + f.getPath() + "\"";
				}
				encoder = Bass.BASS_Encode_Start(handle, path,  BASS_ENCODE.BASS_ENCODE_AUTOFREE, null, null);
			}else{
				new ErrorMessage(KeppysMidiConverter.frame, "Error", "Error converting MIDI: invalid mode", false, false).displayError();
				endConversion();
				conversionCrashed = true;
			}
		}
		
		private Pointer pointerFromString(String s){
			return BufferUtils.asPointer(BufferUtils.fromString(s));
		}
		
	}
	
	/*
	 * Window listener to abort conversion and finish up when window is closed
	 */
	private class MainWindowListener implements WindowListener {
		
		private MainWindowListener(){}
		
		@Override
		public void windowOpened(WindowEvent e) {}
		
		@Override
		public void windowClosing(WindowEvent e) {
			abortConversion();
			finishUp();
		}
		
		@Override
		public void windowClosed(WindowEvent e) {}
		
		@Override
		public void windowIconified(WindowEvent e) {}
		
		@Override
		public void windowDeiconified(WindowEvent e) {}
		
		@Override
		public void windowActivated(WindowEvent e) {}
		
		@Override
		public void windowDeactivated(WindowEvent e) {}
		
	}
	
	public static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
	
}
