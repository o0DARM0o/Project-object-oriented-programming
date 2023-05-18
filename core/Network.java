package prr.core;

import java.io.Serializable;
import java.io.IOException;
import prr.core.exception.UnrecognizedEntryException;
import java.util.Collections;
import java.util.TreeMap;
import java.util.Map;

/**
 * {@code Network} is a public class that manages {@code Client}s and {@code Terminal}s.
 * Implements the interface {@code Serializable}. <p> 
 * {@code Network} can register a certain {@code Client} or {@code Terminal},
 * and can get one or more instances of both this classes. {@code Network} also stores a balance and can return it.
 * @author Leonardo Pinheiro, Diogo Marques, Grupo 18
 * @version 1.0
 * @see Client 
 * @see Terminal
 **/
public class Network implements Serializable {

  /** Serial number for serialization. **/
  private static final long serialVersionUID = 202208091753L;
  /** The name of the file associated the network. **/
  private  String _associatedFile;
  /** This {@code Map} contanis all {@code Client}s used by it's Network{@code Network}.
   * The key is the {@code Client}'s id **/
  private Map<String, Client> _clients;
  /** This {@code Map} contanis all {@code Terminal}s used by it's {@code Network}.
   * The key is the {@code Terminal}'s id **/
  private Map<String, Terminal> _terminals;
    /** This {@code Map} contanis all {@code Communications}s used by it's {@code Network}.
   * The key is the {@code Communications}'s id **/
  private Map<Integer, Communication> _communications;


  /**
   * {@code Network}'s constructor. Only activates it's atributes
   **/
  public Network() {
  	_terminals = new TreeMap<String, Terminal>(new SortbyTerminalId());
  	_clients = new TreeMap<String, Client>(new SortbyClientId());
    _communications = new TreeMap<Integer, Communication>(new SortbyCommunicationId());
  }
  
  /**
   * Registers a {@code Client} in this {@code Network}, using an id, it's name and it's taxNumber.
   * @param id is a {@code String} that represents a {@code Client}'s id.
   * @param name is a {@code String} that represents a {@code Client}'s name.
   * @param taxNumber is a {@code int} that represents a {@code Client}'s id.
   * @throws IllegalArgumentException when an argument is invalid.
   * @return the {@code Client} that was registered.
   **/
  public Client registerClient(String id, String name, int taxNumber) throws IllegalArgumentException {
    Client client = new Client(id, name, taxNumber);
  	_clients.put(id, client);
    return client;
  }
  /**
   * Registers a {@code Terminal} in this {@code Network}, using a type, an id,
   * and the clientId (the id of the {@code Client} associated with this {@code Terminal}).
   * @param type is a {@code String} that represents a {@code Terminal}'s type ({@code BASIC} or {@code FANCY}).
   * @param id is a {@code String} that represents a {@code Terminal}'s id.
   * @param clientId is a {@code String} that represents the id of the {@code Client} associated with this
   * {@code Terminal}.
   * @throws IllegalArgumentException when an argument is invalid.
   * @return the {@code Terminal} that was registered.
   **/
  public Terminal registerTerminal(String typeString, String id, String clientId) throws IllegalArgumentException {
    Client client = getClient(clientId);
    TerminalType type = TerminalType.valueOf(typeString);
    Terminal terminal = null;
    if (type == TerminalType.BASIC) {
      terminal = new BasicTerminal(this, id, client);
    }
    if (type == TerminalType.FANCY) {
      terminal = new FancyTerminal(this, id, client);
    }

    _terminals.put(id, terminal);
    return terminal;
  } 

  /**
   * Gives the {@code Client} with certain identifier.
   * @param identifier is a {@code String} that represents a {@code Client}'s id'.
   * @return the {@code Client} with that identifier.
   **/
  public Client getClient(String identifier) {
	  return _clients.get(identifier);
  }
  /**
   * Gives the {@code Terminal} with certain identifier.
   * @param identifier is a {@code String} that represents a {@code Terminal}'s id'.
   * @return the {@code Terminal} with that identifier.
   **/
  public Terminal getTerminal(String identifier) {
    return _terminals.get(identifier);
  }
  /**
   * Gives {@code Network}'s  global payments.
   * @return the {@code double} that's that value.
   **/
  public double getPayments() {
    double num = 0;
    for (Terminal terminal: _terminals.values()) {
      num += terminal.getPayments();
    }
    return num;
  }
  /**
   * Gives {@code Network}'s  global debts.
   * @return the {@code double} that's that value.
   **/
  public double getDebts() {
    double num = 0;
    for (Terminal terminal: _terminals.values()) {
      num += terminal.getDebts();
    }
    return num;
  }
  
  /**
   * Returns a {@code unmodifiableMap} with all the {@code Network}'s {@code Client}s.
   * @return the {@code unmodifiableMap} that has all the {@code Network}'s {@code Client}s.
   **/
  public Map<String, Client> allClients() {
  	return Collections.unmodifiableMap(_clients);
  }
  /**
   * Returns a {@code unmodifiableMap} with all the {@code Network}'s {@code Terminal}s.
   * @return the {@code unmodifiableMap} that has all the {@code Network}'s {@code Terminal}s.
   **/
  public Map<String, Terminal> allTerminals() {
  	return Collections.unmodifiableMap(_terminals);
  }
  /**
   * Returns a {@code unmodifiableMap} with all the {@code Network}'s {@code Communication}s.
   * @return the {@code unmodifiableMap} that has all the {@code Network}'s {@code Communication}s.
   **/
  public Map<Integer, Communication> allCommunications() {
  	return Collections.unmodifiableMap(_communications);
  }
  
  /**
   * Adds a friend to a certain {@code Terminal}.
   * @param terminalId is a {@code String} that represents a {@code Terminal}'s id'.
   * @param friendId is a {@code String} that represents a friend's id (which is a {@code Terminal}'s id).
   * @throws IllegalArgumentException if some argument is invalid.
   **/
  public void addFriend(String terminalId, String friendId) throws IllegalArgumentException {
    if (!Terminal.isIdValid(friendId) || !Terminal.isIdValid(terminalId)) {
      throw new IllegalArgumentException();
    }
    getTerminal(terminalId).addFriend(getTerminal(friendId));
  }
  /** 
   * Gives the name of the file where this {@code Network} is saved.
   * @return the {@code String} that is the name the file where this {@code Network} is saved.
  */
  public String getAssociatedFile() {
    return _associatedFile;
  }
  /**
   * Sets the name of the file where this {@code Network} is saved as the given name.
   * @param newName is a {@code String} that represents the new name of the file where this {@code Network} is saved.
   */
  public void setAssociatedFile(String newName) {
    _associatedFile = newName;
  }

  /**
   * Adds the given communication to this {@code Network}'s communications.
   * @param communication is the {@code Communication} to be added.
   */
  void addCommunication(Communication communication) {
    _communications.put(communication.getId(), communication);
  }

  /**
   * Read text input file and create corresponding domain entities.
   * 
   * @param filename name of the text input file
   * @throws UnrecognizedEntryException if some entry is not correct
   * @throws IOException if there is an IO erro while processing the text file
   */
  void importFile(String filename) throws UnrecognizedEntryException, IOException {
    Parser parser = new Parser(this);
    try {
      parser.parseFile(filename);
    } catch (UnrecognizedEntryException uee) {
      throw uee;
    } catch (IOException ioe) {
        throw new IOException();
    }
  }
}
