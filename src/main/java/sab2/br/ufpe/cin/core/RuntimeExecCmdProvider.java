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

    public String executeCommand(String command) throws MojoExecutionException {

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

}
