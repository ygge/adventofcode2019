package dim2;

public enum Direction {
    UP(-1, 0),
    LEFT(0, -1),
    DOWN(1, 0),
    RIGHT(0, 1);

    public final int dy, dx;

    Direction(int dy, int dx) {
        this.dy = dy;
        this.dx = dx;
    }

    public Direction turn(int dir) {
        switch(this) {
            case UP:
                return dir == 0 ? LEFT : RIGHT;
            case LEFT:
                return dir == 0 ? DOWN : UP;
            case DOWN:
                return dir == 0 ? RIGHT : LEFT;
            case RIGHT:
                return dir == 0 ? UP : DOWN;
        }
        throw new IllegalStateException();
    }
}
