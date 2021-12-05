package sab2.br.ufpe.cin.core;

import java.io.BufferedReader;
import org.apache.maven.plugin.MojoExecutionException;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.inject.Named;
import javax.inject.Singleton;

@Named
@Singleton
public class RuntimeExecCmdProvider implements CmdProvider {

    private static String getPluginDir() {

    	String userDir = System.getProperty("user.home");
        String groupId = "sab2.br.ufpe.cin".replace(".", "/");
        String artifactId = "fast-maven-plugin";
        String version = "0.0.1-SNAPSHOT";

    	String pluginDir = String.format("%s/.m2/repository/%s/%s/%s", userDir, groupId, artifactId, version);

    	return pluginDir;
    }

    private static String executeComand(String command) throws MojoExecutionException {

    	ProcessBuilder processBuilder = new ProcessBuilder();

		// -- Linux --

		// Run a shell command
    	processBuilder.command("bash", "-c", command);

		try {

			Process process = processBuilder.start();

			StringBuilder output = new StringBuilder();

			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

			String line;

			while ((line = reader.readLine()) != null) {
				output.append(line + "\n");
			}

			int exitVal = process.waitFor();
			if (exitVal == 0) {
				return output.toString();
			} else {
				return "Error " + output.toString();
			}

		} catch (IOException | InterruptedException e) {
            throw new MojoExecutionException("Execution of command '" + command + "' failed", e);
        }
    }

	public String runCmdCommand(String command) throws MojoExecutionException {

		//check if python v3 if instaled
		String pythonVersion = executeComand("python3 --version");
		String version = pythonVersion.substring(0,8);

		if(!version.equals("Python 3")) {
			return "Python 3 não encontrado. A versão instalada é a " + version;
		}

		// verificar se o pip está instalado
		String pipVersion = executeComand("pip --version");
		String versionPip = pipVersion.substring(0,3);

		if(!versionPip.equals("pip")) {
			return "PIP não encontrado.";
		}

		// instalar o xxhash caso necessário
		String returnIntalationXxhash = executeComand("pip3 install -r " + getPluginDir() + "/FAST/requirements.txt");

		// executar a priorização
		String prioritizeFile = getPluginDir() + "/FAST/py/prioritize.py";

		String projectPath = System.getProperty("user.dir");;

     	String priorizationCommand = String.format("python3 %s %s %s", prioritizeFile, projectPath, command);

     	String returnOfPriorization = executeComand(priorizationCommand);

     	return returnOfPriorization;
    }

}
