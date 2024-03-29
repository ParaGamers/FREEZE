package main;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import Eventos.Inventario;
import comandos.Comandos;

public class Main extends JavaPlugin {	
PluginDescriptionFile pdffile = getDescription();	
	public String version =pdffile.getVersion();
	public String nombre = ChatColor.BLUE+pdffile.getName();
	

	
	//primero que se ejuca cunado se inicia el plugin
	//mensaje por consola cuando se activa el plugin
	public void onEnable() {
		Bukkit.getConsoleSender().sendMessage(nombre+ChatColor.RED+" <<>> Activado Freeze by PG :D  <<>>"+ version);
		comandosRegistrados();
		eventsRegistrados();

	}
	

	//desactiva el plugin
	public void onDisable() {
		Bukkit.getConsoleSender().sendMessage(nombre+ChatColor.RED+"Se desactivo el plugin Freeze by PG :("+ version);
	}
	
	private void comandosRegistrados() {
		this.getCommand("congelar").setExecutor(new Comandos(this));
		
	}
	
	public void eventsRegistrados() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new Inventario(), this);
	}

}
