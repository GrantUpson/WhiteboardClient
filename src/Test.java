import java.io.Serializable;
import java.rmi.Remote;

public class Test implements Remote, Serializable {
    private String name;

    public Test(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void execute() {
        System.out.println(name);
    }
}
