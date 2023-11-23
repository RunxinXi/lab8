package mie.ether_example;

import java.sql.ResultSet;
import java.util.HashMap;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


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
		// Database connection details
		String url = "jdbc:your_database_url"; // Replace with your database URL
		String user = "username"; // Replace with your database username
		String password = "password"; // Replace with your database password

		// Load the list of allowed voters from the database
		HashMap<Integer, Integer> allowedVoters = new HashMap<>();
		try {
		    Connection conn = DriverManager.getConnection(url, user, password);
		    Statement stmt = conn.createStatement();
		    ResultSet rs = stmt.executeQuery("SELECT account, allowed FROM allowedVoters");

		    while (rs.next()) {
		        allowedVoters.put(rs.getInt("account"), rs.getInt("allowed"));
		    }

		    rs.close();
		    stmt.close();
		    conn.close();
		} catch (Exception e) {
		    e.printStackTrace();
		}

		

		// iterate over the accounts
		for (Entry<Integer, EtherAccount> accountEntry : accounts.entrySet()) {
			// if the account is not the company account (number 0),
			// give voting right to the blockchain address of the account
			if (allowedVoters.getOrDefault(accountEntry.getKey(), 0) == 1) {
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
		

		// Check if account 9 is allowed to vote and then delegate its vote to account 5
		if (allowedVoters.getOrDefault(9, 0) == 1) {
		    Credentials credentials9 = accounts.get(9).getCredentials();
		    Credentials credentials5 = accounts.get(5).getCredentials();
		    Address accountAddress5 = new Address(credentials5.getAddress());
		    TransactionReceipt transactionReceipt = null;
		    try {
		        Ballot employeeBallot = Ballot.load(contractAddress, web3, credentials9, EtherUtils.GAS_PRICE, EtherUtils.GAS_LIMIT_CONTRACT_TX);
		        transactionReceipt = employeeBallot.delegate(accountAddress5).get();
		        EtherUtils.reportTransaction("Person 9 delegated vote to Person 5", transactionReceipt);
		    } catch (InterruptedException | ExecutionException e) {
		        e.printStackTrace();
		    }
		}

		// ... [rest of the code]

		
	}
}
