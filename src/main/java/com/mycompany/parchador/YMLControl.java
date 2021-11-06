/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.parchador;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author DiegoCV
 */
public class YMLControl {

    public Configuracion getConfiguracion() throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        File file = new File("configuracion.yaml");
        Configuracion configuracion = mapper.readValue(file, Configuracion.class);

        return configuracion;
    }

//    public static void main(String[] args) {
//        try {
//            YMLControl ymlControl = new YMLControl();
//            System.out.println(ymlControl.getConfiguracion());
//        } catch (IOException ex) {
//            Logger.getLogger(YMLControl.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
}
