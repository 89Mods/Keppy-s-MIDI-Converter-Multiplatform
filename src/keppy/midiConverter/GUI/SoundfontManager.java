package keppy.midiConverter.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import keppy.midiConverter.main.KeppysMidiConverter;
import keppy.midiConverter.resources.Textures;
import javax.swing.JPopupMenu;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/*
 * Soundfont manager dialog box
 */
@SuppressWarnings("serial")
public class SoundfontManager extends JDialog {
	
	public DefaultListModel<String> listModel;
	public static JFileChooser soundfontChooser;
	
	/*
	 * Generate window components
	 */
	public SoundfontManager(JFrame frame){
		super(frame, "Soundfonts manager");
		setLocationRelativeTo(frame);
		setFont(new Font(KeppysMidiConverterPanel.font, Font.PLAIN, 12));
		JPanel panel = new JPanel();
		panel.setLayout(null);
		this.setContentPane(panel);
		this.setPreferredSize(new Dimension(604, 468));
		setModal(true);
		setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		
		JLabel lblSfzimage = new JLabel("");
		lblSfzimage.setIcon(new ImageIcon(Textures.sfzcomp));
		lblSfzimage.setBounds(523, 57, 46, 37);
		panel.add(lblSfzimage);
		
		JLabel lblInfo = new JLabel(convertToMultiline("Use up to 1.000 soundfonts at once, to convert your MIDIs. \nThe first soundfont in the list will always override the latest ones.\nCheck if the soundfonts are not corrupted, before adding them to the list, to avoid random crashes/glitches.\nThe following formats are supported: SF2, SF3, SFZ, SFPACK"));
		lblInfo.setHorizontalAlignment(SwingConstants.LEFT);
		lblInfo.setVerticalAlignment(SwingConstants.TOP);
		lblInfo.setFont(new Font(KeppysMidiConverterPanel.font, Font.PLAIN, 12));
		lblInfo.setBounds(12, 9, 557, 87);
		panel.add(lblInfo);
		
		JList<String> list = new JList<String>();
		list.setFont(new Font(KeppysMidiConverterPanel.font, Font.PLAIN, 12));
		listModel = new DefaultListModel<String>();
		list.setModel(listModel);
		list.setBounds(12, 105, 572, 290);
		
		JScrollPane listScroller = new JScrollPane(list);
		
		JPopupMenu popupMenu = new JPopupMenu();
		popupMenu.add(KeppysMidiConverterPanel.addMenuItem("Import soundfont", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				importSoundfont();
			}
		}, null));
		popupMenu.add(KeppysMidiConverterPanel.addMenuItem("Remove soundfont", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(list.getSelectedIndex() >= 0 && list.getSelectedIndex() < listModel.size()){
					listModel.remove(list.getSelectedIndex());
				}
			}
		}, null));
		popupMenu.addSeparator();
		popupMenu.add(KeppysMidiConverterPanel.addMenuItem("Clear soundfont list", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listModel.clear();
			}
		}, null));
		popupMenu.setFont(new Font(KeppysMidiConverterPanel.font, Font.PLAIN, 12));
		addPopup(list, popupMenu);
		listScroller.setBounds(12, 105, 572, 290);
		listScroller.setBorder(new LineBorder(new Color(126, 180, 234)));
		listScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		getContentPane().add(listScroller);
		
		JButton btnOk = new JButton("OK");
		btnOk.setFont(new Font(KeppysMidiConverterPanel.font, Font.PLAIN, 12));
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		btnOk.setBounds(480, 404, 112, 23);
		panel.add(btnOk);
		
		JButton btnMoveDown = new JButton("Move Down \u25BC");
		btnMoveDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
	            if (listModel.size() > 0)
	            {
	                String selected = list.getSelectedValue();
	                int indx = list.getSelectedIndex();
	                int totl = listModel.size();
	                if (indx == totl - 1)
	                {
	                    listModel.remove(indx);
	                    listModel.insertElementAt(selected, 0);
	                    list.setSelectedIndex(0);
	                }
	                else
	                {
	                    listModel.remove(indx);
	                    listModel.insertElementAt(selected, indx + 1);
	                    list.setSelectedIndex(indx + 1);
	                }
	            }
			}
		});
		btnMoveDown.setFont(new Font(KeppysMidiConverterPanel.font, Font.PLAIN, 12));
		btnMoveDown.setBounds(363, 404, 112, 23);
		panel.add(btnMoveDown);
		
		JButton btnMoveUp = new JButton("Move Up \u25B2");
		btnMoveUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
	            if (listModel.size() > 0)
	            {
	                String selected = list.getSelectedValue();
	                int indx = list.getSelectedIndex();
	                int totl = listModel.size();
	                if (indx == 0)
	                {
	                    listModel.remove(indx);
	                    listModel.insertElementAt(selected, totl - 1);
	                    list.setSelectedIndex(totl - 1);
	                }
	                else
	                {
	                    listModel.remove(indx);
	                    listModel.insertElementAt(selected, indx - 1);
	                    list.setSelectedIndex(indx - 1);
	                }
	            }
			}
		});
		btnMoveUp.setFont(new Font(KeppysMidiConverterPanel.font, Font.PLAIN, 12));
		btnMoveUp.setBounds(246, 404, 112, 23);
		panel.add(btnMoveUp);
		
		soundfontChooser = new JFileChooser();
		soundfontChooser.setDialogTitle("Import a soundfont");
		soundfontChooser.setAcceptAllFileFilterUsed(false);
		soundfontChooser.setFileFilter(new FileNameExtensionFilter("Soundfont files", "sf2", "SF2", "sfz", "SFZ", "sf3", "SF3", "sfpack", "SFPACK"));
		
		JButton btnImport = new JButton("Import");
		btnImport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				importSoundfont();
			}
		});
		btnImport.setFont(new Font(KeppysMidiConverterPanel.font, Font.PLAIN, 12));
		btnImport.setBounds(12, 404, 112, 23);
		panel.add(btnImport);
		
		JButton btnRemove = new JButton("Remove");
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(list.getSelectedIndex() >= 0 && list.getSelectedIndex() < listModel.size()){
					listModel.remove(list.getSelectedIndex());
				}
			}
		});
		btnRemove.setFont(new Font(KeppysMidiConverterPanel.font, Font.PLAIN, 12));
		btnRemove.setBounds(129, 404, 112, 23);
		panel.add(btnRemove);
		
		setResizable(false);
		pack();
	}
	
	/*
	 * Select and add a soundfont to the list
	 */
	private void importSoundfont(){
		if(listModel.size() >= 1000) {
			new ErrorMessage(KeppysMidiConverter.frame, "Error", "You can't add more then 1.000 soundfonts", false, false);
			return;
		}
		int option = soundfontChooser.showOpenDialog(KeppysMidiConverter.frame);
		if(option == JFileChooser.APPROVE_OPTION){
			if(!soundfontChooser.getSelectedFile().exists()){
				ErrorMessage.showErrorMessageNoStackTrace(KeppysMidiConverter.frame, "The selected file doesnt exist", false);
				return;
			}
			if(soundfontChooser.getSelectedFile().getName().endsWith(".exe")){
				new ErrorMessage(KeppysMidiConverter.frame, "Rly?", "Are you seriously trying to add executables to the soundfont list?", false, false);
				return;
			}
			if(soundfontChooser.accept(soundfontChooser.getSelectedFile())){
				listModel.addElement(soundfontChooser.getSelectedFile().getPath());
			}else{
				ErrorMessage.showErrorMessageNoStackTrace(KeppysMidiConverter.frame, "That isnt a soundfont!", false);
				return;
			}
			KeppysMidiConverterPanel.sfSelectorDirectory = soundfontChooser.getCurrentDirectory().getPath();
			KeppysMidiConverterPanel.saveOptions(KeppysMidiConverterPanel.advancedSettings);
		}
	}
	
	/*
	 * Convert string to html multiline String
	 */
	private String convertToMultiline(String orig)
	{
	    return "<html>" + orig.replaceAll("\n", "<br>");
	}
	
	private static void addPopup(Component component, final JPopupMenu popup) {
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
