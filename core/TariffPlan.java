package prr.core;

import java.io.Serializable;

public abstract class TariffPlan implements Serializable {

	ClientLevel _level;

	public TariffPlan(ClientLevel level) {
		_level = level;
	}

	protected  abstract double computeCost(TextCommunication text);
	protected  abstract double computeCost(VoiceCommunication voice);
	protected  abstract double computeCost(VideoCommunication video);

	abstract void checkIfDowngrade(Client client);
	abstract void checkIfUpgrade(Client client);
}
