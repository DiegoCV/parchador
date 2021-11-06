/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.parchador;

import java.io.PrintWriter;
import java.util.List;

/**
 *
 * @author DiegoCV
 */
public class Documentacion {

    private String ruta;
    private String fecha;
    private String autor;
    private boolean hayPaginas;
    private boolean hayClasses;
    private String mensajeCommit;
    private List<String> rutaClasses;
    private List<String> rutaPaginas;
    private String rutaApp;
    private List<String> jaresParchados;
    private boolean hayScriptBD;
    private List<Pair> scriptsBD;
    private String commit;
    private String tag;
    private PrintWriter writer;

    public void generarDocumentacion() {
        FileControl fc = new FileControl();        
        try {
            fc.mkdir(ruta);
            writer = new PrintWriter(this.ruta+"\\"+this.fecha+".txt", "UTF-8");
            imprimirFecha();
            imprimirSaltoLinea();
            imprimirProcedimiento();
            imprimirSaltoLinea();
            imprimirMensajeCommit();
            imprimirSaltoLinea();
            imprimirRutaClasses();
            imprimirRutaPaginas();
            imprimirRutaApp();
            imprimirSaltoLinea();
            imprimirJaresParchados();
            imprimirScriptBD();
            imprimirCommit();
            imprimirTag();
            imprimirSaltoLinea(5);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void imprimirSaltoLinea() {
        writer.println("");
    }
    
    private void imprimirSaltoLinea(int i) {
        while(i > 0){
            writer.println("");
            i--;
        }
    }

    private void imprimirFecha() {
        writer.println("Fecha: " + this.fecha + " Autor: " + this.autor + ".");
    }

    private void imprimirProcedimiento() {
        writer.println("Procedimiento realizado:");
        if (hayPaginas && hayClasses) {
            writer.println("** Se parchan las siguientes páginas y classes de desarrollo en pruebas.");
        } else if (hayPaginas) {
            writer.println("** Se parchan las siguientes páginas de desarrollo en pruebas.");
        } else if (hayClasses) {
            writer.println("** Se parchan las siguientes classes de desarrollo en pruebas.");
        }
    }

    private void imprimirMensajeCommit() {
        writer.println("\t** " + this.mensajeCommit);        
    }

    private void imprimirRutaClasses() {
        if (this.rutaClasses != null && this.rutaClasses.size() > 0) {
            writer.println("\tRuta classes:\t" + this.rutaClasses.get(0));
            for (int i = 1; i < this.rutaClasses.size(); i++) {
                writer.println("\t\t\t" + this.rutaClasses.get(i));
            }
            writer.println("");
        }
    }

    private void imprimirRutaPaginas() {
        if (this.rutaPaginas != null && this.rutaPaginas.size() > 0) {
            writer.println("\tRuta paginas:\t" + this.rutaPaginas.get(0));
            for (int i = 1; i < this.rutaPaginas.size(); i++) {
                writer.println("\t\t\t" + this.rutaPaginas.get(i));
            }
            writer.println("");
        }
    }

    private void imprimirRutaApp() {
        writer.println("\tRuta App:\t" + this.rutaApp);
    }

    private void imprimirJaresParchados() {
        if (this.jaresParchados != null && this.jaresParchados.size() > 0) {
            String nombresJares = "";
            String mensajeAux = "";
            for (int i = 0; i < this.jaresParchados.size(); i++) {                
                nombresJares += "etic_admin_" + this.jaresParchados.get(i) + ".jar, ";
            }
            if (this.jaresParchados.size() == 1) {
                mensajeAux += "** Se parcha el jar " + nombresJares;
            } else if (this.jaresParchados.size() > 1) {
                mensajeAux += "** Se parchan los jares " + nombresJares;
            }
            mensajeAux += "se recomienda reiniciar el servidor.";
            writer.println("\t" + mensajeAux);
            writer.println("");
        }
    }

    private void imprimirScriptBD() {
        if (hayScriptBD) {
            String nombreScripts = "";
            for(Pair script: scriptsBD){
                nombreScripts += script.getSecond() + ", ";
            }
            nombreScripts = nombreScripts.substring(0, nombreScripts.length()-2);
            if(scriptsBD.size() == 1){
                writer.println("\t** Se debe ejecutar el script de base de datos: "+nombreScripts);
            }else if(scriptsBD.size() > 1){
                writer.println("\t** Se deben ejecutar los siguientes scripts de base de datos: "+nombreScripts);
            }
            writer.println("");
        }
    }

    private void imprimirCommit() {
        writer.println("\t--Commit:\t" + this.commit);
        writer.println("");

    }

    private void imprimirTag() {
        writer.println("\t--Tag:\t" + this.tag);
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public boolean isHayPaginas() {
        return hayPaginas;
    }

    public void setHayPaginas(boolean hayPaginas) {
        this.hayPaginas = hayPaginas;
    }

    public boolean isHayClasses() {
        return hayClasses;
    }

    public void setHayClasses(boolean hayClasses) {
        this.hayClasses = hayClasses;
    }

    public String getMensajeCommit() {
        return mensajeCommit;
    }

    public void setMensajeCommit(String mensajeCommit) {
        this.mensajeCommit = mensajeCommit;
    }

    public List<String> getRutaClasses() {
        return rutaClasses;
    }

    public void setRutaClasses(List<String> rutaClasses) {
        this.rutaClasses = rutaClasses;
    }

    public List<String> getRutaPaginas() {
        return rutaPaginas;
    }

    public void setRutaPaginas(List<String> rutaPaginas) {
        this.rutaPaginas = rutaPaginas;
    }

    public String getRutaApp() {
        return rutaApp;
    }

    public void setRutaApp(String rutaApp) {
        this.rutaApp = rutaApp;
    }

    public List<String> getJaresParchados() {
        return jaresParchados;
    }

    public void setJaresParchados(List<String> jaresParchados) {
        this.jaresParchados = jaresParchados;
    }

    public String getCommit() {
        return commit;
    }

    public void setCommit(String commit) {
        this.commit = commit;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public boolean isHayScriptBD() {
        return hayScriptBD;
    }

    public void setHayScriptBD(boolean hayScriptBD) {
        this.hayScriptBD = hayScriptBD;
    }

    public void setScriptsBD(List<Pair> scriptsBD) {
        this.scriptsBD = scriptsBD;
    }        

}
