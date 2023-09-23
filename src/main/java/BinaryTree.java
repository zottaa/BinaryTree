import java.security.InvalidAlgorithmParameterException;

public interface BinaryTree {
    boolean add(UserType item);

    boolean delete(int index);

    UserType at(int index);

    void balance();

    abstract class Abstract implements BinaryTree {
        public class Node {
            public Node left;
            public Node right;
            public UserType item;
            public int weight;

            Node(UserType item) {
                this.item = item;
                this.left = null;
                this.right = null;
                int weight = 1;
            }

            Node(UserType item, Node left, Node right) {
                this.item = item;
                this.left = left;
                this.right = right;
                this.weight = left.weight + right.weight + 1;
            }
        }

        private Node root;
        private int size;

        @Override
        public boolean add(UserType item) {
            if (root == null) {
                this.root = new Node(item);
                this.size++;
                return true;
            }
            return add(root, item);
        }

        private boolean add(Node current, UserType item) {
            if (current.item == item) {
                restoreWeights(root, item);
                return false;
            }
            current.weight += 1;

            if (current.item > item) {
                if (current.left == null) {
                    current.left = new Node(item);
                    size++;
                    return true;
                } else {
                    return add(current.left, item);
                }
            } else {
                if (current.right == null) {
                    current.right = new Node(item);
                    size++;
                    return true;
                } else {
                    return add(current.right, item);
                }
            }
        }

        private void restoreWeights(Node current, UserType item) {
            if (current == null || current.item == item) {
                return;
            }

            current.weight -= 1;

            if (current.item > item) {
                restoreWeights(current.left, item);
            } else {
                restoreWeights(current.right, item);
            }
        }

        @Override
        public boolean delete(int index) {
            if (root == null)
                return false;
            return delete(root, index, null);
        }

        private boolean delete(Node current, int index, Node previous) {

        }


        @Override
        public UserType at(int index) {
            if (index < 0 || index <= size) {
                throw new IndexOutOfBoundsException();
            }

            int currentIndex = this.root.left != null ? this.root.left.weight : 0;
            if (currentIndex == index)
                return this.root.item;

            return currentIndex < index ?
                    at(this.root.right, index - currentIndex - 1) :
                    at(this.root.left, index);
        }

        private UserType at(Node current, int index) {
            int currentIndex = this.root.left != null ? this.root.left.weight : 0;
            if (currentindex == index)
                return this.root.item;

            return currentIndex < index ?
                    at(this.root.right, index - currentIndex - 1) :
                    at(this.root.left, index);
        }

        @Override
        public void balance() {

        }
    }

    class Base extends Abstract {

    }
}
