/*  1:   */ package com.kentropy.agents.cme;
/*  2:   */ 
/*  3:   */ import com.kentropy.cme.qa.transformation.Transformation;
/*  4:   */ import com.kentropy.db.TestXUIDB;
/*  5:   */ import com.kentropy.process.Agent;
/*  6:   */ import com.kentropy.process.CMEStateMachine2;
/*  7:   */ import com.kentropy.process.Process;
/*  8:   */ import com.kentropy.process.StateMachine;
/*  9:   */ import java.io.PrintStream;
/* 10:   */ import java.util.Hashtable;
/* 11:   */ import net.xoetrope.xui.data.XBaseModel;
/* 12:   */ import net.xoetrope.xui.data.XModel;
/* 13:   */ 
/* 14:   */ public class CompletionAgent2
/* 15:   */   implements Agent
/* 16:   */ {
/* 17:17 */   CMEStateMachine2 sm = null;
/* 18:19 */   Process p = null;
/* 19:   */   
/* 20:   */   public void batchExecute() {}
/* 21:   */   
/* 22:   */   public void stateChange(Process p)
/* 23:   */   {
/* 24:24 */     this.p = p;
/* 25:25 */     if (p.states.getCurrentState().equals("coding"))
/* 26:   */     {
/* 27:27 */       System.out.println(" Doing saving for Process " + p.pid);
/* 28:   */       
/* 29:29 */       this.sm = ((CMEStateMachine2)p.states);
/* 30:30 */       System.out.println(" Doing matching for Process " + p.pid);
/* 31:   */       try
/* 32:   */       {
/* 33:33 */         transform();
/* 34:   */       }
/* 35:   */       catch (Exception e)
/* 36:   */       {
/* 37:37 */         e.printStackTrace();
/* 38:38 */         TestXUIDB.getInstance().logAgent(p.pid, getClass().getName(), this.sm.currentState, "Error:" + e.getMessage());
/* 39:   */       }
/* 40:   */     }
/* 41:   */   }
/* 42:   */   
/* 43:   */   public boolean transform()
/* 44:   */     throws Exception
/* 45:   */   {
/* 46:43 */     XModel xm = new XBaseModel();
/* 47:44 */     TestXUIDB.getInstance()
/* 48:45 */       .getData("transformations", "*", "table1='keyvalue'", xm);
/* 49:46 */     boolean flag = false;
/* 50:47 */     Hashtable ht = new Hashtable();
/* 51:48 */     ht.put("uniqno", this.p.pid);
/* 52:49 */     ht.put("phys1", this.sm.assignedfirst);
/* 53:50 */     ht.put("phys2", this.sm.assignedsecond);
/* 54:51 */     ht.put("adjudicator", this.sm.adjudicator);
/* 55:52 */     for (int i = 0; i < xm.getNumChildren(); i++)
/* 56:   */     {
/* 57:53 */       XModel row = xm.get(i);
/* 58:54 */       String field = ((XModel)row.get("field1")).get().toString();
/* 59:55 */       String transformationClass = ((XModel)row.get("transformation_class")).get().toString();
/* 60:56 */       String outTab = ((XModel)row.get("output_table")).get().toString();
/* 61:57 */       String outputFld = ((XModel)row.get("output_field")).get().toString();
/* 62:58 */       System.out.println("Transformation::" + ((XModel)row.get("transformation_class")).get());
/* 63:59 */       Transformation transformation = 
/* 64:   */       
/* 65:   */ 
/* 66:62 */         (Transformation)Class.forName(transformationClass).newInstance();
/* 67:   */       
/* 68:64 */       String retVal = (String)transformation.transform(ht, field, ht.get(field).toString(), null, 
/* 69:65 */         "keyvalue");
/* 70:66 */       flag = true;
/* 71:   */     }
/* 72:68 */     return flag;
/* 73:   */   }
/* 74:   */   
/* 75:   */   public static void main(String[] args) {}
/* 76:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.agents.cme.CompletionAgent2
 * JD-Core Version:    0.7.0.1
 */