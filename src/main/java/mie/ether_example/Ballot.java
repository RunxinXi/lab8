package mie.ether_example;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;

/**
 * Auto generated code.<br>
 * <strong>Do not modify!</strong><br>
 * Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>, or {@link org.web3j.codegen.SolidityFunctionWrapperGenerator} to update.
 *
 * <p>Generated with web3j version 2.3.1.
 */
public final class Ballot extends Contract implements Serializable {
	private static final long serialVersionUID = 7526472295622776147L;
	
    private static final String BINARY = "6060604052341561000f57600080fd5b6040516106ad3803806106ad8339810160405280805190910190505b60008054600160a060020a03191633600160a060020a03908116919091178083551681526001602081905260408220555b81518110156100d057600280546001810161007783826100d8565b916000526020600020906002020160005b604080519081016040528086868151811061009f57fe5b906020019060200201518152600060209091015291905081518155602082015181600101555050505b60010161005c565b5b5050610131565b81548183558181151161010457600202816002028360005260206000209182019101610104919061010a565b5b505050565b61012e91905b8082111561012a5760008082556001820155600201610110565b5090565b90565b61056d806101406000396000f3006060604052361561008b5763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416630121b93f8114610090578063013cf08b146100a85780632e4176cf146100d65780635c19a95c14610105578063609ff1bd146101265780639e7b8d611461014b578063a3ec138d1461016c578063e2ba53f0146101c0575b600080fd5b341561009b57600080fd5b6100a66004356101e5565b005b34156100b357600080fd5b6100be60043561025b565b60405191825260208201526040908101905180910390f35b34156100e157600080fd5b6100e9610289565b604051600160a060020a03909116815260200160405180910390f35b341561011057600080fd5b6100a6600160a060020a0360043516610298565b005b341561013157600080fd5b6101396103e2565b60405190815260200160405180910390f35b341561015657600080fd5b6100a6600160a060020a0360043516610456565b005b341561017757600080fd5b61018b600160a060020a03600435166104de565b6040519384529115156020840152600160a060020a031660408084019190915260608301919091526080909101905180910390f35b34156101cb57600080fd5b610139610512565b60405190815260200160405180910390f35b600160a060020a03331660009081526001602081905260409091209081015460ff161561021157600080fd5b6001818101805460ff191690911790556002808201839055815481549091908490811061023a57fe5b906000526020600020906002020160005b50600101805490910190555b5050565b600280548290811061026957fe5b906000526020600020906002020160005b50805460019091015490915082565b600054600160a060020a031681565b600160a060020a033316600090815260016020819052604082209081015490919060ff16156102c657600080fd5b33600160a060020a031683600160a060020a0316141515156102e757600080fd5b5b600160a060020a03838116600090815260016020819052604090912001546101009004161561034a57600160a060020a03928316600090815260016020819052604090912001546101009004831692331683141561034557600080fd5b6102e7565b506001818101805460ff1916821774ffffffffffffffffffffffffffffffffffffffff001916610100600160a060020a0386169081029190911790915560009081526020829052604090209081015460ff16156103d4578154600282810154815481106103b357fe5b906000526020600020906002020160005b50600101805490910190556103dc565b815481540181555b5b505050565b600080805b600254811015610450578160028281548110151561040157fe5b906000526020600020906002020160005b5060010154111561044757600280548290811061042b57fe5b906000526020600020906002020160005b506001015491508092505b5b6001016103e7565b5b505090565b60005433600160a060020a0390811691161480156104915750600160a060020a0381166000908152600160208190526040909120015460ff16155b80156104b35750600160a060020a038116600090815260016020526040902054155b15156104be57600080fd5b600160a060020a0381166000908152600160208190526040909120555b50565b600160208190526000918252604090912080549181015460029091015460ff8216916101009004600160a060020a03169084565b6000600261051e6103e2565b8154811061052857fe5b906000526020600020906002020160005b505490505b905600a165627a7a723058209d0d387a2d858e1cc1a42f1c6dd9f627f6d1c7b34e6df4564a25f98acd017fdc0029";

    private Ballot(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    private Ballot(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public Future<TransactionReceipt> vote(Uint256 proposal) {
        Function function = new Function("vote", Arrays.<Type>asList(proposal), Collections.<TypeReference<?>>emptyList());
        return executeTransactionAsync(function);
    }

    public Future<List<Type>> proposals(Uint256 param0) {
        Function function = new Function("proposals", 
                Arrays.<Type>asList(param0), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}, new TypeReference<Uint256>() {}));
        return executeCallMultipleValueReturnAsync(function);
    }

    public Future<Address> chairperson() {
        Function function = new Function("chairperson", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeCallSingleValueReturnAsync(function);
    }

    public Future<TransactionReceipt> delegate(Address to) {
        Function function = new Function("delegate", Arrays.<Type>asList(to), Collections.<TypeReference<?>>emptyList());
        return executeTransactionAsync(function);
    }

    public Future<Uint256> winningProposal() {
        Function function = new Function("winningProposal", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeCallSingleValueReturnAsync(function);
    }

    public Future<TransactionReceipt> giveRightToVote(Address voter) {
        Function function = new Function("giveRightToVote", Arrays.<Type>asList(voter), Collections.<TypeReference<?>>emptyList());
        return executeTransactionAsync(function);
    }

    public Future<List<Type>> voters(Address param0) {
        Function function = new Function("voters", 
                Arrays.<Type>asList(param0), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Bool>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
        return executeCallMultipleValueReturnAsync(function);
    }

    public Future<Bytes32> winnerName() {
        Function function = new Function("winnerName", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}));
        return executeCallSingleValueReturnAsync(function);
    }

    public static Future<Ballot> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, BigInteger initialWeiValue, DynamicArray<Bytes32> proposalNames) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(proposalNames));
        return deployAsync(Ballot.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor, initialWeiValue);
    }

    public static Future<Ballot> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, BigInteger initialWeiValue, DynamicArray<Bytes32> proposalNames) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(proposalNames));
        return deployAsync(Ballot.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor, initialWeiValue);
    }

    public static Ballot load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Ballot(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static Ballot load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Ballot(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }
}
