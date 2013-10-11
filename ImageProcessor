/* @author - Matthew Green
 * 
 * %-Hidden Message Format-%
 * If there is a message: yyy[message]~
 * If there is no message: nnn
 * 
 * Beginning marker: yyy or nnn
 * Encoding Length: Terminal Value '~'
 * Message starting point: Pixel 4
 * 
 * 
 */

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import javax.imageio.*;
import java.net.*;
import java.io.*;


/** An instance contains 
 * (1) an original image (of class ImageArray),
 * (2) a possibly altered (by methods in this instance) version of the original image,
 * (3) methods to process the image */
public class ImageProcessor {
    
    /** DM provides methods for extracting components of an rgb pixel. */
    public static final DirectColorModel DM= (DirectColorModel) ColorModel.getRGBdefault();
    
    /** The following constants of this class indicate a color for monochrome images.*/
    /** Color gray */
    public static final int GRAY= 0;
    
    /** Color sepia */
    public static final int SEPIA= 1;
    
    private ImageArray originalIm; // The original image, for restoration purposes
    private ImageArray currentIm;  // The altered image
    
    /** Constructor: an instance for im.
      Precondition: im != null. */
    public ImageProcessor(ImageArray im) {
        originalIm= im;
        currentIm= originalIm.copy();
    }
    
    /** Yields: the current image. */
    public ImageArray getCurrentImage() {
        return currentIm;
    }
    
    /** Yields: the original image. */
    public ImageArray getOriginalImage() {
        return originalIm;
    }
    
    /** Invert the current image, replacing each element with its color complement. */
    public void invert() {
        int len= currentIm.getRows() * currentIm.getCols();
        
        // invert all pixels (leave alpha/transparency value alone)
        
        // invariant: pixels 0..p-1 have been complemented.
        for (int p= 0; p < len; p= p+1) {
            int rgb= currentIm.getPixel(p);
            int red= 255 - DM.getRed(rgb);
            int blue= 255 - DM.getBlue(rgb);
            int green= 255 - DM.getGreen(rgb);
            int alpha= DM.getAlpha(rgb);
            currentIm.setPixel(p,
                               (alpha << 24) | (red << 16) | (green << 8) | blue);
        }
    }
    
    /** Transpose the current image.  */
    public void transpose() {
        // Follow this plan: 
        // (1) Create a new ImageArray ia, using currentIM's row-major order array
        //     and rows and columns, but swap the roles of its numbers
        //     of rows and columns.
        // (2) Store the transpose of the currentIm array in ia, using currentIm's
        //     2-parameter getPixel function and ia's 3-parameter setPixel
        //     function.
        // (3) assign ia to currentIm.
        int rows= currentIm.getRows();
        int cols= currentIm.getCols();
        ImageArray ia= new ImageArray(currentIm.getRmoArray(), cols, rows);
        
        //Copy each element ImageArray[r,c] to ia[c,r]
        // invariant: rows 0..r-1 have been copied to ia[.., 0..r-1]
        for (int r= 0; r != rows; r= r+1)
            // invariant: elements [r..0..c-1] have been copied to ia[0..c-1, r]
            for (int c= 0; c != cols; c= c+1) {
            ia.setPixel(c, r, currentIm.getPixel(r,c));
        }
        
        currentIm= ia;
    }
    
    /** Reflect the current image around the horizontal middle. */
    public void hreflect() {
        int rows= currentIm.getRows();
        int cols= currentIm.getCols();
        int h= 0;
        int k= rows-1;
        //invariant: rows 0..h-1 and k+1.. have been inverted
        while (h < k) {
            // Swap row h with row k
            // invariant: pixels 0..c-1 of rows h and k have been swapped
            for (int c= 0; c != cols; c= c+1) {
                currentIm.swapPixels(h, c, k, c);
            }
            
            h= h+1; k= k-1;
        }
    }
    
    
    /** Reflect the current image around the vertical middle. */
    public void vreflect() {
        int rows= currentIm.getRows();
        int cols= currentIm.getCols();
        int h= 0;
        int k= cols-1;
        //invariant: columns 0..h-1 and k+1.. have been inverted
        while (h < k) {
            // Swap column h with column k
            // invariant: pixels 0..c-1 of columns h and k have been swapped
            for (int c= 0; c != rows; c= c+1) {
                currentIm.swapPixels(c, h, c, k);
            }
            
            h= h+1; k= k-1;
        }
                
    }
    
    
    
    /** Put jail bars on the current image:
     * Put 3-pixel-wide horizontal bars across top and bottom,
     *  Put 4-pixel vertical bars down left and right, and
     *  Put n 4-pixel vertical bars inside, where n is (number of columns - 8) / 50.
     *  The n+2 vertical bars must be evenly spaced. */
    public void putInJail() {
        int rows= currentIm.getRows();
        int cols= currentIm.getCols();
        
        //Top Bar - Row 0
        drawHBar(0, 255, 0, 0);
        
        //Bottom Bar - Final Row-3 [Width of the line]
        drawHBar(rows-3, 255, 0, 0);
        
        //----------------------------------------------//
        
        //Leftmost Vertical Bar - Col 0
        drawVBar(0, 255, 0, 0);
        
        //Rightmost Vertical Bar - Final Col-4
        drawVBar(cols-4, 255, 0, 0);
        
        //---------------------------------------------//
        
        //Compute the number of bars needed
        int numBars = Math.round((cols-8)/50);
        //System.out.println("NumBars: " + numBars);
        
        //Compute the static spacing needed
        int spacingValue = ((cols-8)/(numBars+1));
        //System.out.println("SpacingValue: " + spacingValue);
        
        //Draw numBars number of vertical bars spacingValue apart
        int ii = 0;
        int loopingSpacer = spacingValue;
        
        while(ii < numBars){
            //System.out.println("LoopingSpacer is " + loopingSpacer + " Run Counter is: " + ii);
            drawVBar(loopingSpacer, 255, 0, 0);
            
            loopingSpacer = loopingSpacer + spacingValue;
            ii = ii + 1;
        }
        
    }
    
    
    /** Draw a horizontal 3-pixel-wide bar at row x of the current image
     *  using the color given by rgb components r, g, and b.
     *  Precondition: 0 <= x  &&  x+3 < currentIm.getRows() */
    private void drawHBar(int x, int r, int g, int b) {
        int rows= currentIm.getRows();
        int cols= currentIm.getCols();
        
        /* inv: pixels currentIm[x..x+3][0..c-1] are color c */
        for (int c= 0; c < cols; c= c+1) {
            int alpha= DM.getAlpha(currentIm.getPixel(x,c));
            currentIm.setPixel(x, c, (alpha << 24) | (r << 16) | (g << 8) | b);
            currentIm.setPixel(x+1, c, (alpha << 24) | (r << 16) | (g << 8) | b);
            currentIm.setPixel(x+2, c, (alpha << 24) | (r << 16) | (g << 8) | b);
        }
    }
    
    /** Draw a vertical 4-pixel-wide bar at col c of the current image
     *  using the color given by rgb components r, g, and b.
     *  Precondition: 0 <= c  &&  c+4 < currentIm.getCols() */
    private void drawVBar(int c, int r, int g, int b) {
        int rows= currentIm.getRows();
        int cols= currentIm.getCols();
        
        /* inv: pixels currentIm[c..c+4][0..x-1] are color c */
        for (int x= 0; x < rows; x= x+1) {
            
            int alpha= DM.getAlpha(currentIm.getPixel(x,c));
            currentIm.setPixel(x, c, (alpha << 24) | (r << 16) | (g << 8) | b);
            currentIm.setPixel(x, c+1, (alpha << 24) | (r << 16) | (g << 8) | b);
            currentIm.setPixel(x, c+2, (alpha << 24) | (r << 16) | (g << 8) | b);
            currentIm.setPixel(x, c+3, (alpha << 24) | (r << 16) | (g << 8) | b);
        }
    }
    
    /** Convert the current image to monochrome according to parameter c.
     *  Precondition: c is one of the two constants GRAY or SEPIA of this class.
     *  
     *  If c is GRAY, then remove all color from the image by setting the three
     *  color components of each pixel to that pixels overall brightness, defined as
     *  0.3 * red + 0.6 * green + 0.1 * blue.  If c is SEPIA, make the same 
     *  computation but set green to 0.6 * brighness and blue to 0.4 * brightness.
     *  
     *  The alpha component is not changed.   */
    public void monochromify(int c) {
        assert c == ImageProcessor.GRAY || c == ImageProcessor.SEPIA;
        
        if(c == GRAY){
            grayscale();
        }else{
            sepiaTone();
        }
        
        
    }
    
    /** Converts the image to grayscale by changing the RGB values of 
      * each pixel to the brightness. Brightness is computed by:
      * brightness = 0.3 * red + 0.6 * green + 0.1 * blue. */
    private void grayscale(){
        int len= currentIm.getRows() * currentIm.getCols();
        
        for (int p= 0; p < len; p= p+1) {
            int rgb= currentIm.getPixel(p);
            int red= DM.getRed(rgb);
            int blue= DM.getBlue(rgb);
            int green= DM.getGreen(rgb);
            int alpha= DM.getAlpha(rgb);
            
            double brightness = 0.3 * red + 0.6 * green + 0.1 * blue;
                
            currentIm.setPixel(p,
                               (alpha << 24) | ((int)brightness << 16) | ((int)brightness << 8) | (int)brightness);
        
        }
    }
    
    /** Converts the image to a sepia toned image by altering the green and 
      * blue channels of the RGB spectrum. Green's brightness value, which is 
      * computed by brightness = 0.3 * red + 0.6 * green + 0.1 * blue, is multiplied by 
      * 0.6 and the blue channel by 0.4. */
    private void sepiaTone(){
        int len= currentIm.getRows() * currentIm.getCols();
        
        for (int p= 0; p < len; p= p+1) {
            int rgb= currentIm.getPixel(p);
            int red= DM.getRed(rgb);
            int blue= DM.getBlue(rgb);
            int green= DM.getGreen(rgb);
            int alpha= DM.getAlpha(rgb);
            
            double brightness = 0.3 * red + 0.6 * green + 0.1 * blue;
                
            currentIm.setPixel(p,
                               (alpha << 24) | ((int)brightness << 16) | ((int)(brightness*0.6) << 8) | 
                               (int)(brightness*0.4));
        
        }
    }
    
    /** Simulate vignetting (corner darkening) characteristic of antique lenses. 
     *  Darken each pixel in the image by the factor
     * 
     *      (d / hfD)^2
     * 
     *  where d is the distance from the pixel to the center of the image and hfD (for
     *  half diagonal) is the distance from the center of the image to any of the corners.
     * 
     *  The alpha component is not changed.        */
    public void vignette() {
        int rows= currentIm.getRows();
        int cols= currentIm.getCols();
        
        int middleRow = rows/2; //Height of the right angle
        int middleCol = cols/2; //Width of the right angle
        double hfD = (Math.sqrt(Math.pow(rows,2)+Math.pow(cols,2)))/2;
        
        //Outer: Rows, Inner: Columns
        for(int rr = 0; rr < rows; rr++){
            
            for(int cc = 0; cc < cols; cc++){
                
                int rgb= currentIm.getPixel(rr,cc);
                double red= DM.getRed(rgb);
                double blue= DM.getBlue(rgb);
                double green= DM.getGreen(rgb);
                int alpha= DM.getAlpha(rgb);
                
                double currentDistance = Math.sqrt(Math.pow(Math.abs(middleRow-rr),2)+
                                                Math.pow(Math.abs(middleCol-cc),2));
                
                double vigValue = 1-Math.pow((currentDistance/hfD),2);
                
                red = red * vigValue;
                green = green * vigValue;
                blue = blue * vigValue;
                
                currentIm.setPixel(rr, cc,
                                   (alpha << 24) | ((int)red << 16) | ((int)green << 8) | 
                                   (int)blue);
            }
            
        }
        
    }
    
    
    /** Yields: a String that contains the first n pixels of the current image, 5 to a line,
     *  with annotation (i.e. something at the beginning to say what the string contains). */
    public String getPixels(int n) {
        //Hint: To have the string at some point begin a new line, put '\n' in it.
        //For example, put this in the interactions pane and see what happens: "ABCDE\nEFGH".
        
        //Instruction: Use function ImageArray.toString(int) to get the string representation of a pixel.
        
        String result = "pixels of current image:\n ";
        
        for(int ii = 1; ii <= n; ii++){
            
            
            if(ii%5 == 0 && ii != 0){
                result = result + ImageArray.toString(currentIm.getPixel(ii-1)) + "\n ";
            }else{
                result = result + ImageArray.toString(currentIm.getPixel(ii-1)) + " ";
            }
            
        }    
            
        return result.trim();
    }
    
    /** Hide message m in this image, using the ascii representation of m's chars.
     *  Return true if this is possible and false if not.
     *  If m has more than 999999 characters or the picture doesn't have enough
     *  pixels, return false without storing the message.    */
    public boolean hide(String m) {
        int maxPix = currentIm.getRows() * currentIm.getCols();
        
        
        if(m.length() > 999999 || m.length() <= 0 || m.length() > maxPix || m.length()+3 > maxPix+3){
             //Set message to nnn as No.
            for(int ii = 0; ii < 4; ii = ii + 1){
                  int rgb= currentIm.getPixel(0, ii);
                  double red= DM.getRed(rgb);
                  double blue= DM.getBlue(rgb);
                  double green= DM.getGreen(rgb);
                  int alpha= DM.getAlpha(rgb);
                  
                  //Covert values to char
                  int charValue = (int)'n';
                  int msgChar1 = charValue/100;
                  
                  int msgChar2 = 0;
                  if(charValue > 99){
                      msgChar2 = (charValue-100)/10;
                  }else{
                      msgChar2 = (charValue)/10;
                  }
                                 
                  int msgChar3 = charValue%10;
                  
                  
                  //Check the current values versus the upper limit which is 255 and then reduce by 10 where
                  //appropriate and set the end values to zero.
                  red = readyPixel(red);
                  blue = readyPixel(blue);
                  green = readyPixel(green);
                  
                  //Actually hide the characters now.
                  red = red + msgChar1;
                  green = green + msgChar2;
                  blue = blue + msgChar3;
                  
                  currentIm.setPixel(0, ii,
                                   (alpha << 24) | ((int)red << 16) | ((int)green << 8) | 
                                   (int)blue);
             }
             return false;
        }else{
             //Encode That A Message Is Present
             //Use yyy as Yes.
             for(int ii = 0; ii < 3; ii = ii + 1){
                  int rgb= currentIm.getPixel(0, ii);
                  double red= DM.getRed(rgb);
                  double blue= DM.getBlue(rgb);
                  double green= DM.getGreen(rgb);
                  int alpha= DM.getAlpha(rgb);
                  
                  //Covert values to char
                  int charValue = (int)'y';
                  int msgChar1 = charValue/100;
                  
                  int msgChar2 = 0;
                  if(charValue > 99){
                      msgChar2 = (charValue-100)/10;
                  }else{
                      msgChar2 = (charValue)/10;
                  }
                                 
                  int msgChar3 = charValue%10;
                  
                  
                  //Check the current values versus the upper limit which is 255 and then reduce by 10 where
                  //appropriate and set the end values to zero.
                  red = readyPixel(red);
                  blue = readyPixel(blue);
                  green = readyPixel(green);
                  
                  //Actually hide the characters now.
                  red = red + msgChar1;
                  green = green + msgChar2;
                  blue = blue + msgChar3;
                  
                  currentIm.setPixel(0, ii,
                                   (alpha << 24) | ((int)red << 16) | ((int)green << 8) | 
                                   (int)blue);
             }
             
             //Loop through the entire message and encode it.
             for(int ii = 3; ii < (3+m.length()); ii = ii + 1){
                 
                 int rgb= currentIm.getPixel(0, ii);
                 double red= DM.getRed(rgb);
                 double blue= DM.getBlue(rgb);
                 double green= DM.getGreen(rgb);
                 int alpha= DM.getAlpha(rgb);
                 
                 //Check the current values versus the upper limit which is 255 and then reduce by 10 where
                 //appropriate and set the end values to zero.
                 red = readyPixel(red);
                 blue = readyPixel(blue);
                 green = readyPixel(green);
                 
                 if(ii < 3 + m.length()){
                     //Covert values to char
                     int charValue = (int)(m.charAt(ii-3));
                     int msgChar1 = charValue/100;
                  
                     int msgChar2 = 0;
                     if(charValue > 99){
                         msgChar2 = (charValue-100)/10;
                     }else{
                         msgChar2 = (charValue)/10;
                     }
                     
                     int msgChar3 = charValue%10;
                     
                     //Actually hide the characters now.
                     red = red + msgChar1;
                     green = green + msgChar2;
                     blue = blue + msgChar3;
                     
                     currentIm.setPixel(ii,
                                        (alpha << 24) | ((int)red << 16) | ((int)green << 8) | 
                                        (int)blue);
                 }
             }
                 
                 //Insert the terminal values '~'
                 for(int ii = (3+m.length()); ii < ((3+m.length())+1); ii = ii +1){
                     int rgb= currentIm.getPixel(0, ii);
                     double red= DM.getRed(rgb);
                     double blue= DM.getBlue(rgb);
                     double green= DM.getGreen(rgb);
                     int alpha= DM.getAlpha(rgb);
                     
                     //Check the current values versus the upper limit which is 255 and then reduce by 10 where
                     //appropriate and set the end values to zero.
                     red = readyPixel(red);
                     blue = readyPixel(blue);
                     green = readyPixel(green);
                     
                     if(ii == (3+m.length())){
                         int charValue = (int)'~';
                         int msgChar1 = charValue/100;
                  
                         int msgChar2 = 0;
                         if(charValue > 99){
                             msgChar2 = (charValue-100)/10;
                         }else{
                             msgChar2 = (charValue)/10;
                         }
                         
                         int msgChar3 = charValue%10;
                         
                         red = red + msgChar1;
                         green = green + msgChar2;
                         blue = blue + msgChar3;
                     
                         currentIm.setPixel(ii,
                                        (alpha << 24) | ((int)red << 16) | ((int)green << 8) | 
                                        (int)blue);
                     }
                     
                     
                 }//Next FOR       
             }//Close Else
            return true;
        }//End Hide
    
    /** Yields: a number n representing a pixel component that has been modified to accept
      * a number being added in the least significant spot i.e. 20x where x is the least 
      * significant spot and is modfied in such a way that any value added to it cannot exceed 255. */
    private double readyPixel(double pxc){
        if((pxc-(pxc%10)+9)>255){
            pxc = pxc -10;
        }
        
        if(pxc > 10 && pxc < 256){
            pxc = (pxc-(pxc%10));
        }else{
            pxc = 0;
        }
        
        return pxc;
    }
    
    /** Yields: the number n that is hidden in pixel p of the current image. */
    public int getHidden(int p) {
        int rgb= currentIm.getPixel(p);
        int red= DM.getRed(rgb);
        int green= DM.getGreen(rgb);
        int blue= DM.getBlue(rgb);
        return  (red % 10) * 100  +  (green % 10) * 10  +  blue % 10;
    }
    
    /** Extract and return the message hidden in the current image.
     *  Return null if no message detected. */
    public String reveal1() {
       // Note: You do NOT have to write this method.
        
        
        return null;
    }
    
    /** Extract and return the message hidden in the current image.
     *  Return null if no message detected. */
    public String reveal() {      
        String message = "";
        
        if(verifyMessagePresence()){
            for(int ii = 3; ii < terminalLocation(); ii = ii + 1){
                message = message + (char)(getHidden(ii));
            }
        }else{
            return null;
        }
        return message;
    }
    
    /** Yields: true if the image contains a message and false if it does not. */
    private boolean verifyMessagePresence(){
        boolean overallResult = true;
        
        for(int ii = 0; ii < 3; ii = ii +1){
            if(121 == getHidden(ii)){
                overallResult = true;
            }else{
                overallResult = false;
            }
        }
        
        return overallResult;
    }
    
    /** Yields: The location of the terminal value '~' as an int in the image. */
    private int terminalLocation(){
        int answer = 0;
        for(int ii = 0; ii < (currentIm.getRows() * currentIm.getCols()); ii = ii + 1){
            if(126 == getHidden(ii)){
                return answer = ii;
            }
        }
        
        return answer;
    }
    
    /** Change the current image so that every pixel that is not on one of 
     *  the four edges of the image is replaced with the average of its  
     * current value and the current values of its eight neighboring pixels. */
    public void fuzzify() {
        ImageArray newCopy = currentIm.copy();
        
        int rows= currentIm.getRows();
        int cols= currentIm.getCols();
        
        for(int rr = 1; rr < rows-1; rr++){
            
            for(int cc = 1; cc < cols-1; cc++){
                fuzPixel(rr,cc,newCopy);
                
            }
            
        }
        
        currentIm = newCopy.copy();
        
        
    }
    
    /** Modifies a pixel value given by row rPix and column cPix by the average of itself and its
      * 8 neighbors. This method calls for a copy of the orginal image iCopy
      * so that the computed pixel values are not skewed by the changes being made. */
    private void fuzPixel(int rPix, int cPix, ImageArray iCopy){
        
        /*int rgb= currentIm.getPixel(rPix,cPix);
        double red= DM.getRed(rgb);
        double blue= DM.getBlue(rgb);
        double green= DM.getGreen(rgb);
        int alpha= DM.getAlpha(rgb);*/
        
        int rgb = 0;
        double red = 0.0;
        double blue = 0.0;
        double green = 0.0;
        int alpha = 0;
                
        for(int rr = rPix-1; rr < rPix+2; rr++){
            
            for(int cc = cPix-1; cc <cPix+2; cc++){
                rgb = currentIm.getPixel(rr,cc);
                red = red + DM.getRed(rgb);
                blue = blue + DM.getBlue(rgb);
                green = green + DM.getGreen(rgb);
                alpha= alpha + DM.getAlpha(rgb);
                
            }
            
        }
        
        red = red/9;
        blue = blue/9;
        green = green/9;
        
        iCopy.setPixel(rPix, cPix,
                                   (alpha << 24) | ((int)red << 16) | ((int)green << 8) | 
                                   (int)blue);
    }

 // HELPER METHODS    
    
    /** Assuming the image is broken up into blocks, with nr rows
     *  and nc columns of blocks, set all pixels of the
     *  block at position (row, col) to pixel value pixel.
     *  Precondition: 0 ² row < nr  and  0 ² col < nc */
    public void setBlock(int nr, int nc, int row, int col, int pixel) {
        int height= currentIm.getRows() / nr;
        int width= currentIm.getCols() / nc;
        for (int r= 0; r < height; r= r+1) {
            for (int c= 0; c < width; c= c+1) {
                currentIm.setPixel(row*height+r, col*width+c, pixel);
            }
        }
    }
    
    /** Assuming the image is broken up into blocks, with nr rows
     *  of blocks and nc columns of blocks, swap all pixels of
     *  the blocks at positions (row0, col0) and (row1, col1). */
    public void swapBlocks(int row0, int col0, int row1, int col1, int nr, int nc) {
        int rows= currentIm.getRows();
        int cols= currentIm.getCols();
        int ht= rows/nr;
        int wd= cols/nc;
        for (int r= 0; r < ht; r= r+1) {
            for (int c= 0; c < wd; c= c+1) {
                currentIm.swapPixels(row0*ht+r, col0*wd+c, row1*ht+r, col1*wd+c);
            }
        }
    }
    
    /** Restore the original image in the current one */
    public void restore() {
        currentIm= originalIm.copy();
    }
    
    /** Provided file fname does not appear in the current directory, store the
     *  current image in file fname in the current directory, as a png file.
     *  Write a message on the console indicating whether or not the write was successful.
     *  The png format is lossless, so if the image has a hidden message, it will indeed be
     *  saved in the file. The jpg format is lossy, and saving it can actually change pixels,
     *  so the message woudl generally be lost.*/
    public void writeImage(String fname) throws java.io.IOException {
        System.out.println("Writing the image.");
        
        File f= new File(fname);
        if (f.exists()) {
            System.out.println("File " + f.getAbsolutePath() + " exists. It was not overwritten.");
            return;
        }
        
        int r= currentIm.getRows();
        int c= currentIm.getCols();
        int roa[]= currentIm.getRmoArray();
        
        // Obtain a buffered image with the right size and format to save out this image
        // (only the RGB components, not alpha).
        BufferedImage bimage= new BufferedImage(c, r, BufferedImage.TYPE_INT_RGB);
        
        // Copy the image data into that BufferedImage.
        bimage.setRGB(0, 0, c, r, roa, 0, c);
        
        // Finally, write the image onto the file and give the appropriate message
        ImageIO.write(bimage, "png", f);
        System.out.println("Image written to " + f.getAbsolutePath());
    }  
    
    
    
}
