package de.jonasfranz.Installable.command;


public interface InstallCommandManager {
    public void registerArgument(String arg, InstallArgument iArg);

    public static interface InstallArgument {


        public void execute(String args[], String sender);

    }
}
