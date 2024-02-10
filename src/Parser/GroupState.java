package Parser;

public interface GroupState extends State{
    void append(State s);
}
