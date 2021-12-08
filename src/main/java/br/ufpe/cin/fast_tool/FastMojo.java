package br.ufpe.cin.fast_tool;

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


@Mojo(name = "FAST", defaultPhase = LifecyclePhase.GENERATE_TEST_RESOURCES)
public class FastMojo extends AbstractMojo {

    @Parameter(property = "alg.name", defaultValue = "FAST-pw")
    private String algorithm;

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
        String groupId = "br.ufpe.cin.fast-tool".replace(".", "/");
        String artifactId = "fast-maven-plugin";
        String version = "1.0.0";

    	String pluginDir = String.format("%s/.m2/repository/%s/%s/%s", userDir, groupId, artifactId, version);

    	return pluginDir;
    }

    public void cloneRepo()  {

    	String repoUrl = "https://github.com/FAST-tool/maven-FAST";

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

    	if (checkIfAlgIsValid(algorithm)) {

    		cloneRepo();

    		//check if python v3 if instaled
    		String pythonVersion = cmdProvider.executeCommand("python3 --version");
    		String version = pythonVersion.substring(0,8);

    		if(!version.equals("Python 3")) {
    			getLog().error("Python 3 not found. The installed version is the " + version);
    		} else {
    			// check if the pip is installed
        		String pipVersion = cmdProvider.executeCommand("pip --version");
        		String versionPip = pipVersion.substring(0,3);
        		if(!versionPip.equals("pip")) {
        			getLog().error("PIP not found. Please install the PIP.");
        		}

        		// install dependencies if necessary
        		String returnIntalationXxhash = cmdProvider.executeCommand("pip3 install -r " + getPluginDir() + "/FAST/requirements.txt");

        		// perform prioritization
        		String prioritizeFile = getPluginDir() + "/FAST/py/prioritize.py";

        		String projectPath = System.getProperty("user.dir");;

             	String priorizationCommand = String.format("python3 %s %s %s", prioritizeFile, projectPath, algorithm);

             	String returnOfPriorization = cmdProvider.executeCommand(priorizationCommand);

             	boolean successOrFail = returnOfPriorization.contains("Test case prioritization completed");

             	if(successOrFail) {
             		getLog().info(returnOfPriorization);
             	} else if(returnOfPriorization.contains("No modifications were found in the project tests")){
             		getLog().warn(returnOfPriorization);
             	} else {
             		getLog().error(returnOfPriorization);
             	}
    		}

    	} else {
    		getLog().error( algorithm + " - Invalid algorithm name");
    		getLog().error("Options: FAST-pw, FAST-one, FAST-log, FAST-sqrt, FAST-all");
    	}

    }

}
