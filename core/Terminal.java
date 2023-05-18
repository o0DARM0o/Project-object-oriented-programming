package prr.core;

import java.io.Serializable;
import java.util.TreeMap;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/**
 * A {@code Terminal} is a public abstract class that implements de interface {@code Serializable}.
 * A Terminal can either be {@code BASIC} or {@code FANCY}}. It's always associated to a {@code Client}
 * and can be associated to some friends (a {@code Map} of {@code Terminal}s). <p>
 * @author Leonardo Pinheiro, Diogo Marques, Grupo 18
 * @version 1.0
 * @see Client
 */
public abstract class Terminal implements Serializable {

	/** Serial number for serialization. **/
	private static final long serialVersionUID = 202208091753L;
	/** The size of the {@code Terminal}'s identifier. **/
	private static final int ID_SIZE;

	private Network _network;
	/** This {@code String} is a sequence of numbers with size {@code ID_SIZE} that represents the {@code Network}'s
	 * balance. **/
	private final String _identifier;
	/** This {@code Client} is the  {@code Terminal}'s owner. **/
	private Client _client;
	/** This {@code Map} contains this {@code Terminal}'s friends (which are {@code Terminal}s).
   	 * The key is the {@code Terminal}'s id **/
	private Map<String, Terminal> _friends;

	private Map<String, Terminal> _whoFailedCommmunication;
	/** This {@code Map} contains this {@code Terminal}'s made communications.
   	 * The key is the {@code Communication}'s id **/
	private Map<Integer, Communication> _madeCommunications;
		/** This {@code Map} contains this {@code Terminal}'s received communications.
   	 * The key is the {@code Communication}'s id **/
	private Map<Integer, Communication> _receivedCommunications;
	/** The  {@code Terminal}'s state. By default, is {@code TerminalStateIdle}. **/
	private TerminalState _state;
	/** The  {@code Terminal}'s type. Can either be {@code BASIC} or {@code FANCY}}. **/
	private final TerminalType _type;

	private Map<Integer, Communication> _paidCommunications;
	private Map<Integer, Communication> _debtsCommunications;
	/** Saves the Communication on going right now */
	private Communication _onGoingCommunication;
	/** Saves the previous state */
	private TerminalState _stateBeforeCommunication;
	
	static {ID_SIZE = 6;}
	
	/**
	 * {@code Terminal}'s constructor. By default, {@code Terminal}'s mode is "IDLE".
	 * @param identifier is a {@code String} that represents this {@code Terminal}'s identifier.
	 * @param client is a {@code Client} that is this {@code Terminal}'s owner
	 **/
	public Terminal(TerminalType type, Network network, String identifier, Client client) {
		_identifier = identifier;
		_client = client;
		_friends = new TreeMap<String, Terminal>(new SortbyTerminalId());
		_whoFailedCommmunication = new TreeMap<String, Terminal>(new SortbyTerminalId());
		_madeCommunications = new TreeMap<Integer, Communication>(new SortbyCommunicationId());
		_receivedCommunications = new TreeMap<Integer, Communication>(new SortbyCommunicationId());
		_state = new TerminalStateIdle(this);
		_type = type;
		_network = network;

		_paidCommunications = new HashMap<Integer, Communication>();
		_debtsCommunications = new HashMap<Integer, Communication>();

		client.addTerminal(this);
	} 
	
	/**
	 * Terminal's static method. Checks if the given {@code String} is a valid {@code Terminal}'s identifier.
	 * @param identifier is a {@code String} that represents this {@code Terminal}'s identifier.
	 * @return {@code true} if the identifier is valid and {@code false} otherwise.
	 **/
	public static boolean isIdValid(String identifier) {
		char[] charArr = identifier.toCharArray();
		
		if (charArr.length != ID_SIZE) {return false;}
		for (char c: charArr) {
			if (!Character.isDigit(c)) {return false;}
		}
		return true;
	}
	
	/**
	 * @return The {@code TerminalState} of this terminal
	 */
	TerminalState getState() {return _state;}
	
	/**
	 * @return The {@code TerminalState} of this terminal
	 */
	public TerminalMode getMode() {return _state._mode;}
	
	public Communication getOnGoingCommunication() {
		return _onGoingCommunication;
	}
	
	/**
	 * Gives this {@code Terminal}'s identifier.
	 * @return the {@code String} that is this {@code Terminal}'s identifier.
	 **/
	public String getId() {return _identifier;}
	/**
	 * Gives this {@code Terminal}'s identifier.
	 * @return the {@code TerminalType} that is this {@code Terminal}'s type.
	 **/
	public TerminalType getType() {return _type;}
	
	public Map<Integer, Communication> getMadeCommunications() {
		return Collections.unmodifiableMap(_madeCommunications);
	}
	
	/**
	 * Gives this {@code Terminal}'s payments.
	 * @return the {@code int} that is this {@code Terminal}'s payments.
	 **/
	public double getPayments() {
		double num = 0;
		for (Communication communication: _paidCommunications.values()) {
			num += communication._cost;
		}
		return num;
	}
	/**
	 * Gives this {@code Terminal}'s debts.
	 * @return the {@code int} that is this {@code Terminal}'s debts.
	 **/
	public double getDebts() {
		double num = 0;
		for (Communication communication: _debtsCommunications.values()) {
			num += communication._cost;
		}
		return num;
	}
	
	public boolean isBalancePositive() {
		double balance = getPayments() - getDebts();
		return balance > 0;
	}
	/**
	 * Checks if this {@code Terminal} is used (payments or debts value isn't zero).
	 * @return {@code true} if this {@code Terminal} is used, and {@code false} otherwise.
	 **/
	public boolean isUsed() {
		return getPayments() != 0 || getDebts() != 0;
	}
	
	
	/**
	 * An auxiliare method that gives a {@code String} representing this {@code Terminal}'s friends.
	 * @return the {@code int} that is this {@code Terminal}'s number of friends.
	 **/
	private String friendsToString() {
		if (numberOfFriends() != 0) {
			String str = "|";
			for(Terminal friend: _friends.values()) {
				str = str.concat(String.format("%s,", friend.getId()));
			}
			return str.substring(0, str.length() - 1);
		}
		return "";
	}
	/**
	 * Gives the representation of this {@code Terminal} in a {@code String}.
	 * @return the {@code String} that is the representation of this {@code Terminal}.
	 **/
	@Override
	public String toString() {
		return String.format("%s|%s|%s|%s|%d|%d%s", getType().toString(), getId(),
		_client.getId(), getMode().toString(), Math.round(getPayments()), Math.round(getDebts()), friendsToString());   	
	}
	
	
	/**
	 * Gives this {@code Terminal}'s number of friends.
	 * @return the {@code int} that is this {@code Terminal}'s number of friends.
	 **/
	public int numberOfFriends() {return _friends.size();}
	/**
	 * Adds a {@code Terminal} as a friend.
	 * @param friend is the {@code Terminal} to add as a friend.  
	 **/
	public void addFriend(Terminal friend) {
		if (friend != this) {
			_friends.put(friend.getId(), friend);
		}
	}
	/**
	 * Removes a friend (which is a {@code Terminal}).
	 * @param friend is the friend of this {@code Terminal}. 
	 **/
	public void removeFriend(Terminal friend) {
		_friends.remove(friend.getId());
	}

	public boolean isFriend(Terminal terminal) {
		return _friends.containsKey(terminal.getId());
	}
	

	void setState(TerminalState terminalState) {_state = terminalState;}
	/** 
	 * Terminal.setOnIdle() ---> TerminalState.setOnIdle() <p>
	 * Changes this {@code Terminal}'s state to {@code IDLE}. 
	 */
	public boolean setOnIdle() {return _state.setOnIdle();}
	/** Changes this {@code Terminal}'s state to {@code SILENCE}. */
	public boolean setOnSilent() {return _state.setOnSilent();}
	/** Changes this {@code Terminal}'s state to {@code OFF}. */
	public boolean turnOff() {return _state.turnOff();}
	
	/** 
	 * Terminal.canEndCurrentCommunication() ---> TerminalState.canEndCurrentCommunication() <p>
	 * Checks if this {@code Terminal} can end the current interactive communication.
	 * @return {@code true} if this {@code Terminal} is busy (i.e., it has an active interactive communication) and
	 * {@code false} otherwise.
	 **/
	public boolean canEndCurrentCommunication() {return _state.canEndCurrentCommunication();}
	/**
	 * Checks if this {@code Terminal} can start a new communication.
	 * @return {@code true} if this {@code Terminal} is neither off neither busy, {@code false} otherwise.
	 **/
	public boolean canStartCommunication() {return _state.canStartCommunication();}

	public boolean isSupportedCommunication(CommunicationType communicationType) {
		return !(_type == TerminalType.BASIC && communicationType == CommunicationType.VIDEO);
	}
	
	
	public void makePayment(int idCommunication){
		Communication communication = _debtsCommunications.get(idCommunication);
		communication.setIsPaid(true);
		_debtsCommunications.remove(idCommunication);
		_paidCommunications.put(idCommunication, communication);
		
		_client.makePayment(communication);
	}
	
	/**
	 * Terminal.makeSMS() ---> TerminalState.makeSMS() ---> Terminal.doMakeSMS() ---> <p>
	 * Terminal.acceptSMS() ---> TerminalState.acceptSMS() ---> Terminal.doAcceptSMS()
	 * @param to
	 * @param message
	 */
	public void makeSMS(Terminal to, String message) {
		if (to != this) {
			_state.makeSMS(to, message);
		}
	}

	void doMakeSMS(Terminal to, String message) {
		to.acceptSMS(this, message);
	}

	protected void acceptSMS(Terminal from, String message) {_state.acceptSMS(from, message);}

	void doAcceptSMS(Terminal from, String message) {
		TextCommunication communication = new TextCommunication(from, this, message);
		communication.computeCost(_client.getTariffPlan());
		from.addCommunication(communication);
		this.addCommunication(communication);
		
		from.addDebtsCommunication(communication);
	}
	
	public void makeVoiceCall(Terminal to) {
		if (to != this) {
			_state.makeVoiceCall(to);
		}
	}
	
    void doMakeVoiceCall(Terminal to) {
		to.acceptVoiceCall(this);
    }
	
	protected void acceptVoiceCall(Terminal from) {_state.acceptVoiceCall(from);}
	
	void doAcceptVoiceCall(Terminal from) {
		VoiceCommunication communication = new VoiceCommunication(from, this);
		from.addCommunication(communication);
		from.setOngoingCommunication(communication);
		from.saveState();
		from.setState(new TerminalStateBusy(from));

		this.addCommunication(communication);
		this.setOngoingCommunication(communication);
		this.saveState();
		this.setState(new TerminalStateBusy(this));
	}

	public abstract void makeVideoCall(Terminal to);
	
	void doMakeVideoCall(Terminal to) {
		to.acceptVideoCall(this);
    }

	protected abstract void acceptVideoCall(Terminal from);
	
    void doAcceptVideoCall(Terminal from) {
		VideoCommunication communication = new VideoCommunication(from, this);
		from.addCommunication(communication);
		from.setOngoingCommunication(communication);
		from.saveState();
		from.setState(new TerminalStateBusy(from));

		this.addCommunication(communication);
		this.setOngoingCommunication(communication);
		this.saveState();
		this.setState(new TerminalStateBusy(this));
    }


	public double endOngoingCommunication(int size) {return _state.endOngoingCommunication(size);}
	

	double doEndOngoingCommunication(int size) {
		_onGoingCommunication.setIsOngoing(false);
		_onGoingCommunication.setSize(size);
		double cost = _onGoingCommunication.computeCost(_client.getTariffPlan());
		
		Terminal to = _onGoingCommunication.getTo();
		to.setOngoingCommunication(null);
		to.loadState();
		to.notify(new Notification(to, NotificationType.B2I));
		
		addDebtsCommunication(_onGoingCommunication);
		this.setOngoingCommunication(null);
		this.loadState();
		this.notify(new Notification(this, NotificationType.B2I));

		return cost;
	}


	void setOngoingCommunication(Communication communication) {
		_onGoingCommunication = communication;
	}
	
	void addDebtsCommunication(Communication communication) {
		_debtsCommunications.put(communication.getId(), communication);
		_client.addDebtsCommunication(communication);
	}

	void addCommunication(Communication communication) {
		int id = communication.getId();
		if (this == communication.getFrom()) {
			_madeCommunications.put(id, communication);
			_client.addMadeCommunication(communication);
		}
		if (this == communication.getTo()) {
			_receivedCommunications.put(id, communication);
			_client.addReceivedCommunication(communication);
		}

		if (!_network.allCommunications().containsKey(id)) {
			_network.addCommunication(communication);
		}
	}
	
	private void saveState() {
		_stateBeforeCommunication = _state;
	}
	private void loadState() {
		_state = _stateBeforeCommunication;
	}

	boolean initiatedCommunication() {
		if (_onGoingCommunication != null) {
			return _onGoingCommunication.getFrom() == this;
		}
		return false;
	}

	void addWhoFailedCommunication(Terminal terminal) {
		_whoFailedCommmunication.put(terminal.getId(), terminal);
	}


	private void notifyClient(Notification notification) {
		if (_client.getReceiveNotifications()) {
			_client.addNotification(notification);
		}
	}

	void notify(Notification notification) {
		for (Terminal terminal: _whoFailedCommmunication.values()) {
			terminal.notifyClient(notification);
		}
		_whoFailedCommmunication.clear();
	}
}
