package telran.cars.model;

import telran.cars.dto.Model;
import telran.cars.dto.State;

public abstract class AbstractRentCompany 
implements IRentCompany
{
private static final int DAMAGES_GOOD = 20;
protected int finePercent;
protected int gasPrice;
public AbstractRentCompany() {
	finePercent=15;
	gasPrice=10;
}
public int getFinePercent() {
	return finePercent;
}

public void setFinePercent(int finePercent) {
	this.finePercent = finePercent;
}
public int getGasPrice() {
	return gasPrice;
}
public void setGasPrice(int gasPrice) {
	this.gasPrice = gasPrice;
}
@Override
public String toString() {
	return "AbstractRentCompany [finePercent=" + finePercent + ", gasPrice=" + gasPrice + "]";
}
protected double getCost(String modelName, int rentDays, int delay, int tankPercent) {
	Model model=getModel(modelName);
	int priceDay=model.getPriceDay();
	double cost=priceDay*rentDays;
	if(delay>0)
		cost+=additionalDelayCost(delay,priceDay);
	if(tankPercent<100)
		cost+=additionalGasCost(tankPercent,model.getGasTank());
	return cost;
}

private double additionalGasCost(int tankPercent,int gasTank) {
	
	return gasTank*((double)(100-tankPercent)/100)*gasPrice;
}

private double additionalDelayCost(int delay,int pricePerDay) {
	
	return delay*(pricePerDay*(1+(double)finePercent/100));
}
protected State getState(int damages) {
	State state;
	if(damages>1&&damages<DAMAGES_GOOD)
		state=State.GOOD;
	else if(damages>=DAMAGES_GOOD)
		state=State.BAD;
	else
		state=State.EXCELLENT;
	return state;
}
}
