/*  1:   */ package org.springframework.remoting.jaxws;
/*  2:   */ 
/*  3:   */ import javax.jws.WebService;
/*  4:   */ import javax.xml.ws.Endpoint;
/*  5:   */ import javax.xml.ws.WebServiceProvider;
/*  6:   */ 
/*  7:   */ public class SimpleJaxWsServiceExporter
/*  8:   */   extends AbstractJaxWsServiceExporter
/*  9:   */ {
/* 10:   */   public static final String DEFAULT_BASE_ADDRESS = "http://localhost:8080/";
/* 11:49 */   private String baseAddress = "http://localhost:8080/";
/* 12:   */   
/* 13:   */   public void setBaseAddress(String baseAddress)
/* 14:   */   {
/* 15:62 */     this.baseAddress = baseAddress;
/* 16:   */   }
/* 17:   */   
/* 18:   */   protected void publishEndpoint(Endpoint endpoint, WebService annotation)
/* 19:   */   {
/* 20:68 */     endpoint.publish(calculateEndpointAddress(endpoint, annotation.serviceName()));
/* 21:   */   }
/* 22:   */   
/* 23:   */   protected void publishEndpoint(Endpoint endpoint, WebServiceProvider annotation)
/* 24:   */   {
/* 25:73 */     endpoint.publish(calculateEndpointAddress(endpoint, annotation.serviceName()));
/* 26:   */   }
/* 27:   */   
/* 28:   */   protected String calculateEndpointAddress(Endpoint endpoint, String serviceName)
/* 29:   */   {
/* 30:83 */     String fullAddress = this.baseAddress + serviceName;
/* 31:84 */     if (endpoint.getClass().getName().startsWith("weblogic.")) {
/* 32:86 */       fullAddress = fullAddress + "/";
/* 33:   */     }
/* 34:88 */     return fullAddress;
/* 35:   */   }
/* 36:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.remoting.jaxws.SimpleJaxWsServiceExporter
 * JD-Core Version:    0.7.0.1
 */