package prr.core;

public class BasicTerminal extends Terminal {
    
    public BasicTerminal(Network network, String identifier, Client client) {
        super(TerminalType.BASIC, network, identifier, client);
    }

    @Override
    public void makeVideoCall(Terminal to) {}

    @Override
    protected void acceptVideoCall(Terminal from) {}
}