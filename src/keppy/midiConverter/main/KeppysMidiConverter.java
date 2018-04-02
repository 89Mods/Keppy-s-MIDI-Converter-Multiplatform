package keppy.midiConverter.main;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import keppy.midiConverter.GUI.ErrorMessage;
import keppy.midiConverter.GUI.KeppysMidiConverterPanel;

/*
 * Main class
 */
public class KeppysMidiConverter {
	
	public static final String NAME = "Keppy and TGM's MIDI Converter";
	public static final int VERSION_MAJOR = 15;
	public static final int VERSION_MINOR = 1;
	public static final int VERSION_PATCH = 3;
	public static final String VERSION = VERSION_MAJOR + "." + VERSION_MINOR + "." + VERSION_PATCH;
	public static final boolean muted = true;
	
	//Main Window
	public static JFrame frame;
	
	/*
	 * Application entry point/create frame and Panel
	 */
	public static void main(String[] args){
		try {
	        UIManager.setLookAndFeel(
	                UIManager.getSystemLookAndFeelClassName());
		}catch(Exception e){
			e.printStackTrace();
		}
		String osName = System.getProperty("os.name");
		if(osName != null && osName.toUpperCase().contains("WINDOWS") && !System.getProperty("user.name").equals("Administrator")){
			JOptionPane.showMessageDialog(frame, "I see you're trying to use the java version of KMC under Windows (however you got that to work), but please don't do that.\nThis version of KMC is supposed to be used on Linux based OSes or MacOS/OS X. Please use the original C# version of the converter under windows.\nYou can get it over at https://github.com/KaleidonKep99/Keppys-MIDI-Converter/releases\nIf you're here because C# KMC doesn't work for you, please go to the link but go to \"Issues\" and create a new Issue. Describe your problem in detail and the creator will reply soon to help you fix it.", "Error", JOptionPane.ERROR_MESSAGE);
			//System.exit(1);
		}
		frame = new JFrame(NAME);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//frame.setPreferredSize(new Dimension(652, 434 + 32));
		frame.setResizable(false);
		KeppysMidiConverterPanel keppysMidiConverterPanel = new KeppysMidiConverterPanel();
		frame.setContentPane(keppysMidiConverterPanel);
		keppysMidiConverterPanel.setLayout(null);
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension d = kit.getScreenSize();
		frame.setLocation((int)(d.getWidth() / 2 - 326), (int)(d.getHeight() / 2 -  205));
		frame.pack();
		frame.setVisible(true);
		//Check for updates
		if(KeppysMidiConverterPanel.checkUpdatesOnStart){
			int update = Updater.checkForUpdates();
			if(update == 1){
				int option = JOptionPane.showConfirmDialog(frame, "An new version is available to download. Would you like to download it now?", "Message", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if(option == 0){
					try {
						Desktop.getDesktop().browse(new URL("https://github.com/89Mods/Keppy-s-MIDI-Converter-Multiplatform/releases").toURI());
					} catch(Exception e2){
			        	ErrorMessage.showErrorMessage(frame, "Error opening download page", e2, false);
			        	e2.printStackTrace();
			        	return;
					}
				}
			}else if(update == 2){
				JOptionPane.showMessageDialog(frame, "You have a newer version of the converter then the most recent available one. Strange, isnt it?", "You dirty hacker", JOptionPane.WARNING_MESSAGE);
			}
		}
	}
	
}
