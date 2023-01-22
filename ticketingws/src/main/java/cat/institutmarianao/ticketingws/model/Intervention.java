package cat.institutmarianao.ticketingws.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

/**
 * Nova intervenció a una determinada date per resoldre el ticket, indicant les
 * hours dedicades i una breu descripció de l'actuació
 *
 * Durant la creació de la intervenció: - Les hours han de ser >= 1, en cas
 * contrari indicar 1 - Si la descripció és nul·la substituir per un text buit
 *
 *
 * No es pot actualitzar cap atribut a un valor incorrecte
 *
 */
@Entity
@DiscriminatorValue(Action.INTERVENTION)
public class Intervention extends Action implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final int MAX_DESCRIPTION = 500;

	/* Validation */
	@Positive
	private int hours;

	/* Validation */
	@NotBlank
	@Size(max = MAX_DESCRIPTION)
	private String description;

	public Intervention() {
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

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Intervention other)) {
			return false;
		}
		return Objects.equals(id, other.id);
	}
}
