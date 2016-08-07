package keppy.midiConverter.resources;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class Textures {
	
	public static BufferedImage icon;
	public static BufferedImage donatebtn;
	public static BufferedImage eerroricon;
	public static BufferedImage heart;
	public static BufferedImage sfzcomp;
	public static BufferedImage updatebk;
	public static BufferedImage logo;
	
	public static void load() throws Exception {
		icon = ImageIO.read(Textures.class.getResourceAsStream("/icon.png"));
		donatebtn = ImageIO.read(Textures.class.getResourceAsStream("/donatebtn.png"));
		eerroricon = ImageIO.read(Textures.class.getResourceAsStream("/erroricon.png"));
		heart = ImageIO.read(Textures.class.getResourceAsStream("/heart.png"));
		sfzcomp = ImageIO.read(Textures.class.getResourceAsStream("/sfzcomp.png"));
		updatebk = ImageIO.read(Textures.class.getResourceAsStream("/updatebk.png"));
		logo = ImageIO.read(Textures.class.getResourceAsStream("/mainlogo.png"));
	}
	
}
