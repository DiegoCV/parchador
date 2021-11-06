/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.parchador;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author DiegoCV
 */
public class Principal extends javax.swing.JFrame {

    private PrincipalGuiController principalGuiController;
    private Map<String, Pair> jarsSeleccionados;
    private Map<String, Pair> botonesJares;
    private List<Pair> scriptsSeleccionados;
    private Map<String, Integer> listaPaginas;
    private Map<String, Integer> listaClasses;

    /**
     * Creates new form Principal
     */
    public Principal() {
        initComponents();
        this.principalGuiController = new PrincipalGuiController();
        this.construirTitulo();
        this.listaClasses = new HashMap<>();
        this.listaPaginas = new HashMap<>();
        this.principalGuiController.setFechaCreacion(this.fechaCreacion.getText());
        this.botonesJares = new HashMap<>();
        this.cargarBotonesJar();
        if(this.principalGuiController.esRutaErpValida()){
            this.proyecto.setSelectedIndex(0);
            this.cargarElementosVista();
        }else if(this.principalGuiController.esRutaMedicalValida()){
            this.proyecto.setSelectedIndex(1);
            this.cargarElementosVista();
        }        
    }

    private void cargarElementosVista(){
        this.construirComboCommits();
        this.calcularDiferenciaEntreCommits();
        this.jarsSeleccionados = new HashMap<>();
        this.construirComboTags();
        this.scriptsSeleccionados = new ArrayList<>();
    }
    
    private void construirComboCommits() {
        this.principalGuiController.setProyecto(this.proyecto.getSelectedItem().toString());
        List<String> listaCommits = this.principalGuiController.getListaCommits();
        if(listaCommits != null){
            this.comboCommitUltimo.removeAllItems();
            this.comboAnteriorCommit.removeAllItems();
            for (String listaCommit : listaCommits) {
                this.comboCommitUltimo.addItem(listaCommit);
                this.comboAnteriorCommit.addItem(listaCommit);
            }
            this.comboAnteriorCommit.setSelectedIndex(1);
        }
    }

    private void construirTitulo() {
        Calendar calendar = Calendar.getInstance();
        int anno = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH) + 1;
        int dia = calendar.get(Calendar.DATE);
        int hora = calendar.get(Calendar.HOUR_OF_DAY);
        int minutos = calendar.get(Calendar.MINUTE);
        Formatter fmt = new Formatter();
        fmt.format("%02d", mes);
        String titulo = String.valueOf(anno) + fmt.format("%02d", dia) + "_";
        Formatter fmt2 = new Formatter();
        fmt2.format("%02d", hora);
        String tiempo = fmt2.format("%02d", minutos) + "_";
        titulo += tiempo + this.principalGuiController.getInicial();
        this.fechaCreacion.setText(titulo);
        this.principalGuiController.setFechaCreacion(titulo);
    }

    private void cargarBotonesJar() {
        this.botonesJares.put("acreencia", new Pair(this.jButtonAcreencia, 0));
        this.botonesJares.put("cartera", new Pair(this.jButtonCartera, 0));
        this.botonesJares.put("comun", new Pair(this.jButtonComun, 0));
        this.botonesJares.put("contable", new Pair(this.jButtonContable, 0));
        this.botonesJares.put("inventario", new Pair(this.jButtonInventario, 0));
        this.botonesJares.put("medical", new Pair(this.jButtonMedical, 0));
        this.botonesJares.put("nomina", new Pair(this.jButtonNomina, 0));
        this.botonesJares.put("tesoreria", new Pair(this.jButtonTesoreria, 0));
    }

    private void calcularDiferenciaEntreCommits() {
        int indiceUltimoCommit = this.comboCommitUltimo.getSelectedIndex();
        int indiceAnteriorCommit = this.comboAnteriorCommit.getSelectedIndex();
        deshabilitarBotonesJar();
        boolean sonIguales = indiceUltimoCommit == indiceAnteriorCommit;
        if (!sonIguales && indiceUltimoCommit >= 0 && indiceAnteriorCommit >= 0) {
            this.diferenciaEntreCommits.removeAll();
            this.diferenciaEntreCommits.setText(this.principalGuiController.calcularDiferenciaEntreCommits(indiceUltimoCommit, indiceAnteriorCommit));
            activarBotonesJar();
        }        
    }

    private void activarBotonesJar() {
        this.reiniciarListasDeRuta();
        List<String> diferenciasEntreCommit = this.principalGuiController.getListaDiferencias();
        if (diferenciasEntreCommit != null) {
            for (String diferencia : diferenciasEntreCommit) {
                if (diferencia.endsWith(".java")) {
                    if(esERP()){                                            
                        String key = identificarJar(diferencia);
                        if (key != null) {
                            JButton aux = (JButton) this.botonesJares.get(key).getFirst();
                            aux.setEnabled(true);
                            this.botonesJares.get(key).setSecond(1);
                        }
                    }
                    this.principalGuiController.setHayClasses(true);
                    this.agregarRutaClasse(diferencia.substring(0, getIndiceUltimoSlash(diferencia) + 1));
                } else {
                    this.principalGuiController.setHayPaginas(true);
                    this.agregarRutaPagina(diferencia.substring(0, getIndiceUltimoSlash(diferencia) + 1));
                }
            }
        }
        this.principalGuiController.setRutaClasses(parseKeySetFromMapToListKey(this.listaClasses));
        this.principalGuiController.setRutaPaginas(parseKeySetFromMapToListKey(this.listaPaginas));
    }

    private List<String> parseKeySetFromMapToListKey(Map map) {
        List<String> listKey = new ArrayList<>();
        map.forEach((k, v) -> listKey.add((String) k));

        return listKey;
    }

    private void agregarRutaClasse(String rutaClasse) {
        Integer i = this.listaClasses.get(rutaClasse);
        if (i == null) {
            this.listaClasses.put(rutaClasse, 1);
        } else {
            this.listaClasses.put(rutaClasse, i++);
        }
    }

    private void agregarRutaPagina(String rutaPagina) {
        Integer i = this.listaPaginas.get(rutaPagina);
        if (i == null) {
            this.listaPaginas.put(rutaPagina, 0);
        } else {
            this.listaPaginas.put(rutaPagina, 0);
        }
    }

    private void reiniciarListasDeRuta() {
        this.listaClasses.clear();
        this.listaPaginas.clear();
    }

    private int getIndiceUltimoSlash(String diferencia) {
        return diferencia.lastIndexOf("/");
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

    private void deshabilitarBotonesJar() {
        Collection<Pair> collection = this.botonesJares.values();
        for (Iterator<Pair> iterator = collection.iterator(); iterator.hasNext();) {
            Pair next = iterator.next();
            JButton aux = (JButton) next.getFirst();
            aux.setEnabled(false);
            next.setSecond(0);
        }
    }

    private void construirComboTags() {
        this.jComboBoxTags.removeAllItems();
        for (String tag : this.principalGuiController.getTags()) {
            this.jComboBoxTags.addItem(tag);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel8 = new javax.swing.JLabel();
        jScrollBar1 = new javax.swing.JScrollBar();
        jPanel1 = new javax.swing.JPanel();
        fechaCreacion = new javax.swing.JLabel();
        proyecto = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        comboCommitUltimo = new javax.swing.JComboBox<>();
        comboAnteriorCommit = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        jTextFieldRutaApp = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        diferenciaEntreCommits = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jButtonAcreencia = new javax.swing.JButton();
        jButtonCartera = new javax.swing.JButton();
        jButtonComun = new javax.swing.JButton();
        jButtonContable = new javax.swing.JButton();
        jButtonInventario = new javax.swing.JButton();
        jButtonMedical = new javax.swing.JButton();
        jButtonNomina = new javax.swing.JButton();
        jButtonTesoreria = new javax.swing.JButton();
        jComboBoxTags = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();

        jLabel8.setText("jLabel8");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        fechaCreacion.setText("0000_00_00_DC");

        proyecto.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ERP", "MEDICAL" }));
        proyecto.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                proyectoItemStateChanged(evt);
            }
        });

        jLabel1.setText("Ultimo commit");

        jLabel2.setText("vs");

        jLabel3.setText("Anterior commit");

        comboCommitUltimo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboCommitUltimo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboCommitUltimoItemStateChanged(evt);
            }
        });

        comboAnteriorCommit.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboAnteriorCommit.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboAnteriorCommitItemStateChanged(evt);
            }
        });

        jLabel6.setText("Ruta app");

        jTextFieldRutaApp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldRutaAppActionPerformed(evt);
            }
        });

        jLabel7.setText("Tag");

        jLabel9.setText("Diff");

        diferenciaEntreCommits.setColumns(20);
        diferenciaEntreCommits.setRows(5);
        jScrollPane3.setViewportView(diferenciaEntreCommits);

        jButton1.setText("Construir ajuste");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel10.setText("jar");

        jButtonAcreencia.setText("Acreencia");
        jButtonAcreencia.setEnabled(false);
        jButtonAcreencia.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonAcreenciaMouseClicked(evt);
            }
        });
        jButtonAcreencia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAcreenciaActionPerformed(evt);
            }
        });

        jButtonCartera.setText("Cartera");
        jButtonCartera.setEnabled(false);
        jButtonCartera.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCarteraActionPerformed(evt);
            }
        });

        jButtonComun.setText("Comun");
        jButtonComun.setEnabled(false);
        jButtonComun.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonComunActionPerformed(evt);
            }
        });

        jButtonContable.setText("Contable");
        jButtonContable.setEnabled(false);
        jButtonContable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonContableActionPerformed(evt);
            }
        });

        jButtonInventario.setText("Inventario");
        jButtonInventario.setEnabled(false);
        jButtonInventario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonInventarioActionPerformed(evt);
            }
        });

        jButtonMedical.setText("Medical");
        jButtonMedical.setEnabled(false);
        jButtonMedical.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMedicalActionPerformed(evt);
            }
        });

        jButtonNomina.setText("Nomina");
        jButtonNomina.setEnabled(false);
        jButtonNomina.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNominaActionPerformed(evt);
            }
        });

        jButtonTesoreria.setText("Tesoreria");
        jButtonTesoreria.setEnabled(false);
        jButtonTesoreria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonTesoreriaActionPerformed(evt);
            }
        });

        jLabel4.setText("Scripts BD");

        jButton2.setText("Seleccionar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(fechaCreacion, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(proyecto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(comboCommitUltimo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9)
                            .addComponent(jLabel10))
                        .addGap(20, 20, 20)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(comboAnteriorCommit, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane3)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jButtonNomina, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButtonContable, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButtonAcreencia, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jButtonCartera, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButtonComun, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jButtonInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButtonMedical, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jButtonTesoreria, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 15, Short.MAX_VALUE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7)
                            .addComponent(jLabel4))
                        .addGap(47, 47, 47)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldRutaApp)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jComboBoxTags, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fechaCreacion)
                    .addComponent(proyecto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(comboCommitUltimo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(comboAnteriorCommit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jButtonAcreencia)
                    .addComponent(jButtonCartera)
                    .addComponent(jButtonComun))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonContable)
                    .addComponent(jButtonInventario)
                    .addComponent(jButtonMedical))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonNomina)
                    .addComponent(jButtonTesoreria))
                .addGap(22, 22, 22)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jTextFieldRutaApp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jComboBoxTags, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jButton2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonAcreenciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAcreenciaActionPerformed
        this.seleccionarJarAcreencia();
    }//GEN-LAST:event_jButtonAcreenciaActionPerformed

    private void jButtonAcreenciaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonAcreenciaMouseClicked

    }//GEN-LAST:event_jButtonAcreenciaMouseClicked

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked

    }//GEN-LAST:event_jButton1MouseClicked

    private void jTextFieldRutaAppActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldRutaAppActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldRutaAppActionPerformed

    private void comboAnteriorCommitItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboAnteriorCommitItemStateChanged
        this.calcularDiferenciaEntreCommits();
    }//GEN-LAST:event_comboAnteriorCommitItemStateChanged

    private void comboCommitUltimoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboCommitUltimoItemStateChanged
        this.calcularDiferenciaEntreCommits();
    }//GEN-LAST:event_comboCommitUltimoItemStateChanged

    private void proyectoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_proyectoItemStateChanged
        this.principalGuiController.setProyecto(this.proyecto.getSelectedItem().toString());
        boolean esRepositorioCorrecto = false;
        if(this.esERP()){
            esRepositorioCorrecto = this.principalGuiController.esRutaErpValida();
        }else if(this.esMEDICAL()){
            esRepositorioCorrecto = this.principalGuiController.esRutaMedicalValida();
        }        
        if(esRepositorioCorrecto){
            this.construirComboCommits();
            this.calcularDiferenciaEntreCommits();
            this.construirComboTags();
        }else{
            JOptionPane.showMessageDialog(null, "Se presentaron problemas al leer el repositorio git, por favor verifique que las rutas sean las correctas.");
        }
    }//GEN-LAST:event_proyectoItemStateChanged

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (esERP()) {
            this.construirAjusteERP();
        } else if (esMEDICAL()) {
            this.construirAjusteMedical();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private boolean esERP() {        
        return this.proyecto.getSelectedItem().toString().equals("ERP");
    }

    private boolean esMEDICAL() {
        return this.proyecto.getSelectedItem().toString().equals("MEDICAL");
    }

    private void construirAjusteMedical() {        
        this.principalGuiController.setRutaApp(this.jTextFieldRutaApp.getText());  
        String tag = "";
        if(this.jComboBoxTags.getSelectedItem() != null)
            tag = this.jComboBoxTags.getSelectedItem().toString();
        this.principalGuiController.setTag(tag);
        this.principalGuiController.setScriptsSeleccionados(this.scriptsSeleccionados);
        if (this.principalGuiController.construirAjusteMedical()) {
            JOptionPane.showMessageDialog(null, "Ajuste construido con exito");
        } else {
            JOptionPane.showMessageDialog(null, "Se presento un problema");
        }
    }

    private void construirAjusteERP() {
        /*
            1)  Validar que todos los jares esten seleccionados
            2) Validar que los inputs esten llenos
            1) construyo la carpeta destino y copio los jsp
            
         */
        if (validarJaresSeleccionado()) {
            this.principalGuiController.setRutaApp(this.jTextFieldRutaApp.getText());
            this.principalGuiController.setJaresSeleccionados(this.obtenerNombreDeLosJaresSeleccionados());
            this.principalGuiController.setTag(this.jComboBoxTags.getSelectedItem().toString());
            this.principalGuiController.setScriptsSeleccionados(this.scriptsSeleccionados);
            if (this.principalGuiController.construirAjuste()) {
                JOptionPane.showMessageDialog(null, "Ajuste construido con exito");
            } else {
                JOptionPane.showMessageDialog(null, "Se presento un problema");
            }
        }
    }

    private boolean validarJaresSeleccionado() {
        List<String> nombreJaresNoSeleccionados = this.obtenerNombreDeLosJaresNoSeleccionados();
        boolean rta = false;
        if (nombreJaresNoSeleccionados.size() > 0) {
            String mensajeError = "Falta por seleccionar los siguientes jares: ";
            for (String nombreJaresNoSeleccionado : nombreJaresNoSeleccionados) {
                mensajeError += nombreJaresNoSeleccionado + ", ";
            }
            JOptionPane.showMessageDialog(null, mensajeError);
        } else {
            rta = true;
        }

        return rta;
    }

    private List<String> obtenerNombreDeLosJaresNoSeleccionados() {
        List<String> nombreJaresNoSeleccionados = new ArrayList<>();
        for (Map.Entry<String, Pair> entry : this.botonesJares.entrySet()) {
            int i = (int) entry.getValue().getSecond();
            if (i == 1) {
                nombreJaresNoSeleccionados.add(entry.getKey());
            }
        }

        return nombreJaresNoSeleccionados;
    }

    private List<String> obtenerNombreDeLosJaresSeleccionados() {
        List<String> nombreJaresNoSeleccionados = new ArrayList<>();
        for (Map.Entry<String, Pair> entry : this.botonesJares.entrySet()) {
            int i = (int) entry.getValue().getSecond();
            if (i == 2) {
                nombreJaresNoSeleccionados.add(entry.getKey());
            }
        }

        return nombreJaresNoSeleccionados;
    }


    private void jButtonCarteraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCarteraActionPerformed
        this.seleccionarJarCartera();
    }//GEN-LAST:event_jButtonCarteraActionPerformed

    private void jButtonComunActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonComunActionPerformed
        this.seleccionarJarComun();
    }//GEN-LAST:event_jButtonComunActionPerformed

    private void jButtonContableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonContableActionPerformed
        this.seleccionarJarContable();
    }//GEN-LAST:event_jButtonContableActionPerformed

    private void jButtonInventarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonInventarioActionPerformed
        this.seleccionarJarInventario();
    }//GEN-LAST:event_jButtonInventarioActionPerformed

    private void jButtonMedicalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMedicalActionPerformed
        this.seleccionarJarMedical();
    }//GEN-LAST:event_jButtonMedicalActionPerformed

    private void jButtonNominaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNominaActionPerformed
        this.seleccionarJarNomina();
    }//GEN-LAST:event_jButtonNominaActionPerformed

    private void jButtonTesoreriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonTesoreriaActionPerformed
        this.seleccionarJarTesoreria();
    }//GEN-LAST:event_jButtonTesoreriaActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        this.abrirVentanaSeleccionarScripts();
    }//GEN-LAST:event_jButton2ActionPerformed

//     /**
//     * @param args the command line arguments
//     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new Principal().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> comboAnteriorCommit;
    private javax.swing.JComboBox<String> comboCommitUltimo;
    private javax.swing.JTextArea diferenciaEntreCommits;
    private javax.swing.JLabel fechaCreacion;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButtonAcreencia;
    private javax.swing.JButton jButtonCartera;
    private javax.swing.JButton jButtonComun;
    private javax.swing.JButton jButtonContable;
    private javax.swing.JButton jButtonInventario;
    private javax.swing.JButton jButtonMedical;
    private javax.swing.JButton jButtonNomina;
    private javax.swing.JButton jButtonTesoreria;
    private javax.swing.JComboBox<String> jComboBoxTags;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollBar jScrollBar1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextField jTextFieldRutaApp;
    private javax.swing.JComboBox<String> proyecto;
    // End of variables declaration//GEN-END:variables

    private void seleccionarJarAcreencia() {
        this.abrirVentanaSeleccionar("acreencia");
    }

    private void seleccionarJarCartera() {
        this.abrirVentanaSeleccionar("cartera");
    }

    private void seleccionarJarComun() {
        this.abrirVentanaSeleccionar("comun");
    }

    private void seleccionarJarContable() {
        this.abrirVentanaSeleccionar("contable");
    }

    private void seleccionarJarInventario() {
        this.abrirVentanaSeleccionar("inventario");
    }

    private void seleccionarJarMedical() {
        this.abrirVentanaSeleccionar("medical");
    }

    private void seleccionarJarNomina() {
        this.abrirVentanaSeleccionar("nomina");
    }

    private void seleccionarJarTesoreria() {
        this.abrirVentanaSeleccionar("tesoreria");
    }

    private void abrirVentanaSeleccionar(String jarNombre) {
        JFileChooser fileChooser = new JFileChooser();
        FiltroJar filter = new FiltroJar();
        fileChooser.setFileFilter(filter);
        int seleccion = fileChooser.showSaveDialog(this);
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            File fichero = fileChooser.getSelectedFile();
            this.jarsSeleccionados.put(jarNombre, new Pair(fichero.getName(), fichero.getAbsolutePath()));
            this.botonesJares.get(jarNombre).setSecond(2);
            this.principalGuiController.setJarsSeleccionadosMap(jarsSeleccionados);
        }
    }

    private void abrirVentanaSeleccionarScripts() {
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setMultiSelectionEnabled(true);
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {

            fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            if (!fc.isMultiSelectionEnabled()) {
                fc.setMultiSelectionEnabled(true);
            }

            File[] files = fc.getSelectedFiles();
            for (int i = 0; i < files.length; i++) {
                this.scriptsSeleccionados.add(new Pair(files[i].getAbsolutePath(), files[i].getName()));
            }
        }
    }

}
