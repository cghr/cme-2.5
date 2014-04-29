/*   1:    */ package com.kentropy.agents.cme;
/*   2:    */ 
/*   3:    */ import com.kentropy.agents.AgentTransformationSource;
/*   4:    */ import com.kentropy.cme.qa.transformation.Transformation;
/*   5:    */ import com.kentropy.db.TestXUIDB;
/*   6:    */ import com.kentropy.process.Agent;
/*   7:    */ import com.kentropy.process.CMEStateMachine;
/*   8:    */ import com.kentropy.process.Process;
/*   9:    */ import com.kentropy.process.StateMachine;
/*  10:    */ import com.kentropy.sync.ChangeLog;
/*  11:    */ import java.util.Hashtable;
/*  12:    */ import net.xoetrope.xui.data.XBaseModel;
/*  13:    */ import net.xoetrope.xui.data.XModel;
/*  14:    */ import org.apache.log4j.Logger;
/*  15:    */ 
/*  16:    */ public class CompletionAgent
/*  17:    */   implements Agent, AgentTransformationSource
/*  18:    */ {
/*  19: 19 */   Logger logger = Logger.getLogger(getClass().getName());
/*  20: 21 */   CMEStateMachine sm = null;
/*  21: 23 */   Process p = null;
/*  22:    */   String[] resources;
/*  23:    */   String[] resources1;
/*  24: 26 */   String ext = "png";
/*  25:    */   
/*  26:    */   public void batchExecute() {}
/*  27:    */   
/*  28:    */   public void setProcess(Process p)
/*  29:    */   {
/*  30: 34 */     this.p = p;
/*  31: 35 */     this.sm = ((CMEStateMachine)p.states);
/*  32:    */   }
/*  33:    */   
/*  34:    */   public void getResources()
/*  35:    */   {
/*  36: 40 */     String imagepath = TestXUIDB.getInstance().getImagePath();
/*  37: 41 */     String[] resourcesC = { imagepath + this.p.pid + "_0_blank." + this.ext, imagepath + this.p.pid + "_1_blank." + this.ext, imagepath + this.p.pid + "_cod." + this.ext };
/*  38: 42 */     String[] resourcesC1 = { this.p.pid + "_0_blank." + this.ext, this.p.pid + "_1_blank." + this.ext, this.p.pid + "_cod." + this.ext };
/*  39: 43 */     XModel dataModel = new XBaseModel();
/*  40: 44 */     TestXUIDB.getInstance().getKeyValues(dataModel, "keyvalue", "/va/" + this.p.pid);
/*  41: 45 */     String domain = dataModel.get("type/@value").toString();
/*  42: 47 */     if (domain.toLowerCase().equals("maternal"))
/*  43:    */     {
/*  44: 48 */       String maternalImage = (String)dataModel.get("report/maternal_image/@value");
/*  45: 49 */       String[] resourcesM = { imagepath + this.p.pid + "_0_blank." + this.ext, imagepath + this.p.pid + "_1_blank." + this.ext, imagepath + this.p.pid + "_cod." + this.ext, imagepath + maternalImage };
/*  46: 50 */       String[] resourcesM1 = { this.p.pid + "_0_blank." + this.ext, this.p.pid + "_1_blank." + this.ext, this.p.pid + "_cod." + this.ext, maternalImage };
/*  47: 51 */       this.resources = resourcesM;
/*  48: 52 */       this.resources1 = resourcesM1;
/*  49:    */     }
/*  50:    */     else
/*  51:    */     {
/*  52: 56 */       this.resources = resourcesC;
/*  53: 57 */       this.resources1 = resourcesC1;
/*  54:    */     }
/*  55:    */   }
/*  56:    */   
/*  57:    */   public void stateChange(Process p)
/*  58:    */   {
/*  59: 63 */     this.p = p;
/*  60: 64 */     if (p.states.getCurrentState().equals("complete"))
/*  61:    */     {
/*  62: 66 */       this.logger.info(" Doing matching for Process " + p.pid);
/*  63:    */       
/*  64: 68 */       this.sm = ((CMEStateMachine)p.states);
/*  65: 69 */       this.logger.info(" Doing matching for Process " + p.pid);
/*  66:    */       try
/*  67:    */       {
/*  68: 72 */         transform();
/*  69: 73 */         String key = "/va/" + p.pid;
/*  70: 74 */         String vaVal = TestXUIDB.getInstance().getKeyValuesSerialized("keyvalue", key);
/*  71:    */         try
/*  72:    */         {
/*  73: 76 */           XModel xm = new XBaseModel();
/*  74: 77 */           ((XModel)xm.get("key1")).set(key);
/*  75: 78 */           ((XModel)xm.get("value1")).set(vaVal);
/*  76: 79 */           TestXUIDB.getInstance().saveDataM2UTF("outkeyvalue", "key1='" + key + "'", xm);
/*  77:    */           
/*  78: 81 */           key = "/cme/" + p.pid;
/*  79: 82 */           String cmeVal = TestXUIDB.getInstance().getKeyValuesSerialized("keyvalue", key);
/*  80: 83 */           xm = new XBaseModel();
/*  81: 84 */           ((XModel)xm.get("key1")).set(key);
/*  82: 85 */           ((XModel)xm.get("value1")).set(cmeVal);
/*  83: 86 */           TestXUIDB.getInstance().saveDataM2UTF("outkeyvalue", "key1='" + key + "'", xm);
/*  84:    */         }
/*  85:    */         catch (Exception e)
/*  86:    */         {
/*  87: 90 */           e.printStackTrace();
/*  88: 91 */           TestXUIDB.getInstance().logAgent(p.pid, getClass().getName(), this.sm.currentState, "Error: Outkeyvalue " + e.getMessage());
/*  89:    */         }
/*  90: 94 */         getResources();
/*  91: 95 */         String frombookmark = TestXUIDB.getInstance().getLastChangeLog();
/*  92: 96 */         for (int i = 0; i < this.resources1.length; i++)
/*  93:    */         {
/*  94: 98 */           ChangeLog.startLog("images/" + this.resources1[i], "deleteResource", "", TestXUIDB.getInstance().getCurrentUser());
/*  95: 99 */           ChangeLog.endLog();
/*  96:    */         }
/*  97:102 */         key = "/va/" + p.pid;
/*  98:    */         
/*  99:104 */         TestXUIDB.getInstance().createDeleteLog("keyvalue", key + "%");
/* 100:    */         
/* 101:106 */         key = "/cme/" + p.pid;
/* 102:    */         
/* 103:108 */         TestXUIDB.getInstance().createDeleteLog("keyvalue", key + "%");
/* 104:    */         
/* 105:110 */         String tobookmark = TestXUIDB.getInstance().getLastChangeLog();
/* 106:111 */         TestXUIDB.getInstance().addToChangeLogOutboundQueue(this.sm.assignedfirst, frombookmark, tobookmark);
/* 107:112 */         TestXUIDB.getInstance().addToChangeLogOutboundQueue(this.sm.assignedsecond, frombookmark, tobookmark);
/* 108:113 */         if (!this.sm.adjudicator.trim().equals("")) {
/* 109:114 */           TestXUIDB.getInstance().addToChangeLogOutboundQueue(this.sm.adjudicator, frombookmark, tobookmark);
/* 110:    */         }
/* 111:    */       }
/* 112:    */       catch (Exception e)
/* 113:    */       {
/* 114:120 */         e.printStackTrace();
/* 115:121 */         TestXUIDB.getInstance().logAgent(p.pid, getClass().getName(), this.sm.currentState, "Error:" + e.getMessage());
/* 116:    */       }
/* 117:    */     }
/* 118:    */   }
/* 119:    */   
/* 120:    */   public void log(String message)
/* 121:    */   {
/* 122:128 */     TestXUIDB.getInstance().logAgent(this.p.pid, getClass().getName(), this.sm.currentState, message);
/* 123:    */   }
/* 124:    */   
/* 125:    */   public boolean transform()
/* 126:    */     throws Exception
/* 127:    */   {
/* 128:131 */     XModel xm = new XBaseModel();
/* 129:132 */     TestXUIDB.getInstance()
/* 130:133 */       .getData("transformations", "*", "table1='completion' or table1='adjpayment'", xm);
/* 131:134 */     boolean flag = false;
/* 132:135 */     Hashtable ht = getHashtable();
/* 133:136 */     log(" Transformation " + xm.getNumChildren());
/* 134:137 */     for (int i = 0; i < xm.getNumChildren(); i++)
/* 135:    */     {
/* 136:138 */       XModel row = xm.get(i);
/* 137:139 */       String field = ((XModel)row.get("field1")).get().toString();
/* 138:140 */       String transformationClass = ((XModel)row.get("transformation_class")).get().toString();
/* 139:141 */       String outTab = ((XModel)row.get("output_table")).get().toString();
/* 140:142 */       String outputFld = ((XModel)row.get("output_field")).get().toString();
/* 141:143 */       this.logger.info("Transformation::" + ((XModel)row.get("transformation_class")).get());
/* 142:144 */       log(" class " + transformationClass);
/* 143:    */       
/* 144:146 */       Transformation transformation = 
/* 145:    */       
/* 146:    */ 
/* 147:149 */         (Transformation)Class.forName(transformationClass).newInstance();
/* 148:    */       
/* 149:151 */       String retVal = (String)transformation.transform(ht, field, ht.get(field).toString(), null, 
/* 150:152 */         "keyvalue");
/* 151:153 */       flag = true;
/* 152:    */     }
/* 153:155 */     return flag;
/* 154:    */   }
/* 155:    */   
/* 156:    */   public static void main(String[] args) {}
/* 157:    */   
/* 158:    */   public Hashtable getHashtable()
/* 159:    */   {
/* 160:164 */     Hashtable ht = new Hashtable();
/* 161:165 */     ht.put("uniqno", this.p.pid);
/* 162:166 */     ht.put("phys1", this.sm.assignedfirst);
/* 163:167 */     ht.put("phys2", this.sm.assignedsecond);
/* 164:168 */     ht.put("adjudicator", this.sm.adjudicator);
/* 165:169 */     return ht;
/* 166:    */   }
/* 167:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.agents.cme.CompletionAgent
 * JD-Core Version:    0.7.0.1
 */