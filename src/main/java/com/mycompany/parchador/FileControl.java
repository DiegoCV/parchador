/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.parchador;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 *
 * @author DiegoCV
 */
public class FileControl {

    /**
     * Method to <b>copy</b> a file from a source origin (<code>fromFile</code>)
     * to a destination(<code>toFile</code>). Método para <b>copiar</b> un
     * fichero desde un origen (<code>fromFile</code>) a un destino
     * (<code>toFile</code>).
     *
     * @param fromFile <code>String</code> source file path (ruta del fichero
     * origen).
     * @param toFile <code>String</code> destination file path (ruta del fichero
     * destino).
     * @return <code>boolean</code> It returns true if they could copy the file
     * false on error (devuelve true si se pude copiar el fichero false en caso
     * de error).
     */
    public boolean copyFile(String fromFile, String toFile) {
        File origin = new File(fromFile);
        File destination = new File(toFile);
        if (origin.exists()) {
            try {
                InputStream in = new FileInputStream(origin);
                OutputStream out = new FileOutputStream(destination);
                // We use a buffer for the copy (Usamos un buffer para la copia).
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
                return true;
            } catch (IOException ioe) {
                ioe.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }
    
    public boolean mkdir(String patch) {
        boolean result = false;
        File directorio = new File(patch);
        if (!directorio.exists()) {
            result = directorio.mkdirs();
        }
        return result;
    }
    
    /*public static void main(String args[]){
        Documentacion d = new Documentacion();
        d.setRuta("C:\\Users\\DiegoCV\\Documents\\rasi_pruebas\\copia_nube_erp\\jaja.txt");
        d.generarDocumentacion();
                
    }*/
}
