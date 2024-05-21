package gui;

import constants.Colors;
import constants.Dimensions;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class ChainList extends JScrollPane {
	private ChainItem[] chainItems;
	public ChainList(ChainItem[] items) {
		super(VERTICAL_SCROLLBAR_ALWAYS, HORIZONTAL_SCROLLBAR_NEVER);

		assert items != null;
		chainItems = items;

		initiateList();

		verticalScrollBar.setUnitIncrement(Dimensions.DEFAULT_PANEL_SCROLL);
		setPreferredSize(getMinimumSize());
	}

	public ChainItem[] getChainItems() {
		return chainItems;
	}

	private JPanel getPanel(){
		JPanel panel = new JPanel();
		panel.setBackground(Colors.LAYER1);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		return panel;
	}

	private void initiateList(){
		JPanel innerPanel = getPanel();
		for (ChainItem item: chainItems){
			innerPanel.add(item);
			if (item != chainItems[chainItems.length - 1])
				innerPanel.add(Box.createRigidArea(new Dimension(0, Dimensions.DEFAULT_COMPONENT_OFFSET)));
		}
		setViewportView(innerPanel);
	}

	public void addItem(ChainItem item){
		chainItems = Arrays.copyOf(chainItems, chainItems.length + 1);
		chainItems[chainItems.length - 1] = item;
		initiateList();
	}

	public boolean isEmpty() {
		return chainItems.length == 0;
	}
}
