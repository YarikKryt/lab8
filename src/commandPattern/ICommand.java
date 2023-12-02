package commandPattern;

public interface ICommand {
    public void execute(); // executes certain command
    public void unexecute(); // undo certain command
}
