package prr.core;

public class TerminalStateSilence extends TerminalState {

	private Terminal _terminal;

    public TerminalStateSilence(Terminal terminal) {
		super(terminal, TerminalMode.SILENCE);
		_terminal = terminal;
    }

	@Override
	void makeSMS(Terminal to, String message) {_terminal.doMakeSMS(to, message);}

	@Override
	void acceptSMS(Terminal from, String message) {_terminal.doAcceptSMS(from, message);}

	@Override
	void makeVoiceCall(Terminal to) {_terminal.doMakeVoiceCall(to);}

	@Override
	void makeVideoCall(Terminal to) {_terminal.doMakeVideoCall(to);}

	@Override
	boolean setOnIdle() {
		_terminal.setState(new TerminalStateIdle(_terminal));
		_terminal.notify(new Notification(_terminal, NotificationType.S2I));
		return true;
	}

	@Override
	boolean setOnSilent() {return true;}

	@Override
	boolean turnOff() {
		_terminal.setState(new TerminalStateOff(_terminal));
		return true;
	}

	@Override
	boolean canEndCurrentCommunication() {return false;}

	@Override
	boolean canStartCommunication() {return true;}
}
