/*   1:    */ package org.springframework.web.filter;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.util.ArrayList;
/*   5:    */ import java.util.List;
/*   6:    */ import javax.servlet.Filter;
/*   7:    */ import javax.servlet.FilterChain;
/*   8:    */ import javax.servlet.FilterConfig;
/*   9:    */ import javax.servlet.ServletException;
/*  10:    */ import javax.servlet.ServletRequest;
/*  11:    */ import javax.servlet.ServletResponse;
/*  12:    */ 
/*  13:    */ public class CompositeFilter
/*  14:    */   implements Filter
/*  15:    */ {
/*  16: 44 */   private List<? extends Filter> filters = new ArrayList();
/*  17:    */   
/*  18:    */   public void setFilters(List<? extends Filter> filters)
/*  19:    */   {
/*  20: 47 */     this.filters = new ArrayList(filters);
/*  21:    */   }
/*  22:    */   
/*  23:    */   public void destroy()
/*  24:    */   {
/*  25: 56 */     for (int i = this.filters.size(); i-- > 0;)
/*  26:    */     {
/*  27: 57 */       Filter filter = (Filter)this.filters.get(i);
/*  28: 58 */       filter.destroy();
/*  29:    */     }
/*  30:    */   }
/*  31:    */   
/*  32:    */   public void init(FilterConfig config)
/*  33:    */     throws ServletException
/*  34:    */   {
/*  35: 68 */     for (Filter filter : this.filters) {
/*  36: 69 */       filter.init(config);
/*  37:    */     }
/*  38:    */   }
/*  39:    */   
/*  40:    */   public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
/*  41:    */     throws IOException, ServletException
/*  42:    */   {
/*  43: 82 */     new VirtualFilterChain(chain, this.filters, null).doFilter(request, response);
/*  44:    */   }
/*  45:    */   
/*  46:    */   private static class VirtualFilterChain
/*  47:    */     implements FilterChain
/*  48:    */   {
/*  49:    */     private final FilterChain originalChain;
/*  50:    */     private final List<? extends Filter> additionalFilters;
/*  51: 88 */     private int currentPosition = 0;
/*  52:    */     
/*  53:    */     private VirtualFilterChain(FilterChain chain, List<? extends Filter> additionalFilters)
/*  54:    */     {
/*  55: 91 */       this.originalChain = chain;
/*  56: 92 */       this.additionalFilters = additionalFilters;
/*  57:    */     }
/*  58:    */     
/*  59:    */     public void doFilter(ServletRequest request, ServletResponse response)
/*  60:    */       throws IOException, ServletException
/*  61:    */     {
/*  62: 97 */       if (this.currentPosition == this.additionalFilters.size())
/*  63:    */       {
/*  64: 98 */         this.originalChain.doFilter(request, response);
/*  65:    */       }
/*  66:    */       else
/*  67:    */       {
/*  68:100 */         this.currentPosition += 1;
/*  69:101 */         Filter nextFilter = (Filter)this.additionalFilters.get(this.currentPosition - 1);
/*  70:102 */         nextFilter.doFilter(request, response, this);
/*  71:    */       }
/*  72:    */     }
/*  73:    */   }
/*  74:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.filter.CompositeFilter
 * JD-Core Version:    0.7.0.1
 */