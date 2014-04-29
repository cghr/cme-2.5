/*  1:   */ package org.springframework.http.converter.feed;
/*  2:   */ 
/*  3:   */ import com.sun.syndication.feed.atom.Feed;
/*  4:   */ import org.springframework.http.MediaType;
/*  5:   */ 
/*  6:   */ public class AtomFeedHttpMessageConverter
/*  7:   */   extends AbstractWireFeedHttpMessageConverter<Feed>
/*  8:   */ {
/*  9:   */   public AtomFeedHttpMessageConverter()
/* 10:   */   {
/* 11:38 */     super(new MediaType("application", "atom+xml"));
/* 12:   */   }
/* 13:   */   
/* 14:   */   protected boolean supports(Class<?> clazz)
/* 15:   */   {
/* 16:43 */     return Feed.class.isAssignableFrom(clazz);
/* 17:   */   }
/* 18:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.http.converter.feed.AtomFeedHttpMessageConverter
 * JD-Core Version:    0.7.0.1
 */