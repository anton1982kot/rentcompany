package telran.cars.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import telran.cars.entities.DriverJpa;

public interface DriverRepository extends JpaRepository<DriverJpa, Long> {
	@Query(value="select * from drivers where license_id in "
			+ "(select d.license_id from drivers d join records r "
			+ "on d.license_id=r.license_id group by d.license_id "
			+ "having count(*)=(select max(count) from (select "
			+ "count(*) as count from drivers d join records r  "
			+ "on d.license_id=r.license_id group by d.license_id)))",nativeQuery=true)
	List<DriverJpa> findMostActiveDrivers();

}
