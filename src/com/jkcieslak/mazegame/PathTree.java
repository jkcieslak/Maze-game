package com.jkcieslak.mazegame;

//TODO: Stop butchering java conventions and correct most of this

import java.util.ArrayDeque;
import java.util.ArrayList;

public class PathTree {
    private final TreeNode root;
    private final Board board;
    private final ArrayDeque<TreeNode> exitPath;
    private boolean isExitPathConstructed = false;
    public PathTree(Board board){
        this.board = board;
        root = new TreeNode(this.board.getEntrance());
        this.exitPath = new ArrayDeque<>();
        this.addTreeNodeChildren(root);
        this.constructExitPath();
    }
    public static class TreeNode {
        private Cell cell;
        private TreeNode parent;
        private ArrayList<TreeNode> children;
        private boolean isLeaf;
        private int depthLevel;

        public TreeNode(){
            cell = null;
            parent = null;
            children = null;
            depthLevel = -1;
            isLeaf = false;
        }

        /**
         * Single parameter constructor used in constructing a root node in a tree.
         * @param cell Cell on which to construct a root node of a tree (usually maze entrance)
         */
        public TreeNode(Cell cell){
            this();
            this.cell = cell;
            this.depthLevel = 0;
            this.children = new ArrayList<>();
            //System.out.println("Root Node created.");
        }
        public TreeNode(Cell cell, TreeNode parent){
            this();
            this.cell = cell;
            this.parent = parent;
            this.depthLevel = parent.depthLevel +1;
            this.children = new ArrayList<>();
            //System.out.println("Node created. Node depth: " + depthLevel);
        }
        public Cell getCell(){
            return cell;
        }
        public TreeNode getParent(){
            return parent;
        }
        public int getDepthLevel(){
            return depthLevel;
        }
        public ArrayList<TreeNode> getChildren(){
            return children;
        }
        public void addChild(TreeNode treeNode){
            this.children.add(treeNode);
        }
    }

    private void addTreeNodeChildren(TreeNode treeNode){
        ArrayList<Cell> tempNeighborCells = board.getNeighbors(treeNode.getCell(), false);
        for(Cell cell : tempNeighborCells){ //adding children based on neighbor cells
            if(treeNode != root)    //root exclusion to avoid checking for root parent(which is null)
                if(cell == treeNode.getParent().getCell())  //excluding adding a new node based on visited parent cell
                    continue;
            treeNode.addChild(new TreeNode(cell, treeNode));
        }
        for(TreeNode treeNodeI : treeNode.getChildren()){   //recursively adding children for entire tree
            addTreeNodeChildren(treeNodeI);
        }
        if(treeNode.getChildren().size() == 0)  //marking leaf nodes
            treeNode.isLeaf = true;
    }
    void constructExitPath(){
        traverse(root);
    }
    void reconstructExitPath(){
        exitPath.clear();
        isExitPathConstructed = false;
        traverse(root);
    }
    void traverse(TreeNode node){
        if(isExitPathConstructed)
            return;
        exitPath.addLast(node);
        if(exitPath.getLast().getCell() == board.getExit()) {
            isExitPathConstructed = true;
            return;
        }
        for(TreeNode child : node.getChildren())
            traverse(child);
        if(!isExitPathConstructed)
            exitPath.removeLast();
    }
    public ArrayDeque<TreeNode> getExitPath(){
        return exitPath;
    }
    public void printExitPath(){    //self-explanatory name, for debug purposes
        for (TreeNode node : exitPath){
            System.out.print(node.getDepthLevel()+".[" + node.getCell().getX() + ", " + node.getCell().getY() + "]\t");
        }
    }
}
