package gui;

import constants.Colors;
import constants.Dimensions;
import constants.Fonts;
import constants.Text;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class LFileButton extends JButton {
	private JFileChooser fileChooser;
	private File chosenFile;
	public LFileButton(JFileChooser fileChooser){
		super(Dimensions.L_FILE_BUTTON_DEFAULT_TEXT);

		assert fileChooser != null;
		this.fileChooser = fileChooser;

		setMinimumSize(Dimensions.L_FILE_BUTTON);
		setPreferredSize(Dimensions.L_FILE_BUTTON);
		setMaximumSize(Dimensions.L_FILE_BUTTON);
		setBackground(Color.black);
		setFont(Fonts.SMALL_MONO);
		setForeground(Colors.FONT_COLOR);

		addActionListener(actionEvent -> {
			int returnValue = fileChooser.showDialog(this, Text.L_FILE_BUTTON_APPROVAL_BUTTON);
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				chosenFile = fileChooser.getSelectedFile();
				setDestination(chosenFile);
			}
		});
	}

	public LFileButton() {
		this(new JFileChooser());
	}

	protected void setDestination(File file) {
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

	public File getChosenFile() {
		return chosenFile;
	}
}
