package mie.ether_example;

import java.util.HashMap;

import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import org.web3j.abi.datatypes.Address;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import edu.toronto.dbservice.types.EtherAccount;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

public class GiveRight implements JavaDelegate {

	@Override
	public void execute(DelegateExecution execution) {

		// Connect to blockchain server
		Web3j web3 = Web3j.build(new HttpService());

		// load the list of employee accounts
		@SuppressWarnings("unchecked")
		HashMap<Integer, EtherAccount> accounts = (HashMap<Integer, EtherAccount>) execution.getVariable("accounts");

		// load ballot contract based on the process variable contractAddress
		String contractAddress = (String) execution.getVariable("contractAddress");
		Ballot myBallot = Ballot.load(contractAddress, web3, accounts.get(0).getCredentials(), EtherUtils.GAS_PRICE,
				EtherUtils.GAS_LIMIT_CONTRACT_TX);

		// TODO: create a HashMap of allowed voters

		// iterate over the accounts
		for (Entry<Integer, EtherAccount> accountEntry : accounts.entrySet()) {
			// if the account is not the company account (number 0),
			// give voting right to the blockchain address of the account
			if (accountEntry.getKey() != 0) {
				Credentials credentials = accountEntry.getValue().getCredentials();
				Address accountAddress = new Address(credentials.getAddress());
				TransactionReceipt transactionReceipt = null;
				try {
					transactionReceipt = myBallot.giveRightToVote(accountAddress).get();
				} catch (InterruptedException | ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				EtherUtils.reportTransaction("Gave voting right to" + credentials.getAddress(), transactionReceipt);
			}
		}
	}
}
