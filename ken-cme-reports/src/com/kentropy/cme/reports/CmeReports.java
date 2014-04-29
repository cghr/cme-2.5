package com.kentropy.cme.reports;

import org.springframework.jdbc.core.JdbcTemplate;

import com.kentropy.process.Process;
import com.kentropy.util.SpringUtils;

public class CmeReports {
	
	
	
	public static void performanceReports(String[] args)
	{
		try
		{
		 JdbcTemplate jt=new SpringUtils().getJdbcTemplate();
		String[] sql={
				"TRUNCATE TABLE performance_report_cod",
				"INSERT INTO performance_report_cod(SELECT MONTH,assignedto,SUM(IF(DATE_FORMAT(dateassigned,'%Y-%m')= MONTH,1,0)) cod," +
				"SUM(IF(STATUS=1 AND DATE_FORMAT(endtime,'%Y-%m')=MONTH,1,0)) comp,SUM(IF(DATE_FORMAT(duedate,'%Y-%m')<=MONTH ,1,0)) due," +
				"SUM(IF(DATE(duedate)<DATE(endtime)  OR " +
				"( DATE_FORMAT(duedate,'%Y-%m')<=MONTH  AND (STATUS IS NULL OR DATE_FORMAT(endtime,'%Y-%m')<=MONTH)) ,1,0))  `delayed` " +
				" FROM months a LEFT JOIN tasks b ON a.month>=DATE_FORMAT(b.dateassigned,'%Y-%m') " +
				"WHERE task LIKE '%task0/task0'   GROUP BY MONTH,assignedto ORDER BY MONTH ASC) ;" ,
				"TRUNCATE TABLE cod_pending",
				"INSERT INTO cod_pending" +
				"(SELECT  SUM(cod_assigned)-SUM(cod_completed) pending,a.month,physician_id FROM months a LEFT JOIN performance_report_cod b ON a.month>b.date  GROUP BY a.month,b.physician_id )",
				"TRUNCATE TABLE performance_report_recon",
				"INSERT INTO performance_report_recon(SELECT MONTH,assignedto,SUM(IF(DATE_FORMAT(dateassigned,'%Y-%m')= MONTH,1,0)) cod," +
				"SUM(IF(STATUS=1 AND DATE_FORMAT(endtime,'%Y-%m')=MONTH,1,0)) comp,SUM(IF(DATE_FORMAT(duedate,'%Y-%m')<=MONTH ,1,0)) due,SUM(IF(DATE(duedate)<DATE(endtime)  OR" +
				"( DATE_FORMAT(duedate,'%Y-%m')<=MONTH  AND (STATUS IS NULL OR DATE_FORMAT(endtime,'%Y-%m')<=MONTH)) ,1,0))  `delayed`  FROM months a LEFT JOIN tasks b ON a.month>=DATE_FORMAT(b.dateassigned,'%Y-%m')" +
				"WHERE task LIKE '%task0/task1'   GROUP BY MONTH,assignedto ORDER BY MONTH ASC)" ,
				"TRUNCATE TABLE recon_pending",
				"INSERT INTO recon_pending(SELECT  SUM(recon_assigned)-SUM(recon_completed) pending,a.month,physician_id FROM months a LEFT JOIN performance_report_recon b ON a.month>b.date  GROUP BY a.month,b.physician_id )"
				,"TRUNCATE TABLE performance_report_adj",
				"INSERT INTO performance_report_adj(SELECT MONTH,assignedto,SUM(IF(DATE_FORMAT(dateassigned,'%Y-%m')= MONTH,1,0)) cod,SUM(IF(STATUS=1 AND DATE_FORMAT(endtime,'%Y-%m')=MONTH,1,0)) comp,SUM(IF(DATE_FORMAT(duedate,'%Y-%m')<=MONTH ,1,0)) due," +
				"SUM(IF(DATE(duedate)<DATE(endtime)  OR" +
				"( DATE_FORMAT(duedate,'%Y-%m')<=MONTH  AND (STATUS IS NULL OR DATE_FORMAT(endtime,'%Y-%m')<=MONTH)) ,1,0))  `delayed`  FROM months a LEFT JOIN tasks b ON a.month>=DATE_FORMAT(b.dateassigned,'%Y-%m')" +
				"WHERE task LIKE '%task0/task2'   GROUP BY MONTH,assignedto ORDER BY MONTH ASC)",
				"TRUNCATE TABLE adj_pending",
				"INSERT INTO adj_pending(SELECT  SUM(adj_assigned)-SUM(adj_completed) pending,a.month,physician_id FROM months a LEFT JOIN performance_report_adj b ON a.month>b.date  GROUP BY a.month,b.physician_id )"

        };
		jt.batchUpdate(sql);
		
		 Process.refreshProcessStatus();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	
		
		
	}
	public static void refreshProcessStatus(String args[]) 
	{
		try{
			System.out.println("==>  CAlling Refresh Process STatus ");
		 Process.refreshProcessStatus();
		 
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		 
	}
	
	public static void main(String[] args) {
		performanceReports(args);
	}

}
