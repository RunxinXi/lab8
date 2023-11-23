package mie.ether_example;

import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;

import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import edu.toronto.dbservice.config.MIE354DBHelper;
import edu.toronto.dbservice.types.EtherAccount;

@RunWith(Parameterized.class)
public class ShareholderVoteUnitTest extends LabBaseUnitTest {

	@BeforeClass
	public static void setupFile() {
		filename = "src/main/resources/diagrams/ShareholderVote.bpmn";
	}

	SortedMap<Integer, Integer> proxies;
	SortedMap<Integer, Integer> votes;
	Integer expectedWinner;

	public ShareholderVoteUnitTest(SortedMap<Integer, Integer> proxies, SortedMap<Integer, Integer> votes,
			Integer expectedWinner) {
		this.proxies = proxies;
		this.votes = votes;
		this.expectedWinner = expectedWinner;
	}

	@Parameters
	public static Collection<Object[]> parameters() {
		ArrayList<Object[]> parameters = new ArrayList<>();
		
		// TESTCASE 1: No proxies or votes
		parameters.add(new Object[] {null, null, 0});
		
		// TESTCASE 2: Shareholder 1 votes for Proposal 2; No proxies
		SortedMap<Integer, Integer> votes2 = new TreeMap<Integer, Integer>();
		votes2.put(2, 1);
		parameters.add(new Object[] { null, votes2, 2 });
		
		// TESTCASE 3: Shareholder 1 delegates vote to Shareholder 2; Shareholder 2 delegates vote to Shareholder 3;
		// Shareholder 3 votes for Proposal 1; Shareholder 4 votes for Proposal 3
		SortedMap<Integer, Integer> proxies3 = new TreeMap<Integer, Integer>();
		proxies3.put(1, 2);
		proxies3.put(2, 3);
		SortedMap<Integer, Integer> votes3 = new TreeMap<Integer, Integer>();
		votes3.put(3, 2);
		votes3.put(4, 3);
		parameters.add(new Object[] { proxies3, votes3, 2 });
		
		return parameters;
	}

	private void fillManagementProposalsForm() {
		TaskService taskService = flowableContext.getTaskService();
		Task form = taskService.createTaskQuery().taskDefinitionKey("managementProposalsForm").singleResult();

		HashMap<String, String> formEntries = new HashMap<>();
		formEntries.put("managementProposal1", "Proposal M1");
		formEntries.put("managementProposal2", "Proposal M2");
		formEntries.put("managementProposal3", "Proposal M3");

		flowableContext.getFormService().submitTaskFormData(form.getId(), formEntries);
	}

	private void fillShareholderProposalsForm() {
		TaskService taskService = flowableContext.getTaskService();
		Task form = taskService.createTaskQuery().taskDefinitionKey("shareholderProposalsForm").singleResult();

		HashMap<String, String> formEntries = new HashMap<>();
		formEntries.put("shareholderProposal1", "Proposal S1");
		formEntries.put("shareholderProposal2", "Proposal S2");
		formEntries.put("shareholderProposal3", "Proposal S3");

		flowableContext.getFormService().submitTaskFormData(form.getId(), formEntries);
	}

	private void setupProxies() {
		if (proxies == null) {
			return;
		}
		
		Connection conn = MIE354DBHelper.getDBConnection();
		try {
			Statement stmt = conn.createStatement();

			int i = 0;
			for (Entry<Integer, Integer> proxy : proxies.entrySet()) {
				String sql = String.format("INSERT INTO PROXIES VALUES (%d, %d, %d)", i, proxy.getKey(),
						proxy.getValue());
				stmt.execute(sql);
			}
		} catch (SQLException e) {
			//
		}
	}

	private void setupVotes() {
		if (votes == null) {
			return;
		}
		
		Connection conn = MIE354DBHelper.getDBConnection();
		try {
			Statement stmt = conn.createStatement();

			int i = 0;
			for (Entry<Integer, Integer> vote : votes.entrySet()) {
				String sql = String.format("INSERT INTO SHAREHOLDERVOTES VALUES (%d, %d)", vote.getKey(),
						vote.getValue());
				stmt.execute(sql);
			}
		} catch (SQLException e) {
			//
		}
	}

	private void runProcess() {
		RuntimeService runtimeService = flowableContext.getRuntimeService();
		processInstance = runtimeService.startProcessInstanceByKey("ballot_process");
		fillManagementProposalsForm();
		fillShareholderProposalsForm();
	}

	private boolean checkWinner() {
		HashMap<Integer, EtherAccount> accounts = (HashMap<Integer, EtherAccount>) flowableContext.getHistoryService()
				.createHistoricVariableInstanceQuery().variableName("accounts").singleResult().getValue();

		Web3j web3 = Web3j.build(new HttpService());
		String contractAddress = (String) flowableContext.getHistoryService().createHistoricVariableInstanceQuery()
				.variableName("contractAddress").singleResult().getValue();
		Ballot ballot = Ballot.load(contractAddress, web3, accounts.get(0).getCredentials(), EtherUtils.GAS_PRICE,
				EtherUtils.GAS_LIMIT_CONTRACT_TX);
		
		int winner = 0;
		try {
			winner = ballot.winningProposal().get().getValue().intValue();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return expectedWinner == winner;
	}

	@Test
	public void testShareholderVote() {
		setupProxies();
		setupVotes();
		runProcess();
		assertTrue(checkWinner());
	}
}
