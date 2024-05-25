package gui;

import constants.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ChainConfigurationDialog extends LDialog {
	public JComboBox<String> typeComboBox;
	public LFileButton sourceButton = new LFileButton();
	public LFileButton dstButton;
	public ExceptionArea exceptionArea;
	public ChainConfigurationDialog(JFrame frame) {
		super(frame, "", Dimensions.CHAIN_CONFIGURATION_DIALOG_WIDTH, Dimensions.CHAIN_CONFIGURATION_DIALOG_HEIGHT);

		GridBagLayout bagLayout = new GridBagLayout();
		setLayout(bagLayout);

		GridBagConstraints leftElementsConstraints = new GridBagConstraints();
		leftElementsConstraints.weightx = 1;
		leftElementsConstraints.anchor = GridBagConstraints.NORTHWEST;
		leftElementsConstraints.insets =
			new Insets(Dimensions.DEFAULT_COMPONENT_OFFSET, Dimensions.DEFAULT_COMPONENT_OFFSET, 0, 0);
		JPanel leftElements = new LeftElements();
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
		JPanel rightElements = new RightElements();
		bagLayout.setConstraints(rightElements, rightElementsConstraints);
		add(rightElements);

		setVisible(true);
	}

	private class LeftElements extends JPanel{
		LeftElements(){
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			setBackground(new Color(0,0,0,0));

			LLabel typeLabel = new LLabel(Text.TYPE_LABEL);
			typeComboBox = new JComboBox<>();
			typeComboBox.addItemListener(itemEvent -> {
				if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
					String selectedItem = (String) typeComboBox.getSelectedItem();
					if (
						selectedItem.equals(ChainTypes.Content.getVisualRepresentation()) ||
						selectedItem.equals(ChainTypes.Directory.getVisualRepresentation())
					) {
						JFileChooser fileChooser = new JFileChooser();
						fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
						sourceButton.setFileChooser(fileChooser);
					} else if (selectedItem.equals(ChainTypes.File.getVisualRepresentation())){
						JFileChooser fileChooser = new JFileChooser();
						fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
						sourceButton.setFileChooser(fileChooser);
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
			JPanel typePanel =
				LGridBagLayout.componentString(Dimensions.DEFAULT_COMPONENT_OFFSET, typeLabel, typeComboBox);
			typePanel.setPreferredSize(typePanel.getMinimumSize());
			typePanel.setMaximumSize(typePanel.getMinimumSize());
			typePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
			add(typePanel);
			add(Box.createRigidArea(new Dimension(0, Dimensions.DEFAULT_COMPONENT_OFFSET)));

			LLabel sourceLabel = new LLabel(Text.SOURCE_LABEL);
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
			JPanel destinationPanel =
				LGridBagLayout
					.componentString(Dimensions.DEFAULT_COMPONENT_OFFSET, destinationLabel, dstButton);
			destinationPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
			add(destinationPanel);
		}
	}

	private class RightElements extends JPanel{
		RightElements(){
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
			exceptionArea = new ExceptionArea();
			bagLayout.setConstraints(exceptionArea, constraints);
			add(exceptionArea);
		}
	}
}
