/*  1:   */ package com.kentropy.training.cme;
/*  2:   */ 
/*  3:   */ import com.kentropy.db.TestXUIDB;
/*  4:   */ import com.kentropy.process.PhysicianStateMachine3;
/*  5:   */ import com.kentropy.process.Process;
/*  6:   */ import java.io.PrintStream;
/*  7:   */ import java.text.SimpleDateFormat;
/*  8:   */ import java.util.Vector;
/*  9:   */ import net.xoetrope.xui.data.XBaseModel;
/* 10:   */ import net.xoetrope.xui.data.XModel;
/* 11:   */ 
/* 12:   */ public class OutlineView
/* 13:   */ {
/* 14:   */   public String getActivities(String activities, String activityURL)
/* 15:   */   {
/* 16:16 */     String[] actArr1 = activities.split(";");
/* 17:17 */     String[] urlArr1 = activityURL.split(";");
/* 18:18 */     String str = "<table width='80%'>";
/* 19:19 */     str = str + "<tr><th>Activity</th><th>Download Links</th>";
/* 20:21 */     for (int i = 0; i < actArr1.length; i++)
/* 21:   */     {
/* 22:23 */       String url1 = "";
/* 23:24 */       String[] url = { "" };
/* 24:25 */       String url2 = "";
/* 25:26 */       if (i < urlArr1.length)
/* 26:   */       {
/* 27:28 */         url = urlArr1[i].split(":");
/* 28:29 */         url2 = urlArr1[i];
/* 29:30 */         if (url.length == 2) {
/* 30:32 */           url1 = "<a href='../" + url[1] + "'>" + url[0] + "</a>";
/* 31:   */         }
/* 32:   */       }
/* 33:35 */       str = str + "<tr><td width='200'>" + actArr1[i] + "</td><td>" + url1 + " " + "</td></tr>";
/* 34:   */     }
/* 35:37 */     str = str + "</table>";
/* 36:38 */     return str;
/* 37:   */   }
/* 38:   */   
/* 39:   */   public String getOutline(String traineeId)
/* 40:   */     throws Exception
/* 41:   */   {
/* 42:42 */     Process p = TestXUIDB.getInstance().getProcess(traineeId);
/* 43:43 */     PhysicianStateMachine3 psm3 = (PhysicianStateMachine3)p.states;
/* 44:44 */     XModel xm = new XBaseModel();
/* 45:45 */     TestXUIDB.getInstance().getData("training_step", "*", "", xm);
/* 46:46 */     String str = "";
/* 47:47 */     for (int i = 0; i < psm3.steps.size(); i++)
/* 48:   */     {
/* 49:49 */       XModel rowM = xm.get(i);
/* 50:50 */       String status = "Not started";
/* 51:51 */       if (psm3.currentStep == i) {
/* 52:52 */         status = "Current ";
/* 53:   */       }
/* 54:53 */       if (psm3.currentStep > i) {
/* 55:54 */         status = "Complete ";
/* 56:   */       }
/* 57:56 */       str = str + "<UL style='border:1px solid yellow;display:block'>";
/* 58:57 */       str = str + "<LI><B> Step:" + rowM.get("stepId/@value") + "</B>";
/* 59:58 */       str = str + "<LI><B> Objective:" + rowM.get("objectives/@value") + "</B>";
/* 60:59 */       str = str + "<LI> <B>Description</B>:<P>" + rowM.get("description/@value") + "</P>";
/* 61:60 */       str = str + "<LI> <B>Activities:</B>" + getActivities(rowM.get("activities/@value").toString(), rowM.get("activity_urls/@value").toString());
/* 62:61 */       str = str + "<LI><B>Completion Criteria</B>:" + rowM.get("criteria_description/@value");
/* 63:62 */       str = str + "<LI><B>Allowed attempts</B>:" + rowM.get("criteria_attempt/@value");
/* 64:63 */       str = str + "<LI><B>Duration</B>:" + rowM.get("duration/@value") + " days";
/* 65:64 */       str = str + "<LI><B>Completion Date</B>:" + new SimpleDateFormat("dd-MM-yyyy").format(new SimpleDateFormat("yyyy-MM-dd").parse(((PhysicianStateMachine3)psm3.steps.get(i)).eddate));
/* 66:65 */       str = str + "<LI><B>Status</B>:" + status;
/* 67:66 */       str = str + "</UL>";
/* 68:   */     }
/* 69:69 */     return str;
/* 70:   */   }
/* 71:   */   
/* 72:   */   public static void main(String[] args)
/* 73:   */     throws Exception
/* 74:   */   {
/* 75:74 */     OutlineView olv = new OutlineView();
/* 76:   */     
/* 77:76 */     System.out.println(olv.getOutline("199999"));
/* 78:   */   }
/* 79:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.training.cme.OutlineView
 * JD-Core Version:    0.7.0.1
 */