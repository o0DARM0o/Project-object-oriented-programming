package prr.core;

public class GoldTariffPlan extends TariffPlan{
	
	public GoldTariffPlan() {super(ClientLevel.GOLD);}

	@Override
	protected double computeCost(TextCommunication text){
		
		int lenght = text.getSize();
		if(lenght < 50)
			return 10;
		else if(lenght >= 100)
			return 2 * lenght;
		else
			return 10;
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
			return 10;
		else
			return 20;
		
	}

	@Override
	void checkIfDowngrade(Client client) {
		if (client.isBalanceNegative()) {
			client.setTariffPlan(new NormalTariffPlan());
		}
	}

	@Override
	void checkIfUpgrade(Client client) {
		if (!client.isBalanceNegative() && client._videoCommunicationCounter >= 5) {
			client.setTariffPlan(new PlatinumTariffPlan());
		}
	}

}
