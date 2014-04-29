/*  1:   */ package org.springframework.http.converter.feed;
/*  2:   */ 
/*  3:   */ import com.sun.syndication.feed.WireFeed;
/*  4:   */ import com.sun.syndication.io.FeedException;
/*  5:   */ import com.sun.syndication.io.WireFeedInput;
/*  6:   */ import com.sun.syndication.io.WireFeedOutput;
/*  7:   */ import java.io.IOException;
/*  8:   */ import java.io.InputStreamReader;
/*  9:   */ import java.io.OutputStreamWriter;
/* 10:   */ import java.io.Reader;
/* 11:   */ import java.io.Writer;
/* 12:   */ import java.nio.charset.Charset;
/* 13:   */ import org.springframework.http.HttpHeaders;
/* 14:   */ import org.springframework.http.HttpInputMessage;
/* 15:   */ import org.springframework.http.HttpOutputMessage;
/* 16:   */ import org.springframework.http.MediaType;
/* 17:   */ import org.springframework.http.converter.AbstractHttpMessageConverter;
/* 18:   */ import org.springframework.http.converter.HttpMessageNotReadableException;
/* 19:   */ import org.springframework.http.converter.HttpMessageNotWritableException;
/* 20:   */ import org.springframework.util.StringUtils;
/* 21:   */ 
/* 22:   */ public abstract class AbstractWireFeedHttpMessageConverter<T extends WireFeed>
/* 23:   */   extends AbstractHttpMessageConverter<T>
/* 24:   */ {
/* 25:50 */   public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
/* 26:   */   
/* 27:   */   protected AbstractWireFeedHttpMessageConverter(MediaType supportedMediaType)
/* 28:   */   {
/* 29:53 */     super(supportedMediaType);
/* 30:   */   }
/* 31:   */   
/* 32:   */   protected T readInternal(Class<? extends T> clazz, HttpInputMessage inputMessage)
/* 33:   */     throws IOException, HttpMessageNotReadableException
/* 34:   */   {
/* 35:60 */     WireFeedInput feedInput = new WireFeedInput();
/* 36:61 */     MediaType contentType = inputMessage.getHeaders().getContentType();
/* 37:   */     Charset charset;
/* 38:   */     Charset charset;
/* 39:63 */     if ((contentType != null) && (contentType.getCharSet() != null)) {
/* 40:64 */       charset = contentType.getCharSet();
/* 41:   */     } else {
/* 42:66 */       charset = DEFAULT_CHARSET;
/* 43:   */     }
/* 44:   */     try
/* 45:   */     {
/* 46:69 */       Reader reader = new InputStreamReader(inputMessage.getBody(), charset);
/* 47:70 */       return feedInput.build(reader);
/* 48:   */     }
/* 49:   */     catch (FeedException ex)
/* 50:   */     {
/* 51:73 */       throw new HttpMessageNotReadableException("Could not read WireFeed: " + ex.getMessage(), ex);
/* 52:   */     }
/* 53:   */   }
/* 54:   */   
/* 55:   */   protected void writeInternal(T wireFeed, HttpOutputMessage outputMessage)
/* 56:   */     throws IOException, HttpMessageNotWritableException
/* 57:   */   {
/* 58:80 */     String wireFeedEncoding = wireFeed.getEncoding();
/* 59:81 */     if (!StringUtils.hasLength(wireFeedEncoding)) {
/* 60:82 */       wireFeedEncoding = DEFAULT_CHARSET.name();
/* 61:   */     }
/* 62:84 */     MediaType contentType = outputMessage.getHeaders().getContentType();
/* 63:85 */     if (contentType != null)
/* 64:   */     {
/* 65:86 */       Charset wireFeedCharset = Charset.forName(wireFeedEncoding);
/* 66:87 */       contentType = new MediaType(contentType.getType(), contentType.getSubtype(), wireFeedCharset);
/* 67:88 */       outputMessage.getHeaders().setContentType(contentType);
/* 68:   */     }
/* 69:91 */     WireFeedOutput feedOutput = new WireFeedOutput();
/* 70:   */     try
/* 71:   */     {
/* 72:94 */       Writer writer = new OutputStreamWriter(outputMessage.getBody(), wireFeedEncoding);
/* 73:95 */       feedOutput.output(wireFeed, writer);
/* 74:   */     }
/* 75:   */     catch (FeedException ex)
/* 76:   */     {
/* 77:98 */       throw new HttpMessageNotWritableException("Could not write WiredFeed: " + ex.getMessage(), ex);
/* 78:   */     }
/* 79:   */   }
/* 80:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.http.converter.feed.AbstractWireFeedHttpMessageConverter
 * JD-Core Version:    0.7.0.1
 */