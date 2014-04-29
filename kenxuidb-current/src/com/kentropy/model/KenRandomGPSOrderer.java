/*   1:    */ package com.kentropy.model;
/*   2:    */ 
/*   3:    */ import java.io.PrintStream;
/*   4:    */ import java.util.Vector;
/*   5:    */ import net.xoetrope.xui.data.XModel;
/*   6:    */ 
/*   7:    */ public class KenRandomGPSOrderer
/*   8:    */   implements KenModelOrderer
/*   9:    */ {
/*  10:    */   public void order(XModel mdl)
/*  11:    */   {
/*  12: 12 */     Vector v = new Vector();
/*  13: 13 */     int index = 0;
/*  14: 14 */     for (int i = 0; i < mdl.getNumChildren(); i++) {
/*  15: 16 */       if (mdl.get(i).get("latitude") != null)
/*  16:    */       {
/*  17: 18 */         index = i;
/*  18: 19 */         break;
/*  19:    */       }
/*  20:    */     }
/*  21: 23 */     orderByAddress1(mdl);
/*  22: 24 */     roundRobinShift(mdl, index);
/*  23:    */   }
/*  24:    */   
/*  25:    */   public void roundRobinShift(XModel mdl, int index)
/*  26:    */   {
/*  27: 29 */     int count = mdl.getNumChildren();
/*  28: 30 */     XModel[] mdls = new XModel[count];
/*  29: 32 */     for (int i = 0; i < count; i++)
/*  30:    */     {
/*  31: 34 */       int slNo = (i - index + count) % count;
/*  32: 35 */       mdl.get(i).set("slNo", Integer.valueOf(slNo));
/*  33: 36 */       mdls[slNo] = mdl.get(i);
/*  34: 37 */       System.out.println(slNo);
/*  35:    */     }
/*  36: 40 */     mdl.removeChildren();
/*  37: 41 */     for (int i = 0; i < count; i++) {
/*  38: 43 */       mdl.append(mdls[i]);
/*  39:    */     }
/*  40:    */   }
/*  41:    */   
/*  42:    */   public void orderByAddress1(XModel mdl)
/*  43:    */   {
/*  44: 49 */     int count = mdl.getNumChildren();
/*  45: 50 */     Vector mdls = new Vector();
/*  46: 51 */     Vector mdlIdx = new Vector();
/*  47: 53 */     for (int i = 0; i < count; i++)
/*  48:    */     {
/*  49: 55 */       boolean found = false;
/*  50: 56 */       String address1 = (String)((XModel)mdl.get(i).get("address1")).get();
/*  51: 57 */       String curDoor = address1.replaceAll("[a-zA-Z\\W]", " ").trim().replaceAll(" [0-9]", "").trim();
/*  52: 58 */       mdl.get(i).set("doorNo", curDoor);
/*  53: 59 */       Integer curDoor1 = new Integer(0);
/*  54:    */       try
/*  55:    */       {
/*  56: 61 */         curDoor1 = (curDoor == null) || (curDoor.trim().equals("")) ? new Integer(0) : new Integer(curDoor);
/*  57:    */       }
/*  58:    */       catch (Exception e)
/*  59:    */       {
/*  60: 65 */         e.printStackTrace();
/*  61:    */       }
/*  62: 67 */       for (int j = 0; j < mdls.size(); j++)
/*  63:    */       {
/*  64: 69 */         Integer doorNo = (Integer)mdlIdx.get(j);
/*  65: 71 */         if (doorNo.intValue() > curDoor1.intValue())
/*  66:    */         {
/*  67: 73 */           mdls.insertElementAt(mdl.get(i), j);
/*  68: 74 */           mdlIdx.insertElementAt(curDoor1, j);
/*  69: 75 */           found = true;
/*  70: 76 */           break;
/*  71:    */         }
/*  72:    */       }
/*  73: 80 */       if (!found)
/*  74:    */       {
/*  75: 82 */         mdls.add(mdl.get(i));
/*  76: 83 */         mdlIdx.add(curDoor1);
/*  77:    */       }
/*  78:    */     }
/*  79: 88 */     mdl.removeChildren();
/*  80: 89 */     for (int i = 0; i < count; i++) {
/*  81: 91 */       mdl.append((XModel)mdls.get(i));
/*  82:    */     }
/*  83:    */   }
/*  84:    */   
/*  85:    */   public static void main(String[] args)
/*  86:    */   {
/*  87: 97 */     int count = 10;
/*  88: 98 */     int index = 3;
/*  89: 99 */     for (int i = 0; i < count; i++) {
/*  90:101 */       System.out.println(">>" + (i - index + count) % count);
/*  91:    */     }
/*  92:    */   }
/*  93:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.model.KenRandomGPSOrderer
 * JD-Core Version:    0.7.0.1
 */