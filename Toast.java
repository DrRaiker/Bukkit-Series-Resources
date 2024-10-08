package drraiker.beautifulplugin;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class Toast {

    private final NamespacedKey key;
    private final String icon;
    private final String message;
    private final boolean glint;
    private final int customModelData;
    private final Style style;

    private Toast(String icon, int customModelData, boolean glint, String message, Style style) {
        this.key = new NamespacedKey(BeautifulPlugin.getPlugin(), UUID.randomUUID().toString());
        this.icon = icon;
        this.message = message;
        this.style = style;
        this.customModelData = customModelData;
        this.glint = glint;
    }

    private void start(Player player) {
        createAdvancement();
        grantAdvancement(player);

        new BukkitRunnable() {
            @Override
            public void run() {
                revokeAdvancement(player);
            }
        }.runTaskLater(BeautifulPlugin.getPlugin(), 10L);
    }

    private void createAdvancement() {
        Bukkit.getUnsafe().loadAdvancement(key, "{\n" +
                "    \"criteria\": {\n" +
                "        \"trigger\": {\n" +
                "            \"trigger\": \"minecraft:impossible\"\n" +
                "        }\n" +
                "    },\n" +
                "    \"display\": {\n" +
                "        \"icon\": {\n" +
                "            \"id\": \"minecraft:" + icon + "\",\n" +
                "            \"components\": {" +
                "			 \"minecraft:custom_model_data\": " + customModelData + ",\n" +
                "            \"minecraft:enchantment_glint_override\": " + glint +
                "			 }\n" +
                "        },\n" +
                "        \"title\": {\n" +
                "            \"text\": \"" + message.replace("|", "\n") + "\"\n" +
                "        },\n" +
                "        \"description\": {\n" +
                "            \"text\": \"\"\n" +
                "        },\n" +
                "        \"background\": \"minecraft:textures/gui/advancements/backgrounds/adventure.png\",\n" +
                "        \"frame\": \"" + style.toString().toLowerCase() + "\",\n" +
                "        \"announce_to_chat\": false,\n" +
                "        \"show_toast\": true,\n" +
                "        \"hidden\": true\n" +
                "    },\n" +
                "    \"requirements\": [\n" +
                "        [\n" +
                "            \"trigger\"\n" +
                "        ]\n" +
                "    ]\n" +
                "}");
    }

    private void grantAdvancement(Player player) {
        player.getAdvancementProgress(Bukkit.getAdvancement(key)).awardCriteria("trigger");
    }

    private void revokeAdvancement(Player player) {
        player.getAdvancementProgress(Bukkit.getAdvancement(key)).revokeCriteria("trigger");
    }

    public static void displayTo(Player player, String icon, int customModelData, boolean glint, String message, Style style) {
        new Toast(icon, customModelData, glint, message, style).start(player);
    }

    public enum Style {
        GOAL,
        TASK,
        CHALLENGE
    }

}
