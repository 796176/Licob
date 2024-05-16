package gui;


import constants.Colors;
import constants.Dimensions;
import constants.Fonts;

import javax.swing.*;

public class ScriptArea extends JScrollPane {
	public ScriptArea(){
		super(VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_AS_NEEDED);
		JTextArea textArea = new JTextArea();
		textArea.setBackground(Colors.LAST_LAYER);
		textArea.setForeground(Colors.FONT_COLOR);
		textArea.setCaretColor(Colors.FONT_COLOR);
		textArea.setFont(Fonts.MEDIUM_MONO);
		setViewportView(textArea);
		getVerticalScrollBar().setUnitIncrement(Dimensions.DEFAULT_TEXT_SCROLL);
		setPreferredSize(getMinimumSize());
	}
}
