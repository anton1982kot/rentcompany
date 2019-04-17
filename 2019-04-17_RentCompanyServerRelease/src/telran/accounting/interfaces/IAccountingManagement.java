package telran.accounting.interfaces;

import java.time.LocalDateTime;
import java.util.HashSet;

import telran.accounting.api.AccountDto;
import telran.accounting.api.AccountingCodes;

public interface IAccountingManagement {
AccountingCodes addAccount(AccountDto account);
AccountingCodes removeAccount(String username);
AccountingCodes updatePassword(String username,String password);
AccountingCodes revokeAccount(String username);
AccountingCodes activateAccount(String username);
String getPasswordHash(String username);
LocalDateTime getActivationDate(String username);
HashSet<String> getRoles(String username);
AccountingCodes  addRole(String username, String role);
AccountingCodes removeRole(String username, String role);
}
