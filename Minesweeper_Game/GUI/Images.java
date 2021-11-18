/*----------------------------------------------------------------
 *  Author:   K. Walsh
 *  Email:    kwalsh@holycross.edu
 *  Written:  7/13/2015
 *  
 *  A simple Graphical User Interface package.
 *----------------------------------------------------------------*/

package GUI;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.net.URL;
import javax.imageio.*;
import javax.swing.*;

/**
 * The <i>Images</i> class provides functions for loading and saving image
 * files.
 */
public class Images {

    /**
     * Load a java.awt.Image from the given location. The location can be a file
     * path like "flower.png" or "../projects/images/ball.gif". The location can
     * also be a URL like "http://example.com/some/picture.jpg". Supported image
     * file formats include ".png", ".jgp", and ".gif".
     * @throws RuntimeException if the image could not be loaded for any reason.
     */
    public static Image load(String location) {
        String suffix = location.substring(location.lastIndexOf('.') + 1);
        if (!suffix.toLowerCase().equals("png") &&
                !suffix.toLowerCase().equals("gif") &&
                !suffix.toLowerCase().equals("jpg")) {
            throw new RuntimeException(
                    "Image format of " + location +
                    " is not supported. Use a .gif, .jpg or .png image instead.");
                }

        // try to read from file
        ImageIcon icon = new ImageIcon(location);

        // try to read from URL
        if ((icon == null) || (icon.getImageLoadStatus() != MediaTracker.COMPLETE)) {
            try {
                URL url = new URL(location);
                icon = new ImageIcon(url);
            } catch (Exception e) { /* not a url */ }
        }

        // in case file is inside a .jar
        if ((icon == null) || (icon.getImageLoadStatus() != MediaTracker.COMPLETE)) {
            URL url = Canvas.class.getResource(location);
            if (url == null)
                throw new RuntimeException("image " + location + " not found");
            icon = new ImageIcon(url);
        }

        if (icon == null)
            throw new RuntimeException("image " + location + " is corrupt");

        Image image = icon.getImage();
        if (image == null)
            throw new RuntimeException("image " + location + " is corrupt");

        int w = image.getWidth(null);
        int h = image.getHeight(null);
        if (w <= 0 || h <= 0)
            throw new RuntimeException("image " + location + " is corrupt");

        return image;
    }

    /**
     * Save an image to a file. The suffix must be ".png" or ".jpg".
     * @param image the image to save.
     * @param filename the path of the file in which to save the image.
     * @throws IOException if the image could not be saved for any reason.
     */
    public static void save(BufferedImage image, String filename) throws IOException {
        File file = new File(filename);
        String suffix = filename.substring(filename.lastIndexOf('.') + 1);

        if (suffix.toLowerCase().equals("png")) {
            ImageIO.write(image, suffix, file);
        } else if (suffix.toLowerCase().equals("jpg")) {
            // need to change from ARGB to RGB for jpeg
            // reference: http://archives.java.sun.com/cgi-bin/wa?A2=ind0404&L=java2d-interest&D=0&P=2727
            WritableRaster raster = image.getRaster();
            int width = image.getWidth(null);
            int height = image.getHeight(null);
            WritableRaster newRaster;
            newRaster = raster.createWritableChild(0, 0, width, height, 0, 0, new int[] {0, 1, 2});
            DirectColorModel cm = (DirectColorModel) image.getColorModel();
            DirectColorModel newCM = new DirectColorModel(cm.getPixelSize(),
                    cm.getRedMask(),
                    cm.getGreenMask(),
                    cm.getBlueMask());
            BufferedImage rgbBuffer = new BufferedImage(newCM, newRaster, false,  null);
            ImageIO.write(rgbBuffer, suffix, file);
        } else {
            throw new IOException("Image file type not supported: " + suffix);
        }
    }

}
