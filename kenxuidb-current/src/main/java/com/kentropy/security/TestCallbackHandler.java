/*  1:   */ package com.kentropy.security;
/*  2:   */ 
/*  3:   */ import java.io.IOException;
/*  4:   */ import javax.security.auth.callback.Callback;
/*  5:   */ import javax.security.auth.callback.CallbackHandler;
/*  6:   */ import javax.security.auth.callback.NameCallback;
/*  7:   */ import javax.security.auth.callback.PasswordCallback;
/*  8:   */ import javax.security.auth.callback.UnsupportedCallbackException;
/*  9:   */ 
/* 10:   */ public class TestCallbackHandler
/* 11:   */   implements CallbackHandler
/* 12:   */ {
/* 13:   */   public void handle(Callback[] callbacks)
/* 14:   */     throws IOException, UnsupportedCallbackException
/* 15:   */   {
/* 16:16 */     ((NameCallback)callbacks[0]).setName("nimitt.gupta@kentropy.com");
/* 17:17 */     ((PasswordCallback)callbacks[1]).setPassword("password".toCharArray());
/* 18:   */   }
/* 19:   */   
/* 20:   */   public static void main(String[] args) {}
/* 21:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.security.TestCallbackHandler
 * JD-Core Version:    0.7.0.1
 */