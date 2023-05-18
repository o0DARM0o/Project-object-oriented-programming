package prr.core;

public abstract class InteractiveCommunication extends Communication {

	private int _duration;

	public InteractiveCommunication(Terminal from, Terminal to, CommunicationType type) {
		super(from, to, type);
	}

	@Override 
	protected int getSize() {
		return _duration;
	}

	@Override
	protected void setSize(int size) {
		_duration = size;
	}

	protected abstract double computeCost(TariffPlan plan);
}
