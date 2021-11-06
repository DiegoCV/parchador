/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.parchador.util.arbol;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author DiegoCV
 */
public class Node {
    
    private List<Node> children;
    String data;

    public Node() {
        this.children = new ArrayList<>();
    }

    public List<Node> getChildren() {
        return children;
    }

    public void setChildren(List<Node> children) {
        this.children = children;
    }
    
    public void addChildren(Node node){
        this.children.add(node);
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
    
    
    
}
