import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.Comparator;

public interface UserType extends Cloneable, Serializable {
    public String typeName();

    public Object create();

    public Object readValue(InputStreamReader in);

    public Object parseValue(String string);

    public Comparator<Object> getTypeComparator();
}

class Point implements UserType {

    private final int x;
    private final int y;

    Point() {
        this.x = 0;
        this.y = 0;
    }

    Point(int _x, int _y) {
        this.x = _x;
        this.y = _y;
    }

    @Override
    public String typeName() {
        return "Point";
    }

    @Override
    public Object create() {
        return new Point();
    }

    @Override
    public Object readValue(InputStreamReader in) {
        try {
            BufferedReader reader = new BufferedReader(in);

            String line = reader.readLine();

            return parseValue(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Object parseValue(String string) {

        String[] parts = string.split(",");

        if (parts.length == 2) {
            try {
                int x = Integer.parseInt(parts[0]);
                int y = Integer.parseInt(parts[1]);

                return new Point(x, y);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public Comparator<Object> getTypeComparator() {
        return new Comparator<Object>() {

            @Override
            public int compare(Object o1, Object o2) {
                if (o1 instanceof Point && o2 instanceof Point) {
                    Point p1 = (Point) o1;
                    Point p2 = (Point) o2;

                    double distance1 = Math.sqrt(Math.pow(p1.x, 2) + Math.pow(p1.y, 2));
                    double distance2 = Math.sqrt(Math.pow(p2.x, 2) + Math.pow(p2.y, 2));

                    return Double.compare(distance1, distance2);
                } else {
                    return 0;
                }
            }
        };
    }

    @Override
    protected Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}
class Fraction implements UserType {

    private final int numerator;
    private final int denominator;
    private final int intPart;

    Fraction() {
        this.intPart = 0;
        this.denominator = 1;
        this.numerator = 0;
    }

    Fraction(int intPart, int numerator, int denominator) {
        if (denominator == 0) {
            throw new ArithmeticException("Denominator cannot be zero");
        }
        this.intPart = intPart;
        this.numerator = numerator;
        this.denominator = denominator;
    }

    Fraction(int numerator, int denominator) {
        if (denominator == 0) {
            throw new ArithmeticException("Denominator cannot be zero");
        }
        if (numerator < denominator) {
            this.numerator = numerator;
            this.denominator = denominator;
            this.intPart = 0;
        } else {
            this.intPart = numerator / denominator;
            this.numerator = numerator - (denominator * intPart);
            this.denominator = denominator;
        }
    }

    @Override
    public String typeName() {
        return "Fraction";
    }

    @Override
    public Object create() {
        return new Fraction();
    }

    @Override
    public Object readValue(InputStreamReader in) {
        try {
            BufferedReader reader = new BufferedReader(in);

            String line = reader.readLine();

            return parseValue(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Object parseValue(String string) {
        String[] parts = string.split(",");
        try {
            if (parts.length == 2) {

                int numerator = Integer.parseInt(parts[0]);
                int denominator = Integer.parseInt(parts[1]);

                return new Fraction(numerator, denominator);

            } else if (parts.length == 3) {
                int intPart = Integer.parseInt(parts[0]);
                int numerator = Integer.parseInt(parts[1]);
                int denominator = Integer.parseInt(parts[2]);

                return new Fraction(intPart, numerator, denominator);
            }
        } catch(NumberFormatException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Comparator<Object> getTypeComparator() {
        return new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                if (o1 instanceof Fraction && o2 instanceof Fraction) {
                    Fraction f1 = (Fraction)o1;
                    Fraction f2 = (Fraction)o2;

                    if (f1.intPart != f2.intPart) {
                        return Integer.compare(f1.intPart, f2.intPart);
                    } else {
                        return Integer.compare(f1.numerator * f2.denominator, f2.numerator * f1.denominator);
                    }
                } else {
                    return 0;
                }
            }
        };
    }

    @Override
    protected Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}

