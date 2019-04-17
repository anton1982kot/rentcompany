package telran.accounting.management.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AccountingConfiguration {
@Bean
public PasswordEncoder getPasswordEncoder() {
	return new BCryptPasswordEncoder();
}
}
