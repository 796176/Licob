package gui;


import constants.Colors;
import constants.Dimensions;
import constants.Fonts;

import javax.swing.*;

public class ScriptArea extends JScrollPane {
	private final JTextArea textArea;
	public ScriptArea(String content, boolean isActive){
		super(VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_AS_NEEDED);
		textArea = new JTextArea();
		textArea.setCaretColor(Colors.FONT_COLOR);
		textArea.setFont(Fonts.MEDIUM_MONO);
		if (isActive) setActive();
		else setPassive();
		setViewportView(textArea);
		getVerticalScrollBar().setUnitIncrement(Dimensions.DEFAULT_TEXT_SCROLL);
		setPreferredSize(getMinimumSize());
	}

	public ScriptArea(){
		this("", false);
	}

	public ScriptArea(String content){
		this(content, true);
	}

	public void setActive(){
		textArea.setBackground(Colors.LAST_LAYER);
		textArea.setForeground(Colors.FONT_COLOR);
		textArea.setEditable(true);
	}

	public void setPassive(){
		textArea.setBackground(Colors.SCRIPT_AREA_COLOR_INACTIVE);
		textArea.setForeground(Colors.SCRIPT_AREA_FONT_COLOR_INACTIVE);
		textArea.setEditable(false);
	}

	public JTextArea getTextArea() {
		return textArea;
	}

	public String getContent() {
		return textArea.getText();
	}
}
