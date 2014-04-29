/*  1:   */ package org.springframework.web.client.support;
/*  2:   */ 
/*  3:   */ import org.apache.commons.logging.Log;
/*  4:   */ import org.apache.commons.logging.LogFactory;
/*  5:   */ import org.springframework.http.client.ClientHttpRequestFactory;
/*  6:   */ import org.springframework.util.Assert;
/*  7:   */ import org.springframework.web.client.RestTemplate;
/*  8:   */ 
/*  9:   */ public class RestGatewaySupport
/* 10:   */ {
/* 11:39 */   protected final Log logger = LogFactory.getLog(getClass());
/* 12:   */   private RestTemplate restTemplate;
/* 13:   */   
/* 14:   */   public RestGatewaySupport()
/* 15:   */   {
/* 16:48 */     this.restTemplate = new RestTemplate();
/* 17:   */   }
/* 18:   */   
/* 19:   */   public RestGatewaySupport(ClientHttpRequestFactory requestFactory)
/* 20:   */   {
/* 21:56 */     Assert.notNull(requestFactory, "'requestFactory' must not be null");
/* 22:57 */     this.restTemplate = new RestTemplate(requestFactory);
/* 23:   */   }
/* 24:   */   
/* 25:   */   public void setRestTemplate(RestTemplate restTemplate)
/* 26:   */   {
/* 27:65 */     Assert.notNull(restTemplate, "'restTemplate' must not be null");
/* 28:66 */     this.restTemplate = restTemplate;
/* 29:   */   }
/* 30:   */   
/* 31:   */   public RestTemplate getRestTemplate()
/* 32:   */   {
/* 33:73 */     return this.restTemplate;
/* 34:   */   }
/* 35:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.client.support.RestGatewaySupport
 * JD-Core Version:    0.7.0.1
 */