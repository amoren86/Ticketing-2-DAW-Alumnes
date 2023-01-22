package cat.institutmarianao.ticketing.model.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

/**
 * <p>
 * A technical user, the one that can perform intervention actions. Can also
 * perform close actions only if he has any intervention in the referenced
 * ticket
 * </p>
 *
 * <p>
 * A technician has role <code>User.Role.TECHNICIAN</code> and his authorities
 * list contains <code>{"TECHNICIAN"}</code>
 * </p>
 *
 * @see UserDto
 * @see Role
 * @see InterventionDto
 * @see CloseDto
 */
public class TechnicianDto extends UserDto {

	private static final long serialVersionUID = 1L;

	@Positive
	private Long companyId;

	public TechnicianDto() {
	}

	public TechnicianDto(@NotBlank @Size(min = MIN_USERNAME, max = MAX_USERNAME) String username,
			@NotBlank @Size(min = MIN_PASSWORD) String password,
			@NotBlank @Size(min = MIN_FULL_NAME, max = MAX_FULL_NAME) String fullName,
			@Min(0) @Max(MAX_EXTENSION) Integer extension, @Positive Long companyId) {
		super(username, password, fullName, extension);
		this.companyId = companyId;
		super.role = Role.TECHNICIAN;
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
		return super.equals(obj);
	}
}
