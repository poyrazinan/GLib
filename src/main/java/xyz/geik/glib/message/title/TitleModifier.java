package xyz.geik.glib.message.title;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import xyz.geik.glib.utils.ColorAPI;

import java.lang.reflect.Constructor;
import java.util.Objects;

public class TitleModifier {

    protected TitleModifier() {
    }

    /**
     * Send titlebar message to player via packet
     *
     * @param player
     * @param title
     * @param subtitle
     * @param duration
     */
    protected void sendTitle(Player player, String title, String subtitle, int duration) {
        try {
            Object e;
            Object chatTitle;
            Object chatSubtitle;
            Constructor subtitleConstructor;
            Object titlePacket;
            Object subtitlePacket;

            int fadeIn = 20;
            int fadeOut = 20;
            duration = duration >= 60 ? duration - (fadeIn + fadeOut) : duration;

            if (title != null) {
                title = ColorAPI.colorize(title.replaceAll("%player%", player.getName()));

                // Times packets
                e = Objects.requireNonNull(getNMSClass("PacketPlayOutTitle")).getDeclaredClasses()[0].getField("TIMES").get(null);
                chatTitle = Objects.requireNonNull(getNMSClass("IChatBaseComponent")).getDeclaredClasses()[0].getMethod("a", new Class[]{String.class}).invoke(null, "{\"text\":\"" + title + "\"}");
                subtitleConstructor = Objects.requireNonNull(getNMSClass("PacketPlayOutTitle")).getConstructor(Objects.requireNonNull(getNMSClass("PacketPlayOutTitle")).getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE);
                titlePacket = subtitleConstructor.newInstance(e, chatTitle, fadeIn, duration, fadeOut);
                sendPacket(player, titlePacket);

                e = Objects.requireNonNull(getNMSClass("PacketPlayOutTitle")).getDeclaredClasses()[0].getField("TITLE").get(null);
                chatTitle = Objects.requireNonNull(getNMSClass("IChatBaseComponent")).getDeclaredClasses()[0].getMethod("a", new Class[]{String.class}).invoke(null, "{\"text\":\"" + title + "\"}");
                subtitleConstructor = Objects.requireNonNull(getNMSClass("PacketPlayOutTitle")).getConstructor(Objects.requireNonNull(getNMSClass("PacketPlayOutTitle")).getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"));
                titlePacket = subtitleConstructor.newInstance(e, chatTitle);
                sendPacket(player, titlePacket);
            }

            if (subtitle != null) {
                subtitle = ColorAPI.colorize(subtitle.replaceAll("%player%", player.getName()));

                // Times packets
                e = Objects.requireNonNull(getNMSClass("PacketPlayOutTitle")).getDeclaredClasses()[0].getField("TIMES").get(null);
                chatSubtitle = Objects.requireNonNull(getNMSClass("IChatBaseComponent")).getDeclaredClasses()[0].getMethod("a", new Class[]{String.class}).invoke(null, "{\"text\":\"" + title + "\"}");
                subtitleConstructor = Objects.requireNonNull(getNMSClass("PacketPlayOutTitle")).getConstructor(Objects.requireNonNull(getNMSClass("PacketPlayOutTitle")).getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE);
                subtitlePacket = subtitleConstructor.newInstance(e, chatSubtitle, fadeIn, duration, fadeOut);
                sendPacket(player, subtitlePacket);

                e = Objects.requireNonNull(getNMSClass("PacketPlayOutTitle")).getDeclaredClasses()[0].getField("SUBTITLE").get(null);
                chatSubtitle = Objects.requireNonNull(getNMSClass("IChatBaseComponent")).getDeclaredClasses()[0].getMethod("a", new Class[]{String.class}).invoke(null, "{\"text\":\"" + subtitle + "\"}");
                subtitleConstructor = Objects.requireNonNull(getNMSClass("PacketPlayOutTitle")).getConstructor(Objects.requireNonNull(getNMSClass("PacketPlayOutTitle")).getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE);
                subtitlePacket = subtitleConstructor.newInstance(e, chatSubtitle, fadeIn, duration, fadeOut);
                sendPacket(player, subtitlePacket);
            }
        } catch (Exception var11) {
            var11.printStackTrace();
        }
    }

    protected static void sendPacket(Player player, Object packet) {
        try {
            Object handle = player.getClass().getMethod("getHandle").invoke(player);
            Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
            playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected static Class<?> getNMSClass(String name) {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        try {
            return Class.forName("net.minecraft.server." + version + "." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
