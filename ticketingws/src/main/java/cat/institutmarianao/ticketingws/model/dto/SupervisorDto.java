package cat.institutmarianao.ticketingws.model.dto;

import java.io.Serializable;
import java.util.Objects;

public class SupervisorDto extends TechnicianDto implements Serializable {

	private static final long serialVersionUID = 1L;

	public SupervisorDto() {
		// POJO Contructor
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof SupervisorDto close)) {
			return false;
		}
		return Objects.equals(username, close.username);
	}
}
