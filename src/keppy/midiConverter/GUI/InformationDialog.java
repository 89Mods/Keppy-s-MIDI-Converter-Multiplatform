package keppy.midiConverter.GUI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import keppy.midiConverter.main.KeppysMidiConverter;
import keppy.midiConverter.main.Updater;
import keppy.midiConverter.resources.ResourceExtractor;
import keppy.midiConverter.resources.Textures;

@SuppressWarnings("serial")
public class InformationDialog extends JDialog implements MouseListener, KeyListener {
	
	public InformationDialog(JFrame frame){
		super(frame, "Information");
		setModal(true);
		setLocationRelativeTo(frame);
		setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(545, 295));
		this.setContentPane(panel);
		panel.setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setFont(new Font(KeppysMidiConverterPanel.font, Font.PLAIN, 12));
		tabbedPane.setBounds(1, 1, 545, 254);
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
		
		JLabel lblCopyright = new JLabel(convertToMultiline("Copyright(C) 2013-2017, KaleidonKep99\n\nThis program is based around the great BASS libraries\nby Ian Luck from Un4seen Developments and NativeBass by Jérôme Jouvie. Take a look at Ian's site by clicking the \"un4seen.com\" button and Jérôme's with the \"NativeBass website\" button. Source code available here:"));
		lblCopyright.setHorizontalAlignment(SwingConstants.LEFT);
		lblCopyright.setVerticalAlignment(SwingConstants.TOP);
		lblCopyright.setFont(new Font(KeppysMidiConverterPanel.font, Font.PLAIN, 11));
		lblCopyright.setBounds(212, 47, 316, 104);
		panel_1.add(lblCopyright);
		
		JButton btnSourceCode = new JButton("https://github.com/89Mods/Keppy-s-MIDI-Converter-Multiplatform");
		btnSourceCode.setHorizontalAlignment(SwingConstants.LEFT);
		btnSourceCode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				KeppysMidiConverterPanel.openWebpage("https://github.com/89Mods/Keppy-s-MIDI-Converter-Multiplatform");
			}
		});
		btnSourceCode.setOpaque(false);
		btnSourceCode.setContentAreaFilled(false);
		btnSourceCode.setBorderPainted(false);
		btnSourceCode.setForeground(new Color(0,0,255));
		btnSourceCode.setFont(new Font(KeppysMidiConverterPanel.font, Font.PLAIN, 10));
		btnSourceCode.setBounds(216, 151, 337, 16);
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
		
		JButton btnUnseencom = new JButton("Un4seen site");
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
		        	ErrorMessage.showErrorMessage(frame, "Error opening a webpage", e2, false);
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
		tabbedPane.addTab("Updates", null, panel_2, null);
		panel_2.setLayout(null);
		
		JLabel lblImage = new JLabel("");
		lblImage.setIcon(new ImageIcon(Textures.updatebk));
		lblImage.setBounds(-20, -40, 555, 270);
		panel_2.add(lblImage);
		
		JLabel lblKmcUpdateChecker = new JLabel("KMC's Update Checker");
		lblKmcUpdateChecker.setFont(new Font(KeppysMidiConverterPanel.font, Font.PLAIN, 20));
		lblKmcUpdateChecker.setBounds(5, 4, 220, 27);
		panel_2.add(lblKmcUpdateChecker);
		
		JLabel lblCurrentVersion = new JLabel("The current version of the converter, installed on your system, is: " + KeppysMidiConverter.VERSION);
		lblCurrentVersion.setFont(new Font(KeppysMidiConverterPanel.font, Font.PLAIN, 12));
		lblCurrentVersion.setBounds(7, 46, 416, 16);
		panel_2.add(lblCurrentVersion);
		
		JLabel lblClickOnCheck = new JLabel("Click on \"Check\" to check for the latest version of the converter.");
		lblClickOnCheck.setFont(new Font(KeppysMidiConverterPanel.font, Font.PLAIN, 12));
		lblClickOnCheck.setBounds(7, 62, 375, 16);
		panel_2.add(lblClickOnCheck);
		
		JButton btnCheckForUpdates = new JButton("Check for updates");
		btnCheckForUpdates.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int update = Updater.checkForUpdates();
				if(update == 1){
					lblClickOnCheck.setText("A new version is available: " + Integer.toString(Updater.newMajor) + "." + Integer.toString(Updater.newMinor) + "." + Integer.toString(Updater.newPatch));
					int option = JOptionPane.showConfirmDialog(frame, "An new version is available to download. Would you like to download it now?", "Message", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if(option == 0){
						KeppysMidiConverterPanel.openWebpage("https://github.com/89Mods/Keppy-s-MIDI-Converter-Multiplatform/releases");
					}
				}else if(update == 2){
					lblClickOnCheck.setText("You have a newer version. Somehow.");
					JOptionPane.showMessageDialog(frame, "You have a newer version of the converter then the most recent available one. Strange, isnt it?", "You dirty hacker!", JOptionPane.WARNING_MESSAGE);
				}else if(update == 0){
					lblClickOnCheck.setText("No updates have been found. Please try later.");
					JOptionPane.showMessageDialog(frame, "No updates have been found. Please try again later.", "No updates found.", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		btnCheckForUpdates.setFont(new Font(KeppysMidiConverterPanel.font, Font.PLAIN, 11));
		btnCheckForUpdates.setBounds(402, 4, 128, 21);
		panel_2.add(btnCheckForUpdates);
		
		JButton btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		btnOk.setFont(new Font(KeppysMidiConverterPanel.font, Font.PLAIN, 12));
		btnOk.setBounds(460, 262, 75, 23);
		panel.add(btnOk);
		
		JButton btnDonate = new JButton("Donate with PayPal");
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
	            
	    		KeppysMidiConverterPanel.openWebpage(url);
			}
		});
		//btnDonate.setIcon(new ImageIcon(Textures.donatebtn));
		btnDonate.setFont(new Font(KeppysMidiConverterPanel.font, Font.PLAIN, 12));
		btnDonate.setBounds(10, 260, 142, 27);
		panel.add(btnDonate);
		setResizable(false);
		
		panel_1.addMouseListener(this);
		addKeyListener(this);
		for(Component c:getComponents()){
			c.addKeyListener(this);
		}
		for(Component c:panel.getComponents()){
			c.addKeyListener(this);
		}
		for(Component c:panel_1.getComponents()){
			c.addKeyListener(this);
		}
		
		pack();
	}
	
	private String convertToMultiline(String orig)
	{
	    return "<html>" + orig.replaceAll("\n", "<br>");
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		
	}
	
	@Override
	public void mouseEntered(MouseEvent arg0) {
		
	}
	
	@Override
	public void mouseExited(MouseEvent arg0) {
		
	}
	
	@Override
	public void mousePressed(MouseEvent arg0) {
		
	}
	
	@Override
	public void mouseReleased(MouseEvent arg0) {
		if(arg0.getX() >= 267 && arg0.getX() <= 289){
			if(arg0.getY() >= 10 && arg0.getY() <= 20){
				new EasterEgg();
			}
		}
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_F1){
			setVisible(false);
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		
	}
}
