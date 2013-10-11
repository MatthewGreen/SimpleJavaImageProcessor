// @author - Matthew Green

import java.awt.*;
import java.awt.image.*;

/* An instance maintains a row-major order array of pixels for an image. */
public class ImageArray {
    /** DM provides methods for extracting components of an rgb pixel.*/
    public final static DirectColorModel DM= (DirectColorModel) ColorModel.getRGBdefault();
    
    private int rows;    	// number of rows in the image
    private int cols;    	// number of columns in the image
    private int[] rmoArr;   // The pixels of the image, in row-major order
    
    /** Constructor: An instance for image im with r rows and c cols.
     *  Precondition: im is an Image that is a .jpg (or .jpeg) file.              */
    public ImageArray(Image im, int r, int c) {
        rows= r;
        cols= c;
        rmoArr= new int[r*c];
        
        /* The constructor for Class PixelGrabber is given an image, 
         * as well as the rectangular section of the image it should grab 
         * (coordinates of top left corner and width and height),
         * and an int array rmoArray into which this image should be stored
         * (plus an offset into the array and the distance in pixels
         * between one row of pixels and the next).
         * 
         * Calling method grabPixels then causes the image to be stored
         * in one-dimensional array rmoArray --even though a picture is a
         * two-dimensional thing. The two-dimensional array of elements
         * is stored in rmoArray in row-major order.*/
        PixelGrabber pg=
            new PixelGrabber(im, 0, 0, c, r, rmoArr, 0, c);
        try {
            pg.grabPixels();
        }
        catch (InterruptedException e) {
            System.out.println("pixel grab interrupted!");
            return;
        } 
    }
    
    /** Constructor: An instance for an image with r rows and c cols and pixels
     *  (in row-major order) given by rmoa. A copy of rmoa is
     *  made: the argument array is different and remains unchanged.      */
    public ImageArray(int[] rmoa, int r, int c) {
        rows= r;
        cols= c;
        rmoArr= new int[r*c];
        
        // copy rmoa into rmoArray.
        // inv: items 0..i-1 of rmoa have been copied.
        for (int i= 0; i < rmoArr.length; i= i+1) {
            rmoArr[i]= rmoa[i];
        }
    }
    
    /** Yields: the number of rows. */
    public int getRows() {
        return rows;
    }
    
    /** Yields: the number of columns. */
    public int getCols() {
        return cols;
    }
    
    /** Yields: the length of the array */
    public int getLength() {
     return rmoArr.length;
    }
    
    /** Yields: the image (in row-major order). */
    public int[] getRmoArray() {
        return rmoArr;
    }
    
    /** Yields: a copy of this instance. */
    public ImageArray copy() {
        return new ImageArray(rmoArr, rows, cols);
    }
    
    /** Yields: the pixel value at [row, col] of the image. */
    public int getPixel(int row, int col) {
        return rmoArr[row*cols + col];
    }
    
    /** set the pixel value at [row, col] of the image to v. */
    public void setPixel(int row, int col, int v) {
        rmoArr[row*cols + col]= v;
    }
    
    /** swap the pixel at [a, b] with the pixel at [i, j]. */
    public void swapPixels(int a, int b, int i, int j) {
        int temp= getPixel(a, b);
        setPixel(a, b, getPixel(i, j));
        setPixel(i, j, temp);
    }
    
    /** Yields: pixel number p of the image (in row major order),
     *  with pixel number 0 being the first. */
    public int getPixel(int p) {
        return rmoArr[p];
    }
    
    /** Set pixel number p (in row major order) of the image to v. */
    public void setPixel(int p, int v) {
        rmoArr[p]= v;
    }
    
    /** Yields: pixel pix, in the form (red, green, blue), with 3 digits for each. 
     *  The alpha value is not printed. */
    public static String toString(int pix) {
        int red= DM.getRed(pix);
        int green=  DM.getGreen(pix);
        int blue=  DM.getBlue(pix);
        
        return "(" + to3(red) + ", " + to3(green) + ", " + to3(blue) + ")";
    }
    
    /** Yields: n but with at least 3 digits (prepend 0's if necessary).
     *  Precondition: n >= 0. */
    private static String to3(int n) {
        if (n < 10) return "00" + n;
        if (n < 100) return "0" + n;
        return "" + n;
    }
}

