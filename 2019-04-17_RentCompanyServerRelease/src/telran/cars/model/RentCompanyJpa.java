package telran.cars.model;

import java.sql.DriverPropertyInfo;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import telran.cars.dto.*;
import telran.cars.repo.CarRepository;
import telran.cars.repo.DriverRepository;
import telran.cars.repo.ModelRepository;
import telran.cars.repo.RecordRepository;
import telran.cars.entities.*;

@Service
public class RentCompanyJpa extends AbstractRentCompany {
	@Autowired
	CarRepository carRepository;
	@Autowired
	DriverRepository driverRepository;
	@Autowired
	ModelRepository modelRepository;
	@Autowired
	RecordRepository recordRepository;

	@Override
	@Transactional
	public CarsReturnCode addModel(Model model) {
		if (modelRepository.existsById(model.getModelName()))
			return CarsReturnCode.MODEL_EXISTS;
		ModelJpa modelJpa = new ModelJpa(model.getModelName(), model.getGasTank(), model.getCompany(),
				model.getCompany(), model.getPriceDay());
		modelRepository.save(modelJpa);
		return CarsReturnCode.OK;
	}

	@Override
	@Transactional
	public CarsReturnCode addCar(Car car) {
		String regNumber = car.getRegNumber();
		if (carRepository.existsById(regNumber))
			return CarsReturnCode.CAR_EXISTS;
		String modelName = car.getModelName();
		ModelJpa modelJpa = modelRepository.findById(modelName).orElse(null);
		if(modelJpa == null)
			return CarsReturnCode.NO_MODEL;
		CarJpa carJpa = new CarJpa(car.getColor(),regNumber, car.getState(), modelJpa);
		carRepository.save(carJpa);
		return CarsReturnCode.OK;

	}

	@Override
	@Transactional
	public CarsReturnCode addDriver(Driver driver) {
		if(driverRepository.existsById(driver.getLicenseId()))
			return CarsReturnCode.DRIVER_EXISTS;
		DriverJpa driverJpa = new DriverJpa(driver.getLicenseId(), driver.getName(), driver.getBirthYear(),
				driver.getPhone());
		driverRepository.save(driverJpa);
		return CarsReturnCode.OK;
	}

	@Override
	public Model getModel(String modelName) {
		ModelJpa modelJpa = modelRepository.findById(modelName).orElse(null);
		return modelJpa == null?null:getModelDto(modelJpa);
	}

	private Model getModelDto(ModelJpa modelJpa) {
		return new Model(modelJpa.getModelName(), modelJpa.getGasTank(), modelJpa.getCompany(),
				modelJpa.getCountry(), modelJpa.getPriceDay());
	}

	@Override
	public Car getCar(String regNumber) {
		CarJpa carJpa = carRepository.findById(regNumber).orElse(null);
		return carJpa==null ? null:getCarDto(carJpa);
	}

	private Car getCarDto(CarJpa carJpa) {
		Car res = new Car(carJpa.getRegNumber(), carJpa.getColor(), carJpa.getModel().getModelName());
		res.setState(carJpa.getState());
		res.setInUse(recordRepository.findByCarRegNumberAndReturnDateNull(carJpa.getRegNumber())!=null);
		res.setFlRemoved(carJpa.isFlRemoved());
		return res;
	}

	@Override
	public Driver getDriver(long licenseId) {
		DriverJpa driverJpa = driverRepository.findById(licenseId)
				.orElse(null);
		return driverJpa == null?null:getDriverDto(driverJpa);
	}

	private Driver getDriverDto(DriverJpa driverJpa) {
		
		return new Driver(driverJpa.getLicenseId(),
				driverJpa.getName(), driverJpa.getBirthYear(), driverJpa.getPhone());
	}

	@Override
	@Transactional
	public CarsReturnCode rentCar(String regNumber, long licenseId, LocalDate rentDate, int rentDays) {
		CarJpa carJpa = carRepository.findById(regNumber).orElse(null);
		if(carJpa==null)
		return CarsReturnCode.NO_CAR;
		DriverJpa driverJpa = driverRepository.findById(licenseId).orElse(null);
		if(carJpa.isFlRemoved())
			return CarsReturnCode.CAR_REMOVED;
		if(driverJpa==null)
			return CarsReturnCode.NO_DRIVER;
		RecordJpa record = recordRepository.findByCarRegNumberAndReturnDateNull(regNumber);
		if(record != null)
			return CarsReturnCode.CAR_IN_USE;
		RecordJpa recordJpa = new RecordJpa(rentDate, rentDays, carJpa, driverJpa);
		recordRepository.save(recordJpa);
		return CarsReturnCode.OK;
		
	}

	@Override
	@Transactional(readOnly = true)
	public List<Car> getDriverCars(long licenseId) {
		List<Car> cars = recordRepository.findByDriverLicenseId(licenseId).map(r->getCarDto(r.getCar()))
				.distinct().collect(Collectors.toList());
		return cars;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Driver> getCarDrivers(String regNumber) {
		return recordRepository.findByCarRegNumber(regNumber).map(r->getDriverDto(r.getDriver()))
				.distinct().collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<Car> getModelCars(String modelName) {
		
		return carRepository.findByModelModelName(modelName).filter(c->!c.isFlRemoved()&&!isInUse(c.getRegNumber()))
				.map(this::getCarDto).collect(Collectors.toList());
	}

	private boolean isInUse(String regNumber) {
		return recordRepository.findByCarRegNumberAndReturnDateNull(regNumber)!=null;
	}

	@Override
	@Transactional(readOnly = true)
	public List<RentRecord> getRentRecordsAtDates(LocalDate from, LocalDate to) {
		
		return recordRepository.findByRentDateBetween(from, to).map(this::getRentRecordDto)
				.collect(Collectors.toList());
	}
	private RentRecord getRentRecordDto(RecordJpa recordJpa) {
		RentRecord record = new RentRecord(recordJpa.getCar().getRegNumber(),
				recordJpa.getDriver().getLicenseId(), recordJpa.getRentDate(),
				recordJpa.getRentDays());
		record.setCost(recordJpa.getCost());
		record.setDamages(recordJpa.getDamages());
		record.setReturnDate(recordJpa.getRentDate());
		record.setTankPercent(recordJpa.getTankPercent());
		return record;
		
	}

	@Override
	@Transactional
	public RemovedCarData removeCar(String regNumber) {
		CarJpa carJpa = carRepository.findById(regNumber).orElse(null);
		if(carJpa==null || carJpa.isFlRemoved())
		return null;
		Car car = getCarDto(carJpa);
		if(car.isInUse()) {
			carJpa.setFlRemoved(true);
			car.setFlRemoved(true);
			return new RemovedCarData(car, null);
		}
		List<RentRecord>removedRecords = actualRemove(carJpa);
		return new RemovedCarData(car, removedRecords);
	}

	private List<RentRecord> actualRemove(CarJpa carJpa) {
		List<RecordJpa> recordsForRemove = recordRepository.findByCarRegNumber(carJpa.getRegNumber())
				.collect(Collectors.toList());
		recordsForRemove.forEach(recordRepository::delete);
		carRepository.delete(carJpa);
		
		return recordsForRemove.stream().map(this::getRentRecordDto).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public List<RemovedCarData> removeModel(String modelName) {
		List<CarJpa> modelCars = carRepository.findByModelModelName(modelName).collect(Collectors.toList());
		List<RemovedCarData> res = new ArrayList<>();
		modelCars.forEach(c->res.add(removeCar(c.getRegNumber())));
		return res;
	}

	@Override
	@Transactional
	public RemovedCarData returnCar(String regNumber, long licenseId, LocalDate returnDate, int damages,
			int tankPercent) {
		RecordJpa recordJpa = recordRepository.findByCarRegNumberAndReturnDateNull(regNumber);
		if(recordJpa==null)
		return null;
		CarJpa carJpa = carRepository.findById(regNumber).get();
		updateRecord(recordJpa,returnDate,damages,tankPercent,carJpa.getModel().getModelName());
		updaterCar(carJpa,damages);
		RemovedCarData res = null;
		Car car = getCarDto(carJpa);
		if(carJpa.isFlRemoved()) {
			List<RentRecord> removedRecords = actualRemove(carJpa);
			res = new RemovedCarData(car, removedRecords);
		}
		else {
			res = new RemovedCarData(car, null);
		}
		return res;
	}

	private void updaterCar(CarJpa car, int damages) {
		State state = getState(damages);
		if(state == State.BAD)
			car.setFlRemoved(true);
		car.setState(state);
		
	}

	private void updateRecord(RecordJpa record, LocalDate returnDate, int damages, int tankPercent,
			String modelName) {
		record.setDamages(damages);
		record.setReturnDate(returnDate);
		record.setTankPercent(tankPercent);
		int delay = (int) (ChronoUnit.DAYS.between(record.getRentDate(), returnDate)-record.getRentDays());
		record.setCost(getCost(modelName, record.getRentDays(), delay, tankPercent));
		//the method get cost may be found
		//in the abstract superclass as it doesn't depend on the methods implementation
		
	}

	@Override
	public List<String> getMostPopularCarModels(LocalDate fromDate, LocalDate toDate, int fromAge, int toAge) {
		int birthYearFrom = fromDate.getYear()- toAge;
		int birthYearTo = toDate.getYear() - fromAge;
		
		return carRepository.findMostPopularCarModels(fromDate, toDate, birthYearFrom, birthYearTo);
	}

	@Override
	public List<String> getMostProfitableCarModels(LocalDate fromDate, LocalDate toDate) {
		
		return carRepository.findMostProfitableCarModels(fromDate, toDate);
	}

	@Override
	public List<Driver> getMostActiveDrivers() {
		
		return driverRepository.findMostActiveDrivers().stream().map(this::getDriverDto).collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly=true)
	public List<String> getModelNames() {
		
		return modelRepository.findAll().stream().map(ModelJpa::getModelName).collect(Collectors.toList());
	}

}
