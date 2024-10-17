package escuelaing.edu.co;

import java.util.*;

public class BinarySearchTree<T extends Comparable<T>> implements Collection<T> {
    private Node<T> root;
    private int size;

    public static class Node<T> {
        private T value;
        private Node<T> left, right;
        private int height;

        Node(T value) {
            this.value = value;
            this.height = 1;
        }

        public T getValue() {
            return value;
        }

        public Node<T> getLeft() {
            return left;
        }

        public Node<T> getRight() {
            return right;
        }

        public int getHeight() {
            return height;
        }
    }

    @Override
    public boolean add(T value) {
        if (value == null) {
            throw new NullPointerException("Value cannot be null");
        }
        int initialSize = size;
        root = insert(root, value);
        return size > initialSize;
    }

    private Node insert(Node node, T value) {
        if (node == null) {
            size++;
            return new Node(value);
        }

        int compareResult = value.compareTo((T) node.value);
        if (compareResult < 0) {
            node.left = insert(node.left, value);
        } else if (compareResult > 0) {
            node.right = insert(node.right, value);
        }

        node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;
        return node;
    }

    public T search(T value) {
        Node result = searchNode(root, value);
        return result != null ? (T) result.value : null;
    }

    private Node searchNode(Node node, T value) {
        if (node == null || value.compareTo((T) node.value) == 0) {
            return node;
        }
        return value.compareTo((T) node.value) < 0
                ? searchNode(node.left, value)
                : searchNode(node.right, value);
    }

    @Override
    public boolean remove(Object obj) {
        if (!(obj instanceof Comparable)) {
            return false;
        }
        @SuppressWarnings("unchecked")
        T value = (T) obj;
        int initialSize = size;
        root = delete(root, value);
        return size < initialSize;
    }

    private Node delete(Node node, T value) {
        if (node == null) {
            return null;
        }

        int compareResult = value.compareTo((T) node.value);
        if (compareResult < 0) {
            node.left = delete(node.left, value);
        } else if (compareResult > 0) {
            node.right = delete(node.right, value);
        } else {
            if (node.left == null) {
                size--;
                return node.right;
            } else if (node.right == null) {
                size--;
                return node.left;
            }

            Node successor = findMin(node.right);
            node.value = successor.value;
            node.right = delete(node.right, (T) successor.value);
        }

        node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;
        return node;
    }

    public void inOrderTraversal(List<T> result) {
        inOrderTraversal(root, result);
    }

    private void inOrderTraversal(Node node, List<T> result) {
        if (node != null) {
            inOrderTraversal(node.left, result);
            result.add((T) node.value);
            inOrderTraversal(node.right, result);
        }
    }

    public void preOrderTraversal(List<T> result) {
        preOrderTraversal(root, result);
    }

    private void preOrderTraversal(Node node, List<T> result) {
        if (node != null) {
            result.add((T) node.value);
            preOrderTraversal(node.left, result);
            preOrderTraversal(node.right, result);
        }
    }

    public T getRoot() {
        return root != null ? root.getValue() : null;
    }


    public void postOrderTraversal(List<T> result) {
        postOrderTraversal(root, result);
    }

    private void postOrderTraversal(Node node, List<T> result) {
        if (node != null) {
            postOrderTraversal(node.left, result);
            postOrderTraversal(node.right, result);
            result.add((T) node.value);
        }
    }

    public T findMin() {
        if (root == null) return null;
        return (T) findMin(root).value;
    }

    private Node findMin(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    public T findMax() {
        if (root == null) return null;
        return (T) findMax(root).value;
    }

    private Node findMax(Node node) {
        while (node.right != null) {
            node = node.right;
        }
        return node;
    }

    public int height() {
        return getHeight(root);
    }

    private int getHeight(Node node) {
        return node == null ? 0 : node.height;
    }

    public boolean isBalanced() {
        return isBalanced(root);
    }

    private boolean isBalanced(Node node) {
        if (node == null) {
            return true;
        }

        int balanceFactor = Math.abs(getHeight(node.left) - getHeight(node.right));
        return balanceFactor <= 1 && isBalanced(node.left) && isBalanced(node.right);
    }

    public List<T> levelOrderTraversal() {
        List<T> result = new ArrayList<>();
        if (root == null) {
            return result;
        }

        Queue<Node> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            result.add((T) current.value);

            if (current.left != null) {
                queue.offer(current.left);
            }
            if (current.right != null) {
                queue.offer(current.right);
            }
        }

        return result;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        if (!(o instanceof Comparable)) {
            return false;
        }
        @SuppressWarnings("unchecked")
        T value = (T) o;
        return search(value) != null;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private final List<T> elements = new ArrayList<>();
            private int index = 0;

            {
                inOrderTraversal(elements);
            }

            @Override
            public boolean hasNext() {
                return index < elements.size();
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return elements.get(index++);
            }
        };
    }

    @Override
    public Object[] toArray() {
        List<T> list = new ArrayList<>();
        inOrderTraversal(list);
        return list.toArray();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <E> E[] toArray(E[] a) {
        List<T> list = new ArrayList<>();
        inOrderTraversal(list);
        return list.toArray(a);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object element : c) {
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        boolean modified = false;
        for (T element : c) {
            if (add(element)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object element : c) {
            if (remove(element)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        List<T> toRemove = new ArrayList<>();
        for (T element : this) {
            if (!c.contains(element)) {
                toRemove.add(element);
            }
        }
        return removeAll(toRemove);
    }

    // Añade estos métodos a tu clase BinarySearchTree
    public void printTreePyramid() {
        if (root == null) {
            System.out.println("Árbol vacío");
            return;
        }
        int maxLevel = height();
        System.out.println("\nÁrbol BST (Vista Pirámide):");
        printNodeInternal(Collections.singletonList(root), 1, maxLevel);
    }

    private void printNodeInternal(List<Node> nodes, int level, int maxLevel) {
        if (nodes.isEmpty() || isAllElementsNull(nodes)) return;

        int floor = maxLevel - level;
        int endgeLines = (int) Math.pow(2, (Math.max(floor - 1, 0)));
        int firstSpaces = (int) Math.pow(2, (floor)) - 1;
        int betweenSpaces = (int) Math.pow(2, (floor + 1)) - 1;

        printWhitespaces(firstSpaces);

        List<Node> newNodes = new ArrayList<>();
        for (Node node : nodes) {
            if (node != null) {
                System.out.print(node.value);
                newNodes.add(node.left);
                newNodes.add(node.right);
            } else {
                newNodes.add(null);
                newNodes.add(null);
                System.out.print(" ");
            }
            printWhitespaces(betweenSpaces);
        }
        System.out.println();

        for (int i = 1; i <= endgeLines; i++) {
            for (Node node : nodes) {
                printWhitespaces(firstSpaces - i);
                if (node == null) {
                    printWhitespaces(endgeLines + endgeLines + i + 1);
                    continue;
                }

                if (node.left != null) {
                    System.out.print("/");
                } else {
                    printWhitespaces(1);
                }

                printWhitespaces(i + i - 1);

                if (node.right != null) {
                    System.out.print("\\");
                } else {
                    printWhitespaces(1);
                }

                printWhitespaces(endgeLines + endgeLines - i);
            }
            System.out.println();
        }

        printNodeInternal(newNodes, level + 1, maxLevel);
    }

    private void printWhitespaces(int count) {
        for (int i = 0; i < count; i++) {
            System.out.print(" ");
        }
    }

    private boolean isAllElementsNull(List<Node> list) {
        for (Node node : list) {
            if (node != null) return false;
        }
        return true;
    }

    public int getNumberOfNodes() {
        return getNumberOfNodes(root);
    }

    private int getNumberOfNodes(Node<T> node) {
        if (node == null) {
            return 0;
        }
        return 1 + getNumberOfNodes(node.left) + getNumberOfNodes(node.right);
    }



}
