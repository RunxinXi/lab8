package edu.toronto.dbservice.types;

import java.io.Serializable;

public class Proxy implements Serializable{
	private static final long serialVersionUID = 1L;

	private int id;
	private int accountId;
	private int proxyHolderId;
	
	public Proxy(int id, int accountId, int proxyHolderId) {
		this.id = id;
		this.accountId = accountId;
		this.proxyHolderId = proxyHolderId;
	}

	public Integer getId() {
		return id;
	}
	
	public Integer getAccountId() {
		return accountId;
	}
	
	public Integer getProxyHolderId() {
		return proxyHolderId;
	}
}
