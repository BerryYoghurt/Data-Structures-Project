package eg.edu.alexu.csd.filestructure.redblacktree;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RedBlackTreeTest {

    private void generate_ascending_data(RedBlackTree<Integer,String> tree){
        for(int i = -5; i < 6; i++){
            tree.insert(i,Integer.toString(i));
        }
    }

    private void generate_descending_data(RedBlackTree<Integer,String> tree){
        for(int i = 100; i > 70; i--){
            tree.insert(i, Integer.toString(i));
        }
    }

    private void generate_random_data(){

    }

    @Test
    void insert_replace() {
        RedBlackTree<Integer, String> tree = new RedBlackTree<>();
        generate_ascending_data(tree);
        for(int i = 0; i < 6; i++){
            tree.insert(i,Integer.toString(i,2));
        }
        for(int i = -5; i < 0; i++){
            assertEquals(Integer.toString(i),tree.search(i));
        }
        for(int i = 0; i < 6; i++){
            assertEquals(Integer.toString(i,2),tree.search(i));
        }
    }

    @Test
    void clear() {
        RedBlackTree<Integer, String> tree = new RedBlackTree<>();
        assertTrue(tree.isEmpty());
        generate_ascending_data(tree);
        assertFalse(tree.isEmpty());
        tree.clear();
        assertTrue(tree.isEmpty());

        generate_ascending_data(tree);
        assertFalse(tree.isEmpty());
    }

    @Test
    void search() {
        RedBlackTree<Integer,String> tree = new RedBlackTree<>();
        generate_ascending_data(tree);
        generate_descending_data(tree);
        assertEquals("5",tree.search(5));
        assertNull(tree.search(50));
        assertEquals("79", tree.search(79));
    }

    @Test
    void contains() {
        RedBlackTree<Integer,String> tree = new RedBlackTree<>();
        generate_descending_data(tree);
        generate_ascending_data(tree);
        for(int i = -5; i < 6; i++){
            assertTrue(tree.contains(i));
        }
        for(int i = 6; i < 71; i++){
            assertFalse(tree.contains(i));
        }
        for(int i = 71; i <= 100; i++){
            assertTrue(tree.contains(i));
        }
    }

    @Test
    void insert() {
    }

    @Test
    void delete() {
    }
}