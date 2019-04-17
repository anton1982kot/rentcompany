package telran.cars.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import telran.cars.entities.DriverJpa;

public interface DriverRepository extends JpaRepository<DriverJpa, Long> {

}
