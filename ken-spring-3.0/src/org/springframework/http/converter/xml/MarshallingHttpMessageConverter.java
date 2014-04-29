/*   1:    */ package org.springframework.http.converter.xml;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import javax.xml.transform.Result;
/*   5:    */ import javax.xml.transform.Source;
/*   6:    */ import org.springframework.beans.TypeMismatchException;
/*   7:    */ import org.springframework.http.HttpHeaders;
/*   8:    */ import org.springframework.http.converter.HttpMessageNotReadableException;
/*   9:    */ import org.springframework.http.converter.HttpMessageNotWritableException;
/*  10:    */ import org.springframework.oxm.Marshaller;
/*  11:    */ import org.springframework.oxm.MarshallingFailureException;
/*  12:    */ import org.springframework.oxm.Unmarshaller;
/*  13:    */ import org.springframework.oxm.UnmarshallingFailureException;
/*  14:    */ import org.springframework.util.Assert;
/*  15:    */ 
/*  16:    */ public class MarshallingHttpMessageConverter
/*  17:    */   extends AbstractXmlHttpMessageConverter<Object>
/*  18:    */ {
/*  19:    */   private Marshaller marshaller;
/*  20:    */   private Unmarshaller unmarshaller;
/*  21:    */   
/*  22:    */   public MarshallingHttpMessageConverter() {}
/*  23:    */   
/*  24:    */   public MarshallingHttpMessageConverter(Marshaller marshaller)
/*  25:    */   {
/*  26: 71 */     Assert.notNull(marshaller, "Marshaller must not be null");
/*  27: 72 */     this.marshaller = marshaller;
/*  28: 73 */     if ((marshaller instanceof Unmarshaller)) {
/*  29: 74 */       this.unmarshaller = ((Unmarshaller)marshaller);
/*  30:    */     }
/*  31:    */   }
/*  32:    */   
/*  33:    */   public MarshallingHttpMessageConverter(Marshaller marshaller, Unmarshaller unmarshaller)
/*  34:    */   {
/*  35: 85 */     Assert.notNull(marshaller, "Marshaller must not be null");
/*  36: 86 */     Assert.notNull(unmarshaller, "Unmarshaller must not be null");
/*  37: 87 */     this.marshaller = marshaller;
/*  38: 88 */     this.unmarshaller = unmarshaller;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public void setMarshaller(Marshaller marshaller)
/*  42:    */   {
/*  43: 96 */     this.marshaller = marshaller;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public void setUnmarshaller(Unmarshaller unmarshaller)
/*  47:    */   {
/*  48:103 */     this.unmarshaller = unmarshaller;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public boolean supports(Class<?> clazz)
/*  52:    */   {
/*  53:109 */     return this.unmarshaller.supports(clazz);
/*  54:    */   }
/*  55:    */   
/*  56:    */   protected Object readFromSource(Class<?> clazz, HttpHeaders headers, Source source)
/*  57:    */     throws IOException
/*  58:    */   {
/*  59:114 */     Assert.notNull(this.unmarshaller, "Property 'unmarshaller' is required");
/*  60:    */     try
/*  61:    */     {
/*  62:116 */       Object result = this.unmarshaller.unmarshal(source);
/*  63:117 */       if (!clazz.isInstance(result)) {
/*  64:118 */         throw new TypeMismatchException(result, clazz);
/*  65:    */       }
/*  66:120 */       return result;
/*  67:    */     }
/*  68:    */     catch (UnmarshallingFailureException ex)
/*  69:    */     {
/*  70:123 */       throw new HttpMessageNotReadableException("Could not read [" + clazz + "]", ex);
/*  71:    */     }
/*  72:    */   }
/*  73:    */   
/*  74:    */   protected void writeToResult(Object o, HttpHeaders headers, Result result)
/*  75:    */     throws IOException
/*  76:    */   {
/*  77:129 */     Assert.notNull(this.marshaller, "Property 'marshaller' is required");
/*  78:    */     try
/*  79:    */     {
/*  80:131 */       this.marshaller.marshal(o, result);
/*  81:    */     }
/*  82:    */     catch (MarshallingFailureException ex)
/*  83:    */     {
/*  84:134 */       throw new HttpMessageNotWritableException("Could not write [" + o + "]", ex);
/*  85:    */     }
/*  86:    */   }
/*  87:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.http.converter.xml.MarshallingHttpMessageConverter
 * JD-Core Version:    0.7.0.1
 */