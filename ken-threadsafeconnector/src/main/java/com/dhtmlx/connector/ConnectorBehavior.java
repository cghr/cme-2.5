/*   1:    */ package com.dhtmlx.connector;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import javax.servlet.http.HttpServletRequest;
/*   5:    */ import javax.servlet.http.HttpServletResponse;
/*   6:    */ 
/*   7:    */ public class ConnectorBehavior
/*   8:    */ {
/*   9:    */   private ConnectorBehavior instance;
/*  10:    */   
/*  11:    */   public ConnectorBehavior()
/*  12:    */   {
/*  13: 26 */     this.instance = null;
/*  14:    */   }
/*  15:    */   
/*  16:    */   public void attach(ConnectorBehavior custom)
/*  17:    */   {
/*  18: 36 */     if (this.instance != null) {
/*  19: 37 */       this.instance.attach(custom);
/*  20:    */     } else {
/*  21: 39 */       this.instance = custom;
/*  22:    */     }
/*  23:    */   }
/*  24:    */   
/*  25:    */   public ConnectorBehavior trigger()
/*  26:    */   {
/*  27: 48 */     return this;
/*  28:    */   }
/*  29:    */   
/*  30:    */   public void beforeSort(ArrayList<SortingRule> sorters)
/*  31:    */   {
/*  32: 60 */     if (this.instance != null) {
/*  33: 61 */       this.instance.beforeSort(sorters);
/*  34:    */     }
/*  35:    */   }
/*  36:    */   
/*  37:    */   public void beforeFilter(ArrayList<FilteringRule> filters)
/*  38:    */   {
/*  39: 70 */     if (this.instance != null) {
/*  40: 71 */       this.instance.beforeFilter(filters);
/*  41:    */     }
/*  42:    */   }
/*  43:    */   
/*  44:    */   public void beforeRender(DataItem data)
/*  45:    */   {
/*  46: 83 */     if (this.instance != null) {
/*  47: 84 */       this.instance.beforeRender(data);
/*  48:    */     }
/*  49:    */   }
/*  50:    */   
/*  51:    */   public void beforeProcessing(DataAction action)
/*  52:    */   {
/*  53: 97 */     if (this.instance != null) {
/*  54: 98 */       this.instance.beforeProcessing(action);
/*  55:    */     }
/*  56:    */   }
/*  57:    */   
/*  58:    */   public void afterDBError(DataAction action, Throwable e)
/*  59:    */   {
/*  60:111 */     if (this.instance != null) {
/*  61:112 */       this.instance.afterDBError(action, e);
/*  62:    */     }
/*  63:    */   }
/*  64:    */   
/*  65:    */   public void afterProcessing(DataAction action)
/*  66:    */   {
/*  67:125 */     if (this.instance != null) {
/*  68:126 */       this.instance.afterProcessing(action);
/*  69:    */     }
/*  70:    */   }
/*  71:    */   
/*  72:    */   public void beforeDelete(DataAction action)
/*  73:    */   {
/*  74:139 */     if (this.instance != null) {
/*  75:140 */       this.instance.beforeDelete(action);
/*  76:    */     }
/*  77:    */   }
/*  78:    */   
/*  79:    */   public void beforeInsert(DataAction action)
/*  80:    */   {
/*  81:153 */     if (this.instance != null) {
/*  82:154 */       this.instance.beforeInsert(action);
/*  83:    */     }
/*  84:    */   }
/*  85:    */   
/*  86:    */   public void beforeUpdate(DataAction action)
/*  87:    */   {
/*  88:167 */     if (this.instance != null) {
/*  89:168 */       this.instance.beforeUpdate(action);
/*  90:    */     }
/*  91:    */   }
/*  92:    */   
/*  93:    */   public void afterDelete(DataAction action)
/*  94:    */   {
/*  95:181 */     if (this.instance != null) {
/*  96:182 */       this.instance.afterDelete(action);
/*  97:    */     }
/*  98:    */   }
/*  99:    */   
/* 100:    */   public void afterInsert(DataAction action)
/* 101:    */   {
/* 102:195 */     if (this.instance != null) {
/* 103:196 */       this.instance.afterInsert(action);
/* 104:    */     }
/* 105:    */   }
/* 106:    */   
/* 107:    */   public void afterUpdate(DataAction action)
/* 108:    */   {
/* 109:209 */     if (this.instance != null) {
/* 110:210 */       this.instance.afterUpdate(action);
/* 111:    */     }
/* 112:    */   }
/* 113:    */   
/* 114:    */   public void beforeOutput(ConnectorOutputWriter out, HttpServletRequest http_request, HttpServletResponse http_response)
/* 115:    */   {
/* 116:223 */     if (this.instance != null) {
/* 117:224 */       this.instance.beforeOutput(out, http_request, http_response);
/* 118:    */     }
/* 119:    */   }
/* 120:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-threadsafeconnector\ken-threadsafeconnector.jar
 * Qualified Name:     com.dhtmlx.connector.ConnectorBehavior
 * JD-Core Version:    0.7.0.1
 */