package UPBEAT;

public class Position {
    public int i;
    public int j;

    public Position(int i, int j){
        this.i = i;
        this.j = j;
    }

    public void nextPos(int direction){
        switch (direction){
            case 1 -> i--;
            case 2 -> {
                if(j%2==0) i--;
                j++;
            }
            case 3 -> {
                if(j%2==0) {j++;}
                else {j++;i++;}
            }
            case 4 -> i++;
            case 5 -> {
                if(j%2==0) {j--;}
                else {j--;i++;}
            }
            case 6 -> {
                if(j%2==0) i--;
                j--;
            }
        }
    }
}
