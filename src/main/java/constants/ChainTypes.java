package constants;

public enum ChainTypes {
	Directory(Text.DIRECTORY), Content(Text.CONTENT);
	ChainTypes(String s) {
		visualRepresentation = s;
	}
	private final String visualRepresentation;

	public String getVisualRepresentation() {
		return visualRepresentation;
	}
}
