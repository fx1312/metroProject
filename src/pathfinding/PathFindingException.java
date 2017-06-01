package pathfinding;

// TODO Should this extend Excpetion and force us to try / catch / throw ?
public class PathFindingException extends RuntimeException {
    public PathFindingException(String message) {
        super(message);
    }

    public PathFindingException(Exception reason) {
        super(reason);
    }

    public PathFindingException(String message, Exception reason) {
        super(message, reason);
    }
}
