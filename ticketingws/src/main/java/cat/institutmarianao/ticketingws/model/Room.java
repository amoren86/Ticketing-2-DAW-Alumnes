/**
 *
 */
package cat.institutmarianao.ticketingws.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import cat.institutmarianao.ticketingws.validation.groups.OnUserCreate;

/**
 * A name, there the employee is
 */
@Entity
@Table(name = "rooms")
public class Room implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final int MAX_ROOM = 100;

	/* Validation */
	@Positive(groups = OnUserCreate.class)
	/* JPA */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Long id;

	/* Validation */
	@Size(max = MAX_ROOM)
	@NotBlank(groups = OnUserCreate.class)
	/* JPA */
	@Column(unique = true, nullable = false)
	private String name;

	public Room() {
		// POJO constructor
	}

	public Room(@NotEmpty String name) {
		this.name = name;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Room other)) {
			return false;
		}
		return Objects.equals(id, other.id);
	}

}
