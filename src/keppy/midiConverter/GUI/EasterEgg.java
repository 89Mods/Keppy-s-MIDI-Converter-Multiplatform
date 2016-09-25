package keppy.midiConverter.GUI;


import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import keppy.midiConverter.main.KeppysMidiConverter;
import keppy.midiConverter.resources.Textures;

@SuppressWarnings("serial")
public class EasterEgg extends JDialog {
	
	public EasterEgg(){
		super(KeppysMidiConverter.frame, "Wählt AFD!!!!");
		setLocationRelativeTo(KeppysMidiConverter.frame);
		this.getContentPane().setPreferredSize(new Dimension(500, 250));
		setModal(true);
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		this.getContentPane().setLayout(null);
		JLabel label = new JLabel(new ImageIcon(Textures.afd));
		label.setBounds(0, 0, 500, 250);
		add(label);
		
		pack();
		new QuestionThread(this);
		setVisible(true);
	}
	
	private class QuestionThread implements Runnable {
		
		private EasterEgg eg;
		
		public QuestionThread(EasterEgg eg){
			this.eg = eg;
			new Thread(this).start();
		}
		
		@Override
		public void run() {
			int result = JOptionPane.showConfirmDialog(eg, "<html>Wähl AFD!<br>~TGM", "Wichtig!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			if(result == 1){
				JOptionPane.showMessageDialog(eg, "<html>Gut so!<br>~TGM", "Wählt sie bitte nicht!", JOptionPane.INFORMATION_MESSAGE);
			}else{
				JOptionPane.showMessageDialog(eg, "<html>Das war ein Witz! WÄHLT DIE NICHT!<br>~TGM", "Wählt sie bitte nicht!", JOptionPane.ERROR_MESSAGE);
			}
			eg.setVisible(false);
			eg.dispose();
		}
		
	}
	
}
