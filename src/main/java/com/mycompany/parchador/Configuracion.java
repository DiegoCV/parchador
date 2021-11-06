/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.parchador;

/**
 *
 * @author DiegoCV
 */
public class Configuracion {

    private String path_erp;
    private String path_medical;
    private String path_erp_copia_nube;
    private String path_medical_copia_nube;
    private String autor;
    private String inicial;
    
    public Configuracion() {
    }

    public Configuracion(String path_erp, String path_medical, String path_erp_copia_nube, String path_medical_copia_nube, String autor, String inicial) {
        this.path_erp = path_erp;
        this.path_medical = path_medical;
        this.path_erp_copia_nube = path_erp_copia_nube;
        this.path_medical_copia_nube = path_medical_copia_nube;
        this.autor = autor;
        this.inicial = inicial;
    }    

    public String getPath_erp() {
        return path_erp;
    }

    public void setPath_erp(String path_erp) {
        this.path_erp = path_erp;
    }

    public String getPath_medical() {
        return path_medical;
    }

    public void setPath_medical(String path_medical) {
        this.path_medical = path_medical;
    }

    public String getPath_erp_copia_nube() {
        return path_erp_copia_nube;
    }

    public void setPath_erp_copia_nube(String path_erp_copia_nube) {
        this.path_erp_copia_nube = path_erp_copia_nube;
    }    

    public String getPath_medical_copia_nube() {
        return path_medical_copia_nube;
    }

    public void setPath_medical_copia_nube(String path_medical_copia_nube) {
        this.path_medical_copia_nube = path_medical_copia_nube;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getInicial() {
        return inicial;
    }

    public void setInicial(String inicial) {
        this.inicial = inicial;
    }
    
    
}
