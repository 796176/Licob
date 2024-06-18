/* Licob - Licob Is a Chain-Oriented Backup
 * Copyright (C) 2024 Yegore Vlussove
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */


package gui;


import constants.Colors;
import constants.Dimensions;
import constants.Fonts;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ScriptArea extends JScrollPane {
	private final JTextArea textArea;
	private boolean touched;
	public ScriptArea(String content, boolean isActive){
		super(VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_AS_NEEDED);
		textArea = new JTextArea();
		textArea.setText(content);
		textArea.setCaretColor(Colors.FONT_COLOR);
		textArea.setFont(Fonts.MEDIUM_MONO);
		textArea.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				touched = true;
			}
		});
		if (isActive) setActive();
		else setPassive();
		setViewportView(textArea);
		getVerticalScrollBar().setUnitIncrement(Dimensions.DEFAULT_TEXT_SCROLL);
		setPreferredSize(getMinimumSize());
		touched = false;
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
		touched = true;
	}

	public void setPassive(){
		textArea.setBackground(Colors.SCRIPT_AREA_COLOR_INACTIVE);
		textArea.setForeground(Colors.SCRIPT_AREA_FONT_COLOR_INACTIVE);
		textArea.setEditable(false);
		touched = true;
	}

	public JTextArea getTextArea() {
		return textArea;
	}

	public String getContent() {
		return textArea.getText();
	}

	public boolean isTouched() {
		return touched;
	}

	public void untouch() {
		touched = false;
	}
}
