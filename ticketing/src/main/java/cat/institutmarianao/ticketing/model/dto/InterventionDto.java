package cat.institutmarianao.ticketing.model.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public class InterventionDto extends ActionDto implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final int MAX_DESCRIPTION = 500;

	/* Validation */
	@Positive
	private int hours;

	/* Validation */
	@NotBlank
	@Size(max = InterventionDto.MAX_DESCRIPTION)
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
