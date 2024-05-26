package gui;

import constants.Colors;
import constants.Dimensions;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class ChainList extends JScrollPane {
	private ChainItem[] chainItems = new ChainItem[]{};
	public ChainList() {
		super(VERTICAL_SCROLLBAR_ALWAYS, HORIZONTAL_SCROLLBAR_NEVER);

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

	public void removeItem(ChainItem item) {
		int index = 0;
		while (chainItems[index] != item && ++index < chainItems.length);
		if (index == chainItems.length) return;

		System.arraycopy(chainItems, index + 1, chainItems, index, chainItems.length - 1 - index);
		chainItems = Arrays.copyOf(chainItems, chainItems.length - 1);
		initiateList();
	}

	public void editItem(ChainItem oldItem, ChainItem newItem) {
		int index = 0;
		while (chainItems[index] != oldItem && ++index < chainItems.length);
		if (index == chainItems.length) return;
		chainItems[index] = newItem;
		initiateList();
	}

	public boolean isEmpty() {
		return chainItems.length == 0;
	}
}
