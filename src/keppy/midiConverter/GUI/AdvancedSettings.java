package keppy.midiConverter.GUI;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.border.TitledBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/*
 * Advanced Settings dialog window
 */
@SuppressWarnings("serial")
public class AdvancedSettings extends JDialog {
	
	public JCheckBox chckbxOnlyRelease;
	public JComboBox<String> frequencyComboBox;
	public JCheckBox chckbxDisableSoundEffects;
	public JCheckBox chckbxOverrideMidiTempo;
	public JComboBox<String> bitrateComboBox;
	public JCheckBox chckbxForceConstantBitrate;
	public JSpinner tempoSpinner;
	public JCheckBox chckbxEnableTooltipGuide;
	//A Copy of this to be used inside ActionListeners
	private AdvancedSettings instance;
	
	/*
	 * Create dialog
	 */
	public AdvancedSettings(JFrame frame){
		super(frame, "Advanced settings");
		instance = this;
		setLocationRelativeTo(frame);
		setFont(new Font(KeppysMidiConverterPanel.font, Font.PLAIN, 12));
		JPanel panel = new JPanel();
		panel.setLayout(null);
		setContentPane(panel);
		
		//Save options when menu is closed
		JButton btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				KeppysMidiConverterPanel.saveOptions(instance);
			}
		});
		btnOk.setFont(new Font(KeppysMidiConverterPanel.font, Font.PLAIN, 12));
		btnOk.setBounds(313, 148, 79, 23);
		panel.add(btnOk);
		
		JPanel panel_1 = new JPanel();
		panel_1.setFont(new Font(KeppysMidiConverterPanel.font, Font.PLAIN, 12));
		panel_1.setBorder(new TitledBorder(null, "Settings", TitledBorder.LEADING, TitledBorder.TOP, new Font(KeppysMidiConverterPanel.font, Font.PLAIN, 12), null));
		panel_1.setBounds(5, 2, 394, 139);
		panel.add(panel_1);
		panel_1.setLayout(null);
		
		JLabel lblAudioFrequency = new JLabel("Audio frequency:");
		lblAudioFrequency.setFont(new Font(KeppysMidiConverterPanel.font, Font.PLAIN, 12));
		lblAudioFrequency.setBounds(7, 20, 90, 16);
		panel_1.add(lblAudioFrequency);
		
		JLabel lblHz = new JLabel("Hz");
		lblHz.setFont(new Font(KeppysMidiConverterPanel.font, Font.PLAIN, 12));
		lblHz.setBounds(162, 20, 19, 16);
		panel_1.add(lblHz);
		
		frequencyComboBox = new JComboBox<String>();
		frequencyComboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"192000", "176400", "142180", "96000", "88200", "74750", "66150", "50400", "50000", "48000", "47250", "44100", "44056", "37800", "32000", "22050", "16000", "11025", "8000", "4000"}));
		frequencyComboBox.setSelectedIndex(11);
		frequencyComboBox.setFont(new Font(KeppysMidiConverterPanel.font, Font.PLAIN, 11));
		frequencyComboBox.setBounds(96, 19, 66, 21);
		panel_1.add(frequencyComboBox);
		
		chckbxOnlyRelease = new JCheckBox("<html>Only release the oldest instance upon a note off event when there are overlapping instances of the note.");
		chckbxOnlyRelease.setFont(new Font(KeppysMidiConverterPanel.font, Font.PLAIN, 12));
		chckbxOnlyRelease.setBounds(7, 42, 353, 31);
		panel_1.add(chckbxOnlyRelease);
		
		chckbxDisableSoundEffects = new JCheckBox("Disable sound effects");
		chckbxDisableSoundEffects.setFont(new Font(KeppysMidiConverterPanel.font, Font.PLAIN, 12));
		chckbxDisableSoundEffects.setBounds(7, 72, 155, 18);
		panel_1.add(chckbxDisableSoundEffects);
		
		chckbxOverrideMidiTempo = new JCheckBox("Override MIDI tempo");
		tempoSpinner = new JSpinner();
		tempoSpinner.setEnabled(chckbxOverrideMidiTempo.isSelected());
		chckbxOverrideMidiTempo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tempoSpinner.setEnabled(chckbxOverrideMidiTempo.isSelected());
			}
		});
		chckbxOverrideMidiTempo.setFont(new Font(KeppysMidiConverterPanel.font, Font.PLAIN, 12));
		chckbxOverrideMidiTempo.setBounds(7, 95, 155, 18);
		panel_1.add(chckbxOverrideMidiTempo);
		
		chckbxForceConstantBitrate = new JCheckBox("Force constant bitrate (OGG Vorbis)");
		bitrateComboBox = new JComboBox<String>();
		bitrateComboBox.setFont(new Font(KeppysMidiConverterPanel.font, Font.PLAIN, 12));
		chckbxForceConstantBitrate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(chckbxForceConstantBitrate.isSelected()){
					bitrateComboBox.setEnabled(true);
				}else{
					bitrateComboBox.setEnabled(false);
				}
			}
		});
		chckbxForceConstantBitrate.setFont(new Font(KeppysMidiConverterPanel.font, Font.PLAIN, 12));
		chckbxForceConstantBitrate.setBounds(7, 117, 224, 18);
		panel_1.add(chckbxForceConstantBitrate);
		chckbxForceConstantBitrate.setVisible(true);
		
		JLabel lblNewTempoValue = new JLabel("New tempo value: ");
		lblNewTempoValue.setFont(new Font(KeppysMidiConverterPanel.font, Font.PLAIN, 12));
		lblNewTempoValue.setBounds(210, 97, 101, 13);
		panel_1.add(lblNewTempoValue);
		
		tempoSpinner.setFont(new Font(KeppysMidiConverterPanel.font, Font.PLAIN, 12));
		tempoSpinner.setModel(new SpinnerNumberModel(120, 1, 2097120, 1));
		tempoSpinner.setBounds(312, 95, 78, 21);
		panel_1.add(tempoSpinner);
		
		JLabel lblBitrate = new JLabel("Bitrate:");
		lblBitrate.setFont(new Font(KeppysMidiConverterPanel.font, Font.PLAIN, 12));
		lblBitrate.setBounds(253, 120, 43, 16);
		panel_1.add(lblBitrate);
		lblBitrate.setVisible(true);
		
		bitrateComboBox.setEnabled(chckbxForceConstantBitrate.isSelected());
		bitrateComboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"500", "480", "450", "320", "256", "192", "128", "96", "64"}));
		bitrateComboBox.setSelectedIndex(0);
		bitrateComboBox.setBounds(298, 116, 66, 21);
		panel_1.add(bitrateComboBox);
		bitrateComboBox.setVisible(true);
		
		JLabel lblKbps = new JLabel("kbps");
		lblKbps.setFont(new Font(KeppysMidiConverterPanel.font, Font.PLAIN, 12));
		lblKbps.setBounds(364, 120, 29, 16);
		panel_1.add(lblKbps);
		lblKbps.setVisible(true);
		
		chckbxEnableTooltipGuide = new JCheckBox("Enable Tooltip guide");
		chckbxEnableTooltipGuide.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(chckbxEnableTooltipGuide.isSelected()){
					enableTooltips();
				}else{
					disableTooltips();
				}
			}
		});
		chckbxEnableTooltipGuide.setFont(new Font(KeppysMidiConverterPanel.font, Font.PLAIN, 12));
		chckbxEnableTooltipGuide.setBounds(14, 152, 138, 17);
		panel.add(chckbxEnableTooltipGuide);
		setPreferredSize(new Dimension(420, 210));
		setModal(true);
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		
		setResizable(false);
		pack();
	}
	
	/*
	 * Enable the tooltip guide. This one is public so it can be accessed from the loadOptions function in KeppysMidiConverterPanel
	 */
	public void enableTooltips(){
		chckbxOnlyRelease.setToolTipText("There's really nothing to explain here");
		chckbxDisableSoundEffects.setToolTipText("Forces BASSMIDI to disable all the MIDI effects/external FXs (DX7 effects and MIDI chorus/reverb)");
		chckbxOverrideMidiTempo.setToolTipText("Override the MIDI tempo events. (WARNING, it'll override ALL the events, nothing excluded!)");
		chckbxForceConstantBitrate.setToolTipText("<html>Override the self-guided bitrate system used by Voris, and force it to use a fixed bitrate.<br>(1 = 64kbps~, 10 = 500/600kbps~)");
		bitrateComboBox.setToolTipText("<html>10 = Very High Quality<br>...<br>5 = Standart Quality<br>...<br>1 = Very Low Quality");
		tempoSpinner.setToolTipText("Set the tempo with a value from 1bpm to 2097120bpm.");
	}
	
	/*
	 * Disable the tooltip guide
	 */
	private void disableTooltips(){
		chckbxOnlyRelease.setToolTipText("");
		chckbxDisableSoundEffects.setToolTipText("");
		chckbxOverrideMidiTempo.setToolTipText("");
		chckbxForceConstantBitrate.setToolTipText("");
		bitrateComboBox.setToolTipText("");
		tempoSpinner.setToolTipText("");
	}
	
	/*
	 * Disable all components
	 */
	public void disableAll(){
		chckbxOnlyRelease.setEnabled(false);
		frequencyComboBox.setEnabled(false);
		chckbxDisableSoundEffects.setEnabled(false);
		chckbxOverrideMidiTempo.setEnabled(false);
		bitrateComboBox.setEnabled(false);
		chckbxForceConstantBitrate.setEnabled(false);
		tempoSpinner.setEnabled(false);
	}
	
	/*
	 * Enable all components
	 */
	public void enableAll(){
		chckbxOnlyRelease.setEnabled(true);
		frequencyComboBox.setEnabled(true);
		chckbxDisableSoundEffects.setEnabled(true);
		chckbxOverrideMidiTempo.setEnabled(true);
		bitrateComboBox.setEnabled(chckbxOverrideMidiTempo.isSelected());
		chckbxForceConstantBitrate.setEnabled(true);
		tempoSpinner.setEnabled(chckbxOverrideMidiTempo.isSelected());
		
	}
	
}