package telran.cars.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import telran.cars.dto.*;
public interface IRentCompany extends Serializable {
int getGasPrice();
void setGasPrice(int price);
int getFinePercent();
void setFinePercent(int finePercent);
CarsReturnCode addModel(Model model);
CarsReturnCode addCar(Car car);
CarsReturnCode addDriver(Driver driver);
Model getModel(String modelName);
Car getCar(String regNumber);
Driver getDriver(long licenseId);
//Sprint2
CarsReturnCode rentCar(String regNumber,long licenseId,
		LocalDate rentDate,int rentDays);
List<Car> getDriverCars(long licenseId);
List<Driver> getCarDrivers(String regNumber);
List<Car> getModelCars(String modelName);
List<RentRecord> getRentRecordsAtDates(LocalDate from,
		LocalDate to);
//Sprint3
RemovedCarData removeCar(String regNumber);
List<RemovedCarData> removeModel(String modelName);
RemovedCarData returnCar(String regNumber,long licenseId,
		LocalDate returnDate,int damages,int tankPercent);
//Sprint4
List<String> getMostPopularCarModels(LocalDate fromDate,LocalDate toDate,
		int fromAge,int toAge);
List<String> getMostProfitableCarModels(LocalDate fromDate,LocalDate toDate);
List<Driver> getMostActiveDrivers();
List<String> getModelNames();

}
