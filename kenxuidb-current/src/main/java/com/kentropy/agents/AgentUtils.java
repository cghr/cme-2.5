/*  1:   */ package com.kentropy.agents;
/*  2:   */ 
/*  3:   */ import com.kentropy.cme.qa.transformation.Transformation;
/*  4:   */ import com.kentropy.db.TestXUIDB;
/*  5:   */ import com.kentropy.process.Agent;
/*  6:   */ import com.kentropy.process.Process;
/*  7:   */ import com.kentropy.process.StateMachine;
/*  8:   */ import java.io.PrintStream;
/*  9:   */ import java.util.Hashtable;
/* 10:   */ import net.xoetrope.xui.data.XBaseModel;
/* 11:   */ import net.xoetrope.xui.data.XModel;
/* 12:   */ 
/* 13:   */ public class AgentUtils
/* 14:   */ {
/* 15:   */   public static void log(Process p, Agent a, String message)
/* 16:   */   {
/* 17:17 */     TestXUIDB.getInstance().logAgent(p.pid, a.getClass().getName(), p.states.getCurrentState(), message);
/* 18:   */   }
/* 19:   */   
/* 20:   */   public static boolean transform(String type, AgentTransformationSource a, Process p)
/* 21:   */     throws Exception
/* 22:   */   {
/* 23:23 */     XModel xm = new XBaseModel();
/* 24:24 */     TestXUIDB.getInstance()
/* 25:25 */       .getData("transformations", "*", "table1='" + type + "'", xm);
/* 26:26 */     boolean flag = false;
/* 27:27 */     a.setProcess(p);
/* 28:29 */     for (int i = 0; i < xm.getNumChildren(); i++)
/* 29:   */     {
/* 30:30 */       XModel row = xm.get(i);
/* 31:31 */       String field = ((XModel)row.get("field1")).get().toString();
/* 32:32 */       String transformationClass = ((XModel)row.get("transformation_class")).get().toString();
/* 33:33 */       String outTab = ((XModel)row.get("output_table")).get().toString();
/* 34:34 */       String outputFld = ((XModel)row.get("output_field")).get().toString();
/* 35:35 */       System.out.println("Transformation::" + ((XModel)row.get("transformation_class")).get());
/* 36:   */       
/* 37:37 */       Transformation transformation = 
/* 38:   */       
/* 39:   */ 
/* 40:40 */         (Transformation)Class.forName(transformationClass).newInstance();
/* 41:   */       
/* 42:42 */       Hashtable ht = a.getHashtable();
/* 43:43 */       String retVal = (String)transformation.transform(ht, field, ht.get(field).toString(), null, 
/* 44:44 */         "keyvalue");
/* 45:45 */       flag = true;
/* 46:   */     }
/* 47:47 */     return flag;
/* 48:   */   }
/* 49:   */   
/* 50:   */   public static void main(String[] args) {}
/* 51:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.agents.AgentUtils
 * JD-Core Version:    0.7.0.1
 */