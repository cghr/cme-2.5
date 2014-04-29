/*   1:    */ package com.kentropy.db;
/*   2:    */ 
/*   3:    */ import java.io.PrintStream;
/*   4:    */ import java.util.StringTokenizer;
/*   5:    */ import net.xoetrope.xui.data.XBaseModel;
/*   6:    */ 
/*   7:    */ public class XLogisticsModel
/*   8:    */   extends XBaseModel
/*   9:    */ {
/*  10:  9 */   boolean fetched = false;
/*  11:    */   
/*  12:    */   public Object get(String arg0)
/*  13:    */   {
/*  14: 13 */     System.out.println("Debug Get1 called " + arg0);
/*  15: 15 */     if ((arg0 != null) && (!arg0.startsWith("@")))
/*  16:    */     {
/*  17: 17 */       StringTokenizer st = new StringTokenizer(arg0, "/");
/*  18: 18 */       String currentId = arg0;
/*  19: 19 */       if (st.hasMoreTokens()) {
/*  20: 21 */         currentId = st.nextToken();
/*  21:    */       }
/*  22: 24 */       String path = ((String)get("@path") != null) && (!((String)get("@path")).equals("")) ? (String)get("@path") + "/" + getId() : getId();
/*  23: 25 */       XLogisticsModel child = TestXUIDB.getInstance().getLogisticsM("logistics", currentId, path);
/*  24: 26 */       if (child != null)
/*  25:    */       {
/*  26: 28 */         child.setId(currentId);
/*  27: 29 */         append(child);
/*  28:    */       }
/*  29:    */     }
/*  30: 34 */     return super.get(arg0);
/*  31:    */   }
/*  32:    */   
/*  33:    */   public void set(Object arg0)
/*  34:    */   {
/*  35: 39 */     System.out.println("Set called");
/*  36: 40 */     super.set(arg0);
/*  37:    */     try
/*  38:    */     {
/*  39: 42 */       if (arg0 != null) {
/*  40: 43 */         save();
/*  41:    */       }
/*  42:    */     }
/*  43:    */     catch (Exception e)
/*  44:    */     {
/*  45: 47 */       e.printStackTrace();
/*  46:    */     }
/*  47:    */   }
/*  48:    */   
/*  49:    */   public void set(String arg0, Object arg1)
/*  50:    */   {
/*  51: 53 */     System.out.println("Set 2 called");
/*  52: 54 */     super.set(arg0, arg1);
/*  53:    */   }
/*  54:    */   
/*  55:    */   public void setAddByDefault(boolean arg0)
/*  56:    */   {
/*  57: 59 */     super.setAddByDefault(arg0);
/*  58:    */   }
/*  59:    */   
/*  60:    */   public void setAttribValue(int arg0, Object arg1)
/*  61:    */   {
/*  62: 64 */     System.out.println("Set Attrib1 called" + arg1);
/*  63: 65 */     super.setAttribValue(arg0, arg1);
/*  64:    */   }
/*  65:    */   
/*  66:    */   public void readChildren()
/*  67:    */   {
/*  68: 70 */     if (!this.fetched)
/*  69:    */     {
/*  70: 72 */       TestXUIDB.getInstance().getLogisticsData(get("@path") + "/" + getId(), this);
/*  71:    */       
/*  72: 74 */       this.fetched = true;
/*  73:    */     }
/*  74:    */   }
/*  75:    */   
/*  76:    */   public int getNumChildren()
/*  77:    */   {
/*  78: 79 */     System.out.println(" Get Num called " + getId());
/*  79:    */     
/*  80: 81 */     readChildren();
/*  81:    */     
/*  82: 83 */     return super.getNumChildren();
/*  83:    */   }
/*  84:    */   
/*  85:    */   public void setAttribValue(int arg0, String arg1, Object arg2)
/*  86:    */   {
/*  87: 88 */     System.out.println("Set Attrib2 called");
/*  88: 89 */     super.setAttribValue(arg0, arg1, arg2);
/*  89:    */   }
/*  90:    */   
/*  91:    */   public void save()
/*  92:    */     throws Exception
/*  93:    */   {
/*  94: 95 */     TestXUIDB.getInstance().saveLogistics(this);
/*  95:    */   }
/*  96:    */   
/*  97:    */   public static void main(String[] args)
/*  98:    */     throws Exception
/*  99:    */   {
/* 100:101 */     XLogisticsModel xl = new XLogisticsModel();
/* 101:102 */     xl.setId("logistics");
/* 102:103 */     xl.set("@path", "");
/* 103:104 */     XLogisticsModel xli = (XLogisticsModel)xl.get("incoming");
/* 104:105 */     xli.set("test");
/* 105:106 */     ((XLogisticsModel)xli.get("test1")).set("test1");
/* 106:107 */     XLogisticsModel xl2 = (XLogisticsModel)xl.get("fielddbs/2010-05-08/Victor RM/samples");
/* 107:108 */     System.out.println(xl2.getNumChildren());
/* 108:    */   }
/* 109:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.db.XLogisticsModel
 * JD-Core Version:    0.7.0.1
 */