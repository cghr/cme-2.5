/*  1:   */ package org.springframework.web.servlet.view.feed;
/*  2:   */ 
/*  3:   */ import com.sun.syndication.feed.WireFeed;
/*  4:   */ import com.sun.syndication.io.WireFeedOutput;
/*  5:   */ import java.io.OutputStreamWriter;
/*  6:   */ import java.util.Map;
/*  7:   */ import javax.servlet.ServletOutputStream;
/*  8:   */ import javax.servlet.http.HttpServletRequest;
/*  9:   */ import javax.servlet.http.HttpServletResponse;
/* 10:   */ import org.springframework.util.StringUtils;
/* 11:   */ import org.springframework.web.servlet.view.AbstractView;
/* 12:   */ 
/* 13:   */ public abstract class AbstractFeedView<T extends WireFeed>
/* 14:   */   extends AbstractView
/* 15:   */ {
/* 16:   */   protected final void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response)
/* 17:   */     throws Exception
/* 18:   */   {
/* 19:53 */     T wireFeed = newFeed();
/* 20:54 */     buildFeedMetadata(model, wireFeed, request);
/* 21:55 */     buildFeedEntries(model, wireFeed, request, response);
/* 22:   */     
/* 23:57 */     response.setContentType(getContentType());
/* 24:58 */     if (!StringUtils.hasText(wireFeed.getEncoding())) {
/* 25:59 */       wireFeed.setEncoding("UTF-8");
/* 26:   */     }
/* 27:62 */     WireFeedOutput feedOutput = new WireFeedOutput();
/* 28:63 */     ServletOutputStream out = response.getOutputStream();
/* 29:64 */     feedOutput.output(wireFeed, new OutputStreamWriter(out, wireFeed.getEncoding()));
/* 30:65 */     out.flush();
/* 31:   */   }
/* 32:   */   
/* 33:   */   protected abstract T newFeed();
/* 34:   */   
/* 35:   */   protected void buildFeedMetadata(Map<String, Object> model, T feed, HttpServletRequest request) {}
/* 36:   */   
/* 37:   */   protected abstract void buildFeedEntries(Map<String, Object> paramMap, T paramT, HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse)
/* 38:   */     throws Exception;
/* 39:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.view.feed.AbstractFeedView
 * JD-Core Version:    0.7.0.1
 */