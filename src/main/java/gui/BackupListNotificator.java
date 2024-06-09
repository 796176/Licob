package gui;

public class BackupListNotificator {
	private BackupItem currentItem;

	public BackupListNotificator(BackupItem item) {
		assert item != null;

		currentItem = item;
	}

	public void notify(BackupList bl, ChainConfigurationFrame ccf) {
		assert bl != null && ccf != null;

		String backupName = ccf.getBackupName();
		int chainNumber = ccf.getChainNumber();
		boolean isScriptActive = ccf.getScriptState();
		String scriptContent = ccf.getScriptContent();
		String date = currentItem.getLastTimeExecuted();
		bl.changeBackupItem(currentItem, backupName, chainNumber, isScriptActive, scriptContent, date);

		BackupItem[] updatedItems = bl.getBackupItems();
		int index = 0;
		while (!(updatedItems[index].getBackupName().equals(backupName)) && ++index < updatedItems.length);
		currentItem = updatedItems[index];
	}
}
