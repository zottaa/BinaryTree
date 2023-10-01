import java.io.InputStreamReader;
import java.util.Comparator;

public interface UserType {
    public String typeName();

    public Object create();

    public Object readValue(InputStreamReader in);

    public Object parseValue(String string);

    public Comparator<Object> getTypeComparator();
}

