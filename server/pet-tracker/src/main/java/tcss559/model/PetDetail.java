package tcss559.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Model class represents the structure in pet_detail table in the paw_tracker database.
 */
@Entity
@Table(name = "pet_detail")
public class PetDetail {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pet_detail_id")
    private int id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "species")
	private String species;
	
	@Column(name = "age")
	private String age;
	
	@Column(name = "breed")
	private String breed;
	
	@Column(name = "color")
	private String color;
	
	@Column(name = "gender")
	private String gender;

	@Column(name = "active")
	private boolean active;
	
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pet_id")
	private Pet pet;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSpecies() {
		return species;
	}

	public void setSpecies(String species) {
		this.species = species;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getBreed() {
		return breed;
	}

	public void setBreed(String breed) {
		this.breed = breed;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Pet getPet() {
		return pet;
	}

	public void setPet(Pet pet) {
		this.pet = pet;
	}
	
	
}
