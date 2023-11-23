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

public class DeployContractServiceTask implements JavaDelegate{
	
	public static final BigInteger FUND_AMOUNT = BigInteger.valueOf(25000000000000000L);
	
	private void fundAccounts(Web3j web3, HashMap<Integer, EtherAccount> accounts, BigInteger amountPerAccount) throws Exception {
		EthCoinbase coinbase = web3.ethCoinbase().sendAsync().get();
		
		for (EtherAccount account : accounts.values()) {
			Credentials credentials = account.getCredentials();
			EthGetTransactionCount transactionCount = web3
					.ethGetTransactionCount(coinbase.getAddress(), DefaultBlockParameterName.LATEST)
					.sendAsync()
					.get();
			
			BigInteger nonce = transactionCount.getTransactionCount();
			
			Transaction aliceMoney = Transaction.createEtherTransaction(
					coinbase.getAddress(), 
					nonce, 
					EtherUtils.GAS_PRICE, 
					EtherUtils.GAS_LIMIT_ETHER_TX.multiply(BigInteger.valueOf(2)), 
					credentials.getAddress(), 
					BigInteger.valueOf(25000000000000000L));
			

			EthSendTransaction sendTransaction = web3.ethSendTransaction(aliceMoney).sendAsync().get();
			TransactionReceipt transactionReceipt =
					web3.ethGetTransactionReceipt(sendTransaction.getTransactionHash()).sendAsync().get().getResult();
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

			String strPropM1, strPropM2, strPropM3, strPropM4;
			String strPropE1, strPropE2, strPropE3, strPropE4;
		
			String currentFromValue = (String) execution.getVariable("ProposalsFrom");
			
			// Collect proposals from Manager
			if (currentFromValue.equals("-1")) {
				System.out.println("Collect proposals from MANAGEMENT...");
				// read proposal names from Management
				strPropM1 = (String) execution.getVariable("aProposalM1");
				strPropM2 = (String) execution.getVariable("aProposalM2");
				strPropM3 = (String) execution.getVariable("aProposalM3");
				strPropM4 = (String) execution.getVariable("aProposalM4");
				
				// encode proposal name using Byte32
				Bytes32 propM1 = EtherUtils.stringToBytes32(strPropM1);
				Bytes32 propM2 = EtherUtils.stringToBytes32(strPropM2);
				Bytes32 propM3 = EtherUtils.stringToBytes32(strPropM3);
				Bytes32 propM4 = EtherUtils.stringToBytes32(strPropM4);
				
				DynamicArray<Bytes32> proposals = new DynamicArray<Bytes32>(propM1, propM2, propM3,propM4);
							
				// deploy new contract, send proposal names to constructor
				Ballot myBallot = null;
				try {
					myBallot = Ballot.deploy(web3, accounts.get(0).getCredentials(), EtherUtils.GAS_PRICE, EtherUtils.GAS_LIMIT_CONTRACT_TX, BigInteger.ZERO, proposals).get();
				} catch (InterruptedException | ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				TransactionReceipt deployReceipt = myBallot.getTransactionReceipt().get();
				EtherUtils.reportTransaction("Contract deployed at " + myBallot.getContractAddress(), deployReceipt);
		
				// save contract address as a process variable
				execution.setVariable("contractAddress", myBallot.getContractAddress());
			}


			// Collect proposals from Employees
			else if (currentFromValue.equals("1")) {
				System.out.println("Collect proposals from EMPLOYEES...");				
				// read proposal names from Management
				strPropE1 = (String) execution.getVariable("aProposalE1");
				strPropE2 = (String) execution.getVariable("aProposalE2");
				strPropE3 = (String) execution.getVariable("aProposalE3");
				strPropE4 = (String) execution.getVariable("aProposalE4");
				
				// encode proposal name using Byte32
				Bytes32 propE1 = EtherUtils.stringToBytes32(strPropE1);
				Bytes32 propE2 = EtherUtils.stringToBytes32(strPropE2);
				Bytes32 propE3 = EtherUtils.stringToBytes32(strPropE3);
				Bytes32 propE4 = EtherUtils.stringToBytes32(strPropE4);
				DynamicArray<Bytes32> proposals = new DynamicArray<Bytes32>(propE1, propE2, propE3,propE4);
							
				// deploy new contract, send proposal names to constructor
				Ballot myBallot = null;
				try {
					myBallot = Ballot.deploy(web3, accounts.get(0).getCredentials(), EtherUtils.GAS_PRICE, EtherUtils.GAS_LIMIT_CONTRACT_TX, BigInteger.ZERO, proposals).get();
				} catch (InterruptedException | ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				TransactionReceipt deployReceipt = myBallot.getTransactionReceipt().get();
				EtherUtils.reportTransaction("Contract deployed at " + myBallot.getContractAddress(), deployReceipt);
		
				// save contract address as a process variable
				execution.setVariable("contractAddress", myBallot.getContractAddress());
			}
			
			
			// Collect proposals from both Management and Employees
			else if (currentFromValue.equals("0")) {
				System.out.println("Collect proposals from both MANAGEMENT AND EMPLOYEES...");
				// read proposal names from Management
				strPropM1 = (String) execution.getVariable("aProposalM1");
				strPropM2 = (String) execution.getVariable("aProposalM2");
				strPropM3 = (String) execution.getVariable("aProposalM3");
				strPropM4 = (String) execution.getVariable("aProposalM4");
	
				// read proposal names from Employees
				strPropE1 = (String) execution.getVariable("aProposalE1");
				strPropE2 = (String) execution.getVariable("aProposalE2");
				strPropE3 = (String) execution.getVariable("aProposalE3");
				strPropE4 = (String) execution.getVariable("aProposalE4");
				// encode proposal name using Byte32
				Bytes32 propM1 = EtherUtils.stringToBytes32(strPropM1);
				Bytes32 propM2 = EtherUtils.stringToBytes32(strPropM2);
				Bytes32 propM3 = EtherUtils.stringToBytes32(strPropM3);
				Bytes32 propM4 = EtherUtils.stringToBytes32(strPropM4);
				
				Bytes32 propE1 = EtherUtils.stringToBytes32(strPropE1);
				Bytes32 propE2 = EtherUtils.stringToBytes32(strPropE2);
				Bytes32 propE3 = EtherUtils.stringToBytes32(strPropE3);
				Bytes32 propE4 = EtherUtils.stringToBytes32(strPropE4);
				DynamicArray<Bytes32> proposals = new DynamicArray<Bytes32>(propM1, propM2, propM3,propM4, propE1, propE2, propE3,propE4);
							
				// deploy new contract, send proposal names to constructor
				Ballot myBallot = null;
				try {
					myBallot = Ballot.deploy(web3, accounts.get(0).getCredentials(), EtherUtils.GAS_PRICE, EtherUtils.GAS_LIMIT_CONTRACT_TX, BigInteger.ZERO, proposals).get();
				} catch (InterruptedException | ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				TransactionReceipt deployReceipt = myBallot.getTransactionReceipt().get();
				EtherUtils.reportTransaction("Contract deployed at " + myBallot.getContractAddress(), deployReceipt);
		
				// save contract address as a process variable
				execution.setVariable("contractAddress", myBallot.getContractAddress());
			}
			

			
			
			
			
	}

}
