package org.aurora.tag.util;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedChatComponent;

/**
 * Handle holograms via packets.
 * TODO use ProtocolLib in the future to avoid version conflict.
 * @author RussianMushroom
 *
 */
public class HologramManager {
	
	public static void display(String title, String subtitle, Player player) {
		ProtocolManager pManager = ProtocolLibrary.getProtocolManager();
		final PacketContainer pTitleContainer = pManager.createPacket(PacketType.Play.Server.TITLE);
		final PacketContainer pSubtitleContainer = pManager.createPacket(PacketType.Play.Server.TITLE);
		
		pTitleContainer.getTitleActions().write(0, EnumWrappers.TitleAction.TITLE);
		pSubtitleContainer.getTitleActions().write(0, EnumWrappers.TitleAction.SUBTITLE);
		
		pTitleContainer.getChatComponents().write(0, WrappedChatComponent.fromText(ChatColor.GOLD + title));
		pSubtitleContainer.getChatComponents().write(0, WrappedChatComponent.fromText(ChatColor.YELLOW + subtitle));
		
		try {
			pManager.sendServerPacket(player, pTitleContainer);
			pManager.sendServerPacket(player, pSubtitleContainer);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}
