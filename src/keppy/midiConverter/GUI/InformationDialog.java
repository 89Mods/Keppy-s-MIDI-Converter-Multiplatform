package keppy.midiConverter.GUI;

import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import keppy.midiConverter.main.KeppysMidiConverter;
import keppy.midiConverter.main.Updater;
import keppy.midiConverter.resources.ResourceExtractor;
import keppy.midiConverter.resources.Textures;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Color;
import java.awt.Desktop;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;
import java.awt.event.ActionEvent;
import javax.swing.border.BevelBorder;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class InformationDialog extends JDialog {
	
	public InformationDialog(JFrame frame){
		super(frame, "Information");
		setModal(true);
		setLocationRelativeTo(frame);
		setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(545, 345));
		this.setContentPane(panel);
		panel.setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setFont(new Font(KeppysMidiConverterPanel.font, Font.PLAIN, 12));
		tabbedPane.setBounds(1, 1, 545, 304);
		panel.add(tabbedPane);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.WHITE);
		tabbedPane.addTab("Information", null, panel_1, null);
		panel_1.setLayout(null);
		tabbedPane.setSelectedIndex(0);
		
		JLabel label = new JLabel("");
		label.setIcon(new ImageIcon(Textures.logo));
		label.setBounds(6, 8, 200, 200);
		panel_1.add(label);
		
		JLabel lblK = new JLabel(KeppysMidiConverter.NAME + " " + KeppysMidiConverter.VERSION);
		lblK.setFont(new Font(KeppysMidiConverterPanel.font, Font.PLAIN, 11));
		lblK.setBounds(212, 8, 316, 16);
		panel_1.add(lblK);
		
		JLabel lblCompiledFor = new JLabel("Compiled for ARM, x86 and x86_64 platforms");
		lblCompiledFor.setFont(new Font(KeppysMidiConverterPanel.font, Font.PLAIN, 11));
		lblCompiledFor.setBounds(212, 21, 316, 16);
		panel_1.add(lblCompiledFor);
		
		JLabel lblCopyright = new JLabel(convertToMultiline("Copyright(C) 2013-2016, KaleidonKep99\n\nThis program is based around the great BASS libraries\nby Ian Luck from Un4seen Developments and NativeBass by Jérôme Jouvie. Take a look at Ian's site by clicking the \"un4seen.com\" button and Jérôme's with the \"NativeBass website\" button. Source code for KMC available here:"));
		lblCopyright.setHorizontalAlignment(SwingConstants.LEFT);
		lblCopyright.setVerticalAlignment(SwingConstants.TOP);
		lblCopyright.setFont(new Font(KeppysMidiConverterPanel.font, Font.PLAIN, 11));
		lblCopyright.setBounds(212, 47, 316, 104);
		panel_1.add(lblCopyright);
		
		JButton btnSourceCode = new JButton("Source code on github");
		btnSourceCode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				KeppysMidiConverterPanel.openWebpage("https://github.com/89Mods/Keppy-s-MIDI-Converter-Multiplatform");
			}
		});
		btnSourceCode.setOpaque(false);
		btnSourceCode.setContentAreaFilled(false);
		btnSourceCode.setBorderPainted(false);
		btnSourceCode.setForeground(new Color(0,0,255));
		btnSourceCode.setFont(new Font(KeppysMidiConverterPanel.font, Font.PLAIN, 12));
		btnSourceCode.setBounds(212, 151, 290, 16);
		panel_1.add(btnSourceCode);
		
		JButton btnLicense = new JButton("License");
		btnLicense.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					ResourceExtractor.extractRecourceFromJar("/license.rtf", "license.rtf");
					Desktop.getDesktop().open(new File("license.rtf"));
				} catch(Exception e2){
					ErrorMessage.showErrorMessage(frame, "Error extracting/opening license", e2, false);
					e2.printStackTrace();
					return;
				}
			}
		});
		btnLicense.setFont(new Font(KeppysMidiConverterPanel.font, Font.PLAIN, 11));
		btnLicense.setBounds(219, 185, 102, 23);
		panel_1.add(btnLicense);
		
		JPanel panelJavainfo = new JPanel();
		panelJavainfo.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panelJavainfo.setBackground(Color.WHITE);
		panelJavainfo.setBounds(6, 214, 525, 58);
		panel_1.add(panelJavainfo);
		panelJavainfo.setLayout(null);
		
		JLabel lblJavaVersion = new JLabel("Java version: " + System.getProperty("java.version"));
		lblJavaVersion.setFont(new Font(KeppysMidiConverterPanel.font, Font.PLAIN, 11));
		lblJavaVersion.setBounds(4, 1, 510, 16);
		panelJavainfo.add(lblJavaVersion);
		
		/*
		 * Win versions:
		 * BASS version: 2.4.12.8
		 * BASSMIDI version: 2.4.9.30
		 * BASSenc version: 2.4.12.9
		 */
		/*
		 * Linux/Mac version:
		 * BASS version: 2.4.12.1
		 * BASSMIDI version: 2.4.9.0
		 * BASSenc version: 2.4.13.0
		 */
		
		JLabel lblA = new JLabel("BASS version: 2.4.12.8");
		lblA.setFont(new Font(KeppysMidiConverterPanel.font, Font.PLAIN, 11));
		lblA.setBounds(4, 14, 510, 16);
		panelJavainfo.add(lblA);
		
		JLabel lblB = new JLabel("BASSMIDI version: 2.4.9.30");
		lblB.setFont(new Font(KeppysMidiConverterPanel.font, Font.PLAIN, 11));
		lblB.setBounds(4, 28, 521, 16);
		panelJavainfo.add(lblB);
		
		JLabel lblC = new JLabel("BASSenc version: 2.4.12.9");
		lblC.setFont(new Font(KeppysMidiConverterPanel.font, Font.PLAIN, 11));
		lblC.setBounds(4, 42, 510, 16);
		panelJavainfo.add(lblC);
		
		JLabel lblNativebassVersion = new JLabel("NativeBass version: 1.1.2");
		lblNativebassVersion.setFont(new Font(KeppysMidiConverterPanel.font, Font.PLAIN, 11));
		lblNativebassVersion.setBounds(393, 1, 124, 16);
		panelJavainfo.add(lblNativebassVersion);
		
		JButton btnUnseencom = new JButton("un4seen.com");
		btnUnseencom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				KeppysMidiConverterPanel.openWebpage("http://www.un4seen.com/");
			}
		});
		btnUnseencom.setFont(new Font(KeppysMidiConverterPanel.font, Font.PLAIN, 11));
		btnUnseencom.setBounds(427, 185, 102, 23);
		panel_1.add(btnUnseencom);
		
		JButton btnNativeBassWebsite = new JButton("<html>Native Bass Website");
		btnNativeBassWebsite.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Desktop.getDesktop().browse(new URL("http://jerome.jouvie.free.fr/nativebass/index.php").toURI());
				} catch(Exception e2){
		        	ErrorMessage.showErrorMessage(frame, "Error opening webpage page", e2, false);
		        	e2.printStackTrace();
		        	return;
				}
			}
		});
		btnNativeBassWebsite.setFont(new Font(KeppysMidiConverterPanel.font, Font.PLAIN, 9));
		btnNativeBassWebsite.setBounds(323, 185, 102, 23);
		panel_1.add(btnNativeBassWebsite);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(Color.WHITE);
		tabbedPane.addTab("Updater", null, panel_2, null);
		panel_2.setLayout(null);
		
		JLabel lblImage = new JLabel("");
		lblImage.setIcon(new ImageIcon(Textures.updatebk));
		lblImage.setBounds(170, 78, 387, 206);
		panel_2.add(lblImage);
		
		JButton btnCheckForUpdates = new JButton("Check");
		btnCheckForUpdates.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int update = Updater.checkForUpdates();
				if(update == 1){
					int option = JOptionPane.showConfirmDialog(frame, "An new version is available to download. Would you like to download it now?", "Message", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if(option == 0){
						try {
							Desktop.getDesktop().browse(new URL("https://github.com/KaleidonKep99/Keppys-MIDI-Converter/releases").toURI());
						} catch(Exception e2){
				        	ErrorMessage.showErrorMessage(frame, "Error opening download page", e2, false);
				        	e2.printStackTrace();
				        	return;
						}
					}
				}else if(update == 2){
					JOptionPane.showMessageDialog(frame, "You have a newer version of the converter then the most recent available one. Strange, isnt it?");
				}else if(update == 0){
					JOptionPane.showMessageDialog(frame, "This release is allready updated.", "No updates found.", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		btnCheckForUpdates.setFont(new Font(KeppysMidiConverterPanel.font, Font.PLAIN, 12));
		btnCheckForUpdates.setBounds(425, 6, 106, 23);
		panel_2.add(btnCheckForUpdates);
		
		JLabel lblKmcUpdateChecker = new JLabel("KMC's Update Checker");
		lblKmcUpdateChecker.setFont(new Font(KeppysMidiConverterPanel.font, Font.BOLD, 20));
		lblKmcUpdateChecker.setBounds(5, 4, 220, 27);
		panel_2.add(lblKmcUpdateChecker);
		
		JLabel lblCurrentVersion = new JLabel("The current version of the driver, installed on your system, is: " + KeppysMidiConverter.VERSION);
		lblCurrentVersion.setFont(new Font(KeppysMidiConverterPanel.font, Font.PLAIN, 12));
		lblCurrentVersion.setBounds(7, 46, 381, 16);
		panel_2.add(lblCurrentVersion);
		
		JLabel lblClickOnCheck = new JLabel("Click on \"Check\" to check for the latest version of the converter.");
		lblClickOnCheck.setFont(new Font(KeppysMidiConverterPanel.font, Font.PLAIN, 12));
		lblClickOnCheck.setBounds(7, 62, 375, 16);
		panel_2.add(lblClickOnCheck);
		
		JButton btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		btnOk.setFont(new Font(KeppysMidiConverterPanel.font, Font.PLAIN, 12));
		btnOk.setBounds(460, 312, 75, 23);
		panel.add(btnOk);
		
		JButton btnDonate = new JButton("");
		btnDonate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
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
	            
	    		try {
	    			Desktop.getDesktop().browse(new URL(url).toURI());
	    		} catch(Exception e2){
	            	ErrorMessage.showErrorMessage(frame, "Error opening donation website", e2, false);
	            	e2.printStackTrace();
	            	return;
	    		}
			}
		});
		btnDonate.setIcon(new ImageIcon(Textures.donatebtn));
		btnDonate.setFont(new Font(KeppysMidiConverterPanel.font, Font.PLAIN, 12));
		btnDonate.setBounds(10, 310, 142, 27);
		panel.add(btnDonate);
		setResizable(false);
		
		pack();
	}
	
	private String convertToMultiline(String orig)
	{
	    return "<html>" + orig.replaceAll("\n", "<br>");
	}
}
