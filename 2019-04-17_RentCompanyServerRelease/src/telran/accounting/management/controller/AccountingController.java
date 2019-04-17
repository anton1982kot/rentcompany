package telran.accounting.management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import telran.accounting.api.AccountDto;
import telran.accounting.api.AccountingCodes;
import telran.accounting.api.RoleData;
import telran.accounting.interfaces.IAccountingManagement;

import static telran.accounting.api.AccountingApiConstants.*;
@RestController
public class AccountingController {
	@Autowired
	IAccountingManagement accountingManagement;
@PostMapping (value=ADD_ACCOUNT)
AccountingCodes addAccount(@RequestBody AccountDto account) {
	return accountingManagement.addAccount(account);
}
@DeleteMapping(value=DELETE_ACCOUNT)
AccountingCodes removeAccount
(@RequestParam(name=USERNAME) String username) {
	return accountingManagement.removeAccount(username);
}
@PostMapping (value=UPDATE_PASSWORD)
AccountingCodes updatePassword(@RequestBody AccountDto account) {
	return accountingManagement.updatePassword(account.username, account.password);
}
@PostMapping(value=ACTIVATE_ACCOUNT)
AccountingCodes activateAccount(@RequestBody String username) {
	return accountingManagement.activateAccount(username);
}
@PostMapping(value=REVOKE_ACCOUNT)
AccountingCodes revokeAccount(@RequestBody String username) {
	return accountingManagement.removeAccount(username);
}
@PostMapping(value=ADD_ROLE)
AccountingCodes addRole(@RequestBody RoleData roleData) {
	return accountingManagement.addRole(roleData.username, roleData.role);
}
@DeleteMapping(value=REMOVE_ROLE)
AccountingCodes removeRole(@RequestParam(name=USERNAME) String username,
		@RequestParam(name=ROLE) String role) {
	return accountingManagement.removeRole(username, role);
}
}
