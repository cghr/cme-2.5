/*   1:    */ package com.kentropy.agents.cme;
/*   2:    */ 
/*   3:    */ import com.kentropy.db.TestXUIDB;
/*   4:    */ import com.kentropy.db.XTaskModel;
/*   5:    */ import com.kentropy.process.Agent;
/*   6:    */ import com.kentropy.process.CMEStateMachine2;
/*   7:    */ import com.kentropy.process.Process;
/*   8:    */ import com.kentropy.process.StateMachine;
/*   9:    */ import com.kentropy.transfer.Client;
/*  10:    */ import java.io.PrintStream;
/*  11:    */ import java.text.SimpleDateFormat;
/*  12:    */ import java.util.Calendar;
/*  13:    */ import java.util.Date;
/*  14:    */ import java.util.GregorianCalendar;
/*  15:    */ import java.util.Vector;
/*  16:    */ import net.xoetrope.xui.data.XBaseModel;
/*  17:    */ import net.xoetrope.xui.data.XModel;
/*  18:    */ 
/*  19:    */ public class AssignmentAgent3
/*  20:    */   implements Agent
/*  21:    */ {
/*  22: 22 */   CMEStateMachine2 sm = null;
/*  23: 24 */   Process p = null;
/*  24: 26 */   public String ext = "png";
/*  25: 28 */   int i = 0;
/*  26: 30 */   int count = 0;
/*  27:    */   
/*  28:    */   public void sendImage(String phy)
/*  29:    */     throws Exception
/*  30:    */   {
/*  31: 35 */     String imagepath = new TestXUIDB().getProperty("imagePath");
/*  32: 36 */     String firstpage = this.p.pid + "_0_blank." + this.ext;
/*  33: 37 */     String secondpage = this.p.pid + "_1_blank." + this.ext;
/*  34: 38 */     String codcrop = this.p.pid + "_cod." + this.ext;
/*  35:    */     
/*  36: 40 */     Client cl = new Client();
/*  37:    */     
/*  38: 42 */     cl.run(imagepath + firstpage, firstpage, phy);
/*  39: 43 */     cl.run(imagepath + secondpage, secondpage, phy);
/*  40: 44 */     cl.run(imagepath + codcrop, codcrop, phy);
/*  41:    */   }
/*  42:    */   
/*  43:    */   public void updateWorkload(String physician)
/*  44:    */   {
/*  45: 49 */     String sql = "update physician_workload set workload=workload+1 where physician='" + 
/*  46: 50 */       physician + "'";
/*  47:    */   }
/*  48:    */   
/*  49:    */   public void assign(String phy)
/*  50:    */     throws Exception
/*  51:    */   {
/*  52: 56 */     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
/*  53: 57 */     Calendar cal = new GregorianCalendar();
/*  54: 58 */     cal.add(5, 10);
/*  55: 59 */     String dueDate = sdf.format(cal.getTime());
/*  56: 60 */     String assignDate = sdf.format(new Date());
/*  57: 61 */     String bookmark = TestXUIDB.getInstance().getLastChangeLog();
/*  58: 62 */     XTaskModel rootM = XTaskModel.getRoot(phy, 
/*  59: 63 */       "cme", "6");
/*  60: 64 */     XTaskModel cmeM = (XTaskModel)rootM.get("task0");
/*  61: 65 */     cmeM.area = "1";
/*  62: 66 */     cmeM.household = "1";
/*  63: 67 */     cmeM.house = "1";
/*  64: 68 */     cmeM.assignedTo = phy;
/*  65: 69 */     cmeM.set("@assignedto", phy);
/*  66: 70 */     XTaskModel codM1 = (XTaskModel)cmeM.get("task0-" + this.p.pid);
/*  67: 71 */     codM1.assignedTo = phy;
/*  68: 72 */     codM1.set("@assignedto", phy);
/*  69: 73 */     codM1.set("@dateassigned", assignDate);
/*  70: 74 */     codM1.set("@duedate", dueDate);
/*  71: 75 */     cmeM.save();
/*  72: 76 */     codM1.save();
/*  73:    */     
/*  74: 78 */     Vector keys = new Vector();
/*  75: 79 */     keys.add("key1");
/*  76: 80 */     keys.add("value1");
/*  77:    */   }
/*  78:    */   
/*  79:    */   public void stateChange(Process p)
/*  80:    */   {
/*  81: 85 */     this.p = p;
/*  82: 86 */     if (p.states.getCurrentState().equals("assignment"))
/*  83:    */     {
/*  84: 87 */       this.sm = ((CMEStateMachine2)p.states);
/*  85: 88 */       System.out.println(" Doing assignment for Process " + p.pid);
/*  86:    */       
/*  87: 90 */       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
/*  88:    */       
/*  89: 92 */       Vector v = findPhysicians(p.pid);
/*  90: 93 */       System.out.println(" Assignment physicians " + v.size() + " pid=" + p.pid);
/*  91: 95 */       if (v.size() < 1) {
/*  92: 96 */         return;
/*  93:    */       }
/*  94:    */       try
/*  95:    */       {
/*  96:100 */         Calendar cal = new GregorianCalendar();
/*  97:101 */         cal.add(5, 10);
/*  98:102 */         String dueDate = sdf.format(cal.getTime());
/*  99:103 */         String assignDate = sdf.format(new Date());
/* 100:104 */         String bookmark = TestXUIDB.getInstance().getLastChangeLog();
/* 101:106 */         for (int i = 0; i < v.size(); i++)
/* 102:    */         {
/* 103:108 */           System.out.println("Physician+++++++++++++++++++++++++++++++" + v.get(i));
/* 104:    */           
/* 105:110 */           assign(v.get(i).toString());
/* 106:111 */           this.sm.assigned.add(v.get(i).toString());
/* 107:    */         }
/* 108:    */       }
/* 109:    */       catch (Exception e)
/* 110:    */       {
/* 111:117 */         e.printStackTrace();
/* 112:    */       }
/* 113:120 */       Process.transition(p.pid);
/* 114:    */     }
/* 115:    */   }
/* 116:    */   
/* 117:    */   public void batchExecute() {}
/* 118:    */   
/* 119:    */   public Vector findPhysicians(String vaId)
/* 120:    */   {
/* 121:130 */     XModel workloadModel = new XBaseModel();
/* 122:    */     
/* 123:132 */     String language = TestXUIDB.getInstance().getValue("keyvalue", 
/* 124:133 */       "/va/" + vaId + "/gi/language");
/* 125:    */     
/* 126:135 */     TestXUIDB.getInstance().getPhysiciansWithLessWorkload(language, "a.coder=1", workloadModel);
/* 127:    */     
/* 128:137 */     Vector ind = new Vector();
/* 129:138 */     System.out.println("Going to call assign");
/* 130:140 */     for (int i = 0; i < 2; i++)
/* 131:    */     {
/* 132:141 */       String phy = ((XModel)workloadModel.get(i).get("id")).get().toString();
/* 133:142 */       System.out.println("physician::" + phy);
/* 134:143 */       ind.add(phy);
/* 135:    */     }
/* 136:146 */     return ind;
/* 137:    */   }
/* 138:    */   
/* 139:    */   public static void main(String[] args)
/* 140:    */   {
/* 141:151 */     AssignmentAgent2 a = new AssignmentAgent2();
/* 142:152 */     a.findPhysicians("09100249_01_04");
/* 143:    */   }
/* 144:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.agents.cme.AssignmentAgent3
 * JD-Core Version:    0.7.0.1
 */