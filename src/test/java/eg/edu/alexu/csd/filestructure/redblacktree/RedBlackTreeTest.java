package eg.edu.alexu.csd.filestructure.redblacktree;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RedBlackTreeTest {

    private int black_height;

    private void generate_ascending_data(RedBlackTree<Integer,String> tree){
        for(int i = 0; i < 10; i++){
            tree.insert(i,Integer.toString(i));
            verify_red_property(tree.getRoot(),i);
            black_height = -1;
            dfs(tree.getRoot(), 0, i);
        }
    }

    private void generate_descending_data(RedBlackTree<Integer,String> tree){
        for(int i = 100; i > 70; i--){
            tree.insert(i, Integer.toString(i));
            verify_red_property(tree.getRoot(),i);
            black_height = -1;
            dfs(tree.getRoot(), 0, i);
        }
    }

    private void verify_red_property(INode<Integer, String> root, int value){
        if(root.getParent() != null){
            //assert that either parent is not red or this node itself is not red
            assertTrue(root.getParent().getColor() != INode.RED || root.getColor() != INode.RED, "red property not held at value " + value);
        }
        if(!root.isNull()){
            verify_red_property(root.getRightChild(),value);
            verify_red_property(root.getLeftChild(),value);
        }
    }

    @Test
    void insert_replace() {
        RedBlackTree<Integer, String> tree = new RedBlackTree<>();
        generate_ascending_data(tree);
        for(int i = 5; i < 10; i++){
            tree.insert(i,Integer.toString(i,2));
        }
        for(int i = 0; i < 5; i++){
            assertEquals(Integer.toString(i),tree.search(i));
        }
        for(int i = 5; i < 10; i++){
            assertEquals(Integer.toString(i,2),tree.search(i));
        }
    }

    @Test
    void insert(){
        RedBlackTree<Integer, String> tree = new RedBlackTree<>();
        generate_ascending_data(tree);
        generate_descending_data(tree);
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
        generate_descending_data(tree);
        generate_ascending_data(tree);
        assertEquals("5",tree.search(5));
        assertNull(tree.search(50));
        assertEquals("79", tree.search(79));
    }

    @Test
    void contains() {
        RedBlackTree<Integer,String> tree = new RedBlackTree<>();
        generate_descending_data(tree);
        generate_ascending_data(tree);
        for(int i = 0; i < 10; i++){
            assertTrue(tree.contains(i));
        }
        for(int i = 10; i < 71; i++){
            assertFalse(tree.contains(i));
        }
        for(int i = 71; i <= 100; i++){
            assertTrue(tree.contains(i));
        }
    }

    private void dfs(INode<Integer,String> root, int height, int value){
        if(root.getColor() == INode.BLACK){
            height++;
        }
        if(root.isNull()){
            if(black_height == -1){
                black_height = height;
            }else{
                assertEquals(black_height, height,"error after " + value);
            }
        }else{
            dfs(root.getLeftChild(),height, value);
            dfs(root.getRightChild(),height, value);
        }
    }

    @Test
    void testHeight() {
        RedBlackTree<Integer,String> tree = new RedBlackTree<>();
        generate_ascending_data(tree);
        generate_descending_data(tree);
        black_height = -1;
        dfs(tree.getRoot(),0, -1);
    }

    @Test
    void delete() {
        RedBlackTree<Integer,String> tree = new RedBlackTree<>();
        generate_ascending_data(tree);
        generate_descending_data(tree);

        for(int i = 0; i < 10; i++){
            assertTrue(tree.delete(i));
        }
        for(int i = 10; i < 71; i++){
            assertFalse(tree.delete(i));
        }
        for(int i = 71; i <= 100; i++){
            assertTrue(tree.delete(i));
        }
        assertTrue(tree.isEmpty());
        assertTrue(tree.getRoot().isNull());
    }
}