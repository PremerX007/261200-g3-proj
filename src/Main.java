import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws Exception {
        ExprTokenizer exr = new ExprTokenizer();
        while (exr.hasNextToken()){
            System.out.println(exr.consume());
        }
    }
}
