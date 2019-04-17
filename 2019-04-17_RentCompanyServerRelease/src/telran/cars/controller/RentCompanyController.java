package telran.cars.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import telran.cars.dto.*;
import telran.cars.model.IRentCompany;

import static telran.cars.api.ApiConstants.*;

import java.time.LocalDate;
import java.util.List;
@RestController
public class RentCompanyController {
	@Autowired
	IRentCompany company;
@PostMapping(value=ADD_MODEL)
CarsReturnCode addModel(@RequestBody Model model) {
	return company.addModel(model);
}
@PostMapping(value=ADD_CAR)
CarsReturnCode addCar(@RequestBody Car car) {
	return company.addCar(car);
}
@PostMapping(value=ADD_DRIVER)
CarsReturnCode addDriver(@RequestBody Driver driver) {
	return company.addDriver(driver);
}
@PostMapping(value=RETURN_CAR)
RemovedCarData returnCar(@RequestBody ReturnCarData rcd) {
	return company.returnCar
			(rcd.getRegNumber(), rcd.getLicenseId(),
					rcd.getReturnDate(), rcd.getDamages(),
					rcd.getTankPercent());
}
@PostMapping(value=RENT_CAR)
CarsReturnCode rentCar(@RequestBody RentCarData rcd) {
	return company.rentCar
			(rcd.getRegNumber(), rcd.getLicenseId(),
					rcd.getRentDate(), rcd.getRentDays());
}
@DeleteMapping(value=REMOVE_CAR)
RemovedCarData removeCar
(@RequestParam(name=CAR_NUMBER)String regNumber) {
	return company.removeCar(regNumber);
}
@DeleteMapping(value=REMOVE_MODEL)
List<RemovedCarData> removeModel
(@RequestParam(name=MODEL_NAME)String modelName) {
	return company.removeModel(modelName);
}
@GetMapping(value=GET_CAR_MODELS)
List<String> getModelNames() {
	return company.getModelNames();
}
@GetMapping(value=GET_CAR)
Car getCar(@RequestParam(name=CAR_NUMBER)
String regNumber) {
	return company.getCar(regNumber);
}
@GetMapping(value=GET_MODEL)
Model getModel(@RequestParam(name=MODEL_NAME)
String modelName) {
	return company.getModel(modelName);
}
@GetMapping(value=GET_DRIVER_CARS)
List<Car> getDriverCars(@RequestParam(name=LICENSE_ID)
long licenseId) {
	return company.getDriverCars(licenseId);
}
@GetMapping(value=GET_MODEL_CARS)
List<Car> getModelCars(@RequestParam(name=MODEL_NAME)
String modelName) {
	return company.getModelCars(modelName);
}
@GetMapping(value=GET_CAR_DRIVERS)
List<Driver> getCarDrivers(@RequestParam(name=CAR_NUMBER)
String regNumber) {
	return company.getCarDrivers(regNumber);
}
@GetMapping(value=GET_DRIVER)
Driver getCar(@RequestParam(name=LICENSE_ID)
long licenseId) {
	return company.getDriver(licenseId);
}
@GetMapping(value=GET_RECORDS)
List<RentRecord> getRecords(@RequestParam(name=DATE_FROM)
String dateFrom, @RequestParam(name=DATE_TO)
String dateTo) 
{
	LocalDate from = LocalDate.parse(dateFrom);
	LocalDate to = LocalDate.parse(dateTo);
	return company.getRentRecordsAtDates(from, to);
}
@GetMapping(value=GET_MOST_ACTIVE_DRIVERS)
List<Driver> getRecords() 
{
	return company.getMostActiveDrivers();
}
@GetMapping(value=GET_MOST_POPULAR_MODELS)
List<String> getMostPopularModels(@RequestParam(name=DATE_FROM)
String dateFrom, @RequestParam(name=DATE_TO)
String dateTo,@RequestParam(name=AGE_FROM) int ageFrom,
@RequestParam(name=AGE_TO) int ageTo) 
{
	LocalDate from = LocalDate.parse(dateFrom);
	LocalDate to = LocalDate.parse(dateTo);
	return company.getMostPopularCarModels(from, to, ageFrom, ageTo);
}
@GetMapping(value=GET_MOST_PROFITABLE_MODELS)
List<String> getMostProfitableModels(@RequestParam(name=DATE_FROM)
String dateFrom, @RequestParam(name=DATE_TO)
String dateTo) 
{
	LocalDate from = LocalDate.parse(dateFrom);
	LocalDate to = LocalDate.parse(dateTo);
	return company.getMostProfitableCarModels(from, to);
}
}
