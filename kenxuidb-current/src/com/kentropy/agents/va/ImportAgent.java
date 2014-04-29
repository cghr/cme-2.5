/*   1:    */ package com.kentropy.agents.va;
/*   2:    */ 
/*   3:    */ import com.kentropy.cme.qa.transformation.Transformation;
/*   4:    */ import com.kentropy.db.BufferedDBWriter;
/*   5:    */ import com.kentropy.db.TestXUIDB;
/*   6:    */ import com.kentropy.flow.Age;
/*   7:    */ import com.kentropy.process.Agent;
/*   8:    */ import com.kentropy.process.Process;
/*   9:    */ import com.kentropy.process.VAStateMachine;
/*  10:    */ import com.kentropy.util.DbConnection;
/*  11:    */ import java.io.PrintStream;
/*  12:    */ import java.sql.Connection;
/*  13:    */ import java.sql.ResultSet;
/*  14:    */ import java.sql.Statement;
/*  15:    */ import java.util.Enumeration;
/*  16:    */ import java.util.Hashtable;
/*  17:    */ import java.util.Vector;
/*  18:    */ import net.xoetrope.xui.data.XBaseModel;
/*  19:    */ import net.xoetrope.xui.data.XModel;
/*  20:    */ 
/*  21:    */ public class ImportAgent
/*  22:    */   implements Agent
/*  23:    */ {
/*  24: 24 */   VAStateMachine sm = null;
/*  25: 26 */   Process p = null;
/*  26: 28 */   public String ext = "png";
/*  27: 30 */   int count = 0;
/*  28:    */   
/*  29:    */   public void stateChange(Process p)
/*  30:    */   {
/*  31: 34 */     System.out.println("Inside AssignmentAgent.stateChange()");
/*  32: 35 */     this.p = p;
/*  33: 36 */     String[] tokens = p.pid.split("/");
/*  34: 37 */     BufferedDBWriter bdb = BufferedDBWriter.getInstance();
/*  35: 38 */     this.sm = ((VAStateMachine)p.states);
/*  36: 39 */     if (this.sm.currentState.equals("import")) {
/*  37:    */       try
/*  38:    */       {
/*  39: 42 */         XModel memberDataM = new XBaseModel();
/*  40: 43 */         TestXUIDB.getInstance().getData("members", "*", "concat(enum_area,idc)='" + tokens[1] + "'", memberDataM);
/*  41: 44 */         XModel xm = memberDataM.get(0);
/*  42: 45 */         String age = ((XModel)xm.get("deathage")).get().toString();
/*  43: 46 */         String ageUnit = ((XModel)xm.get("deathageunit")).get().toString();
/*  44: 47 */         Age age1 = new Age(age + "," + ageUnit);
/*  45: 48 */         String deathType = null;
/*  46: 49 */         if (age1.compareTo(new Age("15,Y")) >= 0) {
/*  47: 50 */           deathType = "adult";
/*  48: 51 */         } else if (age1.compareTo(new Age("28,D")) > 0) {
/*  49: 52 */           deathType = "child";
/*  50:    */         } else {
/*  51: 54 */           deathType = "neonatal";
/*  52:    */         }
/*  53: 56 */         this.sm.domain = deathType;
/*  54: 57 */         String uniqno = tokens[1];
/*  55: 58 */         this.sm.surveyor = "1";
/*  56: 59 */         Hashtable record = new Hashtable();
/*  57: 60 */         boolean complStatus = importKeyValue(uniqno, deathType, record);
/*  58: 61 */         Hashtable flags = new Hashtable();
/*  59: 62 */         Enumeration keys = record.keys();
/*  60: 63 */         while (keys.hasMoreElements())
/*  61:    */         {
/*  62: 64 */           String key = keys.nextElement().toString();
/*  63: 65 */           flags.put(key, "V");
/*  64:    */         }
/*  65: 67 */         flags.put("uniqno", uniqno);
/*  66: 68 */         System.out.println("Compl Status:" + record.get("compl_status"));
/*  67: 70 */         if (complStatus)
/*  68:    */         {
/*  69: 71 */           vaTransformation(record, deathType, "va");
/*  70: 72 */           vaTransformation(record, deathType, "va/narrative");
/*  71: 73 */           bdb.addToJob(record, deathType);
/*  72: 74 */           bdb.addToJob(flags, deathType + "_qa");
/*  73: 76 */           if (waitForWrite(uniqno, deathType))
/*  74:    */           {
/*  75: 77 */             this.sm.vacomplete = true;
/*  76: 78 */             Process.transition(p.pid);
/*  77:    */           }
/*  78:    */         }
/*  79:    */       }
/*  80:    */       catch (Exception e)
/*  81:    */       {
/*  82: 85 */         e.printStackTrace();
/*  83:    */       }
/*  84:    */     }
/*  85:    */   }
/*  86:    */   
/*  87:    */   public boolean waitForWrite(String uniqno, String domain)
/*  88:    */     throws InterruptedException, Exception
/*  89:    */   {
/*  90: 92 */     for (int i = 0; i < 50; i++)
/*  91:    */     {
/*  92: 93 */       Thread.currentThread();Thread.sleep(50L);
/*  93: 94 */       String sql = "SELECT * from " + domain + " WHERE uniqno='" + uniqno + "'";
/*  94: 95 */       System.out.println("SQL:" + sql);
/*  95: 96 */       Connection con = DbConnection.getConnection();
/*  96: 97 */       Statement stmt = con.createStatement();
/*  97: 98 */       ResultSet rs = stmt.executeQuery(sql);
/*  98: 99 */       if (rs.next())
/*  99:    */       {
/* 100:100 */         System.out.println("Match Found");
/* 101:101 */         stmt.close();
/* 102:102 */         con.close();
/* 103:103 */         return true;
/* 104:    */       }
/* 105:105 */       stmt.close();
/* 106:106 */       con.close();
/* 107:    */     }
/* 108:109 */     return false;
/* 109:    */   }
/* 110:    */   
/* 111:    */   public void vaTransformation(Hashtable vaDataTable, String domain, String type)
/* 112:    */     throws Exception
/* 113:    */   {
/* 114:115 */     Vector keys = new Vector(vaDataTable.keySet());
/* 115:116 */     System.out.println(" Type:" + type + " " + vaDataTable.size());
/* 116:117 */     String sql = "SELECT * FROM transformations WHERE table1='" + type + "'";
/* 117:118 */     Connection con = DbConnection.getConnection();
/* 118:119 */     Statement stmt = con.createStatement();
/* 119:120 */     ResultSet rs = stmt.executeQuery(sql);
/* 120:121 */     while (rs.next())
/* 121:    */     {
/* 122:122 */       String transformationClass = rs.getString("transformation_class");
/* 123:123 */       System.out.println(" Class:" + transformationClass);
/* 124:124 */       Transformation transformation = (Transformation)Class.forName(transformationClass).newInstance();
/* 125:125 */       transformation.transform(vaDataTable, "uniqno", (String)vaDataTable.get("uniqno"), null, domain);
/* 126:    */     }
/* 127:127 */     stmt.close();
/* 128:128 */     con.close();
/* 129:    */   }
/* 130:    */   
/* 131:    */   private void readXModel(XModel xm, Hashtable ht)
/* 132:    */   {
/* 133:133 */     for (int i = 0; i < xm.getNumChildren(); i++)
/* 134:    */     {
/* 135:134 */       String key = xm.get(i).getId();
/* 136:135 */       String value = (String)xm.get(i).get();
/* 137:136 */       System.out.println("Key:" + key + " Value:" + value);
/* 138:137 */       ht.put(key, value == null ? "" : value);
/* 139:    */     }
/* 140:    */   }
/* 141:    */   
/* 142:    */   private static String getCompletionStatus(XModel dataM, XModel resourceM, String table)
/* 143:    */   {
/* 144:141 */     Hashtable ht = new Hashtable();
/* 145:142 */     ht.put("adult", "r_pure_veg");
/* 146:143 */     ht.put("child", "measles_injection");
/* 147:144 */     ht.put("neonatal", "body_discolored");
/* 148:    */     
/* 149:146 */     System.out.println("ResourceM Children:" + resourceM.getNumChildren());
/* 150:148 */     for (int i = 0; i < resourceM.getNumChildren(); i++) {
/* 151:149 */       if ((((XModel)resourceM.get(i).get("narrative_translated")).get() != null) && ((XModel)resourceM.get(i).get("cod_translated") != null)) {
/* 152:150 */         return "true";
/* 153:    */       }
/* 154:    */     }
/* 155:153 */     return "false";
/* 156:    */   }
/* 157:    */   
/* 158:    */   public boolean importKeyValue(String uniqno, String deathType, Hashtable record)
/* 159:    */     throws Exception
/* 160:    */   {
/* 161:158 */     XModel keyValueModel = new XBaseModel();
/* 162:159 */     XModel domainDataModel = new XBaseModel();
/* 163:160 */     System.out.println("Imporing " + uniqno);
/* 164:161 */     TestXUIDB.getInstance().getKeyValues(keyValueModel, "keyvalue", "/va/" + uniqno);
/* 165:162 */     for (int i = 0; i < keyValueModel.getNumChildren(); i++)
/* 166:    */     {
/* 167:163 */       String surveyor = keyValueModel.get(i).getId();
/* 168:164 */       XModel recordModel = keyValueModel.get(i);
/* 169:165 */       domainDataModel = new XBaseModel();
/* 170:166 */       domainDataModel.set("uniqno", uniqno);
/* 171:167 */       XModel resourceM = new XBaseModel();
/* 172:168 */       TestXUIDB.getInstance().getKeyValues(resourceM, "resource", "/va/" + uniqno);
/* 173:169 */       String complStatus = getCompletionStatus(domainDataModel, resourceM, deathType);
/* 174:170 */       if ((complStatus != null) && (complStatus.equals("true")))
/* 175:    */       {
/* 176:171 */         domainDataModel.set("surveyor", surveyor);
/* 177:172 */         for (int j = 0; j < recordModel.getNumChildren(); j++)
/* 178:    */         {
/* 179:173 */           String field = recordModel.get(j).getId();
/* 180:174 */           String value = (String)recordModel.get(j).get();
/* 181:175 */           domainDataModel.set(field, value);
/* 182:    */         }
/* 183:179 */         ((XModel)domainDataModel.get("compl_status")).set(complStatus);
/* 184:180 */         ((XModel)domainDataModel.get("language_code"))
/* 185:181 */           .set("English");
/* 186:    */         
/* 187:183 */         System.out.println("uniqno:" + uniqno + " surveyor:" + surveyor);
/* 188:    */         
/* 189:185 */         readXModel(domainDataModel, record);
/* 190:186 */         return true;
/* 191:    */       }
/* 192:    */     }
/* 193:190 */     return false;
/* 194:    */   }
/* 195:    */   
/* 196:    */   public void batchExecute() {}
/* 197:    */   
/* 198:    */   public static void main(String[] args)
/* 199:    */     throws Exception
/* 200:    */   {
/* 201:200 */     Process.createProcess("01300088_01_02");
/* 202:201 */     Process.processTransitions();
/* 203:    */   }
/* 204:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.agents.va.ImportAgent
 * JD-Core Version:    0.7.0.1
 */