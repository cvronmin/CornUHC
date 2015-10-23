package io.github.cvronmin.uhc;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.util.Vector;

public class EventHandler implements Listener{
	public void onPlayerDeath(PlayerDeathEvent event){
		if (event.getEntity().getStatistic(Statistic.DEATHS) <=1) {
			Bukkit.broadcastMessage("Player "+event.getEntity().getDisplayName()+"Death,death time:" + event.getEntity().getStatistic(Statistic.DEATHS));
		}
	}
	public static class CommandHandler implements CommandExecutor{
		private final CornUHC pluginMain;
		public CommandHandler(CornUHC cornUHC) {
			pluginMain = cornUHC;
		}
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
			if (cmd.getName().equalsIgnoreCase("requestpvp")){
				if(sender instanceof Player){
					if(sender.hasPermission("uhc.joinpvp") & sender.hasPermission("uhc.rqpvp")){
						if (args.length < 1){
							sender.sendMessage("missing arg:Player requested");
						}
						else if (!pluginMain.record.isPVPEnd()) {
							sender.sendMessage("PvP is not end yet!");
						}
						else{
							Player target = (Bukkit.getServer().getPlayer(args[0]));
							if(target == null){
								sender.sendMessage("Participant " + ChatColor.BLUE + args[0] + ChatColor.RESET + " undefined");
							}
							else if (!target.hasPermission("uhc.joinpvp")) {
								sender.sendMessage("Participant" + ChatColor.BLUE + args[0] + ChatColor.RESET + " has not permission to join pvp");
							}
							else if (target.equals(sender)) {
								sender.sendMessage("Inviting yourself is not allowed!");
							}
							else {
								pluginMain.record.newPvp();
								pluginMain.record.writePVPer(((Player) sender), target);
								sendRequestMessage(target, sender.getName());
								sendPVPMessage(sender, target);
								return true;
							}
						}
					}
				}
				else {
					sender.sendMessage("Non-player cannot use this command!");
				}
			}
			if (cmd.getName().equalsIgnoreCase("pvpyes")){
				if(sender instanceof Player){
					if(sender.hasPermission("uhc.joinpvp")){
						return true;
					}
				}
				else {
					sender.sendMessage("Non-player cannot use this command!");
				}
			}
			if (cmd.getName().equalsIgnoreCase("pvpno")){
				if(sender instanceof Player){
					pluginMain.record.writePVPEnd();
					return true;
				}
				else {
					sender.sendMessage("Non-player cannot use this command!");
				}
			}
			if (cmd.getName().equalsIgnoreCase("parenaset")){
				if (sender.hasPermission("uhc.setarenabase")) {
					if (args.length < 3){
						sender.sendMessage("missing arg:complete coordinate");
					}
					else {
						try {
							int x = Integer.parseInt(args[0]);
							int y = Integer.parseInt(args[1]);
							int z = Integer.parseInt(args[2]);
							Vector vec = new Vector(x, y, z);
							pluginMain.arena.setBase(vec);
							sender.sendMessage("set base");
							return true;
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				}
			}
			if (cmd.getName().equalsIgnoreCase("parenado")){
				if (sender.hasPermission("uhc.setarenabase")) {
					pluginMain.arena.generateArena(pluginMain.pvpArena);
				}
			}
			return false;
		}
		public void sendRequestMessage(Player target, String requester){
			target.sendMessage(ChatColor.DARK_GRAY+"=========================");
			target.sendMessage("Participant" + requester + "has requested you to join PVP with him");
			target.sendMessage("Please enter \" /pvpyes \" for joining PVP");
			target.sendMessage("Please enter \" /pvpno \" for not joining PVP");
			target.sendMessage(ChatColor.DARK_GRAY+"=========================");
		}
		public void sendPVPMessage(CommandSender requester, Player target){
			requester.sendMessage(ChatColor.DARK_GRAY+"=========================");
			requester.sendMessage("You have requested " + target.getDisplayName() + " to join PVP");
			requester.sendMessage(ChatColor.DARK_GRAY+"=========================");
		}
	}
}
