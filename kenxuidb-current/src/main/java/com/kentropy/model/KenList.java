/*   1:    */ package com.kentropy.model;
/*   2:    */ 
/*   3:    */ import java.io.PrintStream;
/*   4:    */ import java.util.StringTokenizer;
/*   5:    */ import java.util.Vector;
/*   6:    */ 
/*   7:    */ public class KenList
/*   8:    */   extends Vector
/*   9:    */ {
/*  10:    */   public KenList(String liststr)
/*  11:    */   {
/*  12: 11 */     StringTokenizer st = new StringTokenizer(liststr, "/");
/*  13: 12 */     while (st.hasMoreTokens())
/*  14:    */     {
/*  15: 13 */       System.out.println(st.countTokens() + " " + size());
/*  16: 14 */       super.add(st.nextToken());
/*  17:    */     }
/*  18:    */   }
/*  19:    */   
/*  20:    */   public KenList() {}
/*  21:    */   
/*  22:    */   public KenList left(String tt)
/*  23:    */   {
/*  24: 21 */     KenList ken = new KenList();
/*  25: 22 */     for (int i = 0; i < size(); i++)
/*  26:    */     {
/*  27: 24 */       StringTokenizer st = new StringTokenizer(get(i).toString(), tt);
/*  28:    */       
/*  29: 26 */       ken.add(st.nextToken());
/*  30:    */     }
/*  31: 28 */     return ken;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public KenList right(String tt)
/*  35:    */   {
/*  36: 33 */     KenList ken = new KenList();
/*  37: 34 */     for (int i = 0; i < size(); i++)
/*  38:    */     {
/*  39: 36 */       StringTokenizer st = new StringTokenizer(get(i).toString(), tt);
/*  40: 37 */       if (st.hasMoreTokens())
/*  41:    */       {
/*  42: 39 */         st.nextToken();
/*  43: 40 */         if (st.hasMoreTokens()) {
/*  44: 41 */           ken.add(st.nextToken());
/*  45:    */         }
/*  46:    */       }
/*  47:    */     }
/*  48: 44 */     return ken;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public KenList add1(KenList l1)
/*  52:    */   {
/*  53: 48 */     KenList ken = new KenList();
/*  54: 49 */     for (int i = 0; i < size(); i++) {
/*  55: 51 */       if (l1.size() > i) {
/*  56: 52 */         ken.add(get(i).toString() + l1.get(i));
/*  57:    */       } else {
/*  58: 54 */         ken.add(get(i).toString() + l1.get(l1.size() - 1));
/*  59:    */       }
/*  60:    */     }
/*  61: 56 */     return ken;
/*  62:    */   }
/*  63:    */   
/*  64:    */   public String toString()
/*  65:    */   {
/*  66: 61 */     String tt = "";
/*  67: 62 */     for (int i = 0; i < size(); i++) {
/*  68: 64 */       tt = tt + (i == 0 ? "" : "/") + get(i).toString();
/*  69:    */     }
/*  70: 67 */     return tt;
/*  71:    */   }
/*  72:    */   
/*  73:    */   public KenList add1(String l1)
/*  74:    */   {
/*  75: 71 */     KenList ken = new KenList();
/*  76: 72 */     for (int i = 0; i < size(); i++) {
/*  77: 74 */       ken.add(get(i).toString() + l1);
/*  78:    */     }
/*  79: 76 */     return ken;
/*  80:    */   }
/*  81:    */   
/*  82:    */   public String toString(String sep)
/*  83:    */   {
/*  84: 81 */     String tt = "";
/*  85: 82 */     for (int i = 0; i < size(); i++) {
/*  86: 84 */       tt = tt + (i == 0 ? "" : sep) + get(i).toString();
/*  87:    */     }
/*  88: 87 */     return tt;
/*  89:    */   }
/*  90:    */   
/*  91:    */   public String toString(String sep, String enclose)
/*  92:    */   {
/*  93: 92 */     String tt = "";
/*  94: 93 */     for (int i = 0; i < size(); i++) {
/*  95: 95 */       tt = tt + (i == 0 ? "" : sep) + enclose + get(i).toString() + enclose;
/*  96:    */     }
/*  97: 98 */     return tt;
/*  98:    */   }
/*  99:    */   
/* 100:    */   public KenList subset(int startIdx, int endIndx)
/* 101:    */   {
/* 102:103 */     KenList ken = new KenList();
/* 103:104 */     for (int i = startIdx; (i <= endIndx) && (i < size()); i++) {
/* 104:106 */       ken.add(get(i).toString());
/* 105:    */     }
/* 106:108 */     return ken;
/* 107:    */   }
/* 108:    */   
/* 109:    */   public static void main(String[] args)
/* 110:    */   {
/* 111:113 */     KenList ken = new KenList("task0-1/task0-9/task1-10");
/* 112:114 */     System.out.println(" EST " + ken.size() + " " + ken.right("-").toString() + " " + ken.left("-").toString() + ken.add1("-").add1(new KenList("1/2/3/4")).toString());
/* 113:    */   }
/* 114:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.model.KenList
 * JD-Core Version:    0.7.0.1
 */