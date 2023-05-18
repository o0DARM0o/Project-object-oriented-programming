package prr.core;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import prr.core.exception.ImportFileException;
import prr.core.exception.MissingFileAssociationException;
import prr.core.exception.UnavailableFileException;
import prr.core.exception.UnrecognizedEntryException;


/**
 * Manage access to network and implement load/save operations.
 */
public class NetworkManager {

  /** The network itself. */
  private Network _network = new Network();
   
  /** Gives the network.
   * @return the network of  this NetworkManager.
   */
  public Network getNetwork() {
    return _network;
  }
  
  /**
   * @param filename name of the file containing the serialized application's state
   *        to load.
   * @throws UnavailableFileException if the specified file does not exist or there is
   *         an error while processing this file.
   */
  public void load(String filename) throws UnavailableFileException {
    ObjectInputStream objIn = null;
    try {
      objIn = new ObjectInputStream(new FileInputStream(filename));
      _network = (Network) objIn.readObject();

    } catch (IOException | ClassNotFoundException e) {
      throw new UnavailableFileException(filename);

    } finally {
      if (objIn != null) {
        try {
          objIn.close();
        } catch (IOException e) {
          throw new UnavailableFileException(filename);
        }
      }
    }
  }
  
  /**
   * Saves the serialized application's state into the file associated to the current network.
   *
   * @throws MissingFileAssociationException if the current network does not have a file.
   * @throws FileNotFoundException if for some reason the file cannot be created or opened. 
   * @throws IOException if there is some error while serializing the state of the network to disk.
   */
  public void save() throws MissingFileAssociationException, FileNotFoundException, IOException {
    if (getAssociatedFile() == null) {
      throw new MissingFileAssociationException();
    }
    saveAs(getAssociatedFile());
  }
  
  /**
   * Saves the serialized application's state into the specified file. The current network is
   * associated to this file.
   *
   * @param filename the name of the file.
   * @throws FileNotFoundException if for some reason the file cannot be created or opened.
   * @throws IOException if there is some error while serializing the state of the network to disk.
   */
  public void saveAs(String filename) throws FileNotFoundException, IOException {
    ObjectOutputStream obOut = null;
    try {
      obOut = new ObjectOutputStream(new FileOutputStream(filename));
      obOut.writeObject(_network);
      setAssociatedFile(filename);
    } catch (FileNotFoundException fnfe) {
      throw fnfe;
    } catch (IOException ioe) {
      throw ioe;
    } finally {
      if (obOut != null) {
        obOut.close();
      }
    }
  }

  /**
   * Sets the name of the file associated with the {@code Network} as the given {@code String}.
   * @param filename is the name of the new file associated with the {@code Network}.
   **/
  public void setAssociatedFile(String filename) {
    _network.setAssociatedFile(filename);
  }
  public String getAssociatedFile() {
    return _network.getAssociatedFile();
  }

  /**
   * Read text input file and create domain entities..
   * 
   * @param filename name of the text input file
   * @throws ImportFileException
   */
  public void importFile(String filename) throws ImportFileException {
    try {
      _network.importFile(filename);
    } catch (IOException | UnrecognizedEntryException e) {
      throw new ImportFileException(filename, e);
    }
  }  
}
