package edu.toronto.dbservice.types;

import java.io.Serializable;

public class ShareholderVote implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer accountId;
	private Integer proposalId;
	
	public ShareholderVote(Integer accountId, Integer proposalId) {
		this.accountId = accountId;
		this.proposalId = proposalId;
	}
	
	public Integer getAccountId() {
		return accountId;
	}
	
	public Integer getProposalId() {
		return proposalId;
	}

}