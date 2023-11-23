package mie.ether_example;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;

import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;

import edu.toronto.dbservice.types.EmployeeVote;
import edu.toronto.dbservice.types.EtherAccount;
import edu.toronto.dbservice.types.Proxy;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import java.util.*;

public class DelegateProxy implements JavaDelegate {

	@SuppressWarnings("unchecked")
	@Override
	public void execute(DelegateExecution execution) {
		System.out.println("DelegateProxy - IMPLEMENT ME!");
	}
}
