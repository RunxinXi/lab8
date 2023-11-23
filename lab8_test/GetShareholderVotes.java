package mie.ether_example;

import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import edu.toronto.dbservice.config.MIE354DBHelper;
import edu.toronto.dbservice.types.EmployeeVote;
import edu.toronto.dbservice.types.EtherAccount;
import edu.toronto.dbservice.types.ShareholderVote;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

public class GetShareholderVotes implements JavaDelegate {

	Connection dbCon = null;

	public GetShareholderVotes() {
		dbCon = MIE354DBHelper.getDBConnection();
	}

	@Override
	public void execute(DelegateExecution execution) {

		// Loading the contract
		Web3j web3 = Web3j.build(new HttpService());
		String contractAddress = (String) execution.getVariable("contractAddress");
		HashMap<Integer, EtherAccount> accounts = (HashMap<Integer, EtherAccount>) execution.getVariable("accounts");
		Ballot myBallot = Ballot.load(contractAddress, web3, accounts.get(0).getCredentials(), EtherUtils.GAS_PRICE,
				EtherUtils.GAS_LIMIT_CONTRACT_TX);

		// Selecting the employee votes from the data table
		List<ShareholderVote> votes = new ArrayList<>();

		try {
			Statement statement = dbCon.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM SHAREHOLDERVOTES");
			while (resultSet.next()) {
				Integer accountId = resultSet.getInt("ACCOUNTID");
				Integer proposalId = resultSet.getInt("PROPOSALID");
				ShareholderVote vote = new ShareholderVote(accountId, proposalId);
				votes.add(vote);
			}
			resultSet.close();

			// Saving the list of employee votes as a process variable
		} catch (SQLException e) {
			e.printStackTrace();
		}

		execution.setVariable("voteList", votes);
	}

}
