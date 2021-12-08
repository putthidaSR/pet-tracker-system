package tcss559.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Model class represents the structure in pet_vaccination table in the paw_tracker database.
 */
@Entity
@Table(name = "pet_vaccination")
public class PetVaccination {	

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
    private int id;
	
	@Column(name = "pet_id")
	private int petId;
	
	@Column(name = "creation_time")
	private Date creationTime;
	
	@Column(name = "modification_time")
	private Date modificationTime;
	
	@Column(name = "vaccination_name")
	private String vaccinationName;
	
	@Column(name = "immunization_date")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date immunizationDate;
	
	@Column(name = "veterinarian_name")
	private String veterinarianName;
	
	@Column(name = "veterinarian_contact")
	private String veterinarianContact;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public int getPetId() {
		return petId;
	}

	public void setPetId(int petId) {
		this.petId = petId;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Date getModificationTime() {
		return modificationTime;
	}

	public void setModificationTime(Date modificationTime) {
		this.modificationTime = modificationTime;
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

}
