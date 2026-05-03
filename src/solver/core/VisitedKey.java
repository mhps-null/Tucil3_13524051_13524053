package solver.core;

import java.util.Objects;

public class VisitedKey {
    public int x, y, nextNumber;

    public VisitedKey(int x, int y, int nextNumber) {
        this.x = x;
        this.y = y;
        this.nextNumber = nextNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof VisitedKey))
            return false;
        VisitedKey v = (VisitedKey) o;
        return x == v.x && y == v.y && nextNumber == v.nextNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, nextNumber);
    }
}