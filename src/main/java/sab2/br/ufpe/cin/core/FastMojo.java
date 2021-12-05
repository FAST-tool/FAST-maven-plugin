package sab2.br.ufpe.cin.core;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import javax.inject.Inject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;


/**
 * An example Maven Mojo that resolves the current project's git revision and adds that a new {@code exampleVersion}
 * property to the current Maven project.
 */
@Mojo(name = "version", defaultPhase = LifecyclePhase.INITIALIZE)
public class FastMojo extends AbstractMojo {

    /**
     * The git command used to retrieve the current commit hash.
     */
    @Parameter(property = "alg.name", defaultValue = "FAST-pw")
    private String command;

    @Parameter(property = "project", readonly = true)
    private MavenProject project;
    
    @Inject
    private CmdProvider cmdProvider;
    
    private String fastMavenPluginFlagCMD = "[" + "\u001B[34m" + "\033[0;1m" + "FAST-maven-plugin" + "\033[0;0m" + "\u001B[0m" + "] ";
    
    private static boolean checkIfAlgIsValid(String algname)
    {
        if (algname.equals("FAST-pw") || algname.equals("FAST-one") || algname.equals("FAST-log") || algname.equals("FAST-sqrt") || algname.equals("FAST-all")) {
        	return true;
        } else {
        	return false;
        }
    }
    
    private static String getPluginDir() {
    	
    	String userDir = System.getProperty("user.home");
        String groupId = "sab2.br.ufpe.cin".replace(".", "/");
        String artifactId = "fast-maven-plugin";
        String version = "0.0.1-SNAPSHOT";
    	
    	String pluginDir = String.format("%s/.m2/repository/%s/%s/%s", userDir, groupId, artifactId, version);

    	return pluginDir;
    }
    
    public void cloneRepo()  {
    	
    	String repoUrl = "https://github.com/DinoSaulo/FAST-parameterized";
    	
    	String cloneDirectoryPath = getPluginDir() + "/FAST";
    	
    	File dir = new File(cloneDirectoryPath);
        if (dir.exists() && dir.isDirectory()){
        	
        	System.out.println(fastMavenPluginFlagCMD + "FAST repository already present");
        	
        } else {
        	try {
        	    System.out.println(fastMavenPluginFlagCMD + "Cloning FAST repository ("+ repoUrl + ") in this Maven environment");
        	    Git.cloneRepository()
        	        .setURI(repoUrl)
        	        .setDirectory(Paths.get(cloneDirectoryPath).toFile())
        	        .call();
        	    System.out.println(fastMavenPluginFlagCMD + "Completed Cloning");
        	} catch (GitAPIException e) {
        	    System.out.println(fastMavenPluginFlagCMD + "Exception occurred while cloning repo");
        	    e.printStackTrace();
        	}
        }
    }

    public void execute() throws MojoExecutionException, MojoFailureException {
    	
        
    	if (checkIfAlgIsValid(command)) {
    		
    		cloneRepo();
    		
    		String cmdReturn = cmdProvider.runCmdCommand(command);
    		System.out.println(fastMavenPluginFlagCMD + cmdReturn);
    		
    	} else {
    		 System.out.println(fastMavenPluginFlagCMD + "\u001B[31m" + "\033[0;1m" + "Invalid algorithm name" + "\033[0;0m" + "\u001B[0m");
    	}
    	
    }
    
    public static void main(String[] args) throws IOException, XmlPullParserException {
    	
        try {
            File file = new File(getPluginDir() + "/FAST");
            System.out.println(file.exists() && file.isDirectory());
         } catch(Exception e) {
            e.printStackTrace();
         }   	

    	System.out.println("artifact = ");
    	

	}

    
}
