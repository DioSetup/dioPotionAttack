package me.diocletianus.potionattack;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Main extends JavaPlugin implements Listener {
  private final String KORLUK_BASE_NAME = ChatColor.RED + "Körlük +";
  
  private final String YAVASLATMA_BASE_NAME = ChatColor.RED + "Yavaşlatma +";
  
  public void onEnable() {
    getLogger().info("PotionAttack aktif");
    getServer().getPluginManager().registerEvents(this, this);
  }
  
  public void onDisable() {
    getLogger().info("PotionAttack deaktif");
  }
  
  @EventHandler
  public void onEntityDamage(EntityDamageByEntityEvent event) {
    if (event.getDamager() instanceof Player) {
      Player player = (Player)event.getDamager();
      ItemStack[] inventory = player.getInventory().getContents();
      boolean hasKorluk = false;
      boolean hasYavaslatma = false;
      byte b;
      int i;
      ItemStack[] arrayOfItemStack1;
      for (i = (arrayOfItemStack1 = inventory).length, b = 0; b < i; ) {
        ItemStack item = arrayOfItemStack1[b];
        if (item != null && 
          item.getType() == Material.INK_SACK) {
          String itemName = item.getItemMeta().getDisplayName();
          if (itemName != null)
            if (itemName.startsWith(this.KORLUK_BASE_NAME)) {
              hasKorluk = true;
            } else if (itemName.startsWith(this.YAVASLATMA_BASE_NAME)) {
              hasYavaslatma = true;
            }  
        } 
        b++;
      } 
      if (hasKorluk && !hasYavaslatma) {
        int korlukLevel = getHighestLevel(inventory, this.KORLUK_BASE_NAME);
        Player damagedPlayer = (Player)event.getEntity();
        damagedPlayer.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, korlukLevel * 50, 0));
      } else if (!hasKorluk && hasYavaslatma) {
        int yavaslatmaLevel = getHighestLevel(inventory, this.YAVASLATMA_BASE_NAME);
        Player damagedPlayer = (Player)event.getEntity();
        damagedPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, yavaslatmaLevel * 50, 0));
      } 
    } 
  }
  
  private int getHighestLevel(ItemStack[] inventory, String baseName) {
    int highestLevel = 0;
    byte b;
    int i;
    ItemStack[] arrayOfItemStack;
    for (i = (arrayOfItemStack = inventory).length, b = 0; b < i; ) {
      ItemStack item = arrayOfItemStack[b];
      if (item != null && item.getType() == Material.INK_SACK) {
        String itemName = item.getItemMeta().getDisplayName();
        if (itemName != null && itemName.startsWith(baseName)) {
          int level = getItemLevel(itemName, baseName);
          highestLevel = Math.max(highestLevel, level);
        } 
      } 
      b++;
    } 
    return highestLevel;
  }
  
  private int getItemLevel(String itemName, String baseName) {
    String levelString = itemName.substring(baseName.length()).trim();
    try {
      return Integer.parseInt(levelString);
    } catch (NumberFormatException e) {
      return 0;
    } 
  }
}
