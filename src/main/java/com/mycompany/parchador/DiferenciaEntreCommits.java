/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.parchador;

import java.util.List;

/**
 *
 * @author DiegoCV
 */
public class DiferenciaEntreCommits {
    private int indiceCommitUltimo;
    private String hashCommitUltimo;
    private String mensajeCommitUltimo;
       
    private int indiceCommitAnterior;
    private String hashCommitAnterior;
    private String mensajeCommitAnterior;
    
    private List<String> listaDiferencias;

    public int getIndiceCommitUltimo() {
        return indiceCommitUltimo;
    }

    public void setIndiceCommitUltimo(int indiceCommitUltimo) {
        this.indiceCommitUltimo = indiceCommitUltimo;
    }

    public String getHashCommitUltimo() {
        return hashCommitUltimo;
    }

    public void setHashCommitUltimo(String hashCommitUltimo) {
        this.hashCommitUltimo = hashCommitUltimo;
    }

    public String getMensajeCommitUltimo() {
        return mensajeCommitUltimo;
    }

    public void setMensajeCommitUltimo(String mensajeCommitUltimo) {
        this.mensajeCommitUltimo = mensajeCommitUltimo;
    }

    public int getIndiceCommitAnterior() {
        return indiceCommitAnterior;
    }

    public void setIndiceCommitAnterior(int indiceCommitAnterior) {
        this.indiceCommitAnterior = indiceCommitAnterior;
    }

    public String getHashCommitAnterior() {
        return hashCommitAnterior;
    }

    public void setHashCommitAnterior(String hashCommitAnterior) {
        this.hashCommitAnterior = hashCommitAnterior;
    }

    public String getMensajeCommitAnterior() {
        return mensajeCommitAnterior;
    }

    public void setMensajeCommitAnterior(String mensajeCommitAnterior) {
        this.mensajeCommitAnterior = mensajeCommitAnterior;
    }

    public List<String> getListaDiferencias() {
        return listaDiferencias;
    }

    public void setListaDiferencias(List<String> listaDiferencias) {
        this.listaDiferencias = listaDiferencias;
    }
    
    
}
