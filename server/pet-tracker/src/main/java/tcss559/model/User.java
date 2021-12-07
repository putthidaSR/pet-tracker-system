package tcss559.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.*;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

/**
 * Model class represents the structure of "user" and "account_detail" tables in PawTracker database.
 * Note that user table has a one-to-one relationship with account_detail table.
 */
@Entity
@Table(name = "user")
@SecondaryTable(name = "account_detail", pkJoinColumns = @PrimaryKeyJoinColumn(name = "id"))
@DynamicUpdate(false)
public class User {

	public static final String ROLE_PET_OWNER = "PetOwner";
	public static final String ROLE_VETERINARIAN = "Veterinarian";
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
    private int id;
	
	@Column(name = "creation_time")
	private Date creationTime;
	
	@Column(name = "modification_time")
	private Date modificationTime;

	@Column(name = "login_name")
	private String loginName;
	
	@Column(name = "login_password")
	private String loginPassword;
	
	@Column(name = "role", table = "account_detail")
	private String role;
	
	@Column(name = "badge_number", table = "account_detail")
	private String badgeNumber;
	
	@Column(name = "email", table = "account_detail")
	private String email;
	
	@Column(name = "phone_number", table = "account_detail")
	private String phoneNumber;
	
	@Column(name = "address", table = "account_detail")
	private String address;
	
	@Column(name = "active", table = "account_detail")
	private boolean active;
	
	@Column(name = "confirmation_code", table = "account_detail")
	private int confirmationCode;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private Set<Pet> pets;
	
    // Default constructor
	public User() {
		super();
	}
	
	public User(int id) {
		this.id = id;
	}

	public User(int id, Date modificationTime, String role, String loginName, String badgeNumber, String email,
			String phoneNumber, String address, boolean active) {
		this.id = id;
		this.modificationTime = modificationTime;
		this.role = role;
		this.loginName = loginName;
		this.badgeNumber = badgeNumber;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.address = address;
		this.active = active;
	}
	
	/*
	 * Getters and Settings methods
	 */

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getBadgeNumber() {
		return badgeNumber;
	}

	public void setBadgeNumber(String badgeNumber) {
		this.badgeNumber = badgeNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	public int getConfirmationCode() {
		return confirmationCode;
	}

	public void setConfirmationCode(int confirmationCode) {
		this.confirmationCode = confirmationCode;
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

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getLoginPassword() {
		return loginPassword;
	}

	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}

	public Set<Pet> getPets() {
		return pets;
	}

	public void setPets(Set<Pet> pets) {
		this.pets = pets;
	}
	

}
