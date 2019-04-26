package telran.cars.repo;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import telran.cars.dto.Car;
import telran.cars.entities.CarJpa;

public interface CarRepository extends 
JpaRepository<CarJpa, String> {

	Stream<CarJpa> findByModelModelName(String modelName);

	@Query(value="select model_name from cars c join records r "
			+ "join drivers  d on c.reg_number=r.reg_number "
			+ "and r.license_id=d.license_id where rent_date between "
			+ ":fromDate and :toDate and birth_year between :yearFrom "
			+ "and :yearTo group by model_name having count(*)="
			+ "(select max(count) from (select count(*) as count from "
			+ " cars c join records r "
			+ "join drivers  d on c.reg_number=r.reg_number "
			+ "and r.license_id=d.license_id where rent_date between "
			+ ":fromDate and :toDate and birth_year between :yearFrom "
			+ "and :yearTo group by model_name))",nativeQuery=true)
	List<String> findMostPopularCarModels
	(@Param("fromDate")LocalDate fromDate,
			@Param("toDate")LocalDate toDate,
			@Param("yearFrom")int birthYearFrom,
			@Param("yearTo")int birthYearTo);
	//TODO
	@Query(value="select c.model_name from cars c join records r join models m "
			+ "on c.reg_number=r.reg_number and c.model_name=m.model_name"
			+ " where rent_date between :fromDate and :toDate group by "
			+ "c.model_name having sum(cost) ="
			+ " (select max(cost) from (select sum(cost) as cost from cars c join records r join models m "
			+ "on c.reg_number=r.reg_number and c.model_name=m.model_name"
			+ " where rent_date between :fromDate and :toDate group by "
			+ "c.model_name))",nativeQuery=true)
	List<String> findMostProfitableCarModels(@Param("fromDate")LocalDate fromDate,
			@Param("toDate")LocalDate toDate);

}
