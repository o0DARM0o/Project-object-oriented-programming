package prr.core;

public class TextCommunication extends Communication {

	private String _message;

	public TextCommunication(Terminal from, Terminal to, String message) {
		super(from, to, CommunicationType.TEXT);
		_message = message;
	}
	
	@Override
	protected double computeCost(TariffPlan plan) {
		super._cost = plan.computeCost(this);
		return super._cost;
	}

	@Override
	protected int getSize() {
		return _message.length();
	}
}
