package br.ufpe.cin.fast_tool;

import org.apache.maven.plugin.MojoExecutionException;

public interface CmdProvider {
    String executeCommand(String command) throws MojoExecutionException;
}