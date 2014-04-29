/*  1:   */ package org.springframework.transaction.interceptor;
/*  2:   */ 
/*  3:   */ import java.beans.PropertyEditorSupport;
/*  4:   */ import java.util.Enumeration;
/*  5:   */ import java.util.Properties;
/*  6:   */ import org.springframework.beans.propertyeditors.PropertiesEditor;
/*  7:   */ import org.springframework.util.StringUtils;
/*  8:   */ 
/*  9:   */ public class TransactionAttributeSourceEditor
/* 10:   */   extends PropertyEditorSupport
/* 11:   */ {
/* 12:   */   public void setAsText(String text)
/* 13:   */     throws IllegalArgumentException
/* 14:   */   {
/* 15:53 */     MethodMapTransactionAttributeSource source = new MethodMapTransactionAttributeSource();
/* 16:54 */     if (StringUtils.hasLength(text))
/* 17:   */     {
/* 18:56 */       PropertiesEditor propertiesEditor = new PropertiesEditor();
/* 19:57 */       propertiesEditor.setAsText(text);
/* 20:58 */       Properties props = (Properties)propertiesEditor.getValue();
/* 21:   */       
/* 22:   */ 
/* 23:61 */       TransactionAttributeEditor tae = new TransactionAttributeEditor();
/* 24:62 */       Enumeration propNames = props.propertyNames();
/* 25:63 */       while (propNames.hasMoreElements())
/* 26:   */       {
/* 27:64 */         String name = (String)propNames.nextElement();
/* 28:65 */         String value = props.getProperty(name);
/* 29:   */         
/* 30:67 */         tae.setAsText(value);
/* 31:68 */         TransactionAttribute attr = (TransactionAttribute)tae.getValue();
/* 32:   */         
/* 33:70 */         source.addTransactionalMethod(name, attr);
/* 34:   */       }
/* 35:   */     }
/* 36:73 */     setValue(source);
/* 37:   */   }
/* 38:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.transaction.interceptor.TransactionAttributeSourceEditor
 * JD-Core Version:    0.7.0.1
 */