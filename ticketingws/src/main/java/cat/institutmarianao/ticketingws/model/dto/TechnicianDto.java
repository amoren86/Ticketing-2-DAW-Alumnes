package cat.institutmarianao.ticketingws.model.dto;

import java.io.Serializable;
import java.util.Objects;

import javax.validation.constraints.NotNull;

import cat.institutmarianao.ticketingws.validation.groups.OnUserCreate;

public class TechnicianDto extends UserDto implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final int MAX_COMPANY = 100;

	/* JSON */
	// @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	/* Validation */
//	@Size(max = MAX_COMPANY)
//	@NotEmpty(groups = OnUserCreate.class)
//	private String company;

	/* JSON */
	// @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	/* Allow identify the room only by its id field */
//	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
//	@JsonIdentityReference(alwaysAsId = true)
//	/* Validation */
//	@NotNull(groups = OnUserCreate.class)
//	@Valid
//	private CompanyDto company;

	@NotNull(groups = OnUserCreate.class)
	private Long companyId;

	public TechnicianDto() {
		// POJO constructor
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
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
		if (!(obj instanceof TechnicianDto technician)) {
			return false;
		}
		return Objects.equals(username, technician.username);
	}
}
