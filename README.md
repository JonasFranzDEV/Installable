[![Build Status](https://travis-ci.org/JonasFranzDEV/Installable.svg?branch=master)](https://travis-ci.org/JonasFranzDEV/Installable)
# Installable
This is a lightweight Library that helps developers to create faster plugins with configurations. 

# German
Diese Library hilft Entwicklern leichter Plugins mit Konfigurationen zu erstellen. Außerdem erleichtert es den Serverownern die Konfigurierung Ihrer Plugins. 
Die Library verwandelt eine config.yml zu einem Inventar-Menü, welches der nutzer mit wenigen Klicks ingame bedienen kann. Um eine Variable zu speichern muss sie lediglich mit @Install gekennzeichnet werden. Dazu finden Sie in der unteren Anleitung (Coming soon) mehr.
## In Ihrem Plugin nutzen
Um das System in Ihren Plugin zu nutzen, müssen Sie Installable integrieren. Dafür gibt es vier Optionen:
### Mit Maven
#### Standalone
Fügen Sie dies zu Ihrer pom.xml datei hinzu:
```xml
<repositories>
  <repository>
    <id>releases</id>
    <url>http://maven.dein-plugin.de/nexus/content/repositories/releases</url>
  </repository>
</repositories>

<dependencies>
  <dependency>
    <groupId>de.jonasfranz</groupId>
    <artifactId>Installable</artifactId>
    <version>LATEST</version>
    <scope>provided</scope>
  </dependency>
</dependencies>
```
*Wenn Sie Development-Builds verwenden wollen, nutzen Sie:*
```xml
<repositories>
  <repository>
    <id>snapshots</id>
    <url>http://maven.dein-plugin.de/nexus/content/repositories/snapshots</url>
  </repository>
</repositories>

<dependencies>
  <dependency>
    <groupId>de.jonasfranz</groupId>
    <artifactId>Installable</artifactId>
    <version>1.0.18-SNAPSHOT</version>
    <scope>provided</scope>
  </dependency>
</dependencies>
```
#### Integriert
Fügen Sie dies zu Ihrer pom.xml hinzu. Fügen Sie außerdem das maven-shade-plugin hinzu, und konfigurieren Sie dieses.
```xml
<repositories>
  <repository>
    <id>releases</id>
    <url>http://maven.dein-plugin.de/nexus/content/repositories/releases</url>
  </repository>
</repositories>

<dependencies>
  <dependency>
    <groupId>de.jonasfranz</groupId>
    <artifactId>Installable</artifactId>
    <version>LATEST</version>
  </dependency>
</dependencies>
```
*Wenn Sie Development-Builds verwenden wollen, nutzen Sie:*
```xml
<repositories>
  <repository>
    <id>snapshots</id>
    <url>http://maven.dein-plugin.de/nexus/content/repositories/snapshots</url>
  </repository>
</repositories>

<dependencies>
  <dependency>
    <groupId>de.jonasfranz</groupId>
    <artifactId>Installable</artifactId>
    <version>1.0.18-SNAPSHOT</version>
  </dependency>
</dependencies>
```
### Ohne Maven
#### Standalone
Fügen Sie die JAR-Datei zum Buildpath hinzu. In Eclipse nutzen Sie dazu Properties > Java Build Path > Add external jar
## Installation
Nachdem Sie nun die Library hinzugefügt haben, können Sie beginnen das Plugin auf die Library anzupassen. Alles was Sie dazu tun müssen, wird hier erläutert:
1. Ersetzen Sie "extends JavaPlugin" in Ihrer Hauptklasse mit "extends BukkitInstallPlugin". **WICHTIG: Wenn Sie onEnable() oder onDisable() nutzen, müssen Sie am Ende super.onEnable(); bzw. super.onDisable(); hinzufügen!**
```java
import de.jonasfranz.Installable.bukkit.BukkitInstallPlugin;

public class IhrPlugin extends BukkitInstallPlugin {

}

```
2. Wenn Sie die Library nicht als "Standalone" nutzen, müssen Sie in der plugin.yml den Command "install" hinzufügen.
````yaml
name: Example
version: 1.0
author: Jonas Franz

main: path.to.IhrPlugin

commands:
  install:
````
3. Fügen Sie Variabeln hinzu, die gespeichert werden sollen. Fügen Sie oberhalb der Variabel die Install-Annotation hinzu. Geben Sie für "name" den Namen der Variabl ein, den der Benutzer nacher sehen soll. Geben Sie für "plugin" den in der plugin.yml aufgeführten Namen Ihres Plugins auf. **WICHTIG! Die Variable muss "public" sein, damit das System Zugriff auf diese hat.**
```java
import de.jonasfranz.Installable.bukkit.BukkitInstallPlugin;

public class IhrPlugin extends BukkitInstallPlugin {
	@Install(name = "Prefix", plugin = "Example")
	public static String prefix;
}

```
4. Damit das System die Klasse übernimmt, muss diese noch Installable implementieren und muss im InstallManager registriert werden. Setzen Sie als erstes Argument die Kategorie der Einstellung ein, in der es aufgeführt werden soll.
```java
import de.jonasfranz.Installable.bukkit.BukkitInstallPlugin;
import de.jonasfranz.Installable.Installabel;

public class IhrPlugin extends BukkitInstallPlugin implements Installabel{
	@Install(name = "Prefix", plugin = "Example")
	public static String prefix;
		@Override
	public void onEnable() {
		InstallManager.items.put("Example-Einstellungen", this);
		super.onEnable();
	}
}

```
5. Der Rest wird von der Library gemacht. Exportieren Sie das Plugin und starten Sie Ihren Server. Geben Sie /install ein, um das Plugin zu testen.

