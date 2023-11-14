package binaryTree;

import java.io.*;

public interface Serialize {
    public void serialize(BinaryTree tree, String filename, String type);

    public BinaryTree deserialize(String filename);

    abstract class Abstract implements Serialize {
        @Override
        public void serialize(BinaryTree tree, String filename, String type) {
            try (PrintWriter writer = new PrintWriter(filename)) {
                writer.println(type);
                tree.forEachFromRoot(new ElementProcessor<UserType>() {
                    @Override
                    public void toDo(UserType v) {
                        writer.print(" ");
                        writer.print(v);
                    }
                });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        @Override
        public BinaryTree deserialize(String filename) {
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filename))) {
                String type = bufferedReader.readLine();
                UserFactory userFactory = new UserFactory();
                if (!userFactory.getTypeNameList().contains(type)) {
                    throw new IllegalArgumentException("Wrong type");
                }

                String line;
                BinaryTree tree = new BinaryTree.Base();
                while ((line = bufferedReader.readLine()) != null) {
                    String[] items = line.split(" ");

                    for (String item : items) {
                        UserType builder = userFactory.getBuilderByName(type);
                        Object object = builder.parseValue(item);
                        if (object != null) {
                            tree.add((UserType) object);
                        }
                    }
                }
                return tree;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

    }

    class Base extends Abstract {

    }
}
