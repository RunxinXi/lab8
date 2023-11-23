package mie.ether_example;


import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import edu.toronto.dbservice.config.MIE354DBHelper;
import edu.toronto.dbservice.types.EmployeeVote;
import edu.toronto.dbservice.types.Proxy;
import edu.toronto.dbservice.types.EtherAccount;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

public class GetProxies implements JavaDelegate{

	Connection dbCon = null;

	public GetProxies() {
		dbCon = MIE354DBHelper.getDBConnection();
	}
	
	public void execute(DelegateExecution execution) {
		
		Statement statement = null;
		ResultSet resultSet = null;
		List<Proxy> proxies = new ArrayList<>();
		
		try {
			statement = dbCon.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM PROXIES ORDER BY ID");
			while (resultSet.next()) {
				Integer id = resultSet.getInt("ID");
				Integer accountId = resultSet.getInt("ACCOUNTID");
				Integer proxyHolderId = resultSet.getInt("PROXYHOLDERID");
				
				Proxy proxy = new Proxy(id, accountId, proxyHolderId);
				proxies.add(proxy);
			}	
			resultSet.close();

			execution.setVariable("proxyList", proxies);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
