package telran.cars.entities;

import javax.persistence.*;

@Table(name = "drivers")
@Entity
public class DriverJpa {
	@Id
	@Column(name = "license_id")
	private long licenseId;
	private String name;
	@Column(name = "birth_year")
	private int birthYear;
	private String phone;

	public DriverJpa(long licenseId, String name, int birthYear, String phone) {
		super();
		this.licenseId = licenseId;
		this.name = name;
		this.birthYear = birthYear;
		this.phone = phone;
	}

	public DriverJpa() {
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public long getLicenseId() {
		return licenseId;
	}

	public String getName() {
		return name;
	}

	public int getBirthYear() {
		return birthYear;
	}

}
