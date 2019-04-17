package telran.cars.controller.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import telran.accounting.interfaces.IAccountingManagement;
import telran.accounting.management.configuration.Authenticator;
import telran.accounting.management.configuration.SecurityConfigurator;
import telran.accounting.management.service.AccountingManagementService;

@Configuration
@EnableMongoRepositories("telran.accounting.management.domain.repo")
public class SecurityConfiguration {
@Bean
Authenticator getAuthenticator() {
	return new Authenticator();
}

@Bean
PasswordEncoder getPasswordEncoder() {
	return new BCryptPasswordEncoder();
}
@Bean
IAccountingManagement getAccountingManagement() {
	return new AccountingManagementService();
}
}
