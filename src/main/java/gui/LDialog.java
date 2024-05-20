package gui;

import constants.Colors;

import javax.swing.*;
import java.awt.*;

public class LDialog extends JDialog {
	public LDialog(JFrame frame, String title, int dialogWidth, int dialogHeight) {
		super(frame, title);

		getContentPane().setBackground(Colors.LAYER0);
		setSize(dialogWidth, dialogHeight);
		setResizable(false);
		setDefaultCloseOperation(HIDE_ON_CLOSE);

		Rectangle bounds = getGraphicsConfiguration().getBounds();
		int xLocation = bounds.width / 2 - dialogWidth / 2;
		int yLocation = bounds.height / 2 - dialogHeight / 2;
		setLocation(xLocation, yLocation);
	}
}
