import java.util.Arrays;
import java.util.Comparator;

/**
 * @author fengyu .
 * @Date 2017-12-31 18:44
 */
public class LengthComparetorLambda implements Comparator<String> {


    @Override
    public int compare(String o1, String o2) {
        return Integer.compare(o1.length(),o2.length());
    }
    public static void main(String[] args) {
        String[] string=new String[10];
        Arrays.sort(string,new LengthComparetor());
        Arrays.sort(string, (String first, String second)->Integer.compare(first.length(), second.length()));

    }
}
