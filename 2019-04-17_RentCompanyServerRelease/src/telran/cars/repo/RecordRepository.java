package telran.cars.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import telran.cars.entities.RecordJpa;

public interface RecordRepository extends JpaRepository<RecordJpa, Long> {

	RecordJpa findByCarRegNumberAndReturnDateNull(String regNumber);

}
