package gui;

import constants.Colors;
import constants.Fonts;

import javax.swing.*;

public class LogArea extends JScrollPane {
	public LogArea(){
		super(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		JTextArea textArea = new JTextArea();
		textArea.setBackground(Colors.LAST_LAYER);
		textArea.setForeground(Colors.FONT_COLOR);
		textArea.setFont(Fonts.SMALL_MONO);
		textArea.setEditable(false);
		setViewportView(textArea);
	}
}
