package Parser.Statement;

public interface GroupState extends State{
    void append(State s);
}
