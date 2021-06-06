package eg.edu.alexu.csd.filestructure.redblacktree;

import javax.management.RuntimeErrorException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class RedBlackTree<T extends Comparable<T>,V> implements IRedBlackTree<T ,V> {
    private INode<T,V> root;

    public RedBlackTree(){
        root = new Node<>(null); //null node
    }
    @Override
    public INode<T , V> getRoot() {
        return root;
    }

    @Override
    public boolean isEmpty() {
        return root.isNull();
    }

    @Override
    public void clear() {
        root = new Node<>(null); //null node
    }

    private void handle_null_input(Object o){
        if(o == null)
            throw new RuntimeErrorException(new Error("Key cannot be null"));
    }

    /**returns the null node which should contain the key if the key is not found*/
    private INode<T,V> find_node(INode<T,V> sub_root, T key){
        if(sub_root.isNull()){
            return sub_root;
        }else{
            int cmp = sub_root.getKey().compareTo(key);
            if(cmp == 0){
                return sub_root;
            }else if(cmp > 0){
                //the target key is less than the key in sub_root
                return find_node(sub_root.getLeftChild(),key);
            }else{
                return find_node(sub_root.getRightChild(),key);
            }
        }
    }

    @Override
    public V search(T key) {
        handle_null_input(key);
        return find_node(root,key).getValue();
    }

    @Override
    public boolean contains(T key) {
        handle_null_input(key);
        //return true if the node that should contain this key is not the null node
        return !find_node(root, key).isNull();
    }

    /**flip the right child above the parent*/
    private void left_rotate(INode<T,V> parent){
        INode<T,V> grandparent = parent.getParent(), rightChild = parent.getRightChild();
        rightChild.setParent(grandparent);
        /*deal with the left subtree of rightChild*/
        parent.setParent(rightChild);
        parent.setRightChild(rightChild.getLeftChild());
        parent.getRightChild().setParent(parent);
        rightChild.setLeftChild(parent);

        /*deal with the grandparent*/
        if(grandparent != null){
            if(grandparent.getRightChild() == parent){
                grandparent.setRightChild(rightChild);
            }else{
                grandparent.setLeftChild(rightChild);
            }
        }else{//parent was the root
            root = rightChild;
        }
        /*six pointers modified in total*/
    }

    /**flip the left child above the parent*/
    private void right_rotate(INode<T,V> parent){
        INode<T,V> grandparent = parent.getParent(), leftChild = parent.getLeftChild();
        leftChild.setParent(grandparent);
        /*deal with the right subtree of leftChild*/
        parent.setParent(leftChild);
        parent.setLeftChild(leftChild.getRightChild());
        parent.getLeftChild().setParent(parent);
        leftChild.setRightChild(parent);

        /*deal with the grandparent*/
        if(grandparent != null){
            if(grandparent.getRightChild() == parent){
                grandparent.setRightChild(leftChild);
            }else{
                grandparent.setLeftChild(leftChild);
            }
        }else{
            root = leftChild;
        }
        /*six pointers modified in total*/
    }

    private void general_resolve_double_red(INode<T,V> problematic_node,
                                            UnaryOperator<INode<T,V>> getRightChild,
                                            UnaryOperator<INode<T,V>> getLeftChild,
                                            Consumer<INode<T,V>> right_rotate,
                                            Consumer<INode<T,V>> left_rotate){
        INode<T,V> parent = problematic_node.getParent(), grandparent = parent.getParent(), uncle = getRightChild.apply(grandparent);
        if(uncle.getColor() == INode.BLACK){
            if(getRightChild.apply(parent) == problematic_node){
                //left right
                left_rotate.accept(parent);
                /*maintain the relationships (problematic node is the lower node*/
                problematic_node = parent;
                parent = problematic_node.getParent();
            }
            right_rotate.accept(grandparent);
            parent.setColor(INode.BLACK);
            grandparent.setColor(INode.RED);
        }else{
            uncle.setColor(INode.BLACK);
            parent.setColor(INode.BLACK);
            grandparent.setColor(INode.RED);
            resolve_double_red(grandparent);
        }

    }

    /**@param problematic_node must be red, may or may not be double red*/
    private void resolve_double_red(INode<T,V> problematic_node){
        INode<T,V> parent = problematic_node.getParent();

        if(parent == null){//no parent, problematic_node is the root
            problematic_node.setColor(INode.BLACK);
        }else if(parent.getColor() == INode.RED){
            //if the parent is black, there is nothing to do
            //because there is no problem. The problem arises when the parent is red
            INode<T,V> grandparent = parent.getParent(), uncle;
            //grandparent is, by definition, never null because parent is red

            if(grandparent.getLeftChild() == parent){
                general_resolve_double_red(problematic_node,
                        INode::getRightChild,
                        INode::getLeftChild,
                        this::right_rotate,
                        this::left_rotate);
            }else{ //parent is right child of grandparent
                general_resolve_double_red(problematic_node,
                        INode::getLeftChild,
                        INode::getRightChild,
                        this::left_rotate,
                        this::right_rotate);
            }
        }
    }

    @Override
    public void insert(T key, V value) {
        handle_null_input(key);
        handle_null_input(value);
        INode<T,V> dest_node = find_node(root, key);
        if(!dest_node.isNull()){//key already there
            dest_node.setValue(value);
        }else{
            dest_node.setKey(key);
            dest_node.setValue(value);
            dest_node.setColor(INode.RED); //by default add a red node
            //set null nodes as children
            dest_node.setLeftChild(new Node<>(dest_node));
            dest_node.setRightChild(new Node<>(dest_node));
            //no need to set parent
            resolve_double_red(dest_node);//correct call because dest_node is red
        }
    }

    void general_resolve_double_black(INode<T,V> problematic_node,
                                      UnaryOperator<INode<T,V>> getRightChild,
                                      UnaryOperator<INode<T,V>> getLeftChild,
                                      Consumer<INode<T,V>> right_rotate,
                                      Consumer<INode<T,V>> left_rotate){
        INode<T,V> parent = problematic_node.getParent(), sibling;
        sibling = getRightChild.apply(parent);
        if(sibling.getColor() == INode.RED){
            left_rotate.accept(parent);
            parent.setColor(INode.RED);
            sibling.setColor(INode.BLACK);
            resolve_double_black(problematic_node); //will be a base case
        }else{
            //sibling is black TODO sibling could also be NullNode???
            if(sibling.isNull()){
                System.out.println("Should not happen");
            }
            if(getRightChild.apply(sibling).getColor() == INode.BLACK && getLeftChild.apply(sibling).getColor() == INode.BLACK){
                //with 2 black children
                sibling.setColor(INode.RED);
                if(parent.getColor() == INode.BLACK){
                    resolve_double_black(parent); //correct call
                }else{
                    parent.setColor(INode.BLACK);
                }
            }else{
                //with at least one red child
                if(getRightChild.apply(sibling).getColor() == INode.BLACK){
                    //with exacly one red child (left child)
                    right_rotate.accept(sibling);
                    sibling.setColor(INode.RED);
                    sibling = getRightChild.apply(parent);//new sibling now
                    sibling.setColor(INode.BLACK); // was originally red, which is why we are here is the first place
                }
                //now we are sure that the right child of sibling is red,
                // and sibling is black
                left_rotate.accept(parent);
                getRightChild.apply(sibling).setColor(INode.BLACK);
                if(parent.getColor() == INode.RED){
                    sibling.setColor(INode.RED);
                    parent.setColor(INode.BLACK);
                }
            }
        }
    }

    /**@param problematic_node must be a black node*/
    private void resolve_double_black(INode<T,V> problematic_node){
        INode<T,V> parent = problematic_node.getParent();
        //if the problematic_node is the root, nothing has to be done
        if(parent == null){
            root = problematic_node;//TODO sure?
            return;
        }
        if(parent.getLeftChild() == problematic_node){
            //I have written this code and decided it would be too tedious to re-write it for the other case, so I will use functional interfaces
            general_resolve_double_black(problematic_node,
                    INode::getRightChild,
                    INode::getLeftChild,
                    this::right_rotate,
                    this::left_rotate);
        }else{
            general_resolve_double_black(problematic_node,
                    INode::getLeftChild,
                    INode::getRightChild,
                    this::left_rotate,
                    this::right_rotate);
        }

    }

    @Override
    public boolean delete(T key) {
        handle_null_input(key);
        INode<T,V> target_node = find_node(root,key);
        if(target_node.isNull()){
            return false; //didn't find the value
        }

        if(!target_node.getRightChild().isNull() && !target_node.getLeftChild().isNull()){
            //if node has 2 children, replace with predecessor
            /*find in-order predecessor*/
            INode<T,V> predecessor = target_node.getLeftChild();
            while(!predecessor.getRightChild().isNull()){
                predecessor = predecessor.getRightChild();
            }
            /*move the contents of predecessor to target_node*/
            target_node.setKey(predecessor.getKey());
            target_node.setValue(predecessor.getValue());
            target_node = predecessor;
        }
        //now we are sure the target node has at most one child

        /*delete the target node*/
        INode<T,V> parent = target_node.getParent();
        //nullify unused reference
        target_node.setParent(null);

        if(target_node.getColor() == INode.RED){
            //it sure has zero children
            if(parent.getRightChild() == target_node){
                /*reuse the null nodes of the target node*/
                parent.setRightChild(target_node.getRightChild());
                parent.getRightChild().setParent(parent);
            }else{
                parent.setLeftChild(target_node.getRightChild());
                parent.getRightChild().setParent(parent);
            }

            /*nullify references to assist garbage collector*/
            target_node.setRightChild(null);
        }else{
            /*target node could be root in case the root has no predecessor*/
            INode<T,V> replacement;
            if(target_node.getRightChild().isNull()){
                //replace it with its left child
                replacement = target_node.getLeftChild();
                //nullify unused references
                target_node.setLeftChild(null);
            }else{
                //replace it with its right child
                replacement = target_node.getRightChild();
                //nullify unused references
                target_node.setRightChild(null);
            }

            replacement.setParent(parent);
            if(parent == null){ //the target node was the root
                root = replacement;
            }else{
                if(parent.getRightChild() == target_node){
                    parent.setRightChild(replacement);
                }else{
                    parent.setLeftChild(replacement);
                }
            }

            if(replacement.getColor() == INode.BLACK){
                resolve_double_black(replacement); //correct call
            }else{
                replacement.setColor(INode.BLACK);
            }
        }
        return true;
    }
}
