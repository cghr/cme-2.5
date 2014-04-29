/*   1:    */ package com.kentropy.agents.cme;
/*   2:    */ 
/*   3:    */ import com.kentropy.db.TestXUIDB;
/*   4:    */ import com.kentropy.db.XTaskModel;
/*   5:    */ import java.io.PrintStream;
/*   6:    */ import java.util.Date;
/*   7:    */ import java.util.Vector;
/*   8:    */ import net.xoetrope.xui.data.XBaseModel;
/*   9:    */ import net.xoetrope.xui.data.XModel;
/*  10:    */ 
/*  11:    */ public class SendTasks
/*  12:    */ {
/*  13:    */   public synchronized void send(String physician)
/*  14:    */   {
/*  15: 15 */     throw new Error("Unresolved compilation problem: \n\tThe method createVAChangeLogs(String) is undefined for the type TestXUIDB\n");
/*  16:    */   }
/*  17:    */   
/*  18:    */   public synchronized void send1(String physician)
/*  19:    */   {
/*  20:    */     try
/*  21:    */     {
/*  22: 22 */       Vector keyFields = new Vector();
/*  23: 23 */       keyFields.add("task");
/*  24: 24 */       keyFields.add("assignedto");
/*  25: 25 */       keyFields.add("member");
/*  26: 26 */       String bookmark = TestXUIDB.getInstance().getLastChangeLog();
/*  27:    */       
/*  28: 28 */       TestXUIDB.getInstance().createChangeLog("tasks", " assignedto='" + physician + "'", keyFields);
/*  29:    */       
/*  30: 30 */       createReconChangeLogs(physician);
/*  31: 31 */       TestXUIDB.getInstance().sendServerLogs("admin", 
/*  32: 32 */         physician, 
/*  33: 33 */         bookmark, "999999");
/*  34:    */     }
/*  35:    */     catch (Exception e)
/*  36:    */     {
/*  37: 37 */       e.printStackTrace();
/*  38:    */     }
/*  39:    */   }
/*  40:    */   
/*  41:    */   public void createReconChangeLogs(String phy)
/*  42:    */     throws Exception
/*  43:    */   {
/*  44: 44 */     throw new Error("Unresolved compilation problem: \n\tThe method createNotification(String, String, String, String, String, String, String, String) in the type TestXUIDB is not applicable for the arguments (String, String)\n");
/*  45:    */   }
/*  46:    */   
/*  47:    */   public static void trainingRecon(String pid, String assignedfirst, String assignedsecond)
/*  48:    */     throws Exception
/*  49:    */   {
/*  50: 50 */     String assignDate = "2011-12-6";
/*  51: 51 */     String dueDate = "2012-1-20";
/*  52:    */     
/*  53: 53 */     XTaskModel rootM = XTaskModel.getRoot(assignedfirst, "cme", "6");
/*  54: 54 */     XTaskModel cmeM = (XTaskModel)rootM.get("task0");
/*  55: 55 */     cmeM.area = "1";cmeM.household = "1";cmeM.house = "1";
/*  56: 56 */     cmeM.assignedTo = assignedfirst;
/*  57: 57 */     cmeM.set("@assignedto", assignedfirst);
/*  58: 58 */     XTaskModel codM1 = (XTaskModel)cmeM.get("task1-" + pid);
/*  59: 59 */     codM1.assignedTo = assignedfirst;
/*  60: 60 */     codM1.set("@assignedto", assignedfirst);
/*  61: 61 */     codM1.set("@dateassigned", assignDate);
/*  62: 62 */     codM1.set("@duedate", dueDate);
/*  63: 63 */     cmeM.save();
/*  64: 64 */     codM1.save();
/*  65:    */     
/*  66: 66 */     Vector keys = new Vector();
/*  67: 67 */     keys.add("key1");
/*  68:    */     
/*  69: 69 */     TestXUIDB.getInstance().createChangeLog("keyvalue", "key1 like '/va/" + pid + "/%'", keys);
/*  70: 70 */     TestXUIDB.getInstance().createChangeLog("keyvalue", "key1 like '/cme/" + pid + "/stage%'", keys);
/*  71: 71 */     TestXUIDB.getInstance().createChangeLog("keyvalue", "key1 like '/cme/" + pid + "/Coding/" + assignedfirst + "/%'", keys);
/*  72: 72 */     TestXUIDB.getInstance().createChangeLog("keyvalue", "key1 like '/cme/" + pid + "/Coding/" + assignedsecond + "/%'", keys);
/*  73: 73 */     TestXUIDB.getInstance().createChangeLog("keyvalue", "key1 like '/cme/" + pid + "/Coding/Comments/" + assignedfirst + "/%'", keys);
/*  74: 74 */     TestXUIDB.getInstance().createChangeLog("keyvalue", "key1 like '/cme/" + pid + "/Coding/Comments/" + assignedsecond + "/%'", keys);
/*  75:    */   }
/*  76:    */   
/*  77:    */   public static void main(String[] args)
/*  78:    */     throws Exception
/*  79:    */   {
/*  80: 79 */     XModel xm = new XBaseModel();
/*  81: 80 */     String table = "t2_assignment a LEFT JOIN pairs1 b ON a.id=b.id";
/*  82: 81 */     String fields = "a.id,b.report,a.physician p1,IF(b.p1=a.physician,b.p2,b.p1) p2";
/*  83: 82 */     String where = "a.physician=109 ORDER BY a.physician";
/*  84: 83 */     String bookmark = TestXUIDB.getInstance().getLastChangeLog();
/*  85:    */     
/*  86: 85 */     TestXUIDB.getInstance().getData(table, fields, where, xm);
/*  87: 86 */     String physician = "";
/*  88: 87 */     for (int i = 0; i < xm.getNumChildren(); i++)
/*  89:    */     {
/*  90: 88 */       String pid = ((XModel)xm.get(i).get("report")).get().toString();
/*  91: 89 */       TestXUIDB.getInstance().saveKeyValue("keyvalue", "/cme/" + pid + "/stage", "Reconciliation");
/*  92: 90 */       String p1 = ((XModel)xm.get(i).get("physician")).get().toString();
/*  93: 91 */       String p2 = ((XModel)xm.get(i).get("p2")).get().toString();
/*  94: 92 */       if (i == 0)
/*  95:    */       {
/*  96: 93 */         physician = p1;
/*  97:    */       }
/*  98: 95 */       else if (!physician.equals(p1))
/*  99:    */       {
/* 100: 97 */         TestXUIDB.getInstance().sendServerLogs("admin", physician, bookmark, "999999");
/* 101: 98 */         physician = p1;
/* 102: 99 */         bookmark = TestXUIDB.getInstance().getLastChangeLog();
/* 103:    */       }
/* 104:101 */       System.out.println("PID:" + pid + " P1:" + p1 + " P2:" + p2);
/* 105:102 */       trainingRecon(pid, p1, p2);
/* 106:    */     }
/* 107:105 */     TestXUIDB.getInstance().sendServerLogs("admin", physician, bookmark, "999999");
/* 108:106 */     System.out.println("END TIME::" + new Date().toString());
/* 109:    */   }
/* 110:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.agents.cme.SendTasks
 * JD-Core Version:    0.7.0.1
 */