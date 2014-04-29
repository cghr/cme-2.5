/*   1:    */ package org.springframework.ui.velocity;
/*   2:    */ 
/*   3:    */ import java.io.StringWriter;
/*   4:    */ import java.io.Writer;
/*   5:    */ import java.util.Map;
/*   6:    */ import org.apache.commons.logging.Log;
/*   7:    */ import org.apache.commons.logging.LogFactory;
/*   8:    */ import org.apache.velocity.VelocityContext;
/*   9:    */ import org.apache.velocity.app.VelocityEngine;
/*  10:    */ import org.apache.velocity.exception.VelocityException;
/*  11:    */ 
/*  12:    */ public abstract class VelocityEngineUtils
/*  13:    */ {
/*  14: 38 */   private static final Log logger = LogFactory.getLog(VelocityEngineUtils.class);
/*  15:    */   
/*  16:    */   public static void mergeTemplate(VelocityEngine velocityEngine, String templateLocation, Map model, Writer writer)
/*  17:    */     throws VelocityException
/*  18:    */   {
/*  19:    */     try
/*  20:    */     {
/*  21: 57 */       VelocityContext velocityContext = new VelocityContext(model);
/*  22: 58 */       velocityEngine.mergeTemplate(templateLocation, velocityContext, writer);
/*  23:    */     }
/*  24:    */     catch (VelocityException ex)
/*  25:    */     {
/*  26: 61 */       throw ex;
/*  27:    */     }
/*  28:    */     catch (RuntimeException ex)
/*  29:    */     {
/*  30: 64 */       throw ex;
/*  31:    */     }
/*  32:    */     catch (Exception ex)
/*  33:    */     {
/*  34: 67 */       logger.error("Why does VelocityEngine throw a generic checked exception, after all?", ex);
/*  35: 68 */       throw new VelocityException(ex.toString());
/*  36:    */     }
/*  37:    */   }
/*  38:    */   
/*  39:    */   public static void mergeTemplate(VelocityEngine velocityEngine, String templateLocation, String encoding, Map model, Writer writer)
/*  40:    */     throws VelocityException
/*  41:    */   {
/*  42:    */     try
/*  43:    */     {
/*  44: 89 */       VelocityContext velocityContext = new VelocityContext(model);
/*  45: 90 */       velocityEngine.mergeTemplate(templateLocation, encoding, velocityContext, writer);
/*  46:    */     }
/*  47:    */     catch (VelocityException ex)
/*  48:    */     {
/*  49: 93 */       throw ex;
/*  50:    */     }
/*  51:    */     catch (RuntimeException ex)
/*  52:    */     {
/*  53: 96 */       throw ex;
/*  54:    */     }
/*  55:    */     catch (Exception ex)
/*  56:    */     {
/*  57: 99 */       logger.error("Why does VelocityEngine throw a generic checked exception, after all?", ex);
/*  58:100 */       throw new VelocityException(ex.toString());
/*  59:    */     }
/*  60:    */   }
/*  61:    */   
/*  62:    */   public static String mergeTemplateIntoString(VelocityEngine velocityEngine, String templateLocation, Map model)
/*  63:    */     throws VelocityException
/*  64:    */   {
/*  65:121 */     StringWriter result = new StringWriter();
/*  66:122 */     mergeTemplate(velocityEngine, templateLocation, model, result);
/*  67:123 */     return result.toString();
/*  68:    */   }
/*  69:    */   
/*  70:    */   public static String mergeTemplateIntoString(VelocityEngine velocityEngine, String templateLocation, String encoding, Map model)
/*  71:    */     throws VelocityException
/*  72:    */   {
/*  73:144 */     StringWriter result = new StringWriter();
/*  74:145 */     mergeTemplate(velocityEngine, templateLocation, encoding, model, result);
/*  75:146 */     return result.toString();
/*  76:    */   }
/*  77:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.ui.velocity.VelocityEngineUtils
 * JD-Core Version:    0.7.0.1
 */