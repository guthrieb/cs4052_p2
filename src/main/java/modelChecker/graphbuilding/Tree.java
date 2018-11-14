package modelChecker.graphbuilding;

import java.util.ArrayList;
import java.util.List;

public class Tree {

    public class NodeContents {
        private String symbol;
        private String type;

        public NodeContents(String symbol, String type) {
            this.symbol = symbol;
            this.type = type;
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    private Node<NodeContents> root;

    public Tree(NodeContents rootData) {
        root = new Node<NodeContents>();
        root.data = rootData;
        root.children = new ArrayList<Node<NodeContents>>();
    }

    public static class Node<T> {
        private T data;
        private Node<T> parent;
        private List<Node<T>> children;
    }
}
