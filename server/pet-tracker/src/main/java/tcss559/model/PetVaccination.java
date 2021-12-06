package tcss559.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "pet_vaccination")
public class PetVaccination {
	
	@Id
    private int id;
	
	@Column(name = "pet_id")
    private int petId;
	
	@Column(name = "vaccination")
    private String vaccination;
	
	@Column(name = "create_time")
	private Date createTime;
	
	@Column(name = "active")
	private String active;

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

	public String getVaccination() {
		return vaccination;
	}

	public void setVaccination(String vaccination) {
		this.vaccination = vaccination;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}
	
	

}
