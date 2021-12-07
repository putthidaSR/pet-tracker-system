package tcss559.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Model class represents the structure in pet_location table in the paw_tracker database.
 */
@Entity
@Table(name = "pet_location")
public class PetLocation {
	
	@Id
    private int id;
	
	@ManyToOne
    @JoinColumn(name = "pet_id")
	private Pet pet;
	
	@Column(name = "longitude")
    private double longitude;
	
	@Column(name = "latitude")
    private double latitude;
	
	@Column(name = "last_seen")
	private Date lastSeenDate;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public Pet getPet() {
		return pet;
	}

	public void setPet(Pet pet) {
		this.pet = pet;
	}

	public Date getLastSeenDate() {
		return lastSeenDate;
	}

	public void setLastSeenDate(Date lastSeenDate) {
		this.lastSeenDate = lastSeenDate;
	}
	
}
