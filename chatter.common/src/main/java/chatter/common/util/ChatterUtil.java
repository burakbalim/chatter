package chatter.common.util;

import chatter.common.exception.ChatterException;
import com.google.gson.Gson;

import java.io.*;
import java.nio.charset.Charset;

public class ChatterUtil {

    private ChatterUtil() {
    }

    public static String readFile(String path) throws ChatterException {
        try {
            File file = new File(path);
            FileInputStream fileInputStream = new FileInputStream(file);
            return readToString(fileInputStream);
        }
        catch (IOException e) {
            throw new ChatterException("Exception while reading file", e);
        }
    }

    public static String readToString(InputStream in) throws IOException {
        StringBuilder lines = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, Charset.defaultCharset()));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            lines.append(line);
        }
        bufferedReader.close();
        return lines.toString();
    }

    public static byte[] toByteArray(InputStream in) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = in.read(buffer)) != -1) {
            os.write(buffer, 0, len);
            os.flush();
        }

        return os.toByteArray();
    }

    public static <T> T readJson(String value, Class<T> clazz) {
        return new Gson().fromJson(value, clazz);
    }

}
