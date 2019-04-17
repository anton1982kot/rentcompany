package telran.accounting.management.configuration;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import telran.accounting.interfaces.IAccountingManagement;

@Configuration
public class Authenticator implements UserDetailsService {
	@Autowired
	IAccountingManagement accountingManagement;
	@Value("${activationPeriod:15000000}")
	long activationPeriod;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		LocalDateTime activationDate = accountingManagement.getActivationDate(username);
		if (activationDate == null
				|| ChronoUnit.SECONDS.between(activationDate, LocalDateTime.now()) > activationPeriod)
			throw new UsernameNotFoundException(
					"Either account doesn't exist or" + " activation period has been exceeded");
		String[] roles = accountingManagement.getRoles(username).stream().map(r -> "ROLE_" + r).toArray(String[]::new);
		return new User(username, accountingManagement.getPasswordHash(username),
				AuthorityUtils.createAuthorityList(roles));
	}

}
