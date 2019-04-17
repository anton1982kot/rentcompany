package telran.accounting.management.domain.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import telran.accounting.management.domain.Account;

public interface AccountRepository extends 
MongoRepository<Account, String> {

}
