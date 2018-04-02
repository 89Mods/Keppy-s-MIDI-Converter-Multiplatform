package keppy.midiConverter.resources;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class Textures {
	
	public static BufferedImage donatebtn;
	public static BufferedImage eerroricon = null;
	public static BufferedImage heart;
	public static BufferedImage sfzcomp;
	public static BufferedImage updatebk;
	public static BufferedImage logo;
	public static BufferedImage warningicon = null;
	public static BufferedImage afd;
	public static BufferedImage webIcon;
	public static BufferedImage infoIcon;
	public static BufferedImage editIcon;
	public static BufferedImage deleteIcon;
	public static BufferedImage upIcon;
	public static BufferedImage downIcon;
	public static BufferedImage addIcon;
	public static BufferedImage removeIcon;
	public static BufferedImage backIcon;
	public static BufferedImage configureIcon;
	public static BufferedImage audioIcon;
	public static BufferedImage speakerIcon;
	
	public static void load() throws Exception {
		donatebtn = ImageIO.read(Textures.class.getResourceAsStream("/donatebtn.png"));
		eerroricon = ImageIO.read(Textures.class.getResourceAsStream("/erroricon.png"));
		heart = ImageIO.read(Textures.class.getResourceAsStream("/heart.png"));
		sfzcomp = ImageIO.read(Textures.class.getResourceAsStream("/sfzcomp.png"));
		updatebk = ImageIO.read(Textures.class.getResourceAsStream("/updatebk.png"));
		logo = ImageIO.read(Textures.class.getResourceAsStream("/mainlogo.png"));
		warningicon = ImageIO.read(Textures.class.getResourceAsStream("/warningicon.png"));
		afd = ImageIO.read(Textures.class.getResourceAsStream("/afd.png"));
		webIcon = ImageIO.read(Textures.class.getResourceAsStream("/worldwideweb.png"));
		infoIcon = ImageIO.read(Textures.class.getResourceAsStream("/information-icon.png"));
		editIcon = ImageIO.read(Textures.class.getResourceAsStream("/edit-icon.png"));
		deleteIcon = ImageIO.read(Textures.class.getResourceAsStream("/delete-icon.png"));
		upIcon = ImageIO.read(Textures.class.getResourceAsStream("/up-icon.png"));
		downIcon = ImageIO.read(Textures.class.getResourceAsStream("/down-icon.png"));
		addIcon = ImageIO.read(Textures.class.getResourceAsStream("/add-icon.png"));
		removeIcon = ImageIO.read(Textures.class.getResourceAsStream("/remove-icon.png"));
		backIcon = ImageIO.read(Textures.class.getResourceAsStream("/back-icon.png"));
		configureIcon = ImageIO.read(Textures.class.getResourceAsStream("/configure-icon.png"));
		audioIcon = ImageIO.read(Textures.class.getResourceAsStream("/audio-icon.png"));
		speakerIcon = ImageIO.read(Textures.class.getResourceAsStream("/speaker-icon.png"));
	}
	
}
