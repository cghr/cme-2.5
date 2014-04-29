/*   1:    */ package org.springframework.http.converter.xml;
/*   2:    */ 
/*   3:    */ import java.io.ByteArrayInputStream;
/*   4:    */ import java.io.ByteArrayOutputStream;
/*   5:    */ import java.io.IOException;
/*   6:    */ import java.io.OutputStream;
/*   7:    */ import javax.xml.transform.Result;
/*   8:    */ import javax.xml.transform.Source;
/*   9:    */ import javax.xml.transform.TransformerException;
/*  10:    */ import javax.xml.transform.dom.DOMResult;
/*  11:    */ import javax.xml.transform.dom.DOMSource;
/*  12:    */ import javax.xml.transform.sax.SAXSource;
/*  13:    */ import javax.xml.transform.stream.StreamResult;
/*  14:    */ import javax.xml.transform.stream.StreamSource;
/*  15:    */ import org.springframework.http.HttpHeaders;
/*  16:    */ import org.springframework.http.MediaType;
/*  17:    */ import org.springframework.http.converter.HttpMessageConversionException;
/*  18:    */ import org.springframework.http.converter.HttpMessageNotReadableException;
/*  19:    */ import org.springframework.http.converter.HttpMessageNotWritableException;
/*  20:    */ import org.xml.sax.InputSource;
/*  21:    */ 
/*  22:    */ public class SourceHttpMessageConverter<T extends Source>
/*  23:    */   extends AbstractXmlHttpMessageConverter<T>
/*  24:    */ {
/*  25:    */   public boolean supports(Class<?> clazz)
/*  26:    */   {
/*  27: 52 */     return (DOMSource.class.equals(clazz)) || (SAXSource.class.equals(clazz)) || (StreamSource.class.equals(clazz)) || (Source.class.equals(clazz));
/*  28:    */   }
/*  29:    */   
/*  30:    */   protected T readFromSource(Class clazz, HttpHeaders headers, Source source)
/*  31:    */     throws IOException
/*  32:    */   {
/*  33:    */     try
/*  34:    */     {
/*  35: 59 */       if (DOMSource.class.equals(clazz))
/*  36:    */       {
/*  37: 60 */         DOMResult domResult = new DOMResult();
/*  38: 61 */         transform(source, domResult);
/*  39: 62 */         return new DOMSource(domResult.getNode());
/*  40:    */       }
/*  41: 64 */       if (SAXSource.class.equals(clazz))
/*  42:    */       {
/*  43: 65 */         ByteArrayInputStream bis = transformToByteArrayInputStream(source);
/*  44: 66 */         return new SAXSource(new InputSource(bis));
/*  45:    */       }
/*  46: 68 */       if ((StreamSource.class.equals(clazz)) || (Source.class.equals(clazz)))
/*  47:    */       {
/*  48: 69 */         ByteArrayInputStream bis = transformToByteArrayInputStream(source);
/*  49: 70 */         return new StreamSource(bis);
/*  50:    */       }
/*  51: 73 */       throw new HttpMessageConversionException("Could not read class [" + clazz + 
/*  52: 74 */         "]. Only DOMSource, SAXSource, and StreamSource are supported.");
/*  53:    */     }
/*  54:    */     catch (TransformerException ex)
/*  55:    */     {
/*  56: 78 */       throw new HttpMessageNotReadableException("Could not transform from [" + source + "] to [" + clazz + "]", 
/*  57: 79 */         ex);
/*  58:    */     }
/*  59:    */   }
/*  60:    */   
/*  61:    */   private ByteArrayInputStream transformToByteArrayInputStream(Source source)
/*  62:    */     throws TransformerException
/*  63:    */   {
/*  64: 84 */     ByteArrayOutputStream bos = new ByteArrayOutputStream();
/*  65: 85 */     transform(source, new StreamResult(bos));
/*  66: 86 */     return new ByteArrayInputStream(bos.toByteArray());
/*  67:    */   }
/*  68:    */   
/*  69:    */   protected Long getContentLength(T t, MediaType contentType)
/*  70:    */   {
/*  71: 91 */     if ((t instanceof DOMSource)) {
/*  72:    */       try
/*  73:    */       {
/*  74: 93 */         CountingOutputStream os = new CountingOutputStream(null);
/*  75: 94 */         transform(t, new StreamResult(os));
/*  76: 95 */         return Long.valueOf(os.count);
/*  77:    */       }
/*  78:    */       catch (TransformerException localTransformerException) {}
/*  79:    */     }
/*  80:101 */     return null;
/*  81:    */   }
/*  82:    */   
/*  83:    */   protected void writeToResult(T t, HttpHeaders headers, Result result)
/*  84:    */     throws IOException
/*  85:    */   {
/*  86:    */     try
/*  87:    */     {
/*  88:107 */       transform(t, result);
/*  89:    */     }
/*  90:    */     catch (TransformerException ex)
/*  91:    */     {
/*  92:110 */       throw new HttpMessageNotWritableException("Could not transform [" + t + "] to [" + result + "]", ex);
/*  93:    */     }
/*  94:    */   }
/*  95:    */   
/*  96:    */   private static class CountingOutputStream
/*  97:    */     extends OutputStream
/*  98:    */   {
/*  99:116 */     private long count = 0L;
/* 100:    */     
/* 101:    */     public void write(int b)
/* 102:    */       throws IOException
/* 103:    */     {
/* 104:120 */       this.count += 1L;
/* 105:    */     }
/* 106:    */     
/* 107:    */     public void write(byte[] b)
/* 108:    */       throws IOException
/* 109:    */     {
/* 110:125 */       this.count += b.length;
/* 111:    */     }
/* 112:    */     
/* 113:    */     public void write(byte[] b, int off, int len)
/* 114:    */       throws IOException
/* 115:    */     {
/* 116:130 */       this.count += len;
/* 117:    */     }
/* 118:    */   }
/* 119:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.http.converter.xml.SourceHttpMessageConverter
 * JD-Core Version:    0.7.0.1
 */