package telran.cars.model;

import java.time.LocalDate;
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
	public CarsReturnCode addDriver(Driver driver) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Model getModel(String modelName) {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return null;
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
	public List<Car> getDriverCars(long licenseId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Driver> getCarDrivers(String regNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Car> getModelCars(String modelName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RentRecord> getRentRecordsAtDates(LocalDate from, LocalDate to) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RemovedCarData removeCar(String regNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RemovedCarData> removeModel(String modelName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RemovedCarData returnCar(String regNumber, long licenseId, LocalDate returnDate, int damages,
			int tankPercent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getMostPopularCarModels(LocalDate fromDate, LocalDate toDate, int fromAge, int toAge) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getMostProfitableCarModels(LocalDate fromDate, LocalDate toDate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Driver> getMostActiveDrivers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getModelNames() {
		
		return modelRepository.findAll().stream().map(ModelJpa::getModelName).collect(Collectors.toList());
	}

}
