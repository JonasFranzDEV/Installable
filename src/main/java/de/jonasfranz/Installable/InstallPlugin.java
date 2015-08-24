package de.jonasfranz.Installable;

import de.jonasfranz.Installable.command.InstallCommandManager;


/**
 * Diese Datei unterliegt dem Urheberrecht und darf nur mit der Genehmigung des Autors verwendet, bearbeitet, veröffentlicht oder weitergegeben werden.
 * Autor des Quelltexts: ${user}
 * Sollten Zeilen bearbeitet oder hinzugefügt werden, muss dies per Kommentar gekennzeichnet werden.
 * Dieser Hinweis darf NICHT ENTFERNT werden!
 * Erstelldatum: ${date}
 * Projekt: Installable
 */
public interface InstallPlugin {


    public InstallCommandManager getCommandManager();

    public void sendMessage(String to, String msg);

    public static InstallPlugin instance = InstanceManager.instance;

    public String getInstallCommand();
}
