// @author - Matthew Green

import java.awt.*;

import java.net.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.File;
import java.io.IOException;


/** An instance is a JFrame that contains an image (in two forms:
 *  the  original image and a current version)  with a title and
 *  buttons to alter the current image.
 *  When no image is in the JFrame, it is hidden.
 *  When an image is placed in it, it becomes visible. */
public class ImageFrame extends JFrame {

	// Constants that represent buttons
	public static final int BUTTON_RESTORE   = 0;
	public static final int BUTTON_INVERT    = 1;
	public static final int BUTTON_TRANSPOSE = 2;
	public static final int BUTTON_H_REFLECT = 3;
	public static final int BUTTON_V_REFLECT = 4;
	public static final int BUTTON_MONOCHROM = 5;
	public static final int BUTTON_VIGNETTE  = 6;
	public static final int BUTTON_HIDE      = 7;
	public static final int BUTTON_REVEAL    = 8;
	public static final int BUTTON_FUZZIFY   = 9;
	public static final int BUTTON_PUT_JAIL  = 10;
	public static final int BUTTON_SAVE      = 11;
    
    // Labels and list for the JButtons to be put in buttonBox
    // IMPORTANT: Label names must match constants above
    private final String[] buttonLabelList= {
  		"restore", "invert", "transpose", "hor reflect", "ver reflect",
  		"monochromify", "vignette", "hide message", "reveal message", 
  		"fuzzify", "put in jail", "save"
 	};
 	
    private JButton[] buttonList = new JButton[buttonLabelList.length];
    private Box buttonBox= new Box(BoxLayout.Y_AXIS);
    
    // The radio buttons
    private JRadioButton greyButton= new JRadioButton("grayscale", true);
    private JRadioButton sepiaButton= new JRadioButton("sepia", false);
    private Box checkboxBox= new Box(BoxLayout.X_AXIS);
    
    private ButtonGroup group= new ButtonGroup();
    
    
    // The panel with the original image, the panel with the current image,
    // and the box that contains both.
    private ImagePanel originalPanel;
    private ImagePanel currentPanel;
    private Box inputBox= new Box(BoxLayout.Y_AXIS);
    
    // The text area for the user to give a message
    private TextArea messageArea= new TextArea(10, 20); /*JTextArea or TextArea */
    private JScrollPane scrollPane= new JScrollPane(messageArea);
    private JLabel jlabel= new JLabel("Type the message to be hidden in this text area:");
	private JPanel areaPanel = new JPanel();
    private Box areaBox= new Box(BoxLayout.Y_AXIS);
    
    // imageBox contains the original image and the current one
    private Box imagebox= new Box(BoxLayout.X_AXIS);

    /** Constructor: an instance for Image im.
     *  If im is null, the window is not shown. */
    public ImageFrame(Image im) {
        setUp(im, "An image");
    }
    
    /** Constructor: an instance for an Image that is found using a file dialog
     *  window. If no Image is produced using the file dialog,
     *  use null as the Image. */
    public ImageFrame() {
        String f= getImageName();
        Image im= getImage(f, this);
        setUp(im, "image: " + f); 
    }
    
  	/** Set up this JFrame for Image im, with title t. This includes adding buttons 
  	 *  and checkboxes to the GUI, performing a few other minor operations to make the GUI
     *  work properly, and placing the image in the JFrame.      */
    private void setUp(Image im, String t) {
        setTitle(t);

  		// Build box buttonBox of buttons.
  		
  		// First compute largest button size.
  		int msize = 0;
		for (int i=0; i < buttonLabelList.length; i= i+1) {
			if (msize < buttonLabelList[i].length()) {
				msize = buttonLabelList[i].length();
			}
		}  		
        
        /* inv: buttonList[0..i-1] contain JButtons with labels
         * buttonLabelList[0..i-1], each JButton has been added to buttonBox,
         *  and this ImageGUI is registered as a listener to these JButtons
         */
		Dimension size = new Dimension(140,buttonList.length*31);
		buttonBox.setPreferredSize(size);
		buttonBox.setSize(size);
		for (int i=0; i < buttonLabelList.length; i= i+1) {
            buttonList[i]= new JButton(buttonLabelList[i]);
            buttonBox.add(buttonList[i]);
		}
        getContentPane().add(BorderLayout.WEST, buttonBox);
        
        // Build box inputBox of the color buttons.
        checkboxBox.add(greyButton);
        checkboxBox.add(sepiaButton);
        inputBox.add(checkboxBox);
        
        // Add the color buttons to group.
        group.add(greyButton);
        group.add(sepiaButton);
        
        getContentPane().add(BorderLayout.NORTH, inputBox);
        
        // Add a text area with label
  		areaBox.add(jlabel);
 	    areaBox.add(scrollPane);
  	    getContentPane().add(BorderLayout.SOUTH, areaBox);
        
        // If there is no image, make JFrame invisible and return
        if (im == null) {
            setVisible(false);
            return;
        }
        
        // Create the two panels and the ImageProcessor processor.
        originalPanel= new ImagePanel(im);
        currentPanel= new ImagePanel(im);
        
        // Add the panels to the GUI, in imagebox.
        imagebox= new Box(BoxLayout.X_AXIS);
        imagebox.add(originalPanel);
        imagebox.add(currentPanel);
        getContentPane().add(BorderLayout.CENTER, imagebox);
        
        // Place and resize the window
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocation(200,100);
        pack();
        setVisible(true);
        repaint();
    }
 	 
    /** Adds an action listener to handle the buttons. */
    public void addActionListener(ActionListener listener) {
        for (int i=0; i < buttonLabelList.length; i= i+1) {
            buttonList[i].addActionListener(listener);
        }      
    }
    
    /** Yields: the grey check box in the GUI is selected */
    public boolean greyIsChecked() {
        return greyButton.isSelected();
    }
    
    /** Yields: the sepia check box in the GUI is selected */
    public boolean sepiaIsChecked() {
        return sepiaButton.isSelected();
    }
        
    /** Yields: the current panel */
    public ImagePanel getCurrentPanel() {
        return currentPanel;
    }
    
    /** Yields: the original panel */
    public ImagePanel getOriginalPanel() {
        return originalPanel;
    }
    
    /** Yields: button number n */
    public JButton getButton(int n) {
    	return buttonList[n];
    }
    
    /** Sets the message area of the JFrame to display text */
    public void setMessage(String m) {
    	messageArea.setText(m);
    }

    /** Yields: the currently displayed message */
    public String getMessage() {
    	return messageArea.getText();
    }

    /** Yields: the file name of an image loaded by the user using a dialog window */
    public static String getImageName() {
        FileDialog fd = new FileDialog(new Frame(), "Open File", FileDialog.LOAD);
        fd.setVisible(true);
        if (fd.getFile() == null) {
            return null;
        }
        return fd.getDirectory() + fd.getFile();
    }
    
    /** Yields: the image for file name f, using the toolkit for jframe */
    public static Image getImage(String f, JFrame jframe) {
        Image image= null;
        try {
            image= jframe.getToolkit().getImage(new URL("file:" + f));
        } catch (MalformedURLException e) {
            System.err.println("Bad URL!");
            return null;
        }
        
        // set media tracker to wait for image to load
        MediaTracker tracker= new MediaTracker(jframe);
        tracker.addImage(image,0);    
        
        // wait for image to load
        try {
   			tracker.waitForID(0); 
        } catch (InterruptedException e) {
            // handler.flashMessage(ImageHandler.LOAD_INTERRUPTED);
            return null;
        }
        return image;
    }
    
    /** Yields: the current image */
    public Image getImage() {
    	return originalPanel.getImage();
    }

}
