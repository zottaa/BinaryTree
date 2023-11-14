package binaryTree;

import java.util.ArrayList;
import java.util.List;

public class UserFactory {
    public ArrayList<String> getTypeNameList() {
        return new ArrayList<>(List.of("Point", "Fraction"));
    }

    public UserType getBuilderByName(String name){
        return switch (name) {
            case "Point" -> new Point();
            case "Fraction" -> new Fraction();
            default -> null;
        };

    }
}
