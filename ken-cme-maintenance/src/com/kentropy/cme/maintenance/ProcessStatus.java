/*   1:    */ package com.kentropy.cme.maintenance;
/*   2:    */ 
/*   3:    */ import com.kentropy.util.DbUtil;
/*   4:    */ import com.kentropy.util.SpringUtils;
/*   5:    */ import java.util.List;
/*   6:    */ import java.util.Map;
/*   7:    */ import org.springframework.jdbc.core.JdbcTemplate;
/*   8:    */ import org.springframework.jdbc.support.rowset.SqlRowSet;
/*   9:    */ 
/*  10:    */ public class ProcessStatus
/*  11:    */ {
/*  12: 14 */   JdbcTemplate jt = new SpringUtils().getJdbcTemplate();
/*  13: 15 */   DbUtil db = new DbUtil();
/*  14: 17 */   String sql = "SELECT pid,IF(stateMachine LIKE '%coding%','coding',IF(stateMachine LIKE '%recon%','reconciliation',IF(stateMachine LIKE '%adj%','adjudication',IF(stateMachine LIKE '%cancelled%','cancelled','-')))) stage FROM `process` WHERE  stateMachine LIKE '%coding%' OR  stateMachine LIKE '%recon%'  OR  stateMachine LIKE '%adj%'  OR  stateMachine LIKE '%cancelled%'  ";
/*  15:    */   
/*  16:    */   public void updateProcessStatus()
/*  17:    */   {
/*  18: 22 */     SqlRowSet rs = this.jt.queryForRowSet(this.sql);
/*  19: 23 */     this.db.execSQl("truncate table process_status");
/*  20: 24 */     int rows = Integer.parseInt(this.db.uniqueResultCached("SELECT COUNT(*) FROM (" + this.sql + ")"));
/*  21: 25 */     String[] sqls = new String[rows];
/*  22: 26 */     int i = 0;
/*  23: 28 */     while (rs.next())
/*  24:    */     {
/*  25: 31 */       String pid = rs.getString("pid");
/*  26: 32 */       String state = getStates(pid);
/*  27: 33 */       sqls[i] = ("INSERT INTO process_status(pid,state) (" + pid + "," + state + ")");
/*  28:    */     }
/*  29: 37 */     this.jt.batchUpdate(sqls);
/*  30:    */   }
/*  31:    */   
/*  32:    */   public String getStates(String pid)
/*  33:    */   {
/*  34: 51 */     String sql = "SELECT  stage,COUNT(*) comp  FROM  `keyvalue_icd_bystage`  WHERE uniqno=? GROUP BY uniqno,stage ORDER BY  stage='Adjudication',stage='Reconciliation',stage='Coding' ";
/*  35:    */     
/*  36:    */ 
/*  37: 54 */     List<Map> list = this.db.getDataAsListofMaps("`keyvalue_icd_bystage`", "stage,COUNT(*)", "uniqno=? GROUP BY uniqno,stage ORDER BY  stage='Adjudication',stage='Reconciliation',stage='Coding'", new Object[] { pid });
/*  38:    */     
/*  39: 56 */     String state = "";
/*  40: 58 */     switch (list.size())
/*  41:    */     {
/*  42:    */     case 0: 
/*  43: 62 */       state = "2,0,0,0,0,0";
/*  44:    */       
/*  45: 64 */       break;
/*  46:    */     case 1: 
/*  47: 67 */       for (Map map : list)
/*  48:    */       {
/*  49:    */         int codincomp;
/*  50: 69 */         if (map.get("stage").equals("Coding"))
/*  51:    */         {
/*  52: 71 */           int codcomp = ((Integer)map.get("comp")).intValue();
/*  53: 72 */           codincomp = 2 - codcomp;
/*  54:    */         }
/*  55: 77 */         else if (map.get("stage").equals("Reconciliation"))
/*  56:    */         {
/*  57: 79 */           int comp = ((Integer)map.get("comp")).intValue();
/*  58: 80 */           int incomp = 2 - comp;
/*  59: 81 */           state = incomp + "," + comp + "0,0,0,0";
/*  60:    */         }
/*  61: 86 */         else if (map.get("stage").equals("Adjudication"))
/*  62:    */         {
/*  63: 88 */           int comp = ((Integer)map.get("comp")).intValue();
/*  64: 89 */           int incomp = 2 - comp;
/*  65: 90 */           state = incomp + "," + comp + "0,0,0,0";
/*  66:    */         }
/*  67:    */       }
/*  68:100 */       break;
/*  69:    */     case 2: 
/*  70:    */       break;
/*  71:    */     }
/*  72:123 */     return null;
/*  73:    */   }
/*  74:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-cme-maintenance\ken-cme-maintenance.jar
 * Qualified Name:     com.kentropy.cme.maintenance.ProcessStatus
 * JD-Core Version:    0.7.0.1
 */