package tcss559.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Model class represents the structure in pet_medical table in the paw_tracker database.
 */
@Entity
@Table(name = "pet_medical")
public class PetMedical {
	
	@Id
    private int id;
	
	@Column(name = "pet_id")
    private int petId;
	
	@Column(name = "medical")
    private String medical;
	
	@Column(name = "medical_assign_date")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date medicalAssignDate;
	
	@Column(name = "creation_time")
	private Date createTime;

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


	public String getMedical() {
		return medical;
	}

	public void setMedical(String medical) {
		this.medical = medical;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getMedicalAssignDate() {
		return medicalAssignDate;
	}

	public void setMedicalAssignDate(Date medicalAssignDate) {
		this.medicalAssignDate = medicalAssignDate;
	}

}
