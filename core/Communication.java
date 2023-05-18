package prr.core;

import java.io.Serializable;

public abstract class Communication implements Serializable {
	private static int _counter;
	private final int _id;
	private Terminal _from;
	private Terminal _to;
	private boolean _isPaid;

	protected double _cost;
	protected boolean _isOngoing;  

	private CommunicationType _type;

	static {_counter = 0;}

    public Communication(Terminal from, Terminal to, CommunicationType type) {
		_from = from;
		_to = to;
		_type = type;
		_counter++;
		_id = _counter;
		_isOngoing = !(type == CommunicationType.TEXT);
    }
	
	protected abstract double computeCost(TariffPlan plan);
	protected abstract int getSize();

	protected void setSize(int size) {}
	
	public Integer getId() {return _id;}
	
	CommunicationType getType() {return _type;}

	Terminal getFrom() {return _from;}
	
	Terminal getTo() {return _to;}

	void setIsOngoing(boolean bool) {_isOngoing = bool;}

	public String toString() {
		return String.format("%s|%d|%s|%s|%d|%d|%s", _type.toString(), _id,
		_from.getId(), _to.getId(), getSize(), Math.round(_cost), isOngoingToString());
	}

	private String isOngoingToString() {
		return (_isOngoing) ? "ONGOING" : "FINISHED";
	}

	public boolean isPaid() {return _isPaid;}

	public boolean isOngoing() {return _isOngoing;}

	void setIsPaid(boolean bool) {
		_isPaid = bool;
	}
}
