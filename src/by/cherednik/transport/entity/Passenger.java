package by.cherednik.transport.entity;

public class Passenger {
    private long id;

    public boolean isLeaving() {
        return leaving;
    }

    public void setLeaving(boolean leaving) {
        this.leaving = leaving;
    }

    private boolean leaving;

    public Passenger(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Passenger passenger = (Passenger) o;

        if (id != passenger.id) return false;
        return leaving == passenger.leaving;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (leaving ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Passenger{" +
                "id=" + id +
                ", leaving=" + leaving +
                '}';
    }
}
