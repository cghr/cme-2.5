package com.kentropy.cme.reports;


import com.kentropy.util.SpringApplicationContext;
import com.kentropy.util.SpringUtils;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class InvoicePayments {
	
	
	private static Logger log=Logger.getLogger("Billing");
	/*public static void generateInvoices(String args[])
	{
		SqlRowSet rs=null;
		JdbcTemplate jt=new SpringUtils().getJdbcTemplate();
		int newInvoices=0;
		try
		{
		
		String month=(String)jt.queryForObject("SELECT DATE_FORMAT(NOW(),'%m%Y')",String.class);
		String sql="SELECT count(*) FROM billables WHERE DATE_FORMAT(date_of_billable,'%m%Y') LIKE '"+month+"%'";		
		rs=jt.queryForRowSet(sql);
		rs.next();
		newInvoices=rs.getInt(1);
		
		if(newInvoices==0)
		{
			log.info("===> No Pending Invoices to be Generated");
		return;
		
		}
		  String sqls[]={
	    		   "UPDATE billables SET invoice_id=CONCAT(DATE_FORMAT(date_of_billable,'%m%Y'),physician_id) WHERE DATE_FORMAT(date_of_billable,'%m%Y') LIKE '"+month+"%'",
	    		   "INSERT IGNORE INTO  invoices(invoice_id,physician_id,amount)   (SELECT a.invoice_id,a.physician_id,(a.cod*(SELECT rate FROM rate_billables WHERE role='coding'))+(a.adj*(SELECT rate FROM rate_billables WHERE role='adjudication')) amount FROM (SELECT invoice_id,physician_id,SUM(IF(role='coding',1,0)) cod,SUM(IF(role='adjudication',1,0)) adj FROM billables WHERE DATE_FORMAT(date_of_billable,'%m%Y') LIKE '"+month+"%'  GROUP BY invoice_id ) a)",
	    		   "UPDATE invoices SET tax=(amount) DIV  (SELECT tax FROM tax_details WHERE item='invoice') where invoice_id like '"+month+"%'",
	    		   "UPDATE invoices SET net=amount-tax  where invoice_id like '"+month+"%'"
	    		   		
	       };
		   
		  int[] effect=jt.batchUpdate(sqls);
		
		
		
		}
		catch (Exception e) {
			log.error("==> Error in Generating Invoices",e);
		
		}
	}*/

	
	JdbcTemplate jt=new SpringUtils().getJdbcTemplate();
   SqlRowSet rs=null;
  
   
   
   
   
	public int generateInvoices(String month) 
	{
		// Set Initial Balance to Zero for the Current Month
		int res=jt.queryForInt("SELECT COUNT(*) FROM `bank_balance_details` WHERE `month` LIKE  DATE_FORMAT(NOW(),'%m%Y')");
		if(res==0)
			jt.update("INSERT INTO `bank_balance_details`VALUES(0,DATE_FORMAT(NOW(),'%m%Y'))");
		
		
		int newInvoices=0;
		int[] effect=null;
		//Check for Pending Invoices to be generated
		try
		{
	String sql="SELECT count(*) FROM billables WHERE DATE_FORMAT(date_of_billable,'%m%Y') LIKE '"+month+"%'";		
	
	rs=jt.queryForRowSet(sql);
	rs.next();
	newInvoices=rs.getInt(1);
	
	if(newInvoices==0)
	return 0;
	   String sqls[]={
    		   "UPDATE billables SET invoice_id=CONCAT(DATE_FORMAT(date_of_billable,'%m%Y'),physician_id) WHERE DATE_FORMAT(date_of_billable,'%m%Y') LIKE '"+month+"%'",
    		   "INSERT IGNORE INTO  invoices(invoice_id,physician_id,amount)   (SELECT a.invoice_id,a.physician_id,(a.cod*(SELECT rate FROM rate_billables WHERE role='coding'))+(a.adj*(SELECT rate FROM rate_billables WHERE role='adjudication')) amount FROM (SELECT invoice_id,physician_id,SUM(IF(role='coding',1,0)) cod,SUM(IF(role='adjudication',1,0)) adj FROM billables WHERE DATE_FORMAT(date_of_billable,'%m%Y') LIKE '"+month+"%'  GROUP BY invoice_id ) a)",
    		   "UPDATE invoices SET tax=(amount) DIV  (SELECT tax FROM tax_details WHERE item='invoice') where invoice_id like '"+month+"%'",
    		   "UPDATE invoices SET net=amount-tax  where invoice_id like '"+month+"%'"
    		   		
       };
	   
	  effect=jt.batchUpdate(sqls);
		
		
	}
	catch(Exception ex)
	{
		ex.printStackTrace();
	}
	
	log.info("==> No of Invoices to be Generated " +effect[2]);
		
		return effect[2];
	}
	public int startNewPayment(String month)
	{
		int newPayments=0;
		//Check for Pending Payments  to be generated
		String sql="SELECT COUNT(*) FROM invoices WHERE payment_id IS  NULL AND invoice_id like '"+month+"%'";
	    rs=jt.queryForRowSet(sql);
		
	
	rs.next();
	newPayments=rs.getInt(1);
	if(newPayments==0)
	return 0;
	
	try
	{
		
		log.info("==> Trying to Insert data into Payments Table");
		sql="INSERT INTO payments(DATE,amount,formonth) (SELECT CURDATE(),SUM(net),left(invoice_id,6) FROM invoices WHERE payment_id IS NULL AND invoice_id like '"+month+"%' GROUP BY CURDATE())";
		jt.update(sql);
		log.info("==> Inserted Data to payments Successfully");
		log.info("==> Trying to Update Invoices");
		sql="UPDATE invoices SET payment_id=(SELECT MAX(payment_id) FROM payments) WHERE invoice_id like '"+month+"%'";
		jt.update(sql);
		log.info("==> Invoices Updated Successfully");
		String payment_id=(String)jt.queryForObject("SELECT MAX(payment_id) from payments",String.class);
		/*Message msg=(Message) SpringApplicationContext.getBean("msg");
		msg.setQuery(msg.getQuery().replace("?",payment_id));
		msg.setSummaryQuery(msg.getSummaryQuery().replace("?",payment_id));
		log.debug("==> "+msg.getQuery());
		MessageDAO dao=new MessageDAO();
		dao.saveMessage(msg);*/
		
		
	}
	catch(Exception ex)
	{
		ex.printStackTrace();
		//Rollback incase of error
		//conn.rollback();
	}
	
	
		
		
		return newPayments;
		
		
		
	}
	public void resetPaymentsModule() 
	{
		String[] sqls={
				"truncate table invoices",
				"truncate table payments",
				"update billables set invoice_id=NULL"
				
		};
		
		
	  jt.batchUpdate(sqls);
	  log.info("==> Truncated Payments Module Tables");
	}
	public static void main(String[] args)  {
		InvoicePayments ob=new InvoicePayments();
		//System.out.println(ob.generateInvoices()+" New Invoices Generated");
		//System.out.println("Payment Made for "+ob.startNewPayment()+" Invoices");
		//new InvoicePayments().generateInvoices();
	}
}
