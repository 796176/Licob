package gui;

import constants.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.function.BiConsumer;

public class ChainConfigurationDialog extends LDialog {
	private JComboBox<String> typeComboBox;
	private final LFileButton sourceButton = new LFileButton();
	private LFileButton dstButton;
	private ExceptionArea exceptionArea = new ExceptionArea();
	private final BiConsumer<ChainConfigurationFrame, ChainConfigurationDialog> ccdBiConsumer;
	private final ChainConfigurationFrame chainConfigurationFrame;
	public ChainConfigurationDialog(
		ChainConfigurationFrame ccf,
		ChainTypes ct,
		File src,
		File dst,
		String exs,
		BiConsumer<ChainConfigurationFrame, ChainConfigurationDialog> biConsumer
	) {
		super(ccf, "", Dimensions.CHAIN_CONFIGURATION_DIALOG_WIDTH, Dimensions.CHAIN_CONFIGURATION_DIALOG_HEIGHT);

		assert ccf != null && exs != null && biConsumer != null;

		chainConfigurationFrame = ccf;
		ccdBiConsumer = biConsumer;
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		ccf.setEnabled(false);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				ChainConfigurationDialog dialog = ChainConfigurationDialog.this;
				dialog.ccdBiConsumer.accept(dialog.chainConfigurationFrame, dialog);
				dialog.setVisible(false);
				dialog.chainConfigurationFrame.setEnabled(true);
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

	public ChainConfigurationDialog(
		ChainConfigurationFrame frame,
		BiConsumer<ChainConfigurationFrame, ChainConfigurationDialog> biConsumer
	) {
		this(frame, null, null, null, "", biConsumer);
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
		return sourceButton.getChosenFile().orElse("Not set");
	}

	public String getDestination(){
		return dstButton.getChosenFile().orElse("Not set");
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
