package prr.core;

public class TerminalStateBusy extends TerminalState {

	private Terminal _terminal;

	public TerminalStateBusy(Terminal terminal) {
		super(terminal, TerminalMode.BUSY);
		_terminal = terminal;
	}

	@Override
	void acceptSMS(Terminal from, String message) {_terminal.doAcceptSMS(from, message);}

	@Override
	double endOngoingCommunication(int size) {
		if (_terminal.initiatedCommunication()) {
			return _terminal.doEndOngoingCommunication(size);
		}
		return 0d;
	}

	@Override
	boolean setOnIdle() {return false;}

	@Override
	boolean setOnSilent() {return false;}

	@Override
	boolean turnOff() {return false;}

	@Override
	boolean canEndCurrentCommunication() {
		return _terminal.initiatedCommunication();
	}

	@Override
	boolean canStartCommunication() {return false;}
}
