package cat.institutmarianao.ticketingws.model.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import cat.institutmarianao.ticketingws.model.Intervention;

public class InterventionDto extends ActionDto implements Serializable {

	private static final long serialVersionUID = 1L;

	/* Validation */
	@Positive
	private int hours;

	/* Validation */
	@NotBlank
	@Size(max = Intervention.MAX_DESCRIPTION)
	private String description;

	public InterventionDto() {
		// POJO constructor
	}

	public int getHours() {
		return hours;
	}

	public void setHours(int hours) {
		this.hours = hours;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String descripcio) {
		description = descripcio.trim();
	}
}
