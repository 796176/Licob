package gui;

import constants.Colors;
import constants.Fonts;

import javax.swing.*;

public class LLabel extends JLabel {
	public LLabel(String text){
		super(text);
		setFont(Fonts.MEDIUM_DEFAULT);
		setForeground(Colors.FONT_COLOR);
	}

	public LLabel() {
		this("");
	}
}
