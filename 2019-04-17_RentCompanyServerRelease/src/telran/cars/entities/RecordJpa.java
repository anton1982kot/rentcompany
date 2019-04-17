package telran.cars.entities;

import java.time.LocalDate;

import javax.persistence.*;

@Table(name = "records")
@Entity
public class RecordJpa {
	@Id
	@GeneratedValue
	long id;
	private LocalDate rentDate;
	private LocalDate returnDate;
	private int damages;
	private int tankPercent;
	private double cost;
	private int rentDays;
	@ManyToOne
	@JoinColumn(name = "reg_number")
	CarJpa car;
	@ManyToOne
	@JoinColumn(name = "license_id")
	DriverJpa driver;

	public RecordJpa(LocalDate rentDate, int rentDays, CarJpa car, DriverJpa driver) {
		super();
		this.rentDate = rentDate;
		this.rentDays = rentDays;
		this.car = car;
		this.driver = driver;
	}

	public RecordJpa() {
	}

	public LocalDate getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(LocalDate returnDate) {
		this.returnDate = returnDate;
	}

	public int getDamages() {
		return damages;
	}

	public void setDamages(int damages) {
		this.damages = damages;
	}

	public int getTankPercent() {
		return tankPercent;
	}

	public void setTankPercent(int tankPercent) {
		this.tankPercent = tankPercent;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public int getRentDays() {
		return rentDays;
	}

	public void setRentDays(int rentDays) {
		this.rentDays = rentDays;
	}

	public long getId() {
		return id;
	}

	public LocalDate getRentDate() {
		return rentDate;
	}

	public CarJpa getCar() {
		return car;
	}

	public DriverJpa getDriver() {
		return driver;
	}

}
