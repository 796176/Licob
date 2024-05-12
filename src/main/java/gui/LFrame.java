package gui;

import constants.Colors;

import javax.swing.*;
import java.awt.*;


abstract public class LFrame extends JFrame {
	public LFrame(String title, int windowWidth, int windowHeight){
		super(title);
		setSize(windowWidth, windowHeight);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		getContentPane().setBackground(Colors.LAYER0);

		Rectangle bounds = getGraphicsConfiguration().getBounds();
		int xLocation = bounds.width / 2 - windowWidth / 2;
		int yLocation = bounds.height / 2 - windowHeight / 2;
		setLocation(xLocation, yLocation);
	}
}
