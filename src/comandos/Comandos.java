package comandos;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import Eventos.Inventario;
import main.Main;

public class Comandos implements CommandExecutor {
	
	private Main plugin;

	public Comandos(Main miPlugin) {
		this.plugin = miPlugin;
	}
	
	
	public boolean onCommand(CommandSender sender, Command comando, String label, String[] args) {
		// si se ejecuta desde la consola
		if (!(sender instanceof Player)) {
			Bukkit.getConsoleSender().sendMessage(plugin.nombre + "No se puede ejecutar desde la consola");
			return false;
		} 
		else {
			Player jugador = (Player) sender;		
			Inventario inv=new Inventario();
			inv.crearInventario(jugador);			
			return true;
		}
	}

}
