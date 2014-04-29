/*   1:    */ package com.kentropy.workflow;
/*   2:    */ 
/*   3:    */ import com.kentropy.util.SpringUtils;
/*   4:    */ import java.io.PrintStream;
/*   5:    */ import java.util.HashMap;
/*   6:    */ import java.util.Map;
/*   7:    */ import java.util.Vector;
/*   8:    */ import org.springframework.jdbc.core.JdbcTemplate;
/*   9:    */ import org.springframework.jdbc.support.rowset.SqlRowSet;
/*  10:    */ 
/*  11:    */ public class WfTask
/*  12:    */ {
/*  13:    */   public static Vector<String> getHistory(String pid)
/*  14:    */   {
/*  15: 17 */     JdbcTemplate jt = new SpringUtils().getJdbcTemplate();
/*  16: 18 */     String sql = "select * from workflow_tasks where pid='" + pid + "' and status is not null ";
/*  17: 19 */     System.out.println(" TaskDEf  sql " + sql);
/*  18: 20 */     SqlRowSet rs = jt.queryForRowSet(sql);
/*  19: 21 */     Vector<String> row = new Vector();
/*  20: 22 */     while (rs.next()) {
/*  21: 24 */       row.add(" task :" + rs.getString("task") + " Assigned To :" + rs.getString("assignedto") + " ON :" + rs.getString("assignment_time") + "<br> Completed On:" + rs.getString("end_time") + " by " + rs.getString("completedby") + " Action taken:" + rs.getString("action_taken"));
/*  22:    */     }
/*  23: 27 */     return row;
/*  24:    */   }
/*  25:    */   
/*  26:    */   public static Vector<String> getWfTaskDefinition(String process, String task)
/*  27:    */   {
/*  28: 33 */     JdbcTemplate jt = new SpringUtils().getJdbcTemplate();
/*  29: 34 */     String sql = "select * from wf_task_definition where process='" + process + "' and task='" + task + "'";
/*  30: 35 */     System.out.println(" TaskDEf  sql " + sql);
/*  31: 36 */     SqlRowSet rs = jt.queryForRowSet(sql);
/*  32:    */     
/*  33: 38 */     Vector<String> row = new Vector();
/*  34: 39 */     if (rs.next())
/*  35:    */     {
/*  36: 41 */       row.add(rs.getString("url"));
/*  37: 42 */       row.add(rs.getString("action"));
/*  38: 43 */       row.add(rs.getString("js_validation"));
/*  39: 44 */       row.add(rs.getString("label"));
/*  40:    */     }
/*  41: 46 */     return row;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public static Map getTask(String id)
/*  45:    */   {
/*  46: 51 */     Map map = new HashMap();
/*  47: 52 */     JdbcTemplate jt = new SpringUtils().getJdbcTemplate();
/*  48: 53 */     String qry = "SELECT * FROM workflow_tasks where id='" + id + "'";
/*  49: 54 */     System.out.println("work_flowTask:" + qry);
/*  50: 55 */     SqlRowSet rs = jt.queryForRowSet(qry);
/*  51: 56 */     map = jt.queryForMap(qry);
/*  52: 63 */     if (rs.next())
/*  53:    */     {
/*  54: 70 */       String[] toks = map.get("pid").toString().split("_");
/*  55: 71 */       String entity = toks[0];
/*  56:    */       
/*  57: 73 */       Vector<String> row = getWfTaskDefinition(toks[0], map.get("task").toString());
/*  58: 74 */       String url = "../../" + (String)row.get(0);
/*  59: 75 */       String jsValidation = (String)row.get(2);
/*  60: 81 */       for (int i = 1; i < toks.length; i++) {
/*  61: 83 */         url = url.replaceAll("<" + i + ">", toks[i]);
/*  62:    */       }
/*  63: 86 */       url = url + "&taskparams=" + map.get("assignment_time");
/*  64: 87 */       map.put("url", url);
/*  65: 88 */       map.put("jsValidation", jsValidation);
/*  66: 89 */       Vector<String> options = new Vector();
/*  67:    */       
/*  68: 91 */       String[] acttasks = ((String)row.get(1)).split(",");
/*  69: 92 */       String[] labels = ((String)row.get(3)).split(",");
/*  70: 93 */       if (acttasks != null) {
/*  71: 95 */         for (int i = 0; i < acttasks.length; i++) {
/*  72: 97 */           options.add(acttasks[i] + "-" + labels[i]);
/*  73:    */         }
/*  74:    */       }
/*  75:103 */       map.put("options", options);
/*  76:    */     }
/*  77:108 */     return map;
/*  78:    */   }
/*  79:    */   
/*  80:    */   public static void main(String[] args) {}
/*  81:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-utilities6\ken-utilities6.jar
 * Qualified Name:     com.kentropy.workflow.WfTask
 * JD-Core Version:    0.7.0.1
 */