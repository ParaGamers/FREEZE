package Eventos;

import java.awt.desktop.SystemSleepListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.BatToggleSleepEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.mojang.datafixers.types.families.ListAlgebra;

import net.md_5.bungee.api.ChatColor;

public class Inventario implements Listener {

	private List<Player> jugadoresCongelados = new ArrayList<Player>();
	private List<Location> jugadoresLocacion = new ArrayList<Location>();

	public void crearInventario(Player jugador) {

		Inventory inv = Bukkit.createInventory(null, 45, ChatColor.translateAlternateColorCodes('&', "&2A frezear"));
		ItemStack item = new ItemStack(Material.ICE);

		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6FreezeadorINADOR"));
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&7Congela todos los usuarios :)"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		inv.setItem(20, item);

		item = new ItemStack(Material.LAVA_BUCKET);
		meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Descongelar"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&7Descongela todos los usuarios :("));
		meta.setLore(lore);
		item.setItemMeta(meta);
		inv.setItem(22, item);

		item = new ItemStack(Material.PLAYER_HEAD);
		meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Ver usuarios"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&7Congelar por usuario :("));
		meta.setLore(lore);
		item.setItemMeta(meta);
		inv.setItem(24, item);

		ItemStack decoracion = new ItemStack(Material.BLUE_STAINED_GLASS_PANE);
		int aux = 0;
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 9; j++) {
				if (i % (5 - 1) != 0 && j % (9 - 1) != 0) {
					aux++;
				} else {
					inv.setItem(aux, decoracion);
					aux++;
				}
			}
		}
		jugador.openInventory(inv);
		return;

	}

	@EventHandler
	public void clickearInventario(InventoryClickEvent event) throws InterruptedException {
		String nombre = ChatColor.translateAlternateColorCodes('&', "&2A frezear");
		String nombreM = ChatColor.stripColor(nombre);
		Player jugador = (Player) event.getWhoClicked();
		if (ChatColor.stripColor(event.getView().getTitle()).equals(nombreM)) {
			if (event.getCurrentItem() == null || event.getSlotType() == null
					|| event.getCurrentItem().getType() == Material.AIR) {
				event.setCancelled(true);
				return;
			} else {
				if (event.getCurrentItem().hasItemMeta()) {
					event.setCancelled(true);

					if (event.getSlot() == 20) {
						if (jugador.hasPermission("Freezeador.puedeFrezear")) {
							try {
								Collection<Player> players = (Collection<Player>) Bukkit.getOnlinePlayers();
								for (Player listJugador : players) {

									if (listJugador.hasPermission("freezeador.inmune") == false) {

										jugadoresCongelados.add(listJugador);
										// jugadoresLocacion.add(listJugador.getLocation());
										listJugador.closeInventory();
										listJugador.setWalkSpeed(0.0f);
										listJugador.setAllowFlight(false);
										listJugador.setFlySpeed(0.0f);
										listJugador.getLocation();

										listJugador.sendMessage(
												ChatColor.translateAlternateColorCodes('&', "&cfreezeado"));

										// onMove(null);

									} else if (listJugador.hasPermission("freezeador.notify")
											&& jugadoresCongelados.size() > 0) {
										listJugador.sendMessage(
												ChatColor.translateAlternateColorCodes('&', "&6" + jugador.getName()
														+ " &aA congelado a &c" + jugadoresCongelados.size()));

									}

									else {

										listJugador.sendMessage("no te peudes congelar.... supuestamente");

									}

								}

								jugador.sendMessage(ChatColor.translateAlternateColorCodes('&',
										"&dCantidad de jugadores Congelados " + jugadoresCongelados.size()));
							} catch (Exception e) {
								e.printStackTrace();
							}
							return;
						} else {
							jugador.sendMessage("No tienes permiso :(");
							return;
						}

					} else if (event.getSlot() == 22) {
						if (jugador.hasPermission("freezeador.puedeFrezear")) {
							jugador.sendMessage(ChatColor.translateAlternateColorCodes('&',
									"&dSe descongelaron " + jugadoresCongelados.size() + " jugadores"));

							Collection<Player> players = (Collection<Player>) Bukkit.getOnlinePlayers();

							for (Player listJugador : players) {
								if (listJugador.hasPermission("freezeador.notify")) {
									listJugador.sendMessage(
											ChatColor.translateAlternateColorCodes('&', "&6" + jugador.getName()
													+ " &aA descongelado a &c" + jugadoresCongelados.size()));

								}

								if (jugadoresCongelados.contains(listJugador)) {
									jugador.sendMessage("cantidad de usuarios  " + players.size());
									listJugador.setWalkSpeed(0.2f);
									listJugador.closeInventory();
									listJugador.setFlySpeed(0.2f);
									listJugador.setAllowFlight(true);

									// onMove(null);
									listJugador.sendMessage(
											ChatColor.translateAlternateColorCodes('&', "&dDescongelado :)"));
									jugadoresCongelados.remove(listJugador);
								}
							}
							return;
						} else {
							jugador.sendMessage("No tienes permiso :(");
							return;
						}

					} else if (event.getSlot() == 24) {

						if (jugador.hasPermission("freezeador.puedeFrezear")) {
							inventarioUsuarios(jugador, 20);
							return;
						} else {
							jugador.sendMessage("No tienes permiso :(");
							return;
						}

					}

					else {
						return;
					}

				} else {
					event.setCancelled(true);

				}

			}

//----------  bloaquea accion en el invetario-------------------------//

		} else if (ChatColor.stripColor(event.getView().getTitle()).equals("A frezear por jugador")) {
			if (event.getCurrentItem() == null || event.getSlotType() == null
					|| event.getCurrentItem().getType() == Material.AIR) {
				event.setCancelled(true);
				return;
			} else {
				if (event.getCurrentItem().hasItemMeta()) {
					event.setCancelled(true);

					Collection<Player> players = (Collection<Player>) Bukkit.getOnlinePlayers();

					if(event.getSlot() == 33) {//cuando oprimen atras
						crearInventario(jugador);		
						
					}
					
					if (jugador.hasPermission("freezeador.puedeFrezear")) {
						for (Player listJugadores : players) {
							if (listJugadores.getName().equals(event.getCurrentItem().getItemMeta().getDisplayName())) {
								if (listJugadores.hasPermission("freezeador.inmune") == false) {
								//descongelar por usuario
									if (jugadoresCongelados.contains(listJugadores)) {
										jugador.sendMessage("Usuario descongealdo:  " + listJugadores);
										listJugadores.setWalkSpeed(0.2f);
										listJugadores.closeInventory();
										listJugadores.setFlySpeed(0.2f);
										// onMove(null);
										listJugadores.sendMessage(
												ChatColor.translateAlternateColorCodes('&', "&dDescongelado :)"));
										jugadoresCongelados.remove(listJugadores);
									
								//congelar por usuario	
									} else {
										jugadoresCongelados.add(listJugadores);
										// jugadoresLocacion.add(listJugador.getLocation());
										listJugadores.closeInventory();
										listJugadores.setWalkSpeed(0.0f);
										listJugadores.setAllowFlight(false);
										listJugadores.setFlySpeed(0.0f);
										listJugadores.getLocation();
										listJugadores.sendMessage(
												ChatColor.translateAlternateColorCodes('&', "&cfreezador"));
									}

								} else if (listJugadores.hasPermission("freezeador.notify")
										&& jugadoresCongelados.size() > 0) {
									listJugadores.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6"
											+ jugador.getName() + " &aA congelado a &c" + jugadoresCongelados.size()));

								}
							}
						}
					} else {
						jugador.sendMessage("No tienes permiso :(");
					}

				} else {
					event.setCancelled(true);

				}

			}
		}

	}
	

	// ----------------- Inventario por usuarios -----

	private void inventarioUsuarios(Player jugador, int cantJugadores) {

		Inventory inv = Bukkit.createInventory(null, 45,
				ChatColor.translateAlternateColorCodes('&', "&2A frezear por jugador"));

		Collection<Player> players = (Collection<Player>) Bukkit.getOnlinePlayers();
		//si hay mas de 19 usaurios crea un nuevo inventario de manera recursiva
		if (players.size() >= 19) {
			ItemStack item = new ItemStack(Material.ARROW);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Pagina siguiente"));
			List<String> lore = new ArrayList<String>();
			lore.add(ChatColor.translateAlternateColorCodes('&', "&7Busca entre mas usuarios:)"));
			meta.setLore(lore);
			item.setItemMeta(meta);
			inv.setItem(33, item);

			ItemStack itemJugador = new ItemStack(Material.PLAYER_HEAD);
			int aux = 0;
			int cant = cantJugadores;
			int pos = 0;
			for (int i = 0; i < 5; i++) {
				for (int j = 0; j < 9; j++) {
					if (i % (5 - 1) != 0 && j % (9 - 1) != 0) {//cabezas en el centro
						cant++;
						pos++;
						if (pos <= 19) {
							if (cant <= players.size()) {
								if (((List<Player>) players).get(cant - 1)
										.hasPermission("freezeador.inmune") == false) {
									meta = itemJugador.getItemMeta();
									meta.setDisplayName(ChatColor.translateAlternateColorCodes('&',
											"&6" + ((List<Player>) players).get(cant - 1).getName()));
									lore = new ArrayList<String>();
									lore.add(ChatColor.translateAlternateColorCodes('&', "&7Usuario " + cant));
									meta.setLore(lore);
									itemJugador.setItemMeta(meta);
									inv.setItem(aux, itemJugador);
									aux++;

								}
							}
						} else {
							// inventarioUsuarios(jugador, cant);
						}

					} else {

						aux++;
					}
				}
			}
			//inventario de usaurios con menos de 19 usuarios
		} else {
			ItemStack itemJugador = new ItemStack(Material.PLAYER_HEAD);
			int aux = 0;
			int cant = 0;

			for (int i = 0; i < 5; i++) {
				for (int j = 0; j < 9; j++) {
					if (i % (5 - 1) != 0 && j % (9 - 1) != 0) {
						cant++;
						if (cant <= players.size()) {
							if (((List<Player>) players).get(cant - 1).hasPermission("freezeador.inmune") == false) {
								ItemMeta meta = itemJugador.getItemMeta();
								meta.setDisplayName(ChatColor.translateAlternateColorCodes('&',
										"&6" + ((List<Player>) players).get(cant - 1).getName()));
								List<String> lore = new ArrayList<String>();
								lore.add(ChatColor.translateAlternateColorCodes('&', "&7Usuario " + cant));
								meta.setLore(lore);
								itemJugador.setItemMeta(meta);
								inv.setItem(aux, itemJugador);
								aux++;

							}
						}

					} else {

						aux++;
					}
				}
			}

		}
		
		ItemStack itemRetroceder = new ItemStack(Material.BONE);
		ItemMeta meta = itemRetroceder.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&6Retroceder"));
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&7Volver al freezeadorINADOR "));
		meta.setLore(lore);
		itemRetroceder.setItemMeta(meta);
		inv.setItem(33, itemRetroceder);
		
		ItemStack decoracion = new ItemStack(Material.BLUE_STAINED_GLASS_PANE);
		int aux = 0;
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 9; j++) {
				if (i % (5 - 1) != 0 && j % (9 - 1) != 0) {
					aux++;
				} else {
					inv.setItem(aux, decoracion);
					aux++;
				}
			}
		}
		jugador.openInventory(inv);
		return;

	}

	@EventHandler
	public void onMove(PlayerMoveEvent e) throws InterruptedException {
		//congela a los usuarios que estan en la lista

		if (jugadoresCongelados.contains(e.getPlayer())) {

			Location L = new Location(e.getPlayer().getWorld(), e.getPlayer().getLocation().getX(),
					e.getPlayer().getLocation().getY(), e.getPlayer().getLocation().getZ(),
					e.getPlayer().getLocation().getYaw(), e.getPlayer().getLocation().getPitch());
			e.getPlayer().teleport(L);
			e.getPlayer()
					.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNo te puedes mover, estas congelado"));
			e.wait(500);
			// e.setCancelled(true);

		} else {

			e.setCancelled(false);
		}
	}

}
