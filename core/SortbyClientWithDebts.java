package prr.core;

import java.io.Serializable;
import java.util.Comparator;

public class SortbyClientWithDebts implements Comparator<Client>, Serializable {

	@Override
	public int compare(Client arg0, Client arg1) {
			return (int) Math.round(arg1.getDebts() - arg0.getDebts());
	}
}
