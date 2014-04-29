/*   1:    */ package org.springframework.util;
/*   2:    */ 
/*   3:    */ import java.lang.ref.Reference;
/*   4:    */ import java.lang.ref.ReferenceQueue;
/*   5:    */ import java.lang.ref.WeakReference;
/*   6:    */ import java.util.HashMap;
/*   7:    */ import java.util.Map;
/*   8:    */ import org.apache.commons.logging.Log;
/*   9:    */ import org.apache.commons.logging.LogFactory;
/*  10:    */ 
/*  11:    */ public class WeakReferenceMonitor
/*  12:    */ {
/*  13: 50 */   private static final Log logger = LogFactory.getLog(WeakReferenceMonitor.class);
/*  14: 53 */   private static final ReferenceQueue<Object> handleQueue = new ReferenceQueue();
/*  15: 56 */   private static final Map<Reference, ReleaseListener> trackedEntries = new HashMap();
/*  16: 59 */   private static Thread monitoringThread = null;
/*  17:    */   
/*  18:    */   public static void monitor(Object handle, ReleaseListener listener)
/*  19:    */   {
/*  20: 69 */     if (logger.isDebugEnabled()) {
/*  21: 70 */       logger.debug("Monitoring handle [" + handle + "] with release listener [" + listener + "]");
/*  22:    */     }
/*  23: 75 */     WeakReference<Object> weakRef = new WeakReference(handle, handleQueue);
/*  24:    */     
/*  25:    */ 
/*  26: 78 */     addEntry(weakRef, listener);
/*  27:    */   }
/*  28:    */   
/*  29:    */   private static void addEntry(Reference ref, ReleaseListener entry)
/*  30:    */   {
/*  31: 88 */     synchronized (WeakReferenceMonitor.class)
/*  32:    */     {
/*  33: 90 */       trackedEntries.put(ref, entry);
/*  34: 93 */       if (monitoringThread == null)
/*  35:    */       {
/*  36: 94 */         monitoringThread = new Thread(new MonitoringProcess(null), WeakReferenceMonitor.class.getName());
/*  37: 95 */         monitoringThread.setDaemon(true);
/*  38: 96 */         monitoringThread.start();
/*  39:    */       }
/*  40:    */     }
/*  41:    */   }
/*  42:    */   
/*  43:    */   private static ReleaseListener removeEntry(Reference reference)
/*  44:    */   {
/*  45:107 */     synchronized (WeakReferenceMonitor.class)
/*  46:    */     {
/*  47:108 */       return (ReleaseListener)trackedEntries.remove(reference);
/*  48:    */     }
/*  49:    */   }
/*  50:    */   
/*  51:    */   private static boolean keepMonitoringThreadAlive()
/*  52:    */   {
/*  53:117 */     synchronized (WeakReferenceMonitor.class)
/*  54:    */     {
/*  55:118 */       if (!trackedEntries.isEmpty()) {
/*  56:119 */         return true;
/*  57:    */       }
/*  58:122 */       logger.debug("No entries left to track - stopping reference monitor thread");
/*  59:123 */       monitoringThread = null;
/*  60:124 */       return false;
/*  61:    */     }
/*  62:    */   }
/*  63:    */   
/*  64:    */   private static class MonitoringProcess
/*  65:    */     implements Runnable
/*  66:    */   {
/*  67:    */     public void run()
/*  68:    */     {
/*  69:136 */       WeakReferenceMonitor.logger.debug("Starting reference monitor thread");
/*  70:    */       break label85;
/*  71:    */       try
/*  72:    */       {
/*  73:    */         do
/*  74:    */         {
/*  75:140 */           Reference reference = WeakReferenceMonitor.handleQueue.remove();
/*  76:    */           
/*  77:142 */           WeakReferenceMonitor.ReleaseListener entry = WeakReferenceMonitor.removeEntry(reference);
/*  78:143 */           if (entry != null) {
/*  79:    */             try
/*  80:    */             {
/*  81:146 */               entry.released();
/*  82:    */             }
/*  83:    */             catch (Throwable ex)
/*  84:    */             {
/*  85:149 */               WeakReferenceMonitor.logger.warn("Reference release listener threw exception", ex);
/*  86:    */             }
/*  87:    */           }
/*  88:138 */         } while (WeakReferenceMonitor.access$1());
/*  89:    */       }
/*  90:    */       catch (InterruptedException ex)
/*  91:    */       {
/*  92:154 */         synchronized (WeakReferenceMonitor.class)
/*  93:    */         {
/*  94:155 */           WeakReferenceMonitor.monitoringThread = null;
/*  95:    */         }
/*  96:157 */         WeakReferenceMonitor.logger.debug("Reference monitor thread interrupted", ex);
/*  97:    */       }
/*  98:    */       label85:
/*  99:    */     }
/* 100:    */   }
/* 101:    */   
/* 102:    */   public static abstract interface ReleaseListener
/* 103:    */   {
/* 104:    */     public abstract void released();
/* 105:    */   }
/* 106:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.util.WeakReferenceMonitor
 * JD-Core Version:    0.7.0.1
 */