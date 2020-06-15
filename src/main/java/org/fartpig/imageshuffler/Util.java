package org.fartpig.imageshuffler;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public final class Util {
	
	public static void removeJpgMetaData(File imageFile) {
		BufferedImage image;
		try {
			image = ImageIO.read(imageFile);
			ImageIO.write(image, "jpg", imageFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
