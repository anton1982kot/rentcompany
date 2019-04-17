package telran.cars.entities;
import javax.persistence.*;

@Table(name="models")
@Entity
public class ModelJpa {
	@Id
	@Column(name="model_name")
	private String modelName;
	@Column(name="gas_tank")
	private int gasTank;
	private String company;
	private String country;
	@Column(name="price_day")
	private int priceDay;
	public ModelJpa(String modelName, int gasTank, String company, String country, int priceDay) {
		super();
		this.modelName = modelName;
		this.gasTank = gasTank;
		this.company = company;
		this.country = country;
		this.priceDay = priceDay;
	}
	public ModelJpa() {
	}
	public int getPriceDay() {
		return priceDay;
	}
	public void setPriceDay(int priceDay) {
		this.priceDay = priceDay;
	}
	public String getModelName() {
		return modelName;
	}
	public int getGasTank() {
		return gasTank;
	}
	public String getCompany() {
		return company;
	}
	public String getCountry() {
		return country;
	}

}
