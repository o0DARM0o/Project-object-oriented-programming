package prr.core;

import java.io.Serializable;
import java.util.Comparator;

public class SortbyCommunicationId implements Comparator<Integer>, Serializable {

	@Override
	public int compare(Integer arg0, Integer arg1) {
		return arg0 - arg1;
	}
	
}
