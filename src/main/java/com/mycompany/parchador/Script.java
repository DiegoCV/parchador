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
public class Script {
    private String ruta;
    private List<Pair> rutaScripts;
    
    public void copiarScript(){       
       FileControl fc = new FileControl();        
        try {
            fc.mkdir(ruta);
            for (Pair rutaScript : rutaScripts) {                
                fc.copyFile((String)rutaScript.getFirst(), ruta+rutaScript.getSecond());
            }            
        } catch (Exception e) {
            e.printStackTrace();
        }        
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public List<Pair> getRutaScripts() {
        return rutaScripts;
    }

    public void setRutaScripts(List<Pair> rutaScripts) {
        this.rutaScripts = rutaScripts;
    }
       
}
