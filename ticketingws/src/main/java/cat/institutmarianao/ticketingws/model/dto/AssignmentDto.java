package cat.institutmarianao.ticketingws.model.dto;

import java.io.Serializable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class AssignmentDto extends ActionDto implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final int MIN_PRIORITAT = 1;
	public static final int MAX_PRIORITAT = 9;

	/* Validation */
	@NotEmpty
	private String technician;

	@NotNull
	@Min(MIN_PRIORITAT)
	@Max(MAX_PRIORITAT)
	private Integer priority;

	public AssignmentDto() {
		// POJO constructor
	}

	public String getTechnician() {
		return technician;
	}

	public void setTechnician(String technician) {
		this.technician = technician;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
}
