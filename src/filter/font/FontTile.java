package filter.font;

import java.awt.image.BufferedImage;

public class FontTile {
	
	private final Character character;
	private final BufferedImage image;
	private final boolean antialiased;
	
	
	public FontTile(Character character, BufferedImage image, boolean antialiased) {
		super();
		this.character = character;
		this.image = image;
		this.antialiased = antialiased;
	}
	
	public Character getCharacter() {
		return character;
	}
	
	public BufferedImage getImage() {
		return image;
	}
	
	public boolean isAntialiased() {
		return antialiased;
	}

}
