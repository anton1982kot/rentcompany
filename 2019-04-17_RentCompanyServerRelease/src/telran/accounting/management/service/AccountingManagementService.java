package telran.accounting.management.service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import telran.accounting.api.AccountDto;
import telran.accounting.api.AccountingCodes;
import telran.accounting.interfaces.IAccountingManagement;
import telran.accounting.management.domain.Account;
import telran.accounting.management.domain.repo.AccountRepository;
@Service
public class AccountingManagementService 
implements IAccountingManagement{
	@Autowired
	PasswordEncoder encoder;
	@Value("${passwordLength:8}")
	int passwordLength;
	@Autowired
AccountRepository accountRepository;
	@Value("${n_last_hashcodes:3}")
	 int n_last_hashcodes;
	@Override
	public AccountingCodes addAccount(AccountDto account) {
		if(accountRepository.existsById(account.username)) {
			return AccountingCodes.ACCOUNT_ALREADY_EXISTS;
		}
		if(!isPasswordValid(account.password)) {
			return AccountingCodes.WRONG_PASSWORD;
		}
		String username=account.username;
		String hashCode=getHashCode(account.password);
		LocalDateTime activationDate=LocalDateTime.now();
		Account accountDoc=
				new Account(username, hashCode, activationDate,account.roles);
		accountRepository.save(accountDoc);
		return AccountingCodes.OK;
	}

	private String getHashCode(String password) {
		
		return encoder.encode(password);
	}

	private boolean isPasswordValid(String password) {
		
		return password.length()>=passwordLength;
	}

	@Override
	public AccountingCodes removeAccount(String username) {
		if(!accountRepository.existsById(username)) {
			return AccountingCodes.ACCOUNT_NOT_EXISTS;
		}
		accountRepository.deleteById(username);
		return AccountingCodes.OK;
	}

	@Override
	public AccountingCodes updatePassword(String username, String password) {
		if(!isPasswordValid(password)) {
			return AccountingCodes.WRONG_PASSWORD;
		}
		Account account=accountRepository.findById(username).orElse(null);
		if(account==null) {
			return AccountingCodes.ACCOUNT_NOT_EXISTS;
		}
		LinkedList<String> lastHashCodes=account.getLastHashCodes();
		if(isPasswordFromLast(lastHashCodes,password)) {
			return AccountingCodes.WRONG_PASSWORD;
		}
		if(lastHashCodes.size()==n_last_hashcodes) {
			lastHashCodes.removeFirst();
		}
		lastHashCodes.add(account.getHashCode());
		account.setHashCode(encoder.encode(password));
		account.setActivationDate(LocalDateTime.now());
		accountRepository.save(account);
		return AccountingCodes.OK;
	}

	private boolean isPasswordFromLast(LinkedList<String> lastHashCodes, String password) {
		
		return lastHashCodes.stream().anyMatch(c->encoder.matches(password, c));
	}

	@Override
	public AccountingCodes revokeAccount(String username) {
	Account account=accountRepository.findById(username).orElse(null);
	if(account==null)
		return AccountingCodes.ACCOUNT_NOT_EXISTS;
	if(account.isRevoked())
		return AccountingCodes.ALREADY_REVOKED;
	account.setRevoked(true);
	accountRepository.save(account);
		return AccountingCodes.OK;
	}

	@Override
	public AccountingCodes activateAccount(String username) {
		Account account=accountRepository.findById(username).orElse(null);
		if(account==null)
			return AccountingCodes.ACCOUNT_NOT_EXISTS;
		if(!account.isRevoked())
			return AccountingCodes.ALREADY_ACTIVATED;
		account.setRevoked(false);
		account.setActivationDate(LocalDateTime.now());
		accountRepository.save(account);
		return AccountingCodes.OK;
	}

	@Override
	public String getPasswordHash(String username) {
		Account account=accountRepository.findById(username).orElse(null);
		return account==null || account.isRevoked()?null:account.getHashCode();
	}

	@Override
	public LocalDateTime getActivationDate(String username) {
		Account account=accountRepository.findById(username).orElse(null);
		return account==null || account.isRevoked()?null:account.getActivationDate();
	}

	@Override
	public HashSet<String> getRoles(String username) {
		Account account=accountRepository.findById(username).orElse(null);
		return account==null || account.isRevoked()?null:account.getRoles();
	}

	@Override
	public AccountingCodes addRole(String username, String role) {
		Account account=accountRepository.findById(username).orElse(null);
		if(account==null)
			return AccountingCodes.ACCOUNT_NOT_EXISTS;
		HashSet<String>roles=account.getRoles();
		if(roles.contains(role))
			return AccountingCodes.ROLE_ALREADY_EXISTS;
		roles.add(role);
		accountRepository.save(account);
		return AccountingCodes.OK;
	}

	@Override
	public AccountingCodes removeRole(String username, String role) {
		Account account=accountRepository.findById(username).orElse(null);
		if(account==null)
			return AccountingCodes.ACCOUNT_NOT_EXISTS;
		HashSet<String>roles=account.getRoles();
		if(!roles.contains(role))
			return AccountingCodes.ROLE_NOT_EXISTS;
		roles.remove(role);
		accountRepository.save(account);
		return AccountingCodes.OK;
	}

}
