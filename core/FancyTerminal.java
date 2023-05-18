package prr.core;

public class FancyTerminal extends Terminal {
    
    public FancyTerminal(Network network, String identifier, Client client) {
        super(TerminalType.FANCY, network, identifier, client);
    }

    @Override
    public void makeVideoCall(Terminal to) {
        if (to != this) {
            (super.getState()).makeVideoCall(to);
        }
    }

    @Override
    protected void acceptVideoCall(Terminal from) {super.getState().acceptVideoCall(from);}
}
