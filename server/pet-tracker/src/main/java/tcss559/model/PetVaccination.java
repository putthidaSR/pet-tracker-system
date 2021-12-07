package tcss559.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

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
	
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pet_id")
	private Pet pet;
	
	@OneToMany(mappedBy = "petVaccination", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<PetVaccinationDetail> petVaccinationDetails;
	
	@Column(name = "creation_time")
	private Date creationTime;
	
	@Column(name = "modification_time")
	private Date modificationTime;

	public Set<PetVaccinationDetail> getPetVaccinationDetails() {
		return petVaccinationDetails;
	}

	public void setPetVaccinationDetails(Set<PetVaccinationDetail> petVaccinationDetails) {
		this.petVaccinationDetails = petVaccinationDetails;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public Pet getPet() {
		return pet;
	}

	public void setPet(Pet pet) {
		this.pet = pet;
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

}
