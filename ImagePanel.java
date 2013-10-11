// @author - Matthew Green

import java.awt.*;
import java.awt.image.*;
import javax.swing.*;


/** An instance is a JPanel that contains one image. Since it is
    a JPanel, it can be placed in a GUI. The system calls its method
    repaint whenever it is necessary to redraw the image.
   */
public class ImagePanel extends JPanel {
    
    private Image image;        // the image on the JPanel
 
    /** Constructor: a panel for image im with
     *  preferred size the size of im. */
    public ImagePanel(Image im) {
        image= im; 
        if (im == null)
            return;
        int rows= im.getHeight(this);
        int cols= im.getWidth(this);
        
        Dimension dim= new Dimension(cols, rows);
        setSize(dim);
        setPreferredSize(dim);
    }
       
    /** Change the image to the one given by m.
     *  Precondition: m != null.*/
    public void changeImageTo(ImageArray m) {
        int c= m.getCols();
        int r= m.getRows();
        
        /** In the below,
         * java.awt.image.MemoryImageSource.MemoryImageSource(int w, int h, int[] pix, int off, int scan)
         * constructs an ImageProducer object which uses an array of integers 
           in the default RGB ColorModel to produce data for an Image object.
        Parameters:
         w - the width of the rectangle of pixels
         h - the height of the rectangle of pixels
         pix - an array of pixels
         off - the offset into the array of where to store the first pixel
         scan - the distance from one row of pixels to the next in the array. */
        image= createImage(new MemoryImageSource(c, r, m.getRmoArray(), 0, c));
        
        Dimension dim= new Dimension(c, r);
        setPreferredSize(dim);
        setSize(dim);
    }
    
    /** Paint the image on this JPanel (ImagePanel). The system calls
     *  paint whenever it has to redraw this JPanel */
    public void paint(Graphics g) {
        setVisible(false);
        g.drawImage(image, 0, 0, this);
        setVisible(true);
    }
    
    public Image getImage() {
    	return image;
    }
}



