package cat.institutmarianao.ticketingws.model.dto;

import java.io.Serializable;
import java.util.Objects;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import cat.institutmarianao.ticketingws.validation.groups.OnUserCreate;

public class EmployeeDto extends UserDto implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final int MAX_PLACE = 100;

	/* JSON */
//	// @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
//	/* Allow identify the room only by its id field */
//	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
//	@JsonIdentityReference(alwaysAsId = true)
//	/* Validation */
//	@NotNull(groups = OnUserCreate.class)
//	@Valid
//	private RoomDto room;

	@NotNull(groups = OnUserCreate.class)
	private Long roomId;

	/* JSON */
	// @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	/* Validation */
	@Size(max = MAX_PLACE)
	@NotBlank(groups = OnUserCreate.class)
	private String place;

	public EmployeeDto() {
		// POJO constructor
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

	public void setPlace(String lloc) {
		place = lloc;
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
		if (!(obj instanceof EmployeeDto employee)) {
			return false;
		}
		return Objects.equals(username, employee.username);
	}
}
