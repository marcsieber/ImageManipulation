package main;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import filter.BlurredFilter;
import filter.font.FontFilter;
import filter.font.FontFilterFactory;
import util.FileUtil;
import util.FontUtil;


//TODO marcsieber: 05.03.2017 add Ascii art
public class ImageManipulator {
	
	private static final int X0_PIXEL = 0; 			//X_Top left
	private static final int Y0_PIXEL = 0; 			//Y_Top left
	private static final int X1_PIXEL = 10;			//X_right_bottom
	private static final int Y1_PIXEL = X1_PIXEL;	 //Y_right_bottm

	private static int xOffset = 0;
	private static int yOffset = 0;
	
	private static int index = 0;
	
	private static final boolean USE_ANTIALIASING = false;
	//TODO marcsieber: 05.03.2017 use java path
	private static final String type = "jpg";
	private static final String  path = "";
	
	public static void main(String[] args) {
		BufferedImage image = FileUtil.loadImage(path + "." + type);
		
		Font font = FontUtil.getFontMatchingSpace(new Font("Courier New", Font.BOLD, 0), X1_PIXEL, Y1_PIXEL);
		FontFilterFactory fontFilterFactory = FontFilterFactory.getInstance();
		for (Character c : filterText(DEMO_TEXT)) {
			fontFilterFactory.setupFiltersFor(X1_PIXEL, Y1_PIXEL, font, USE_ANTIALIASING, Character.toString(c));
		}
		
		
		while (Y1_PIXEL + yOffset <= image.getHeight()) {
			while (X1_PIXEL + xOffset <= image.getWidth()) {
				BufferedImage subImage = image.getSubimage(X0_PIXEL + xOffset, Y0_PIXEL + yOffset, X1_PIXEL, Y1_PIXEL);
				
				//BlurredFilter
				BlurredFilter f1 = new BlurredFilter(X1_PIXEL, Y1_PIXEL);
				f1.setImage(subImage);
				f1.apply();
				
				// FontFilter
				FontFilter filter = fontFilterFactory.getFontFilterFor(X1_PIXEL, Y1_PIXEL, font, USE_ANTIALIASING, getLetter());
				filter.setImage(subImage);
				filter.apply(USE_ANTIALIASING);
				
				xOffset += X1_PIXEL;
			}
			xOffset = 0;
			yOffset += Y1_PIXEL;
		}
		
		FileUtil.saveImage(image, type, path + "_edited." + type);
		
		final BufferedImage i2 = image;
		JPanel imagePanel = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(i2, 0, 0, this);
			}
		};
		
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		imagePanel.setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
		frame.getContentPane().add(imagePanel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	private static List<Character> filterText(String s) {
		List<Character> list = new ArrayList<>();
		for (Character c : DEMO_TEXT.toCharArray()) {
			if (!list.contains(c)) {
				list.add(c);
			}
		}
		return list;
	}
	
	private static String getLetter() {
		if (index >= DEMO_TEXT.length()-1) {
			index = 0;
		}
		String s = Character.toString(DEMO_TEXT.charAt(index));
		if (s.equals(" ")) s = "*";
		index++;
		return s;
	}
	
	public static final String DEMO_TEXT =
			"Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore "
			+ "et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. "
			+ "Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor "
			+ "sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna "
			+ "aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita "
			+ "kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, "
			+ "consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam "
			+ "erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd "
			+ "gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Duis autem vel eum iriure dolor in "
			+ "hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at "
			+ "vero eros et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit augue duis "
			+ "dolore te feugait nulla facilisi. Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam "
			+ "nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat. Ut wisi enim ad minim "
			+ "veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat. "
			+ "Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore "
			+ "eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit praesent "
			+ "luptatum zzril delenit augue duis dolore te feugait nulla facilisi. Nam liber tempor cum soluta "
			+ "nobis eleifend option congue nihil imperdiet doming id quod mazim placerat facer possim assum. "
			+ "Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt "
			+ "ut laoreet dolore magna aliquam erat volutpat. Ut wisi enim ad minim veniam, quis nostrud exerci "
			+ "tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat. Duis autem vel eum "
			+ "iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat "
			+ "nulla facilisis. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, "
			+ "no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur "
			+ "sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, "
			+ "sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, "
			+ "no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur "
			+ "sadipscing elitr, At accusam aliquyam diam diam dolore dolores duo eirmod eos erat, et nonumy "
			+ "sed tempor et et invidunt justo labore Stet clita ea et gubergren, kasd magna no rebum. sanctus sea "
			+ "sed takimata ut vero voluptua. est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur "
			+ "sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat."
			+ "Consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna "
			+ "aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. "
			+ "Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. "
			+ "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore "
			+ "et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. "
			+ "Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit"
			+ " amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna "
			+ "aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. "
			+ "Stet clita kasd gubergren, no sea takimata sanctus. Lorem ipsum dolor sit amet, consetetur "
			+ "sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, "
			+ "sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, "
			+ "no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing"
			+ " elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua."
			+ " At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus"
			+ " est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy"
			+ " eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et "
			+ "accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est "
			+ "Lorem ipsum dolor sit amet. Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse"
			+ " molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto"
			+ " odio dignissim qui blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi."
			+ " Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt "
			+ "ut laoreet dolore magna aliquam erat volutpat. Ut wisi enim ad minim veniam, quis nostrud exerci tation"
			+ " ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat. Duis autem vel eum iriure "
			+ "dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla "
			+ "facilisis at vero eros et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril "
			+ "delenit augue duis dolore te feugait nulla facilisi. Nam liber tempor cum soluta nobis eleifend "
			+ "option congue nihil imperdiet doming id quod mazim placerat facer possim assum. Lorem ipsum "
			+ "dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet"
			+ " dolore magna aliquam erat volutpat. Ut wisi enim ad minim veniam, quis nostrud exerci tation "
			+ "ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo";

}
