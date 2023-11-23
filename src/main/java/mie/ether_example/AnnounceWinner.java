package mie.ether_example;


import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import edu.toronto.dbservice.types.EtherAccount;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;


public class AnnounceWinner implements JavaDelegate{
	
	@Override
	public void execute(DelegateExecution execution) {
		
		// connect to blockchain server and load contract
		Web3j web3 = Web3j.build(new HttpService());
		String contractAddress = (String) execution.getVariable("contractAddress");
		@SuppressWarnings("unchecked")
		HashMap<Integer, EtherAccount> accounts = (HashMap<Integer, EtherAccount>) execution.getVariable("accounts");
		Ballot myBallot = Ballot.load(contractAddress, web3, accounts.get(0).getCredentials(), EtherUtils.GAS_PRICE, EtherUtils.GAS_LIMIT_CONTRACT_TX);
		
		// get the winner proposal number and name and print them
		Integer winnerProposal = null;
		try {
			winnerProposal = myBallot.winningProposal().get().getValue().intValue();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Bytes32 winnerName = null;
		try {
			winnerName = myBallot.winnerName().get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String decodedWinnerName = EtherUtils.bytes32ToString(winnerName);
		System.out.println("Winning proposal num: " + winnerProposal);
		System.out.println("Winning proposal name: " + decodedWinnerName);
	}

}
