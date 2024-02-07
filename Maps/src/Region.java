import java.util.Map;

public class Region {
    protected long i;
    protected long j;
    protected Map<String,Region> connectReg;
    protected boolean citycenter;
    protected Player president;
    protected double budget;
    protected double interest;
    public Region(long i,long j,double budget,double interest){
        this.i = i;
        this.j = j;
        this.budget = budget;
        this.interest = interest;
        Mapping.computeConnect(this);

    }
    public static void computeInterest(Region r){

    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public double getInterest() {
        return interest;
    }
    public boolean checkCitycenter(){
        return citycenter;
    }
}
