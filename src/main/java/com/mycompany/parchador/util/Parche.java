/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.parchador.util;

/**
 *
 * @author DiegoCV
 */
public class Parche {
    private String raiz, rutaClass;

    public Parche(String raiz, String rutaClass) {
        this.raiz = raiz;
        this.rutaClass = rutaClass;
    }
    
    public String getRaiz() {
        return raiz;
    }

    public void setRaiz(String raiz) {
        this.raiz = raiz;
    }

    public String getRutaClass() {
        return rutaClass;
    }

    public void setRutaClass(String rutaClass) {
        this.rutaClass = rutaClass;
    }
    
}
