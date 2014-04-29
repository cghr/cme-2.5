/*  1:   */ package org.springframework.validation;
/*  2:   */ 
/*  3:   */ import java.beans.PropertyEditor;
/*  4:   */ import java.util.Map;
/*  5:   */ import org.springframework.beans.PropertyEditorRegistry;
/*  6:   */ 
/*  7:   */ public abstract interface BindingResult
/*  8:   */   extends Errors
/*  9:   */ {
/* 10:50 */   public static final String MODEL_KEY_PREFIX = BindingResult.class.getName() + ".";
/* 11:   */   
/* 12:   */   public abstract Object getTarget();
/* 13:   */   
/* 14:   */   public abstract Map<String, Object> getModel();
/* 15:   */   
/* 16:   */   public abstract Object getRawFieldValue(String paramString);
/* 17:   */   
/* 18:   */   public abstract PropertyEditor findEditor(String paramString, Class<?> paramClass);
/* 19:   */   
/* 20:   */   public abstract PropertyEditorRegistry getPropertyEditorRegistry();
/* 21:   */   
/* 22:   */   public abstract void addError(ObjectError paramObjectError);
/* 23:   */   
/* 24:   */   public abstract String[] resolveMessageCodes(String paramString1, String paramString2);
/* 25:   */   
/* 26:   */   public abstract void recordSuppressedField(String paramString);
/* 27:   */   
/* 28:   */   public abstract String[] getSuppressedFields();
/* 29:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.validation.BindingResult
 * JD-Core Version:    0.7.0.1
 */