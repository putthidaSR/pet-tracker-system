package tcss559.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Model class represents the structure in pet_vaccination_details table in the paw_tracker database.
 */
@Entity
@Table(name = "pet_vaccination_details")
public class PetVaccinationDetail {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
    private int id;
	
	@Column(name = "vaccination_name")
	private String vaccinationName;
	
	@Column(name = "immunization_date")
	private Date immunizationDate;
	
	@Column(name = "veterinarian_name")
	private String veterinarianName;
	
	@Column(name = "veterinarian_contact")
	private String veterinarianContact;
	
	@ManyToOne
    @JoinColumn(name = "pet_vaccination_id")
	private PetVaccination petVaccination;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getVaccinationName() {
		return vaccinationName;
	}

	public void setVaccinationName(String vaccinationName) {
		this.vaccinationName = vaccinationName;
	}

	public Date getImmunizationDate() {
		return immunizationDate;
	}

	public void setImmunizationDate(Date immunizationDate) {
		this.immunizationDate = immunizationDate;
	}

	public String getVeterinarianName() {
		return veterinarianName;
	}

	public void setVeterinarianName(String veterinarianName) {
		this.veterinarianName = veterinarianName;
	}

	public String getVeterinarianContact() {
		return veterinarianContact;
	}

	public void setVeterinarianContact(String veterinarianContact) {
		this.veterinarianContact = veterinarianContact;
	}

	public PetVaccination getPetVaccination() {
		return petVaccination;
	}

	public void setPetVaccination(PetVaccination petVaccination) {
		this.petVaccination = petVaccination;
	}
	
}
