/*   1:    */ package com.kentropy.data;
/*   2:    */ 
/*   3:    */ import com.kentropy.model.KenList;
/*   4:    */ import com.kentropy.web.SimpleRequestTransformer;
/*   5:    */ import java.io.InputStream;
/*   6:    */ import java.io.PrintStream;
/*   7:    */ import net.xoetrope.xui.data.XBaseModel;
/*   8:    */ import net.xoetrope.xui.data.XModel;
/*   9:    */ 
/*  10:    */ public class DataService
/*  11:    */ {
/*  12:    */   public static XModel getContext(String uri)
/*  13:    */   {
/*  14: 14 */     KenList kl = new KenList(uri);
/*  15: 15 */     System.out.println(kl.size());
/*  16: 16 */     KenList ids = kl.left("-");
/*  17: 17 */     KenList values = kl.right("-");
/*  18:    */     
/*  19: 19 */     XModel context = new XBaseModel();
/*  20: 20 */     for (int i = 0; i < ids.size(); i++) {
/*  21: 22 */       if (!values.get(i).equals("list"))
/*  22:    */       {
/*  23: 24 */         XModel xm = new XBaseModel();
/*  24: 25 */         xm.setId(ids.get(i).toString());
/*  25: 26 */         xm.set(values.get(i).toString());
/*  26:    */         
/*  27: 28 */         context.append(xm);
/*  28:    */       }
/*  29:    */     }
/*  30: 32 */     return context;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public static XModel get(String uri)
/*  34:    */     throws Exception
/*  35:    */   {
/*  36: 36 */     KenList kl = new KenList(uri);
/*  37: 37 */     System.out.println(kl.size());
/*  38: 38 */     KenList ids = kl.left("-");
/*  39: 39 */     KenList values = kl.right("-");
/*  40: 40 */     XModel context = new XBaseModel();
/*  41: 41 */     for (int i = 0; i < ids.size(); i++) {
/*  42: 43 */       if (!values.get(i).equals("list"))
/*  43:    */       {
/*  44: 45 */         XModel xm = new XBaseModel();
/*  45: 46 */         xm.setId(ids.get(i).toString());
/*  46: 47 */         xm.set(values.get(i).toString());
/*  47:    */         
/*  48: 49 */         context.append(xm);
/*  49:    */       }
/*  50:    */     }
/*  51: 53 */     DataHandler dh = new DataHandlerFactory().getDefaultDataHandler();
/*  52: 54 */     String last = values.lastElement().toString();
/*  53: 55 */     XModel dataM = null;
/*  54: 56 */     if (last.equals("list")) {
/*  55: 58 */       dataM = dh.getChildren(values.get(0).toString(), ids.lastElement().toString(), context, "*", null);
/*  56:    */     } else {
/*  57: 62 */       dataM = dh.getData(values.get(0).toString(), ids.lastElement().toString(), context, "*");
/*  58:    */     }
/*  59: 63 */     return dataM;
/*  60:    */   }
/*  61:    */   
/*  62:    */   public static String translate(XModel context, String absURI)
/*  63:    */   {
/*  64: 68 */     String uri = "";
/*  65: 69 */     KenList kl = new KenList(absURI);
/*  66: 70 */     KenList ids = kl.left("-");
/*  67: 71 */     KenList values = kl.right("-");
/*  68: 72 */     KenList values1 = new KenList();
/*  69: 74 */     for (int i = 0; i < kl.size(); i++)
/*  70:    */     {
/*  71: 76 */       String id = ids.get(i).toString();
/*  72: 77 */       XModel xm = (XModel)context.get(id);
/*  73: 78 */       if (!values.get(i).toString().startsWith("$")) {
/*  74: 79 */         values1.add(kl.get(i));
/*  75:    */       } else {
/*  76: 81 */         values1.add(xm.getId() + "-" + xm.get().toString());
/*  77:    */       }
/*  78:    */     }
/*  79: 85 */     return values1.toString("/");
/*  80:    */   }
/*  81:    */   
/*  82:    */   public static void main(String[] args)
/*  83:    */     throws Exception
/*  84:    */   {
/*  85: 91 */     XModel xm = get("survey-PMS/client-1");
/*  86: 92 */     System.out.println("--- " + xm.getNumChildren());
/*  87: 93 */     System.out.println("--- " + new SimpleRequestTransformer("").transformToJSON(xm));
/*  88: 94 */     System.in.read();
/*  89: 95 */     XModel context = new XBaseModel();
/*  90: 96 */     ((XModel)context.get("survey")).set("Enumeration");
/*  91: 97 */     ((XModel)context.get("area")).set("12");
/*  92: 98 */     ((XModel)context.get("house")).set("10001");
/*  93: 99 */     ((XModel)context.get("household")).set("1000101");
/*  94:100 */     String uri = translate(context, "/survey-Enumeration/area-$current/house-$current/household-$current/member-list");
/*  95:101 */     System.out.println(" URI " + uri);
/*  96:102 */     xm = get(uri);
/*  97:103 */     System.out.println(" " + xm.getNumChildren());
/*  98:104 */     xm = get(uri);
/*  99:105 */     System.out.println(" " + xm.getNumChildren());
/* 100:    */   }
/* 101:    */   
/* 102:    */   public static void save(String uri, XModel dataM)
/* 103:    */     throws Exception
/* 104:    */   {
/* 105:111 */     KenList kl = new KenList(uri);
/* 106:112 */     System.out.println(kl.size());
/* 107:113 */     KenList ids = kl.left("-");
/* 108:114 */     KenList values = kl.right("-");
/* 109:115 */     XModel context = getContext(uri);
/* 110:116 */     new DataHandlerFactory().getDefaultDataHandler().saveData(values.get(0).toString(), ids.lastElement().toString(), context, dataM);
/* 111:    */   }
/* 112:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.data.DataService
 * JD-Core Version:    0.7.0.1
 */