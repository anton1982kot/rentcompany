package telran.cars.controller.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import static telran.cars.api.ApiConstants.*;
@Configuration
public class SecurityConfigurator extends WebSecurityConfigurerAdapter {
@Override
protected void configure(HttpSecurity http) throws Exception {
	http.httpBasic();
	http.csrf().disable();
	http.authorizeRequests().antMatchers(GET_MODEL,GET_CAR_MODELS).permitAll();
	http.authorizeRequests().antMatchers(ADD_DRIVER,
			RETURN_CAR,GET_MODEL_CARS,RENT_CAR).hasRole("CLERK");
	http.authorizeRequests().antMatchers
	(GET_MOST_ACTIVE_DRIVERS,
			GET_MOST_POPULAR_MODELS,
			GET_MOST_PROFITABLE_MODELS).hasRole("STATIST");
	http.authorizeRequests().antMatchers(ADD_CAR,ADD_MODEL,
			REMOVE_CAR, REMOVE_MODEL).hasRole("MANAGER");
	http.authorizeRequests().antMatchers(GET_DRIVER,GET_CAR_DRIVERS,
			GET_DRIVER_CARS).hasAnyRole("CLERK","DRIVER");
	http.authorizeRequests().antMatchers(GET_CAR).authenticated();
}
}
