/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.parchador;

import java.io.File;
import java.io.IOException;
import org.eclipse.jgit.api.errors.GitAPIException;

/**
 *
 * @author Mario Pérez Esteso
 *
 */

public class Test {
    
    /*public static void main(String[] args) throws IOException, GitAPIException, Exception {
      //  String localPath = "C:\\Users\\DiegoCV\\Documents\\Gabo";
        //String localPath = "C:\\Users\\DiegoCV\\Documents\\rasi\\medical";   
        String localPath = "C:\\Users\\DiegoCV\\Documents\\rasi\\RASI-ERP"; 
        String remotePath = "https://github.com/GeekyTheory/GitTutorial.git";
        remotePath.trim();       
        GitControl gc = new GitControl(localPath, remotePath);
        String rutaJar = "C:\\Users\\DiegoCV\\Documents\\rasi_pruebas\\RASI-ERP\\RUNNING.txt";                
        File f = new File(rutaJar);
        System.out.println("we " + f.getParent());
        /*String[] k = f.list();
        for (int i = 0; i < k.length; i++) {
            System.out.println("sdf -> " + i + " " + k[i] );
        }*/
         
        //Clone repository
       // gc.cloneRepo();
        //Add files to repository
       // gc.addToRepo();
        //Commit with a custom message
       // gc.commitToRepo("Modified testfile.txt");
        //Push commits
       // gc.pushToRepo();
        //Pull
       // gc.pullFromRepo();
        //gc.diffTo();
        //gc.getListCommit();
    //}
    
}
