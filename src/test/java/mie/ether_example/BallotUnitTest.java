package mie.ether_example;


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.form.FormProperty;
import org.flowable.engine.form.TaskFormData;
import org.flowable.task.api.Task;

import org.junit.BeforeClass;
import org.junit.Test;

public class BallotUnitTest extends LabBaseUnitTest {
	
	@BeforeClass
	public static void setupFile() {
		filename = "src/main/resources/diagrams/BallotDiagram.bpmn";
	}
	
	private void startProcess() {	
		RuntimeService runtimeService = flowableContext.getRuntimeService();
		processInstance = runtimeService.startProcessInstanceByKey("ballot_process");
	}
	
	private void Management_fillProposalsForm() {
		// form fields are filled using a map from field ids to values
		HashMap<String, String> formEntries = new HashMap<>();
		formEntries.put("aProposalM1", "Mamagement First Proposal");
		formEntries.put("aProposalM2", "Management Second Proposal");
		formEntries.put("aProposalM3", "Management Third Proposal");
		formEntries.put("aProposalM4", "Management Fourth Proposal");
		
		// get the user task (collect proposals)
		TaskService taskService = flowableContext.getTaskService();
		Task proposalsTask = taskService.createTaskQuery().taskDefinitionKey("usertask1")
				.singleResult();
		
		// get the list of fields in the form
		List<String> bpmnFieldNames = new ArrayList<>();
		TaskFormData taskFormData = flowableContext.getFormService().getTaskFormData(proposalsTask.getId());
		for (FormProperty fp : taskFormData.getFormProperties()){
			bpmnFieldNames.add(fp.getId());
		}
		
		// build a list of required fields that must be filled
		List<String> requiredFields = new ArrayList<>(
				Arrays.asList("aProposalM1", "aProposalM2", "aProposalM3","aProposalM4"));
		
		// make sure that each of the required fields is in the form
		for (String requiredFieldName : requiredFields) {
			assertTrue(bpmnFieldNames.contains(requiredFieldName));
		}
		
		// make sure that each of the required fields was assigned a value
		for (String requiredFieldName : requiredFields) {
			assertTrue(formEntries.keySet().contains(requiredFieldName));
		}
		
		// submit the form (will lead to completing the use task)
		flowableContext.getFormService().submitTaskFormData(proposalsTask.getId(), formEntries);
	}
	
	
	private void Employees_fillProposalsForm() {
		// form fields are filled using a map from field ids to values
		HashMap<String, String> formEntries = new HashMap<>();
		formEntries.put("aProposalE1", "Employee First Proposal");
		formEntries.put("aProposalE2", "Employee Second Proposal");
		formEntries.put("aProposalE3", "Employee Third Proposal");
		formEntries.put("aProposalE4", "Employee Fourth Proposal");
		
		// get the user task (collect proposals)
		TaskService taskService = flowableContext.getTaskService();
		Task proposalsTask = taskService.createTaskQuery().taskDefinitionKey("usertask2")
				.singleResult();
		
		// get the list of fields in the form
		List<String> bpmnFieldNames = new ArrayList<>();
		TaskFormData taskFormData = flowableContext.getFormService().getTaskFormData(proposalsTask.getId());
		for (FormProperty fp : taskFormData.getFormProperties()){
			bpmnFieldNames.add(fp.getId());
		}
		
		// build a list of required fields that must be filled
		List<String> requiredFields = new ArrayList<>(
				Arrays.asList("aProposalE1", "aProposalE2", "aProposalE3","aProposalE4"));
		
		// make sure that each of the required fields is in the form
		for (String requiredFieldName : requiredFields) {
			assertTrue(bpmnFieldNames.contains(requiredFieldName));
		}
		
		// make sure that each of the required fields was assigned a value
		for (String requiredFieldName : requiredFields) {
			assertTrue(formEntries.keySet().contains(requiredFieldName));
		}
		
		// submit the form (will lead to completing the use task)
		flowableContext.getFormService().submitTaskFormData(proposalsTask.getId(), formEntries);
	}
	
	
	@Test
	public void runProcess() {
		
		startProcess();
		assertNotNull(processInstance);

		Integer currentFromValue = Integer.parseInt(flowableContext.getRuntimeService()
				.getVariable(processInstance.getId(), "ProposalsFrom").toString());
		
		// Collect proposals from Manager
		if (currentFromValue == -1) {
			Management_fillProposalsForm();
		}
		
		// Collect proposals from Employees
		else if (currentFromValue == 1) {
			Employees_fillProposalsForm();
		}
		
		// Collect proposals from both Management and Employees
		else if (currentFromValue == 0) {
			Management_fillProposalsForm();
			Employees_fillProposalsForm();
		}
	}
	
	
	@Test
	public void checkProcessEnded() {
		runProcess();
		completeAllPendingTasks();
		assertPendingTaskSize(0);
	}


	private void assertPendingTaskSize(int num) {
		List<Task> list3 = flowableContext.getTaskService().createTaskQuery().list();
		assertTrue(list3.size() == num);
	}
	
	private void completeAllPendingTasks() {
		List<Task> list2 = flowableContext.getTaskService().createTaskQuery().list();
		for (Task t : list2) {
			flowableContext.getTaskService().complete(t.getId());
		}
	}
	
}
