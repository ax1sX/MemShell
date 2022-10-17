import com.sun.org.apache.bcel.internal.classfile.Utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class ClassToBase64 {
    public static void main(String[] args) throws IllegalAccessException, IOException, InstantiationException {
        String s = fileToBase64("/xx/classes/TestInterceptor.class");
    }
    public static String fileToBase64(String filePath) throws IOException, IllegalAccessException, InstantiationException {
        File file = new File(filePath);
        FileInputStream inputFile = new FileInputStream(file);
        byte[] buffer = new byte[(int) file.length()];
        inputFile.read(buffer);
        inputFile.close();
        byte[] bs = Base64.getEncoder().encode(buffer);
        System.out.println(new String(bs));
        return new String(bs);
    }

    private static String classToBcel(String classFileName) throws IOException {
        Path path = Paths.get(classFileName);
        byte[] bytes = Files.readAllBytes(path);
        String result = Utility.encode(bytes,true);
        return "$$BCEL$$" + result;
    }

}
