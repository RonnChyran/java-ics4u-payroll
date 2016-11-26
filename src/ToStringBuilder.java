public class ToStringBuilder {

    private StringBuilder sb;
    public ToStringBuilder() {
        this.sb = new StringBuilder();
    }

    public ToStringBuilder prop(String name, Object s) {
        this.sb.append(name + ": " + s.toString() +"\n");
        return this;
    }


    public ToStringBuilder prop(String name, int i) {
        this.sb.append(name + ": " + i +"\n");
        return this;
    }

    @Override
    public String toString() {
        return sb.toString();
    }
}