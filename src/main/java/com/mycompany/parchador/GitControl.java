/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.parchador;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.errors.CanceledException;
import org.eclipse.jgit.api.errors.ConcurrentRefUpdateException;
import org.eclipse.jgit.api.errors.DetachedHeadException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidConfigurationException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.api.errors.NoFilepatternException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.api.errors.NoMessageException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevObject;
import org.eclipse.jgit.revwalk.RevTag;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

/**
 *
 * @author Mario Pérez Esteso
 *
 */
public class GitControl {

    private String localPath, remotePath;
    private Repository localRepo;
    private Git git;
    private CredentialsProvider cp;
    private String name = "username";
    private String password = "password";

    public GitControl(String localPath, String remotePath) throws IOException {
        this.localPath = localPath;
        this.remotePath = remotePath;
        this.localRepo = new FileRepository(localPath + "/.git");
        cp = new UsernamePasswordCredentialsProvider(this.name, this.password);
        git = new Git(localRepo);
    }

    public void cloneRepo() throws IOException, NoFilepatternException, GitAPIException {
        Git.cloneRepository()
                .setURI(remotePath)
                .setDirectory(new File(localPath))
                .call();
    }

    public void addToRepo() throws IOException, NoFilepatternException, GitAPIException {
        AddCommand add = git.add();
        add.addFilepattern(".").call();
    }

    public void commitToRepo(String message) throws IOException, NoHeadException,
            NoMessageException, ConcurrentRefUpdateException,
            JGitInternalException, WrongRepositoryStateException, GitAPIException {
        git.commit().setMessage(message).call();
    }

    public void pushToRepo() throws IOException, JGitInternalException,
            InvalidRemoteException, GitAPIException {
        PushCommand pc = git.push();
        pc.setCredentialsProvider(cp)
                .setForce(true)
                .setPushAll();
        try {
            Iterator<PushResult> it = pc.call().iterator();
            if (it.hasNext()) {
                System.out.println(it.next().toString());
            }
        } catch (InvalidRemoteException e) {
            e.printStackTrace();
        }
    }

    public void pullFromRepo() throws IOException, WrongRepositoryStateException,
            InvalidConfigurationException, DetachedHeadException,
            InvalidRemoteException, CanceledException, RefNotFoundException,
            NoHeadException, GitAPIException {
        git.pull().call();
    }

    public void diffTo() throws Exception {
        RevCommit headCommit = getHeadCommit(localRepo);
        RevCommit diffWith = headCommit.getParent(0);
        FileOutputStream stdout = new FileOutputStream(FileDescriptor.out);
        try (DiffFormatter diffFormatter = new DiffFormatter(stdout)) {
            diffFormatter.setRepository(localRepo);
            // System.out.println("DIEGO");
            for (DiffEntry entry : diffFormatter.scan(diffWith, headCommit)) {
                System.out.println(entry.getPath(DiffEntry.Side.NEW));
                //diffFormatter.format(diffFormatter.toFileHeader(entry));
            }
        }
    }

    public DiferenciaEntreCommits diffTo(int indiceUltimoCommit, int indiceAnteriorCommit) throws Exception {
        DiferenciaEntreCommits diferenciaEntreCommits = new DiferenciaEntreCommits();
        System.out.println("int indiceUltimoCommit, int indiceAnteriorCommit" + indiceUltimoCommit +"-"+indiceAnteriorCommit);
        List<String> diferencias = new ArrayList<>();
        RevCommit commitUltimo = null;
        RevCommit commitAnterior = null;
        Iterable<RevCommit> log = git.log().call();
        int i = 0;
        for (Iterator<RevCommit> iterator = log.iterator(); iterator.hasNext();) {
            if (indiceUltimoCommit == i) {
                commitUltimo = (RevCommit) iterator.next();
            } else if (indiceAnteriorCommit == i) {
                commitAnterior = (RevCommit) iterator.next();
            } else {
                iterator.next();
            }
            i++;
        }
        diferenciaEntreCommits.setIndiceCommitUltimo(indiceUltimoCommit);
        diferenciaEntreCommits.setHashCommitUltimo(commitUltimo.getName());
        diferenciaEntreCommits.setMensajeCommitUltimo(commitUltimo.getFullMessage());
        
        diferenciaEntreCommits.setIndiceCommitAnterior(indiceAnteriorCommit);
        diferenciaEntreCommits.setHashCommitAnterior(commitAnterior.getName());
        diferenciaEntreCommits.setMensajeCommitAnterior(commitAnterior.getFullMessage());
        
        if (commitUltimo != null && commitAnterior != null) {        
            FileOutputStream stdout = new FileOutputStream(FileDescriptor.out);
            try (DiffFormatter diffFormatter = new DiffFormatter(stdout)) {
                diffFormatter.setRepository(localRepo);
                for (DiffEntry entry : diffFormatter.scan(commitAnterior, commitUltimo)) {
                    diferencias.add(entry.getPath(DiffEntry.Side.NEW));
                }
            }
        }        
        diferenciaEntreCommits.setListaDiferencias(diferencias);
        
        return diferenciaEntreCommits;
    }

    private static RevCommit getHeadCommit(Repository repository) throws Exception {
        try (Git git = new Git(repository)) {
            Iterable<RevCommit> history = git.log().setMaxCount(1).call();
            return history.iterator().next();
        }
    }

    public List<String> getListCommit() throws GitAPIException {
        List<String> listCommit = new ArrayList<>();
        Iterable<RevCommit> log = git.log().call();
        for (Iterator<RevCommit> iterator = log.iterator(); iterator.hasNext();) {
            RevCommit rev = iterator.next();            
            listCommit.add(rev.getFullMessage());
        }

        return listCommit;
    }

    public List<String> getTags() throws Exception{
        List<String> tagsName = new ArrayList<>();
        List<Ref> list = git.tagList().call();       
        for (Ref tag : list) {
            tagsName.add(tag.getName());
        }
        tagsName = cleanListTagsNames(tagsName);
        Collections.sort(tagsName, (x, y) -> isMayor(y,x));
        
        return tagsName;
    }
    private List<String> cleanListTagsNames(List<String> tagsNames){
        List<String> tagsNameAux = new ArrayList<>();
        for (int i = 0; i < tagsNames.size(); i++) {
            String aux = tagsNames.get(i);
            aux = aux.split("/")[2];
            if(!aux.contains("v."))                       
                tagsNameAux.add(aux);
        }
        
        return tagsNameAux;
    }
    
    private int isMayor(String a, String b){           
        String[] aAux = a.split("\\.");
        String[] bAux = b.split("\\.");
        Integer a1 = Integer.parseInt(aAux[1]);
        Integer a2 = Integer.parseInt(aAux[2]);
        Integer b1 = Integer.parseInt(bAux[1]);
        Integer b2 = Integer.parseInt(bAux[2]);
        if(a1 > b1 || a1 < b1){
            return a1.compareTo(b1);
        }else{
            return a2.compareTo(b2);
        }
    }
}
