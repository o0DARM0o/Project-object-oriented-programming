package prr.core;

public class VoiceCommunication extends InteractiveCommunication {

	public VoiceCommunication(Terminal from, Terminal to) {
		super(from, to, CommunicationType.VOICE);
	}

	@Override
	protected double computeCost(TariffPlan plan) {
		double cost = plan.computeCost(this);
		cost *= super.getSize();
		super._cost = cost;
		return cost;
	}
}
