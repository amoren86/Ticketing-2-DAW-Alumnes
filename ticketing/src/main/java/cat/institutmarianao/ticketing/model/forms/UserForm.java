package cat.institutmarianao.ticketing.model.forms;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import cat.institutmarianao.ticketing.model.dto.EmployeeDto;
import cat.institutmarianao.ticketing.model.dto.SupervisorDto;
import cat.institutmarianao.ticketing.model.dto.TechnicianDto;
import cat.institutmarianao.ticketing.model.dto.UserDto;
import cat.institutmarianao.ticketing.model.dto.UserDto.Role;
import cat.institutmarianao.ticketing.validation.groups.OnUserCreate;
import cat.institutmarianao.ticketing.validation.groups.OnUserUpdate;

public class UserForm {

	@NotBlank(groups = { OnUserCreate.class, OnUserUpdate.class })
	@Size(min = UserDto.MIN_USERNAME, max = UserDto.MAX_USERNAME, groups = { OnUserCreate.class, OnUserUpdate.class })
	protected String username;
	@NotNull(groups = { OnUserCreate.class, OnUserUpdate.class })
	protected Role role;

	@NotBlank(groups = OnUserCreate.class)
	@Size(min = UserDto.MIN_PASSWORD, groups = OnUserCreate.class)
	protected String password;

	@NotBlank(groups = { OnUserCreate.class, OnUserUpdate.class })
	@Size(min = UserDto.MIN_FULL_NAME, max = UserDto.MAX_FULL_NAME, groups = { OnUserCreate.class, OnUserUpdate.class })
	protected String fullName;

	@NotNull(groups = { OnUserCreate.class, OnUserUpdate.class })
	@Min(value = 0, groups = { OnUserCreate.class, OnUserUpdate.class })
	@Max(value = UserDto.MAX_EXTENSION, groups = { OnUserCreate.class, OnUserUpdate.class })
	protected Integer extension;

	@NotBlank(groups = OnUserCreate.class)
	@Size(min = UserDto.MIN_PASSWORD, groups = OnUserCreate.class)
	protected String verify;

	@Positive(groups = { OnUserCreate.class, OnUserUpdate.class })
	private Long roomId;

	private String place;

	@Positive(groups = { OnUserCreate.class, OnUserUpdate.class })
	private Long companyId;

	public UserForm() {
	}

	public UserForm(UserDto userDto) {
		username = userDto.getUsername();
		role = userDto.getRole();
		password = null;
		fullName = userDto.getFullName();
		extension = userDto.getExtension();
		verify = null;

		if (userDto instanceof EmployeeDto employeeDto) {
			roomId = employeeDto.getRoomId();
			place = employeeDto.getPlace();
		} else if (userDto instanceof TechnicianDto technicianDto) {
			companyId = technicianDto.getCompanyId();
		}
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public Integer getExtension() {
		return extension;
	}

	public void setExtension(Integer extension) {
		this.extension = extension;
	}

	public String getVerify() {
		return verify;
	}

	public void setVerify(String verify) {
		this.verify = verify;
	}

	public Long getRoomId() {
		return roomId;
	}

	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public UserDto getUserDto() {
		return switch (role) {
		case EMPLOYEE -> new EmployeeDto(username, password, fullName, extension, roomId, place);
		case SUPERVISOR -> new SupervisorDto(username, password, fullName, extension, companyId);
		case TECHNICIAN -> new TechnicianDto(username, password, fullName, extension, companyId);
		default -> throw new RuntimeException();
		};
	}
}
