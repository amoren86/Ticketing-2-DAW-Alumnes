package cat.institutmarianao.ticketing.model.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

/**
 * <p>
 * A technician who supervises other ones. It performs assignments of tickets to
 * another technicians. He can also close tickets
 * </p>
 * <p>
 * A supervidor has role <code>User.Role.SUPERVISOR</code> and his authorities
 * list contains <code>{"SUPERVISOR","TECHNICIAN"}</code>, since a supervisor is
 * also a technician.
 * </p>
 *
 * @see UserDto
 * @see Role
 * @see InterventionDto
 * @see CloseDto
 */
public class SupervisorDto extends TechnicianDto {

	private static final long serialVersionUID = 1L;

	public SupervisorDto() {
	}

	public SupervisorDto(@NotBlank @Size(min = MIN_USERNAME, max = MAX_USERNAME) String username,
			@NotBlank @Size(min = MIN_PASSWORD) String password,
			@NotBlank @Size(min = MIN_FULL_NAME, max = MAX_FULL_NAME) String fullName,
			@Min(0) @Max(MAX_EXTENSION) Integer extension, @Positive Long companyId) {
		super(username, password, fullName, extension, companyId);
		super.role = Role.SUPERVISOR;
	}

}
