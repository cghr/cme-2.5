/*  1:   */ package com.kentropy.flow;
/*  2:   */ 
/*  3:   */ import java.io.PrintStream;
/*  4:   */ 
/*  5:   */ public class Age
/*  6:   */   implements Comparable
/*  7:   */ {
/*  8: 8 */   public int age = 0;
/*  9: 9 */   public String ageUnit = "";
/* 10:10 */   public String ageValue = "";
/* 11:   */   
/* 12:   */   public int compareTo(Object o)
/* 13:   */   {
/* 14:14 */     String oAgeVal = ageValue(o.toString());
/* 15:15 */     System.out.println(this.ageValue + " " + oAgeVal + " " + this.ageValue.compareTo(oAgeVal));
/* 16:16 */     return this.ageValue.compareTo(ageValue(o.toString()));
/* 17:   */   }
/* 18:   */   
/* 19:   */   public String toString()
/* 20:   */   {
/* 21:20 */     return this.age + "," + this.ageUnit;
/* 22:   */   }
/* 23:   */   
/* 24:   */   public boolean equals(Object o)
/* 25:   */   {
/* 26:24 */     String[] tokens = ((String)o).split("_");
/* 27:25 */     if (tokens.length < 2) {
/* 28:26 */       return false;
/* 29:   */     }
/* 30:27 */     return this.ageValue.equals(ageValue(o.toString()));
/* 31:   */   }
/* 32:   */   
/* 33:   */   public Age(String val)
/* 34:   */   {
/* 35:32 */     this.ageValue = ageValue(val);
/* 36:33 */     String[] tokens = val.split(",");
/* 37:34 */     this.age = Integer.parseInt(tokens[0]);
/* 38:35 */     this.ageUnit = tokens[1];
/* 39:   */   }
/* 40:   */   
/* 41:   */   public void add(Age age)
/* 42:   */     throws Exception
/* 43:   */   {
/* 44:40 */     if (!age.ageUnit.equals(this.ageUnit)) {
/* 45:42 */       throw new Exception("Age Units differ");
/* 46:   */     }
/* 47:45 */     this.age += age.age;
/* 48:46 */     this.ageValue = ageValue(this.age + "," + this.ageUnit);
/* 49:   */   }
/* 50:   */   
/* 51:   */   public String ageValue(String val)
/* 52:   */   {
/* 53:51 */     System.out.println(" AGE... " + val);
/* 54:52 */     String[] tokens = val.split(",");
/* 55:54 */     if (tokens[1].equals("Y"))
/* 56:   */     {
/* 57:55 */       if (tokens[0].length() < 3) {
/* 58:57 */         tokens[0] = ("000".substring(tokens[0].length()) + tokens[0]);
/* 59:   */       }
/* 60:60 */       return tokens[0] + "-" + "00" + "-00";
/* 61:   */     }
/* 62:62 */     if (tokens[1].equals("M"))
/* 63:   */     {
/* 64:64 */       if (tokens[0].length() < 2) {
/* 65:66 */         tokens[0] = ("00".substring(tokens[0].length()) + tokens[0]);
/* 66:   */       }
/* 67:69 */       return "00-" + tokens[0] + "-00";
/* 68:   */     }
/* 69:71 */     if (tokens[1].equals("D"))
/* 70:   */     {
/* 71:73 */       if (tokens[0].length() < 3) {
/* 72:75 */         tokens[0] = ("00".substring(tokens[0].length()) + tokens[0]);
/* 73:   */       }
/* 74:78 */       return "00-00-" + tokens[0];
/* 75:   */     }
/* 76:80 */     return "00-00-00";
/* 77:   */   }
/* 78:   */   
/* 79:   */   public static void main(String[] args) {}
/* 80:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.flow.Age
 * JD-Core Version:    0.7.0.1
 */