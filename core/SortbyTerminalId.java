package prr.core;

import java.io.Serializable;
import java.util.Comparator;

public class SortbyTerminalId implements Comparator<String>, Serializable {

	@Override
	public int compare(String arg0, String arg1) {
			return arg0.compareTo(arg1);
	}
	
}
