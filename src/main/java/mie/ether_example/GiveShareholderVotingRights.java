package mie.ether_example;

import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import org.web3j.abi.datatypes.Address;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;

import edu.toronto.dbservice.config.MIE354DBHelper;
import edu.toronto.dbservice.types.EtherAccount;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

public class GiveShareholderVotingRights implements JavaDelegate {
	Connection dbCon = null;
	Integer allowed = null;
	Integer accountId = null;
	Statement statement = null;
	ResultSet resultSet = null;
	HashMap<Integer, Integer> allowedvoters = new HashMap<>();

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

		// iterate over the accounts
		for (Entry<Integer, EtherAccount> accountEntry : accounts.entrySet()) {
			if (accountEntry.getKey() == 0) {
				continue;
			}

			
			// YOUR IMPLEMENTATION HERE
			System.out.println("GiveShareholderVotingRights - IMPLEMENT ME!");
			

		}
	}
}
