package keppy.midiConverter.main;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import keppy.midiConverter.GUI.ErrorMessage;
import keppy.midiConverter.GUI.KeppysMidiConverterPanel;

/*
 * Main class
 */
public class KeppysMidiConverter {
	
	public static final String NAME = "Keppy and TGM's MIDI Converter";
	public static final int VERSION_MAJOR = 13;
	public static final int VERSION_MINOR = 0;
	public static final int VERSION_PATCH = 7;
	public static final String VERSION = VERSION_MAJOR + "." + VERSION_MINOR + "." + VERSION_PATCH;
	
	//Main Window
	public static JFrame frame;
	
	/*
	 * Application entry point/create frame and Panel
	 */
	public static void main(String[] args){
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
