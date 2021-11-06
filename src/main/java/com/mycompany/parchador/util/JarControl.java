/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.parchador.util;

import com.mycompany.parchador.Pair;
import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;

/**
 *
 * @author DiegoCV
 */
public class JarControl {

    private ZipFile zipFile;

    public JarControl(String rutaJar) {
        this.zipFile = new ZipFile(rutaJar);
    }

    public boolean parchar(List<Parche> parches) {
        boolean rta = false;
        for (Parche parche : parches) {
            try {
                String fuente = parche.getRaiz();
                String destino = parche.getRutaClass();
                ZipParameters zipParameters = new ZipParameters();
                zipParameters.setIncludeRootFolder(false);
                zipParameters.setFileNameInZip(fuente);
                this.zipFile.addFile(new File(destino), zipParameters);
                rta = true;
            } catch (ZipException ex) {
                Logger.getLogger(JarControl.class.getName()).log(Level.SEVERE, null, ex);
                rta = false;
            }
        }
        return rta;
    }

    public void asd() throws ZipException {
       /* String rutaJar = "C:\\Users\\DiegoCV\\Documents\\test\\20210630_1106_DC\\etic_admin_inventario.jar";
        //String rutaClass = "etic\\admin\\inventario\\mediator\\InventarioMDT.class";
        String raiz = "etic/admin/inventario/mediator/InventarioMDT.class";
        String rutaClass = "C:\\Users\\DiegoCV\\Documents\\test\\20210630_1106_DC\\InventarioMDT.class";
        ZipFile zf = new ZipFile(rutaJar);
        ZipParameters zipParameters = new ZipParameters();
        zipParameters.setIncludeRootFolder(false);
        zipParameters.setFileNameInZip(raiz);
//https://github.com/srikanth-lingala/zip4j        
        zf.addFile(new File(rutaClass), zipParameters);
*/
    }

}
