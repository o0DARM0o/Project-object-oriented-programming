package prr.core;

public class VideoCommunication extends InteractiveCommunication {

	public VideoCommunication(Terminal from, Terminal to) {
		super(from, to, CommunicationType.VIDEO);
	}

	@Override
	protected double computeCost(TariffPlan plan) {
		double cost = plan.computeCost(this);
		cost *= super.getSize();
		super._cost = cost;
		return cost;
	}
}
