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
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;


public class GetVoteListTask implements JavaDelegate{

	Connection dbCon = null;

	public GetVoteListTask() {
		dbCon = MIE354DBHelper.getDBConnection();
	}
	
	@Override
	public void execute(DelegateExecution execution) {
		
		// Loading the contract
		Web3j web3 = Web3j.build(new HttpService());
		String contractAddress = (String) execution.getVariable("contractAddress");
		HashMap<Integer, EtherAccount> accounts = (HashMap<Integer, EtherAccount>) execution.getVariable("accounts");
		Ballot myBallot = Ballot.load(contractAddress, web3, accounts.get(0).getCredentials(), EtherUtils.GAS_PRICE, EtherUtils.GAS_LIMIT_CONTRACT_TX);
		
		// Selecting the employee votes from the data table
		Statement statement = null;
		ResultSet resultSet = null;
		List<EmployeeVote> employeeVoteList = new ArrayList<>();
		
		
		
		String currentFromValue = (String) execution.getVariable("ProposalsFrom");
		
		// Collect proposals from Manager
		if (currentFromValue.equals("-1")) {
			try {
				statement = dbCon.createStatement();
				resultSet = statement.executeQuery("SELECT * FROM Management_Vote");
				while (resultSet.next()) {
					Integer accountId = resultSet.getInt("account");
					Integer vote = resultSet.getInt("vote");
					EmployeeVote employeeVote = new EmployeeVote(accountId, vote);
					employeeVoteList.add(employeeVote);
				}	
				resultSet.close();
				
				// Saving the list of employee votes as a process variable
				execution.setVariable("employeeVoteList", employeeVoteList);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		
		// Collect proposals from Employees
		else if (currentFromValue.equals("1")) {
			try {
				statement = dbCon.createStatement();
				resultSet = statement.executeQuery("SELECT * FROM Employees_Vote");
				while (resultSet.next()) {
					Integer accountId = resultSet.getInt("account");
					Integer vote = resultSet.getInt("vote");
					EmployeeVote employeeVote = new EmployeeVote(accountId, vote);
					employeeVoteList.add(employeeVote);
				}	
				resultSet.close();
				
				// Saving the list of employee votes as a process variable
				execution.setVariable("employeeVoteList", employeeVoteList);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		
		
		// Collect proposals from both Management and Employees
		else if (currentFromValue.equals("0")) {
			try {
				statement = dbCon.createStatement();
				resultSet = statement.executeQuery("SELECT * FROM Vote");
				while (resultSet.next()) {
					Integer accountId = resultSet.getInt("account");
					Integer vote = resultSet.getInt("vote");
					EmployeeVote employeeVote = new EmployeeVote(accountId, vote);
					employeeVoteList.add(employeeVote);
				}	
				resultSet.close();
				
				// Saving the list of employee votes as a process variable
				execution.setVariable("employeeVoteList", employeeVoteList);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		
		// Saving the list of employee votes as a process variable
		execution.setVariable("employeeVoteList", employeeVoteList);
	}

}
