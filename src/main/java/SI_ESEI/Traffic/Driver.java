package SI_ESEI.Traffic;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Driver{
	// Id
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	// Age
	private int age;
	
	// Sex
	private String sex;
	
	// Experience
	private int experience;
	
	// Previous infractions
	private int previousInfractions;
	
	// Ill
	private String ill;
	
	public int getId() {
		return id;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public int getExperience() {
		return experience;
	}

	public void setExperience(int experience) {
		this.experience = experience;
	}

	public int getPreviousInfractions() {
		return previousInfractions;
	}

	public void setPreviousInfractions(int previousInfractions) {
		this.previousInfractions = previousInfractions;
	}

	public String getIll() {
		return ill;
	}

	public void setIll(String ill) {
		this.ill = ill;
	}
}
