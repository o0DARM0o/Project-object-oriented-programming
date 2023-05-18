package prr.core;

public class Notification {

    private NotificationType _type;
    private Terminal _terminal;

    public Notification(Terminal terminal, NotificationType type) {
        _terminal = terminal;
        _type = type;
    }

    @Override
    public String toString() {
        return String.format("%s|%s", _type.toString(), _terminal.getId());
    }

    NotificationType getType() {return _type;}

    Terminal getTerminal() {return _terminal;}

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Notification) {
            return _type == ((Notification) obj).getType() && _terminal == ((Notification) obj).getTerminal();
        }
        return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + _type.hashCode();
        result = prime * result + _terminal.hashCode();
        return result;
    }
}
