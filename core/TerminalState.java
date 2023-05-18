package prr.core;

import java.io.Serializable;

/**
 * This is the class that manages things related to states of an {@code Terminal}. <p>
 * It has the {@code Terminal} to whom it belongs; and a {@code TerminalMode} (an enum who represents the state).
 */
public abstract class TerminalState implements Serializable {
	/** The {@code Terminal} to whom this {@code TerminalState} belongs */
	Terminal _terminal;
	/** The enum who represents this state */
	TerminalMode _mode;

	TerminalState(Terminal terminal, TerminalMode mode) {
		_terminal = terminal;
		_mode = mode;
	}

	void makeSMS(Terminal to, String message) {}
	void acceptSMS(Terminal from, String message) {_terminal.addWhoFailedCommunication(from);}
	void makeVoiceCall(Terminal to) {}
	void acceptVoiceCall(Terminal from) {_terminal.addWhoFailedCommunication(from);}
	void makeVideoCall(Terminal to) {}
	void acceptVideoCall(Terminal from) {_terminal.addWhoFailedCommunication(from);}
	double endOngoingCommunication(int size) {return 0d;}
	abstract boolean setOnIdle();
	abstract boolean setOnSilent();
	abstract boolean turnOff();
	abstract boolean canEndCurrentCommunication();
	abstract boolean canStartCommunication();
}
