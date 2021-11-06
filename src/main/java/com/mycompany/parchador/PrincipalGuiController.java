package com.mycompany.parchador;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.jgit.api.errors.GitAPIException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author DiegoCV
 */
public class PrincipalGuiController {

    private GitControl gc_erp;
    private GitControl gc_medical;
    private YMLControl ymlControl;
    private Configuracion configuracion;
    private String proyecto;    
    private List<String> listaDiferencias;
    private DiferenciaEntreCommits diferenciaEntreCommits;
    //** Sacar esto para un dto
    private String fechaCreacion;
    private boolean hayPaginas;
    private boolean hayClasses;
    private List<String> rutaClasses;
    private List<String> rutaPaginas;
    private String rutaApp;
    private List<String> jaresSeleccionados;
    private Map<String, Pair> jarsSeleccionadosMap;
    private List<Pair> scriptsSeleccionados;
    private String tag;
    public PrincipalGuiController() {
        try {
            
            this.proyecto = "ERP";
            this.ymlControl = new YMLControl();
            configuracion = this.ymlControl.getConfiguracion();
            if(!configuracion.getPath_erp().isEmpty()){
                this.gc_erp = new GitControl(configuracion.getPath_erp(), "");
            }
            if(!configuracion.getPath_medical().isEmpty()){
                this.gc_medical = new GitControl(configuracion.getPath_medical(), "");
            }
            this.diferenciaEntreCommits = new DiferenciaEntreCommits();
        } catch (Exception ex) {
            Logger.getLogger(PrincipalGuiController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getInicial(){
        return this.configuracion.getInicial();
    }
    
    public List<String> getListaCommits() {
        List<String> listaCommits = null;
        try {
            if (proyecto.equals("ERP") && this.gc_erp != null) {
                listaCommits = this.gc_erp.getListCommit();
            } else if (proyecto.equals("MEDICAL") && this.gc_medical != null) {
                listaCommits = this.gc_medical.getListCommit();
            }
        } catch (GitAPIException ex) {
            Logger.getLogger(PrincipalGuiController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listaCommits;
    }

    public String calcularDiferenciaEntreCommits(int indiceUltimoCommit, int indiceAnteriorCommit) {
        try {
            if (proyecto.equals("ERP")) {
                diferenciaEntreCommits = this.gc_erp.diffTo(indiceUltimoCommit, indiceAnteriorCommit);
                listaDiferencias = diferenciaEntreCommits.getListaDiferencias();
            } else if (proyecto.equals("MEDICAL")) {
                diferenciaEntreCommits = this.gc_medical.diffTo(indiceUltimoCommit, indiceAnteriorCommit);
                listaDiferencias = diferenciaEntreCommits.getListaDiferencias();
            }
        } catch (Exception ex) {
            Logger.getLogger(PrincipalGuiController.class.getName()).log(Level.SEVERE, null, ex);
        }        
        return this.concatenarListas(listaDiferencias);
    }

    private String concatenarListas(List<String> lista) {
        String mensaje = "";
        for (String string : lista) {
            mensaje += string + "\n";
        }

        return mensaje;
    }

    public String getProyecto() {
        return proyecto;
    }

    public void setProyecto(String proyecto) {
        this.proyecto = proyecto;
    }
    
    public boolean construirAjuste(){
        Ajuste ajuste = new Ajuste();
        ajuste.setDocumentacion(this.armarDocumentacion());
        if((this.scriptsSeleccionados != null && this.scriptsSeleccionados.size() > 0))
            ajuste.setScripts(this.armarScript());
        ajuste.setWebApp(this.armarWebApp());
        return ajuste.construirAjuste();
    }
    
    private Documentacion armarDocumentacion(){
        Documentacion documentacion = new Documentacion();
        if(esErp()){
            documentacion.setRuta(this.configuracion.getPath_erp_copia_nube()+"\\"+this.fechaCreacion+"\\docs\\");
        }else if(esMedical()){
            documentacion.setRuta(this.configuracion.getPath_medical_copia_nube()+"\\"+this.fechaCreacion+"\\docs\\");
        }       
        documentacion.setFecha(this.fechaCreacion);
        documentacion.setAutor(this.configuracion.getAutor());
        documentacion.setHayClasses(this.hayClasses);
        documentacion.setHayPaginas(this.hayPaginas);
        documentacion.setMensajeCommit(this.diferenciaEntreCommits.getMensajeCommitUltimo());
        documentacion.setRutaClasses(this.rutaClasses);
        documentacion.setRutaPaginas(this.rutaPaginas);
        documentacion.setRutaApp(this.rutaApp);
        documentacion.setJaresParchados(this.jaresSeleccionados);
        documentacion.setHayScriptBD((this.scriptsSeleccionados != null && this.scriptsSeleccionados.size() > 0));
        documentacion.setScriptsBD(scriptsSeleccionados);
        documentacion.setCommit(this.diferenciaEntreCommits.getHashCommitUltimo());
        documentacion.setCommit(this.diferenciaEntreCommits.getHashCommitUltimo());
        documentacion.setTag(this.tag);
        
        return documentacion;
    }
    
    private Script armarScript(){
        Script script = new Script();
        if(esErp()){
            script.setRuta(this.configuracion.getPath_erp_copia_nube()+"\\"+this.fechaCreacion+"\\bd\\");
        }else if(esMedical()){
            script.setRuta(this.configuracion.getPath_medical_copia_nube()+"\\"+this.fechaCreacion+"\\bd\\");
        }
        script.setRutaScripts(scriptsSeleccionados);
        
        return script;
    }

    private WebApp armarWebApp(){
        WebApp webApp = new WebApp();
        webApp.setListaDiferencias(this.listaDiferencias);
        webApp.setJarsSeleccionados(jarsSeleccionadosMap);
        webApp.setRutaOrigen(this.obtenerRutaOrigenRaiz());
        webApp.setRutaDestino(this.obtenerRutaDestinoRaiz());        
        return webApp;
    }
    
    private String obtenerRutaOrigenRaiz() {
        String ruta = null;
        if (proyecto.equals("ERP")) {
            ruta = this.configuracion.getPath_erp();
        } else if (proyecto.equals("MEDICAL")) {
            ruta = this.configuracion.getPath_medical();
        }
        String pathSeparator = obtenerSeparador();
        ruta += pathSeparator;
        
        return ruta;
    }
    
    private String obtenerRutaDestinoRaiz(){
        String ruta = null;
        if (proyecto.equals("ERP")) {
            ruta = this.configuracion.getPath_erp_copia_nube();
        } else if (proyecto.equals("MEDICAL")) {
            ruta = this.configuracion.getPath_medical_copia_nube();
        }
        String pathSeparator = obtenerSeparador();
        ruta += pathSeparator + this.fechaCreacion + pathSeparator; 
        return ruta;
    }
    
    private String obtenerSeparador(){
        return System.getProperty("file.separator");
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public List<String> getListaDiferencias() {
        return listaDiferencias;
    }    
    
    public List<String> getTags(){
        List<String> tagsAux = new ArrayList<>();
        try {
            if(esErp()){
                tagsAux = this.gc_erp.getTags();
            }else if(esMedical()){
                tagsAux = this.gc_medical.getTags();
            }
        } catch (Exception ex) {
            Logger.getLogger(PrincipalGuiController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tagsAux;
    }
    
    public String getHashCommitUltimo(){
        return this.diferenciaEntreCommits.getHashCommitUltimo();
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

    public List<String> getJaresSeleccionados() {
        return jaresSeleccionados;
    }

    public void setJaresSeleccionados(List<String> jaresSeleccionados) {
        this.jaresSeleccionados = jaresSeleccionados;
    }

    public List<Pair> getScriptsSeleccionados() {
        return scriptsSeleccionados;
    }

    public void setScriptsSeleccionados(List<Pair> scriptsSeleccionados) {
        this.scriptsSeleccionados = scriptsSeleccionados;
    }

    private boolean esErp(){
        return this.proyecto.equals("ERP");
    }
    
    private boolean esMedical(){
        return this.proyecto.equals("MEDICAL");
    }
            
    public PrincipalGuiController(List<Pair> scriptsSeleccionados) {
        this.scriptsSeleccionados = scriptsSeleccionados;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Map<String, Pair> getJarsSeleccionadosMap() {
        return jarsSeleccionadosMap;
    }

    public void setJarsSeleccionadosMap(Map<String, Pair> jarsSeleccionadosMap) {
        this.jarsSeleccionadosMap = jarsSeleccionadosMap;
    }

    public boolean construirAjusteMedical() {        
        Ajuste ajuste = new Ajuste();
        ajuste.setDocumentacion(this.armarDocumentacion());
        if((this.scriptsSeleccionados != null && this.scriptsSeleccionados.size() > 0)){
            ajuste.setScripts(this.armarScript());
        }
        ajuste.setWebApp(this.armarWebApp());
        return ajuste.construirAjusteMedical();
    }
    
    public boolean esRutaErpValida(){
        return this.gc_erp != null;
    }
    
    public boolean esRutaMedicalValida(){
        return this.gc_medical != null;
    }
}












