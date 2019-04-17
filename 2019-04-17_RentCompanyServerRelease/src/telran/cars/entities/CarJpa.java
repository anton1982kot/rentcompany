package telran.cars.entities;

import javax.persistence.*;

import telran.cars.dto.State;

@Table(name="cars")
@Entity
public class CarJpa {
	@Id
	@Column(name="reg_number")
	private String regNumber;
	private String color;
	@Enumerated(EnumType.STRING)
	private State state;
	@JoinColumn(name="model_name")
	@ManyToOne
	ModelJpa model;
	@Column(name="fl_removed")
	boolean flRemoved=false;
	public boolean isFlRemoved() {
		return flRemoved;
	}
	public void setFlRemoved(boolean flRemoved) {
		this.flRemoved = flRemoved;
	}
	public CarJpa(String regNumber, String color, State state, ModelJpa model) {
		super();
		this.regNumber = regNumber;
		this.color = color;
		this.state = state;
		this.model = model;
	}
	public CarJpa(){
	}
	public State getState() {
		return state;
	}
	public void setState(State state) {
		this.state = state;
	}
	public String getRegNumber() {
		return regNumber;
	}
	public String getColor() {
		return color;
	}
	public ModelJpa getModel() {
		return model;
	}

}
