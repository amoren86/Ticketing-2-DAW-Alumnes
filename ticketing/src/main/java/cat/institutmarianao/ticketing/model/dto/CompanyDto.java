/**
 *
 */
package cat.institutmarianao.ticketing.model.dto;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

/**
 * A name, there the employee is
 */
public class CompanyDto implements Serializable {

	private static final long serialVersionUID = 1L;

	@Positive
	private Long id;

	@NotEmpty
	private String name;

	public CompanyDto() {
	}

	public CompanyDto(@Positive Long id) {
		this.id = id;
	}

	public CompanyDto(@Positive Long id, @NotEmpty String name) {
		this.id = id;
		this.name = name;
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
}
