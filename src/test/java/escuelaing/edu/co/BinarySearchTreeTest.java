package escuelaing.edu.co;

import org.junit.jupiter.api.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class BinarySearchTreeTest {
    private BinarySearchTree<Integer> bst;

    @BeforeEach
    void setUp() {
        bst = new BinarySearchTree<>();
    }

    @Nested
    @DisplayName("Basic Operations Tests")
    class BasicOperationsTests {
        @Test
        @DisplayName("New BST should be empty")
        void newBstShouldBeEmpty() {
            assertTrue(bst.isEmpty());
            assertEquals(0, bst.size());
        }

        @Test
        @DisplayName("Adding elements should increase size")
        void addingElementsShouldIncreaseSize() {
            bst.add(50);
            assertEquals(1, bst.size());
            assertFalse(bst.isEmpty());
        }

        @Test
        @DisplayName("Should not add null elements")
        void shouldNotAddNullElements() {
            assertThrows(NullPointerException.class, () -> bst.add(null));
        }

        @Test
        @DisplayName("Should maintain BST properties after multiple insertions")
        void shouldMaintainBstProperties() {
            int[] values = {50, 30, 70, 20, 40, 60, 80};
            Arrays.stream(values).forEach(bst::add);

            List<Integer> inOrder = new ArrayList<>();
            bst.inOrderTraversal(inOrder);

            // Verify the list is sorted (BST property)
            assertTrue(isSorted(inOrder));
        }
    }

    @Nested
    @DisplayName("Search Operation Tests")
    class SearchOperationTests {
        @BeforeEach
        void setUpSearchTests() {
            int[] values = {50, 30, 70, 20, 40, 60, 80};
            Arrays.stream(values).forEach(bst::add);
        }

        @Test
        @DisplayName("Should find existing elements")
        void shouldFindExistingElements() {
            assertEquals(Integer.valueOf(50), bst.search(50));
            assertEquals(Integer.valueOf(20), bst.search(20));
            assertEquals(Integer.valueOf(80), bst.search(80));
        }

        @Test
        @DisplayName("Should return null for non-existing elements")
        void shouldReturnNullForNonExistingElements() {
            assertNull(bst.search(100));
            assertNull(bst.search(10));
        }

        @Test
        @DisplayName("Contains should work correctly")
        void containsShouldWorkCorrectly() {
            assertTrue(bst.contains(50));
            assertTrue(bst.contains(20));
            assertFalse(bst.contains(100));
        }
    }

    @Nested
    @DisplayName("Deletion Tests")
    class DeletionTests {
        @BeforeEach
        void setUpDeletionTests() {
            int[] values = {50, 30, 70, 20, 40, 60, 80};
            Arrays.stream(values).forEach(bst::add);
        }

        @Test
        @DisplayName("Should remove leaf nodes")
        void shouldRemoveLeafNodes() {
            assertTrue(bst.remove(20));
            assertFalse(bst.contains(20));
            assertEquals(6, bst.size());
        }

        @Test
        @DisplayName("Should remove nodes with one child")
        void shouldRemoveNodesWithOneChild() {
            assertTrue(bst.remove(40));
            assertTrue(bst.remove(30));
            assertFalse(bst.contains(30));
            assertTrue(bst.contains(20));
        }

        @Test
        @DisplayName("Should remove nodes with two children")
        void shouldRemoveNodesWithTwoChildren() {
            assertTrue(bst.remove(30));
            assertFalse(bst.contains(30));
            assertTrue(bst.contains(20));
            assertTrue(bst.contains(40));
        }

        @Test
        @DisplayName("Should handle removing non-existing elements")
        void shouldHandleRemovingNonExistingElements() {
            assertFalse(bst.remove(100));
            assertEquals(7, bst.size());
        }
    }

    @Nested
    @DisplayName("Traversal Tests")
    class TraversalTests {
        private int[] values = {50, 30, 70, 20, 40, 60, 80};

        @BeforeEach
        void setUpTraversalTests() {
            Arrays.stream(values).forEach(bst::add);
        }

        @Test
        @DisplayName("In-order traversal should return sorted elements")
        void inOrderTraversalShouldReturnSortedElements() {
            List<Integer> result = new ArrayList<>();
            bst.inOrderTraversal(result);

            int[] expected = Arrays.stream(values).sorted().toArray();
            assertArrayEquals(expected, result.stream().mapToInt(Integer::intValue).toArray());
        }

        @Test
        @DisplayName("Level-order traversal should work correctly")
        void levelOrderTraversalShouldWorkCorrectly() {
            List<Integer> result = bst.levelOrderTraversal();

            // The expected order for a balanced BST with root 50
            Integer[] expected = {50, 30, 70, 20, 40, 60, 80};
            assertArrayEquals(expected, result.toArray(new Integer[0]));
        }

        @Test
        @DisplayName("Pre-order traversal should work correctly")
        void preOrderTraversalShouldWorkCorrectly() {
            List<Integer> result = new ArrayList<>();
            bst.preOrderTraversal(result);
            Integer[] expected = {50, 30, 20, 40, 70, 60, 80};
            assertArrayEquals(expected, result.toArray(new Integer[0]));
        }

        @Test
        @DisplayName("Post-order traversal should work correctly")
        void postOrderTraversalShouldWorkCorrectly() {
            List<Integer> result = new ArrayList<>();
            bst.postOrderTraversal(result);
            Integer[] expected = {20, 40, 30, 60, 80, 70, 50};
            assertArrayEquals(expected, result.toArray(new Integer[0]));
        }
    }

    @Nested
    @DisplayName("Min/Max Tests")
    class MinMaxTests {
        @Test
        @DisplayName("Should find correct min and max values")
        void shouldFindCorrectMinAndMaxValues() {
            int[] values = {50, 30, 70, 20, 40, 60, 80};
            Arrays.stream(values).forEach(bst::add);

            assertEquals(Integer.valueOf(20), bst.findMin());
            assertEquals(Integer.valueOf(80), bst.findMax());
        }

        @Test
        @DisplayName("Min/Max should be null for empty tree")
        void minMaxShouldBeNullForEmptyTree() {
            assertNull(bst.findMin());
            assertNull(bst.findMax());
        }
    }

    @Nested
    @DisplayName("Collection Interface Tests")
    class CollectionInterfaceTests {
        @Test
        @DisplayName("Should work with Collection methods")
        void shouldWorkWithCollectionMethods() {
            Collection<Integer> numbers = Arrays.asList(50, 30, 70);

            assertTrue(bst.addAll(numbers));
            assertEquals(3, bst.size());

            assertTrue(bst.containsAll(numbers));

            Collection<Integer> subset = Arrays.asList(30, 70);
            assertTrue(bst.retainAll(subset));
            assertEquals(2, bst.size());

            Object[] array = bst.toArray();
            assertEquals(2, array.length);

            Integer[] typedArray = bst.toArray(new Integer[0]);
            assertEquals(2, typedArray.length);
        }

        @Test
        @DisplayName("Iterator should work correctly")
        void iteratorShouldWorkCorrectly() {
            int[] values = {50, 30, 70};
            Arrays.stream(values).forEach(bst::add);

            Iterator<Integer> iterator = bst.iterator();
            List<Integer> iteratedValues = new ArrayList<>();

            while (iterator.hasNext()) {
                iteratedValues.add(iterator.next());
            }

            assertEquals(3, iteratedValues.size());
            assertTrue(isSorted(iteratedValues));
        }
    }

    @Test
    @DisplayName("Tree should maintain balance")
    void treeShouldMaintainBalance() {
        int[] values = {50, 30, 70, 20, 40, 60, 80};
        Arrays.stream(values).forEach(bst::add);

        assertTrue(bst.isBalanced());
    }

    // Helper method to check if a list is sorted
    private boolean isSorted(List<Integer> list) {
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i - 1) > list.get(i)) {
                return false;
            }
        }
        return true;
    }

    @Nested
    @DisplayName("Height Tests")
    class HeightTests {
        @Test
        @DisplayName("Empty tree should have height 0")
        void emptyTreeShouldHaveHeightZero() {
            assertEquals(0, bst.height());
        }

        @Test
        @DisplayName("Single node tree should have height 1")
        void singleNodeTreeShouldHaveHeightOne() {
            bst.add(50);
            assertEquals(1, bst.height());
        }

        @Test
        @DisplayName("Should calculate correct height for larger trees")
        void shouldCalculateCorrectHeightForLargerTrees() {
            int[] values = {50, 30, 70, 20, 40, 60, 80};
            Arrays.stream(values).forEach(bst::add);

            assertEquals(3, bst.height()); // The height of the balanced BST
        }
    }

    @Nested
    @DisplayName("Node Property Tests")
    class NodePropertyTests {
        @BeforeEach
        void setUpNodePropertyTests() {
            int[] values = {50, 30, 70, 20, 40, 60, 80};
            Arrays.stream(values).forEach(bst::add);
        }

        @Test
        @DisplayName("Root node should be correct")
        void rootNodeShouldBeCorrect() {
            assertEquals(Integer.valueOf(50), bst.getRoot());
        }

        @Test
        @DisplayName("Should return correct number of nodes")
        void shouldReturnCorrectNumberOfNodes() {
            assertEquals(7, bst.getNumberOfNodes());
        }
    }

    @Nested
    @DisplayName("String Tests")
    class StringTests {
        private BinarySearchTree<String> stringBst;

        @BeforeEach
        void setUp() {
            stringBst = new BinarySearchTree<>();
        }

        @Test
        @DisplayName("Should add and find strings correctly")
        void shouldAddAndFindStrings() {
            stringBst.add("banana");
            stringBst.add("apple");
            stringBst.add("cherry");

            assertEquals("apple", stringBst.search("apple"));
            assertEquals("banana", stringBst.search("banana"));
            assertEquals("cherry", stringBst.search("cherry"));
            assertNull(stringBst.search("date")); // Non-existing
        }

        @Test
        @DisplayName("Should remove strings correctly")
        void shouldRemoveStrings() {
            stringBst.add("banana");
            stringBst.add("apple");
            stringBst.add("cherry");

            stringBst.printTreePyramid();

            assertTrue(stringBst.remove("banana"));
            assertFalse(stringBst.contains("banana"));
        }

        @Test
        @DisplayName("In-order traversal should return sorted strings")
        void inOrderTraversalShouldReturnSortedStrings() {
            String[] values = {"banana", "apple", "cherry"};
            Arrays.stream(values).forEach(stringBst::add);

            List<String> result = new ArrayList<>();
            stringBst.inOrderTraversal(result);
            String[] expected = Arrays.stream(values).sorted().toArray(String[]::new);
            assertArrayEquals(expected, result.toArray(new String[0]));
        }
    }

    @Nested
    @DisplayName("Double Tests")
    class DoubleTests {
        private BinarySearchTree<Double> doubleBst;

        @BeforeEach
        void setUp() {
            doubleBst = new BinarySearchTree<>();
        }

        @Test
        @DisplayName("Should add and find doubles correctly")
        void shouldAddAndFindDoubles() {
            doubleBst.add(2.5);
            doubleBst.add(1.5);
            doubleBst.add(3.5);

            assertEquals(Double.valueOf(1.5), doubleBst.search(1.5));
            assertEquals(Double.valueOf(2.5), doubleBst.search(2.5));
            assertEquals(Double.valueOf(3.5), doubleBst.search(3.5));
            assertNull(doubleBst.search(4.5)); // Non-existing
        }

        @Test
        @DisplayName("Should remove doubles correctly")
        void shouldRemoveDoubles() {
            doubleBst.add(2.5);
            doubleBst.add(1.5);
            doubleBst.add(3.5);

            assertTrue(doubleBst.remove(2.5));
            assertFalse(doubleBst.contains(2.5));
        }

        @Test
        @DisplayName("In-order traversal should return sorted doubles")
        void inOrderTraversalShouldReturnSortedDoubles() {
            Double[] values = {2.5, 1.5, 3.5};
            Arrays.stream(values).forEach(doubleBst::add);

            List<Double> result = new ArrayList<>();
            doubleBst.inOrderTraversal(result);
            Double[] expected = Arrays.stream(values).sorted().toArray(Double[]::new);
            assertArrayEquals(expected, result.toArray(new Double[0]));
        }
    }

    @Nested
    @DisplayName("Extended Tests with 25 Elements")
    class ExtendedTests {
        private BinarySearchTree<Integer> intBst;

        @BeforeEach
        void setUp() {
            intBst = new BinarySearchTree<>();
        }

        @Test
        @DisplayName("Integer BST should handle 25 elements")
        void integerBstShouldHandle25Elements() {
            int[] values = { 1,3,4,5,6,7,8,9,15,18};

            for (int value : values) {
                intBst.add(value);
            }

            intBst.printTreePyramid();

            assertEquals(10, intBst.size());

            List<Integer> inOrderResult = new ArrayList<>();
            intBst.inOrderTraversal(inOrderResult);
            assertTrue(isSorted(inOrderResult));

            for (int value : values) {
                assertTrue(intBst.contains(value));
            }

            assertFalse(intBst.contains(100)); // Non-existing
        }


    }




}
