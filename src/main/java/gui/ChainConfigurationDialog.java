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

import constants.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.function.Consumer;

public class ChainConfigurationDialog extends LDialog {
	private JComboBox<String> typeComboBox;
	private final LFileButton sourceButton = new LFileButton();
	private LFileButton dstButton;
	private ExceptionArea exceptionArea = new ExceptionArea();
	private final Consumer<ChainConfigurationDialog> ccdConsumer;
	private final JFrame parentFrame;
	public ChainConfigurationDialog(
		JFrame frame,
		ChainTypes ct,
		File src,
		File dst,
		String exs,
		Consumer<ChainConfigurationDialog> consumer
	) {
		super(frame, "", Dimensions.CHAIN_CONFIGURATION_DIALOG_WIDTH, Dimensions.CHAIN_CONFIGURATION_DIALOG_HEIGHT);

		assert frame != null && exs != null && consumer != null;

		parentFrame = frame;
		ccdConsumer = consumer;
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		frame.setEnabled(false);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				ChainConfigurationDialog dialog = ChainConfigurationDialog.this;
				dialog.ccdConsumer.accept(dialog);
				dialog.setVisible(false);
				dialog.parentFrame.setEnabled(true);
			}
		});

		GridBagLayout bagLayout = new GridBagLayout();
		setLayout(bagLayout);

		GridBagConstraints leftElementsConstraints = new GridBagConstraints();
		leftElementsConstraints.weightx = 1;
		leftElementsConstraints.anchor = GridBagConstraints.NORTHWEST;
		leftElementsConstraints.insets =
			new Insets(Dimensions.DEFAULT_COMPONENT_OFFSET, Dimensions.DEFAULT_COMPONENT_OFFSET, 0, 0);
		JPanel leftElements = new LeftElements(ct, src, dst);
		bagLayout.setConstraints(leftElements, leftElementsConstraints);
		add(leftElements);

		GridBagConstraints rightElementsConstraints = new GridBagConstraints();
		rightElementsConstraints.weightx = 5;
		rightElementsConstraints.weighty = 1;
		rightElementsConstraints.fill = GridBagConstraints.BOTH;
		rightElementsConstraints.insets =
			new Insets(
				Dimensions.DEFAULT_COMPONENT_OFFSET,
				0,
				Dimensions.DEFAULT_COMPONENT_OFFSET,
				Dimensions.DEFAULT_COMPONENT_OFFSET
			);
		JPanel rightElements = new RightElements(ct, exs);
		bagLayout.setConstraints(rightElements, rightElementsConstraints);
		add(rightElements);

		setVisible(true);
	}

	public ChainConfigurationDialog(JFrame frame, Consumer<ChainConfigurationDialog> consumer) {
		this(frame, null, null, null, "", consumer);
	}

	private class LeftElements extends JPanel{
		LeftElements(ChainTypes ct, File source, File destination){
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			setBackground(new Color(0,0,0,0));

			LLabel typeLabel = new LLabel(Text.TYPE_LABEL);
			typeComboBox = new JComboBox<>();
			typeComboBox.addItemListener(itemEvent -> {
				if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
					assert typeComboBox.getSelectedItem() != null;

					String selectedItem = (String) typeComboBox.getSelectedItem();
					if (
						selectedItem.equals(ChainTypes.Content.getVisualRepresentation()) ||
						selectedItem.equals(ChainTypes.Directory.getVisualRepresentation())
					) {
						JFileChooser fileChooser = new JFileChooser();
						fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
						sourceButton.setFileChooser(fileChooser);
						exceptionArea.setActive();
					} else if (selectedItem.equals(ChainTypes.File.getVisualRepresentation())){
						JFileChooser fileChooser = new JFileChooser();
						fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
						sourceButton.setFileChooser(fileChooser);
						exceptionArea.setPassive();
					}
				}
			});
			typeComboBox.setFont(Fonts.MEDIUM_DEFAULT);
			typeComboBox.setBackground(Color.black);
			typeComboBox.setForeground(Colors.FONT_COLOR);
			typeComboBox.setLightWeightPopupEnabled(false);
			typeComboBox.addItem(ChainTypes.Directory.getVisualRepresentation());
			typeComboBox.addItem(ChainTypes.Content.getVisualRepresentation());
			typeComboBox.addItem(ChainTypes.File.getVisualRepresentation());
			if (ct != null) typeComboBox.setSelectedItem(ct.getVisualRepresentation());
			JPanel typePanel =
				LGridBagLayout.componentString(Dimensions.DEFAULT_COMPONENT_OFFSET, typeLabel, typeComboBox);
			typePanel.setPreferredSize(typePanel.getMinimumSize());
			typePanel.setMaximumSize(typePanel.getMinimumSize());
			typePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
			add(typePanel);
			add(Box.createRigidArea(new Dimension(0, Dimensions.DEFAULT_COMPONENT_OFFSET)));

			LLabel sourceLabel = new LLabel(Text.SOURCE_LABEL);
			if (source != null) sourceButton.setDestination(source);
			JPanel sourcePanel =
				LGridBagLayout.componentString(Dimensions.DEFAULT_COMPONENT_OFFSET, sourceLabel, sourceButton);
			sourcePanel.setPreferredSize(sourcePanel.getMinimumSize());
			sourcePanel.setMaximumSize(sourcePanel.getMinimumSize());
			sourcePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
			add(sourcePanel);
			add(Box.createRigidArea(new Dimension(0, Dimensions.DEFAULT_COMPONENT_OFFSET)));

			LLabel destinationLabel = new LLabel(Text.DESTINATION_LABEL);
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			dstButton = new LFileButton(fileChooser);
			if (destination != null) dstButton.setDestination(destination);
			JPanel destinationPanel =
				LGridBagLayout
					.componentString(Dimensions.DEFAULT_COMPONENT_OFFSET, destinationLabel, dstButton);
			destinationPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
			add(destinationPanel);
		}
	}

	public String getChainType(){
		assert typeComboBox.getSelectedItem() != null;

		return (String) typeComboBox.getSelectedItem();
	}

	public String getSource(){
		return sourceButton.getChosenFile();
	}

	public String getDestination(){
		return dstButton.getChosenFile();
	}

	public String getExceptions(){
		return exceptionArea.getExceptions();
	}

	private class RightElements extends JPanel{
		RightElements(ChainTypes ct, String exceptions){
			assert exceptions != null;

			setPreferredSize(getMinimumSize());
			setBackground(new Color(0,0,0,0));

			GridBagLayout bagLayout = new GridBagLayout();
			setLayout(bagLayout);

			GridBagConstraints constraints = new GridBagConstraints();
			constraints.gridwidth = GridBagConstraints.REMAINDER;
			constraints.anchor = GridBagConstraints.NORTHWEST;
			constraints.insets = new Insets(0, 0, Dimensions.DEFAULT_COMPONENT_OFFSET, 0);
			LLabel exceptionLabel = new LLabel(Text.EXCEPTION_LABEL);
			bagLayout.setConstraints(exceptionLabel, constraints);
			add(exceptionLabel);

			constraints.fill = GridBagConstraints.BOTH;
			constraints.insets = new Insets(0, 0, 0, 0);
			constraints.weightx = 1;
			constraints.weighty = 1;
			if (ct == null || ct.equals(ChainTypes.Directory) || ct.equals(ChainTypes.Content))
				exceptionArea = new ExceptionArea(exceptions, true);
			else if (ct.equals(ChainTypes.File))
				exceptionArea = new ExceptionArea(exceptions, false);
			bagLayout.setConstraints(exceptionArea, constraints);
			add(exceptionArea);
		}
	}
}
