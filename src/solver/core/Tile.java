package solver.core;

public class Tile {
    public TileType type;
    public int value = -1;

    public Tile(TileType type, int value) {
        this.type = type;
        this.value = value;
    }

    public static Tile fromChar(char c) {
        switch (c) {
            case 'X':
                return new Tile(TileType.WALL, -1);
            case '*':
                return new Tile(TileType.PATH, -1);
            case 'Z':
                return new Tile(TileType.START, -1);
            case 'O':
                return new Tile(TileType.GOAL, -1);
            case 'L':
                return new Tile(TileType.LAVA, -1);
            default:
                if (Character.isDigit(c)) {
                    return new Tile(TileType.NUMBER, c - '0');
                }
                throw new RuntimeException("Invalid tile: " + c);
        }
    }
}