/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.parchador;

import com.mycompany.parchador.util.JarControl;
import com.mycompany.parchador.util.Parche;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author DiegoCV
 */
public class WebApp {

    private List<String> listaDiferencias;
    private Map<String, Pair> jarsSeleccionados;
    private Map<String, List<Parche>> parches;
    private String rutaOrigen;
    private String rutaDestino;
    private FileControl fileControl;     

    public WebApp() {
        this.parches = new HashMap<>();
        fileControl = new FileControl();
    }

    public void copiarCambiosErp(){
        boolean tieneClass = false;
        for (String diferencia : listaDiferencias) {
            if (diferencia.endsWith(".java")) {
                tieneClass = true;                
                String nombreJar = this.identificarJar(diferencia);
                diferencia = diferencia.replaceAll(".java", ".class");
                this.agregarParche(nombreJar, diferencia);                
            } else {
                System.out.println("copiando ... ");
                String[] rutaDividida = this.dividirRutaArchivo(diferencia);
                fileControl.mkdir(rutaDestino + rutaDividida[0]);
                fileControl.copyFile(rutaOrigen + diferencia, rutaDestino + diferencia);
            }
        }
        if(tieneClass)
            this.parchar();
    }
    
    public void copiarCambiosMedical(){
        rutaOrigen = rutaOrigen + "target\\medical\\WEB-INF\\classes\\";
        rutaDestino = rutaDestino + "medical\\WEB-INF\\classes\\";
        for (String diferencia : listaDiferencias) {
            diferencia = limpiarDiferenciaMedical(diferencia);            
            System.out.println("copiando ... ");
            String[] rutaDividida = this.dividirRutaArchivo(diferencia);           
            fileControl.mkdir(rutaDestino + rutaDividida[0]);
            fileControl.copyFile(rutaOrigen + diferencia, rutaDestino + diferencia);            
        }
    }
    
    private String limpiarDiferenciaMedical(String diferencia){                
        diferencia = diferencia.replaceFirst("src/main/java/", "");
        diferencia = diferencia.replaceFirst("src/main/resources/", "");       
        diferencia = diferencia.replaceAll(".java", ".class");                
                        
        return diferencia;
    }
    
    
    private String[] dividirRutaArchivo(String rutaArchivo) {
        int lastPoint = rutaArchivo.lastIndexOf("/");
        String[] aux = new String[2];
        aux[0] = rutaArchivo.substring(0, lastPoint + 1);
        aux[1] = rutaArchivo.substring(lastPoint + 1, rutaArchivo.length());

        return aux;
    }
    
    private void agregarParche(String nombreJar, String diferencia){
        String raiz = diferencia.split("classes")[1];
        raiz = raiz.substring(1);
        if(parches.containsKey(nombreJar)){
            parches.get(nombreJar).add(new Parche(raiz, rutaOrigen + diferencia));
        }else{
            List<Parche> listaParches = new ArrayList<>();
            listaParches.add(new Parche(raiz, rutaOrigen + diferencia));
            parches.put(nombreJar, listaParches);
        }
    }
    
    private void parchar() {
        System.out.println("parchando ... ");
        this.copiarJares();
        for (Map.Entry<String, List<Parche>> entry : this.parches.entrySet()) {
            String nombreJar = (String) this.jarsSeleccionados.get(entry.getKey()).getFirst();
            JarControl jarControl = new JarControl(getRutaJaresDestino() + "\\" + nombreJar);
            jarControl.parchar(entry.getValue());
        }
    }
    
   private void copiarJares(){        
       fileControl.mkdir(getRutaJaresDestino());         
       for (Map.Entry<String, Pair> entry : this.jarsSeleccionados.entrySet()) {           
           fileControl.copyFile((String)entry.getValue().getSecond(), getRutaJaresDestino() + "\\" + entry.getValue().getFirst());
       }
   }
   
   private String getRutaJaresDestino(){
       return rutaDestino + "\\webapps\\etic\\admin\\WEB-INF\\lib";
   }
    
    private String identificarJar(String diferencia) {
        String nombreJar = null;
        if (diferencia.contains("/acreencia/")) {
            nombreJar = "acreencia";
        } else if (diferencia.contains("/cartera/")) {
            nombreJar = "cartera";
        } else if (diferencia.contains("/comun/")) {
            nombreJar = "comun";
        } else if (diferencia.contains("/contable/")) {
            nombreJar = "contable";
        } else if (diferencia.contains("/inventario/")) {
            nombreJar = "inventario";
        } else if (diferencia.contains("/medical/")) {
            nombreJar = "medical";
        } else if (diferencia.contains("/nomina/")) {
            nombreJar = "nomina";
        } else if (diferencia.contains("/tesoreria/")) {
            nombreJar = "tesoreria";
        }
        
        return nombreJar;
    }

    public List<String> getListaDiferencias() {
        return listaDiferencias;
    }

    public void setListaDiferencias(List<String> listaDiferencias) {
        this.listaDiferencias = listaDiferencias;
    }

    public Map<String, Pair> getJarsSeleccionados() {
        return jarsSeleccionados;
    }

    public void setJarsSeleccionados(Map<String, Pair> jarsSeleccionados) {
        this.jarsSeleccionados = jarsSeleccionados;
    }

    public String getRutaOrigen() {
        return rutaOrigen;
    }

    public void setRutaOrigen(String rutaOrigen) {
        this.rutaOrigen = rutaOrigen;
    }

    public String getRutaDestino() {
        return rutaDestino;
    }

    public void setRutaDestino(String rutaDestino) {
        this.rutaDestino = rutaDestino;
    }

}
