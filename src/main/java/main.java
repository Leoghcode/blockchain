import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

class test {
    public static void main(String[] args) {
        long ts = System.currentTimeMillis();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println(df.format(new Date(ts)));
    }
}