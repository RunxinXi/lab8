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
import edu.toronto.dbservice.types.ShareholderVote;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

public class PlaceShareholderVote implements JavaDelegate {
	@SuppressWarnings("unchecked")
	@Override
	public void execute(DelegateExecution execution) {
		System.out.println("PlaceShareholderVote - IMPLEMENT ME!");
	}

}
