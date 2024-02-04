import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/conp.txt"));){

            String line;
            while ((line = reader.readLine()) != null){

            }
            ExprTokenizer ex = new ExprTokenizer();
        } catch (IOException e){
            System.err.format("System can not manage target file : %s%n", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
