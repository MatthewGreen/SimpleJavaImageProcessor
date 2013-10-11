// @author - Matthew Green

import java.awt.*;

import java.net.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;


/**
 * The primary controller class for this application, this initializes
 * all of the instances and hooks them together.  Furthermore, as a 
 * Listener, it receives input commands from the buttons and forwards
 * them to ImageProcessor.            */
public class ImageGUI implements ActionListener  {
    
    // contains the original and current ImageArrays and methods that manipulate them.
    private ImageProcessor processor; 
    
    // contains the JFrame that presents the GUI to the user
    private ImageFrame frame;
    
    // contains the image data, which allows us to modify the image.
    private ImageArray array;
        
    /** Open the A6 GUI for the user */
    public static void main(String[] pars) {
        ImageGUI gui = new ImageGUI();
    }
    
    /** Constructor: an instance for an Image that is found using a file dialog
     *  window. If no Image is produced using the file dialog, use null as the Image. 
     *  As the primary controller, this method creates all of the other instances
     *  (either directly or indirectly) in this application.     */
    public ImageGUI() {
    	frame = new ImageFrame();
    	ImageObserver observer = frame.getOriginalPanel();
    	Image im = frame.getImage();
        array = new ImageArray(im,im.getHeight(observer), im.getWidth(observer));
        processor= new ImageProcessor(array);
        frame.addActionListener(this);
    }
        
    /** Yields: the ImageProcessor for the image. */
    public ImageProcessor getProcessor() {
        return processor;
    }

    /** Yields: the ImageArray for the image. */
    public ImageArray getImageArray() {
        return array;
    }
    
    /** Pause the program for d microseconds */
    public void pause(int d) {
        try { Thread.currentThread().sleep(d); }
        catch (InterruptedException e) { }
    }
       
    /** Change current image to the map given by processor.getCurrentImage(). */
    private void changeCurrentImage() {
        frame.getCurrentPanel().changeImageTo(processor.getCurrentImage());
        frame.pack();
        frame.repaint();
    }
    
    /** Yields: a String that represents the first n pixels of the current image */
    public String getPixels(int n) {
        return processor.getPixels(n);
    }
    
    /** call the quick reveal */
    public void quickReveal() {
        String m= processor.reveal1();
        if (m == null) {
            frame.setMessage("No message found to reveal.");
        } else {
            //System.out.println("Starting to reveal");
            frame.setMessage(m);
            //System.out.println("Finished revealing");
        }
        return;
    }
    
    /** Process a click of one of the buttons */
    public void actionPerformed(ActionEvent e)  {
        // assert statements are simply reminders about which function to call
        // (since buttonLabelList is "far away" in this code)
        
        if (e.getSource() == frame.getButton(ImageFrame.BUTTON_RESTORE)) {
            processor.restore();
            changeCurrentImage();
            return;
        }
        
        if (e.getSource() == frame.getButton(ImageFrame.BUTTON_INVERT)) {
            processor.invert();
            changeCurrentImage();
            return;
        }
        
        if (e.getSource() == frame.getButton(ImageFrame.BUTTON_TRANSPOSE)) {
            processor.transpose();
            changeCurrentImage();
            return;
        }
        
        if (e.getSource() == frame.getButton(ImageFrame.BUTTON_H_REFLECT)) {
            processor.hreflect();
            changeCurrentImage();
            return;
        }
        
        if (e.getSource() == frame.getButton(ImageFrame.BUTTON_V_REFLECT)) {
            processor.vreflect();
            changeCurrentImage();
            return;
        }
        
        if (e.getSource() == frame.getButton(ImageFrame.BUTTON_MONOCHROM)) {
            if (frame.greyIsChecked()) {
                processor.monochromify(ImageProcessor.GRAY);
                changeCurrentImage();
                return;
            }
            if (frame.sepiaIsChecked()) {
                processor.monochromify(ImageProcessor.SEPIA);
                changeCurrentImage();
                return;
            }
        }
        
        if (e.getSource() == frame.getButton(ImageFrame.BUTTON_VIGNETTE)) {
            processor.vignette();
            changeCurrentImage();
            return;
        }
        
        if (e.getSource() == frame.getButton(ImageFrame.BUTTON_HIDE)) {
            String m= frame.getMessage();
            boolean b= processor.hide(m);
            if (b) {
                frame.setMessage("Message of length " + m.length() + " was hidden.\n");
                changeCurrentImage();
            } else {
                frame.setMessage("Message too long to be hidden:\n" + m);
            }
            
            return;
        }
        
        if (e.getSource() == frame.getButton(ImageFrame.BUTTON_REVEAL)) {
            String m= processor.reveal();
            if (m == null) {
                frame.setMessage("No message found to reveal.");
            } else {
                //System.out.println("Starting to reveal");
               frame.setMessage(m);
                //System.out.println("Finished revealing");
            }
            return;
        }
        
        if (e.getSource() == frame.getButton(ImageFrame.BUTTON_FUZZIFY)) {
            processor.fuzzify();
            changeCurrentImage();
            return; 
            
        }
        
        if (e.getSource() == frame.getButton(ImageFrame.BUTTON_PUT_JAIL)) {
            processor.putInJail();
            changeCurrentImage();
            return; 
        }
        
        if (e.getSource() == frame.getButton(ImageFrame.BUTTON_SAVE)) {
            try {
                processor.writeImage("foobar.png");
            } catch (IOException ex) {
                System.err.println("I/O Exception: " + ex.getMessage());
            }
            return; 
        }
        
    }
}
