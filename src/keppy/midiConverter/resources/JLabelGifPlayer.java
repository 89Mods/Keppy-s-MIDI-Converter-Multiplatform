package keppy.midiConverter.resources;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import keppy.midiConverter.GUI.ErrorMessage;
import keppy.midiConverter.main.KeppysMidiConverter;

public class JLabelGifPlayer implements Runnable {
	
	private boolean running;
	public Thread playThread;
	private int FPS;
	private JLabel component;
	private BufferedImage[] frames;
	private int counter = 0;
	
	public JLabelGifPlayer(int FPS, JLabel component, String gifLocation, boolean resource){
		this.FPS = FPS;
		this.component = component;
		try {
			ImageInputStream in ;
		if(resource){
			in = ImageIO.createImageInputStream(this.getClass().getResourceAsStream(gifLocation));
		}else{
			in = ImageIO.createImageInputStream(new File(gifLocation));
		}
		ImageReader reader = ImageIO.getImageReadersBySuffix("GIF").next();
		reader.setInput(in);
		int numImages = reader.getNumImages(true);
		frames = new BufferedImage[numImages];
		for (int i = 0, count = reader.getNumImages(true); i < count; i++)
		{
			BufferedImage image = reader.read(i);
			frames[i] = image;
		}
		} catch(Exception e){
			ErrorMessage.showErrorMessage(KeppysMidiConverter.frame, "Error loading gif", e, false);
			e.printStackTrace();
			return;
		}
	}
	
	@Override
	public void run() {
		long fpsTimer = System.nanoTime();
		long targetTime = 1000000000 / FPS;
		while(running){
			if(System.nanoTime() - fpsTimer >= targetTime){
				fpsTimer = System.nanoTime();
				updateFrame();
			}
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void updateFrame(){
		component.setIcon(new ImageIcon(frames[counter]));
		counter++;
		if(counter == frames.length){
			counter = 0;
		}
	}
	
	public void stop(){
		running = false;
	}
	
	public void start(){
		playThread = new Thread(this);
		running = true;
		playThread.start();
	}
	
}
