package prr.core;
 
import java.io.Serializable;
import java.util.TreeMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Client implements Serializable {

    private final String _identifier;
    private final String _name;
    private final int _taxIdentificationNumber;
    private boolean _receiveNotifications;

    private Map<String, Terminal> _terminals;
    private Map<Integer, Communication> _madeCommunications;
    private Map<Integer, Communication> _receivedCommunications;
    private List<Notification> _notifications;
    private TariffPlan _plan;
    
    private Map<Integer, Communication> _paidCommunications;
    private Map<Integer, Communication> _debtsCommunications;

    int _videoCommunicationCounter;
    int _textCommunicationCounter;
    
    public Client(String id, String name, int taxNumber) {
        
        _name = name;
        _taxIdentificationNumber = taxNumber;
        _identifier = id;
        _receiveNotifications = true;
        _notifications = new ArrayList<Notification>();
        _plan = new NormalTariffPlan();

        _terminals = new TreeMap<String, Terminal>(new SortbyTerminalId());
        _madeCommunications = new TreeMap<Integer, Communication>(new SortbyCommunicationId());
        _receivedCommunications = new TreeMap<Integer, Communication>(new SortbyCommunicationId());

        _paidCommunications = new HashMap<Integer, Communication>();
        _debtsCommunications = new HashMap<Integer, Communication>();
    }
    
    public String getId() {
        return _identifier;
    }

    public Map<String, Terminal> getTerminals() {
        return Collections.unmodifiableMap(_terminals);
    }
    public boolean getReceiveNotifications() {
        return _receiveNotifications;
    }
    public TariffPlan getTariffPlan() {
        return _plan;
    }
    public ClientLevel getClientLevel() {
        return _plan._level;
    }
    public Map<Integer, Communication> getMadeCommunications() {
        return Collections.unmodifiableMap(_madeCommunications);
    }
    public Map<Integer, Communication> getReceivedCommunications() {
        return Collections.unmodifiableMap(_receivedCommunications);
    }
    
    public List<Notification> getNotifications() {
        return Collections.unmodifiableList(_notifications);
    }
    
    public double getDebts() {
        double num = 0;
        for (Communication communication: _debtsCommunications.values()) {
            num += communication._cost;
        }
        return num;
    }
    public double getPayments() {
        double num = 0;
        for (Communication communication: _paidCommunications.values()) {
            num += communication._cost;
        }
        return num;
    }
    
    public boolean isWithDebt() {
        return getDebts() > 0;
    }

    public boolean isBalanceNegative() {
        return getBalance() < 0; 
    }

    public void setReceiveNotifications(boolean bool) {
        _receiveNotifications = bool;
    }
    public void setTariffPlan(TariffPlan plan) {
        _plan = plan;
        _videoCommunicationCounter = 0;
        _textCommunicationCounter = 0;
    }
    public void addTerminal(Terminal terminal) {
        _terminals.put(terminal.getId(), terminal);
    }

    void addMadeCommunication(Communication communication) {
        _madeCommunications.put(communication.getId(), communication);
    }

    void addReceivedCommunication(Communication communication) {
        _receivedCommunications.put(communication.getId(), communication);
    }

    void addDebtsCommunication(Communication communication) {
        if (communication.getType() == CommunicationType.TEXT) {
            _textCommunicationCounter++;
        } else {
            _textCommunicationCounter = 0;
        }
        if (communication.getType() == CommunicationType.VIDEO) {
            _videoCommunicationCounter++;
        } else {
            _videoCommunicationCounter = 0;
        }

        _debtsCommunications.put(communication.getId(), communication);
        checkIfDowngrade();
        checkIfUpgrade();
    }

    void makePayment(Communication communication) {
        _debtsCommunications.remove(communication.getId());
        _paidCommunications.put(communication.getId(), communication);
        checkIfUpgrade();
    }

    void addNotification(Notification notification) {
        if (!_notifications.contains(notification)) {
        _notifications.add(notification);
        
        }
    }

    void checkIfDowngrade() {
		_plan.checkIfDowngrade(this);
	}
    void checkIfUpgrade() {
        _plan.checkIfUpgrade(this);
    }

    @Override
    public String toString() {
        String strNotifications = (_receiveNotifications) ? "YES" : "NO";

        return String.format("CLIENT|%s|%s|%d|%s|%s|%d|%d|%d", _identifier, _name, _taxIdentificationNumber,
        getClientLevel(), strNotifications, _terminals.size(), Math.round(getPayments()), Math.round(getDebts()));
    }

    public double getBalance() {
       return getPayments() - getDebts();
    }

    public void cleanNotifications() {
        _notifications.clear();
    }
}
