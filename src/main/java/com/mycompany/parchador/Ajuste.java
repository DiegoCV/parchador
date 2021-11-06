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
public class Ajuste {
    private Documentacion documentacion;
    private Script scripts;
    private WebApp webApp;
    
    public boolean construirAjuste(){
        boolean rta = false;
        try{
            this.documentacion.generarDocumentacion();
            if(this.scripts != null)
                this.scripts.copiarScript();
            this.webApp.copiarCambiosErp();
            rta = true;
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return rta;
    }
    
    public boolean construirAjusteMedical(){
        boolean rta = false;
        try{            
            this.documentacion.generarDocumentacion();
            if(this.scripts != null)
               this.scripts.copiarScript();
            this.webApp.copiarCambiosMedical();
            rta = true;
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return rta;
    }

    public Documentacion getDocumentacion() {
        return documentacion;
    }

    public void setDocumentacion(Documentacion documentacion) {
        this.documentacion = documentacion;
    }

    public Script getScripts() {
        return scripts;
    }

    public void setScripts(Script scripts) {
        this.scripts = scripts;
    }    

    public WebApp getWebApp() {
        return webApp;
    }

    public void setWebApp(WebApp webApp) {
        this.webApp = webApp;
    }
    
    
}









