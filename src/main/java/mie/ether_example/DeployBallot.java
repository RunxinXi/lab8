package mie.ether_example;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCoinbase;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import edu.toronto.dbservice.types.EtherAccount;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

public class DeployBallot implements JavaDelegate {
	public static final BigInteger FUND_AMOUNT = BigInteger.valueOf(25000000000000000L);

	private void fundAccounts(Web3j web3, HashMap<Integer, EtherAccount> accounts, BigInteger amountPerAccount)
			throws Exception {
		EthCoinbase coinbase = web3.ethCoinbase().sendAsync().get();

		for (EtherAccount account : accounts.values()) {
			Credentials credentials = account.getCredentials();
			EthGetTransactionCount transactionCount = web3
					.ethGetTransactionCount(coinbase.getAddress(), DefaultBlockParameterName.LATEST).sendAsync().get();

			BigInteger nonce = transactionCount.getTransactionCount();

			Transaction aliceMoney = Transaction.createEtherTransaction(coinbase.getAddress(), nonce,
					EtherUtils.GAS_PRICE, EtherUtils.GAS_LIMIT_ETHER_TX.multiply(BigInteger.valueOf(2)),
					credentials.getAddress(), BigInteger.valueOf(25000000000000000L));

			EthSendTransaction sendTransaction = web3.ethSendTransaction(aliceMoney).sendAsync().get();
			TransactionReceipt transactionReceipt = web3.ethGetTransactionReceipt(sendTransaction.getTransactionHash())
					.sendAsync().get().getResult();
			EtherUtils.reportTransaction("Funded account " + credentials.getAddress(), transactionReceipt);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void execute(DelegateExecution execution) {
		// Connect to the blockchain server
		Web3j web3 = Web3j.build(new HttpService());

		// Get list of accounts and provide initial funding to each account
		HashMap<Integer, EtherAccount> accounts = (HashMap<Integer, EtherAccount>) execution.getVariable("accounts");
		try {
			fundAccounts(web3, accounts, FUND_AMOUNT);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String managementProposal1 = (String) execution.getVariable("managementProposal1");
		String managementProposal2 = (String) execution.getVariable("managementProposal2");
		String managementProposal3 = (String) execution.getVariable("managementProposal3");
		String shareholderProposal1 = (String) execution.getVariable("shareholderProposal1");
		String shareholderProposal2 = (String) execution.getVariable("shareholderProposal2");
		String shareholderProposal3 = (String) execution.getVariable("shareholderProposal3");

		DynamicArray<Bytes32> proposals = new DynamicArray<Bytes32>(EtherUtils.stringToBytes32(managementProposal1),
				EtherUtils.stringToBytes32(managementProposal2), EtherUtils.stringToBytes32(managementProposal3),
				EtherUtils.stringToBytes32(shareholderProposal1), EtherUtils.stringToBytes32(shareholderProposal2),
				EtherUtils.stringToBytes32(shareholderProposal3));

		// deploy new contract, send proposal names to constructor
		try {
			Ballot myBallot = Ballot.deploy(web3, accounts.get(0).getCredentials(), EtherUtils.GAS_PRICE,
					EtherUtils.GAS_LIMIT_CONTRACT_TX, BigInteger.ZERO, proposals).get();
			TransactionReceipt deployReceipt = myBallot.getTransactionReceipt().get();
			EtherUtils.reportTransaction("Contract deployed at " + myBallot.getContractAddress(), deployReceipt);

			execution.setVariable("contractAddress", myBallot.getContractAddress());
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
