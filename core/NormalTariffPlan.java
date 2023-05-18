package prr.core;

public class NormalTariffPlan extends TariffPlan{

	public NormalTariffPlan() {super(ClientLevel.NORMAL);}

	@Override
	protected double computeCost(TextCommunication text){
		
		int lenght = text.getSize();
		if(lenght < 50)
			return 10;
		else if(lenght >= 100)
			return 2*lenght;
		else
			return 16;
	}
	
	@Override
	protected double computeCost(VoiceCommunication voice){
		Terminal toTerminal = voice.getTo();
		Terminal fromTerminal = voice.getFrom();
		
		if(fromTerminal.isFriend(toTerminal))
			return 10;
		else
			return 20;
	}
	
	@Override
	protected double computeCost(VideoCommunication video){
		Terminal toTerminal = video.getTo();
		Terminal fromTerminal = video.getFrom();
		
		if(fromTerminal.isFriend(toTerminal))
			return 15;
		else
			return 30;
		
	}

	@Override
	void checkIfDowngrade(Client client) {}
	@Override
	void checkIfUpgrade(Client client) {
		if (client.getBalance() > 500) {
			client.setTariffPlan(new GoldTariffPlan());
		}
	}
}
