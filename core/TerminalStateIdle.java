package prr.core;

public class TerminalStateIdle extends TerminalState {

	private Terminal _terminal;

	public TerminalStateIdle(Terminal terminal) {
		super(terminal, TerminalMode.IDLE);
		_terminal = terminal;
	}

	@Override
	void makeSMS(Terminal to, String message) {_terminal.doMakeSMS(to, message);}

	@Override
	void acceptSMS(Terminal from, String message) {_terminal.doAcceptSMS(from, message);}

	@Override
	void makeVoiceCall(Terminal to) {_terminal.doMakeVoiceCall(to);}

	@Override
	void acceptVoiceCall(Terminal from) {_terminal.doAcceptVoiceCall(from);}

	@Override
	void makeVideoCall(Terminal to) {_terminal.doMakeVideoCall(to);}

	@Override
	void acceptVideoCall(Terminal from) {_terminal.doAcceptVideoCall(from);}

	@Override
	boolean setOnIdle() {return true;}

	@Override
	boolean setOnSilent() {
		_terminal.setState(new TerminalStateSilence(_terminal));
		return true;
	}

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
