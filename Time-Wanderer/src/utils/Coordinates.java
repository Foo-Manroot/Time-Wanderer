package utils;

public class Coordinates {
    public int r, c;
    public Coordinates(int r, int c) {
        this.r = r;
        this.c = c;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Coordinates other = (Coordinates) obj;
        if (this.r != other.r) {
            return false;
        }
        if (this.c != other.c) {
            return false;
        }
        return true;
    }
    
}
