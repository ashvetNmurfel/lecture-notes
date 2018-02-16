package ru.spbau.lecturenotes.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DataHierarchyTree<T extends Serializable> implements Serializable {
    protected Node<T> root = null;
    protected DataHierarchyTree(Node<T> root) {
        this.root = root;
    }

    public static <T extends Serializable> DataHierarchyTree<T> createTree(final @Nullable T data) {
        return new DataHierarchyTree<>(new Leaf<>(data));
    }

    public static <T extends Serializable> DataHierarchyTree<T> createTree(
            final @NotNull DataHierarchyTree<T>... subtrees) {
        return new DataHierarchyTree<>(new InnerNode<>(subtrees));
    }

    public interface Node<T extends Serializable> extends Serializable {
        boolean isLeaf();
        List<Node<T>> getChildren();
        T getData();
        DataHierarchyTree<T> getSubtree();
    }

    protected static class Leaf<T extends Serializable> implements Node<T> {
        private T dataHolder;

        public Leaf(T dataHolder) {
            this.dataHolder = dataHolder;
        }

        @Override
        public boolean isLeaf() {
            return true;
        }

        @Override
        public List<Node<T>> getChildren() {
            throw new UnsupportedOperationException("Attempting to get children of the leaf node");
        }

        @Override
        public T getData() {
            return dataHolder;
        }

        @Override
        public DataHierarchyTree<T> getSubtree() {
            return new DataHierarchyTree<>(this);
        }
    }

    protected static class InnerNode<T extends Serializable> implements Node<T> {
        private List<Node<T>> children;

        public InnerNode(DataHierarchyTree<T>... subtrees) {
            children = new ArrayList<>();
            for (DataHierarchyTree<T> tree : subtrees) {
                children.add(tree.root);
            }
        }

        @Override
        public boolean isLeaf() {
            return false;
        }

        @Override
        public List<Node<T>> getChildren() {
            return children;
        }

        @Override
        public T getData() {
            throw new UnsupportedOperationException("Attempting to get data from the inner node");
        }

        @Override
        public DataHierarchyTree<T> getSubtree() {
            return new DataHierarchyTree<>(this);
        }
    }
}
