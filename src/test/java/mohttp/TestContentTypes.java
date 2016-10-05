package mohttp;


import com.mo.mohttp.constant.ContentType;
import com.mo.mohttp.misc.TextUtils;

public class TestContentTypes {
    public static void main(String[] args) {
        String filename = "C:\\a.txt";
        System.out.println(ContentType.findMimeByExtension(TextUtils.fileExt(filename)));
    }
}
