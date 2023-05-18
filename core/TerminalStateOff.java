package prr.core;

public class TerminalStateOff extends TerminalState {

	private Terminal _terminal;

	public TerminalStateOff(Terminal terminal) {
		super(terminal, TerminalMode.OFF);
		_terminal = terminal;
	}

	@Override
	boolean setOnIdle() {
		_terminal.setState(new TerminalStateIdle(_terminal));
		_terminal.notify(new Notification(_terminal, NotificationType.O2I));
		return true;
	}

	@Override
	boolean setOnSilent() {
		_terminal.setState(new TerminalStateSilence(_terminal));
		_terminal.notify(new Notification(_terminal, NotificationType.O2S));
		return true;
	}

	@Override
	boolean turnOff() {return true;}

	@Override
	boolean canEndCurrentCommunication() {return false;}

	@Override
	boolean canStartCommunication() {return false;}
}
