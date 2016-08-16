package keppy.midiConverter.GUI;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

import keppy.midiConverter.resources.Sounds;
import keppy.midiConverter.resources.Textures;

@SuppressWarnings("serial")
public class ErrorMessage extends JDialog {
	
	public ErrorMessage(JFrame frame, String title, String errorMessage, boolean fatal, boolean isWarning){
		super(frame, title);
		setLocationRelativeTo(frame);
		setPreferredSize(new Dimension(484, 210));
		setModal(true);
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		setFont(new Font(KeppysMidiConverterPanel.font, Font.PLAIN, 12));
		JPanel panel = new JPanel();
		panel.setLayout(null);
		setContentPane(panel);
		
		JLabel lblErroricon = new JLabel(isWarning ? new ImageIcon(Textures.warningicon) : new ImageIcon(Textures.eerroricon));
		lblErroricon.setBounds(12, 8, 48, 48);
		panel.add(lblErroricon);
		
		JLabel lblErrorDuring = new JLabel("<html>Error during the execution of the converter!<br>More information down below:");
		lblErrorDuring.setHorizontalAlignment(SwingConstants.LEFT);
		lblErrorDuring.setVerticalAlignment(SwingConstants.TOP);
		lblErrorDuring.setFont(new Font(KeppysMidiConverterPanel.font, Font.PLAIN, 12));
		lblErrorDuring.setBounds(68, 12, 251, 39);
		panel.add(lblErrorDuring);
		
		JButton btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(fatal){
					System.exit(1);
				}else{
					setVisible(false);
					dispose();
				}
			}
		});
		btnOk.setFont(new Font(KeppysMidiConverterPanel.font, Font.PLAIN, 12));
		btnOk.setBounds(381, 34, 75, 23);
		panel.add(btnOk);
		
		JTextPane textPane = new JTextPane();
		textPane.setText(errorMessage);
		textPane.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		textPane.setEditable(false);
		textPane.setBackground(SystemColor.control);
		
		JScrollPane listScroller = new JScrollPane(textPane);
		
		JPopupMenu popupMenu = new JPopupMenu();
		popupMenu.setFont(new Font(KeppysMidiConverterPanel.font, Font.PLAIN, 12));
		popupMenu.add(KeppysMidiConverterPanel.addMenuItem("Copy all text to clipboard", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				StringSelection stringSelection = new StringSelection(textPane.getText());
				Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
				clpbrd.setContents(stringSelection, null);
			}
		}, null));
		KeppysMidiConverterPanel.addPopup(textPane, popupMenu);
		listScroller.setBounds(12, 63, 444, 96);
		listScroller.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		listScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		getContentPane().add(listScroller);
		
		pack();
	}
	
	public void displayError(){
		if(Sounds.error != null){
			Sounds.playClip(Sounds.error);
		}
		setVisible(true);
	}
	
	public static void showErrorMessage(JFrame frame, String error, Exception e, boolean fatal){
		String errorMessage = "";
		StringWriter w2 = new StringWriter();
		PrintWriter w = new PrintWriter(w2);
		e.printStackTrace(w);
		errorMessage = w2.getBuffer().toString();
		new ErrorMessage(frame, "Error", error + ":\n\n" + errorMessage, fatal, false).displayError();
	}
	
	public static void showWarningMessage(JFrame frame, String error, Exception e, boolean fatal){
		String errorMessage = "";
		StringWriter w2 = new StringWriter();
		PrintWriter w = new PrintWriter(w2);
		e.printStackTrace(w);
		errorMessage = w2.getBuffer().toString();
		new ErrorMessage(frame, "Error", error + ":\n\n" + errorMessage, fatal, true).displayError();
	}
	
}
