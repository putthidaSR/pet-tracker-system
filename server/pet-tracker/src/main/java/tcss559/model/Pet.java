package tcss559.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Model class represents the structure in pet table in the paw_tracker database.
 */
@Entity
@Table(name = "pet")
public class Pet {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
    private int id;
	
	@Column(name = "rfid_number")
	private String rfidNumber;
	
	@Column(name = "registration_time")
	private Date registrationTime;
	
	@Column(name = "modification_time")
	private Date modificationTime;
	
	@ManyToOne
    @JoinColumn(name = "user_id")
	private User user;
	
	@OneToMany(mappedBy = "pet", cascade = CascadeType.ALL)
	private Set<PetLocation> petLocations;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRfidNumber() {
		return rfidNumber;
	}

	public void setRfidNumber(String rfidNumber) {
		this.rfidNumber = rfidNumber;
	}

	public Date getRegistrationTime() {
		return registrationTime;
	}

	public void setRegistrationTime(Date registrationTime) {
		this.registrationTime = registrationTime;
	}

	public Date getModificationTime() {
		return modificationTime;
	}

	public void setModificationTime(Date modificationTime) {
		this.modificationTime = modificationTime;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Set<PetLocation> getPetLocations() {
		return petLocations;
	}

	public void setPetLocations(Set<PetLocation> petLocations) {
		this.petLocations = petLocations;
	}
	
}
