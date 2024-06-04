package gui;

public class ChainRule {
	public String type;
	public String source;
	public String destination;
	public String exceptions;

	public ChainRule(String type, String source, String destination, String exceptions) {
		this.type = type;
		this.source = source;
		this.destination = destination;
		this.exceptions = exceptions;
	}

	public ChainRule(ChainItem item) {
		type = item.getType();
		source = item.getSource();
		destination = item.getDestination();
		exceptions = item.getExceptions();
	}

	public ChainRule() {}
}
