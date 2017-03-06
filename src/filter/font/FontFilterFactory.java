package filter.font;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class FontFilterFactory {
	
	private static FontFilterFactory instance = null;
	private final int MAGENTA = Color.MAGENTA.getRGB();
	private final int WHITE   = Color.WHITE.getRGB();
	
	//TODO marcsieber: 05.03.2017 delete those
	private int height = 0;
	private int width = 0;
	private Font font = null;
	
	private final List<FontFilter> fontfilters = new ArrayList<>();
	
	protected FontFilterFactory() { }

	public static synchronized FontFilterFactory getInstance() {
		if (instance == null) {
			instance = new FontFilterFactory();
		}
		return instance;
	}
	
	//TODO marcsieber: 05.03.2017 allow single characters
	public FontFilterFactory setupFiltersFor(int width, int height, Font f, String sequence) {
		return setupFiltersFor(width, height, f, false, sequence);
	}
	
	// Synchronized --> so the task must be completed before the instance values gets changed
	public synchronized FontFilterFactory setupFiltersFor(int width, int height, Font f, boolean antialiased, String sequence) {
		this.width = width;
		this.height = height;
		this.font = f;
		
		this.fontfilters.addAll(createFontMap(sequence, antialiased));
		return this;
	}
	
	public FontFilter getFontFilterFor(int width, int height, Font f, String sequence) {
		return getFontFilterFor(width, height, f, false, sequence);
	}
	public FontFilter getFontFilterFor(int width, int height, Font f, boolean antialiased,String sequence) {
		for (FontFilter filter : this.fontfilters) {
			if (filter.getSequence().equals(sequence) && filter.getWidth() == width && filter.getHeight() == height 
					&& filter.getFont() == f) {
				return filter;
			}
		}
		setupFiltersFor(width, height, f, sequence);
		return getFontFilterFor(width, height, f, sequence);
	}

	private List<FontFilter> createFontMap(String s, boolean useAntialiasing){
		List<FontFilter> filters = new ArrayList<>();
		
		for (int i = 0; i <= s.length() -1; i++) {
			
			final List<Point> pixelsInLetter = new ArrayList<>();
			final List<Point> antialiasedEffect = new ArrayList<>();
			final char letter = s.charAt(i);
			final FontTile tile = createFontTile(letter, useAntialiasing);
			
			for (int x = 0; x < this.width; x++) {
				for (int y = 0; y < this.height; y++) {
					int color = tile.getImage().getRGB(x, y);
					
					if (color == this.WHITE) {
						continue;
					}
					
					Point point = new Point(x, y);
					if (color == this.MAGENTA) { 
						pixelsInLetter.add(point);
						
					} else {
						antialiasedEffect.add(point);
					}
				}
			}
			filters.add(new FontFilter(font, Character.toString(letter),width, height, pixelsInLetter, antialiasedEffect));
		}
		return filters;
	}
	
	/**
	 * Creates a new tile with a white background and magenta foreground.
	 * 
	 * @param c Character to print in the tile
	 * @param useAntialiasing When <code>true</code> a antialiased font is used
	 * @return
	 */
	private FontTile createFontTile(char c, boolean useAntialiasing) {
		BufferedImage tile = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < this.width; x++) {
			for (int y = 0; y < this.height; y++) {
				tile.setRGB(x, y, Color.WHITE.getRGB());
			}
		}
		Graphics g = tile.getGraphics();
		if (useAntialiasing) {
			Graphics2D g2d = (Graphics2D)g;
			g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
		}
		g.setFont(this.font);
		g.setColor(Color.MAGENTA);
		g.drawString(Character.toString(c), 0, this.height);
		
		return new FontTile(c, tile, useAntialiasing);
	}

}
