/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.parchador;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author DiegoCV
 */
public class FiltroJar extends FileFilter{

    @Override
    public boolean accept(File f) {
        return this.tieneExtensionJar(f);
    }
    
    private boolean tieneExtensionJar(File f){
        String rutaAbsoluta = f.getAbsolutePath();
        return rutaAbsoluta.endsWith(".jar");
    }

    @Override
    public String getDescription() {
        return ("Filtro jar");
    }
    
}
