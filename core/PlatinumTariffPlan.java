package prr.core;

public class PlatinumTariffPlan extends TariffPlan{
	
	public PlatinumTariffPlan() {super(ClientLevel.PLATINUM);}

	@Override
	protected double computeCost(TextCommunication text){
		
		int lenght = text.getSize();
		if (lenght < 50)
			return 0;
		else
			return 4;
	}
	
	@Override
	protected double computeCost(VoiceCommunication voice){
		Terminal toTerminal = voice.getTo();
		Terminal fromTerminal = voice.getFrom();
		
		if(fromTerminal.isFriend(toTerminal))
			return 5;
		else
			return 10;
	}
	
	@Override
	protected double computeCost(VideoCommunication video){
		Terminal toTerminal = video.getTo();
		Terminal fromTerminal = video.getFrom();
		
		if(fromTerminal.isFriend(toTerminal))
			return 5;
		else
			return 10;
		
	}

	@Override
	void checkIfDowngrade(Client client) {
		if (client.isBalanceNegative()) {
			client.setTariffPlan(new NormalTariffPlan());
		}
	}
	@Override
	void checkIfUpgrade(Client client) {
		if (!client.isBalanceNegative() && client._textCommunicationCounter >= 2) {
			client.setTariffPlan(new GoldTariffPlan());
		}
	}
}