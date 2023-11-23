package mie.ether_example;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;

import edu.toronto.dbservice.types.EmployeeVote;
import edu.toronto.dbservice.types.EtherAccount;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

public class Vote implements JavaDelegate{
	
	@SuppressWarnings("unchecked")
	@Override
	public void execute(DelegateExecution execution) {
		
		EmployeeVote currentEmployeeVote = (EmployeeVote) execution.getVariable("currentEmployeeVote");
		Integer employeeNum = currentEmployeeVote.getEmployeeNum();
		Integer employeeVote = currentEmployeeVote.getVote();
		
		Web3j web3 = Web3j.build(new HttpService());
		String contractAddress = (String) execution.getVariable("contractAddress");
		HashMap<Integer, EtherAccount> accounts = (HashMap<Integer, EtherAccount>) execution.getVariable("accounts");
		Ballot employeeBallot = Ballot.load(contractAddress, web3, accounts.get(employeeNum).getCredentials(), EtherUtils.GAS_PRICE, EtherUtils.GAS_LIMIT_CONTRACT_TX);
		
		Uint256 vote = new Uint256(BigInteger.valueOf(employeeVote));
		TransactionReceipt voteReceipt = null;
		try {
			voteReceipt = employeeBallot.vote(vote).get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		EtherUtils.reportTransaction("Person " + employeeNum + " Voted for " + employeeVote, voteReceipt);
		
	}

}
