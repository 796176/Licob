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
import constants.Text;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Optional;

public class LFileButton extends JButton {
	private JFileChooser fileChooser;
	private String chosenFile;
	public LFileButton(JFileChooser fc){
		super(Dimensions.L_FILE_BUTTON_DEFAULT_TEXT);

		assert fc != null;
		fileChooser = fc;

		setMinimumSize(Dimensions.L_FILE_BUTTON);
		setPreferredSize(Dimensions.L_FILE_BUTTON);
		setMaximumSize(Dimensions.L_FILE_BUTTON);
		setBackground(Color.black);
		setFont(Fonts.SMALL_MONO);
		setForeground(Colors.FONT_COLOR);
		setMargin(new Insets(-1,-1,-1,-1));

		addActionListener(actionEvent -> {
			int returnValue = fileChooser.showDialog(this, Text.L_FILE_BUTTON_APPROVAL_BUTTON);
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				setDestination(fileChooser.getSelectedFile());
			}
		});
	}

	public LFileButton() {
		this(new JFileChooser());
	}

	protected void setDestination(File file) {
		chosenFile = file.getAbsolutePath();
		FontMetrics metrics = getFontMetrics(getFont());
		String sep = File.separator;
		String displayedParentDir = file.getParent().equals(sep) ? sep : file.getParent() + sep;
		String displayedFileName = file.getName();
		while (
			metrics.stringWidth(displayedParentDir + displayedFileName) > getPreferredSize().width &&
			!displayedParentDir.equals("..." + sep) &&
			!displayedParentDir.equals(sep)
		) {
			int secondSepIndex = displayedParentDir.indexOf(sep, displayedParentDir.indexOf(sep) + 1);
			displayedParentDir = "..." + displayedParentDir.substring(secondSepIndex);
		}

		while (metrics.stringWidth(displayedParentDir + displayedFileName) > getPreferredSize().width) {
			displayedFileName = displayedFileName.substring(0, displayedFileName.length() - 1 - "...".length()) + "...";
		}

		setText(displayedParentDir + displayedFileName);
	}

	public void setFileChooser(JFileChooser fileChooser) {
		assert fileChooser != null;
		this.fileChooser = fileChooser;
	}

	public JFileChooser getFileChooser() {
		return fileChooser;
	}

	public Optional<String> getChosenFile() {
		return Optional.ofNullable(chosenFile);
	}
}
