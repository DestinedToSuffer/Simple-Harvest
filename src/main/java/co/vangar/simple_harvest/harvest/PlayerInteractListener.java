package co.vangar.simple_harvest.harvest;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PlayerInteractListener implements Listener {

    @EventHandler
    public void hoeHarvest(PlayerInteractEvent e){
        Player p = e.getPlayer();
        ItemStack mainHandItem = p.getInventory().getItemInMainHand();
        String itemName = mainHandItem.getType().name().toLowerCase();
        if(e.getAction() == Action.RIGHT_CLICK_BLOCK && itemName.contains("hoe")) {
            Block block = e.getClickedBlock();
            ItemMeta meta = mainHandItem.getItemMeta();
            int fortune = (meta != null && meta.hasEnchant(Enchantment.LOOT_BONUS_BLOCKS)) ? meta.getEnchantLevel(Enchantment.LOOT_BONUS_BLOCKS) : 0;
            if(itemName.contains("diamond") || itemName.contains("nether")){
                if(Utils.isAreaHarvestable(block)){
                    List<Block> field = new ArrayList<>(Utils.getHarvestArea(block));
                    if(Utils.canBreakAll(mainHandItem, field.size())){
                        for(Block b : field) {
                            updateBlock(p, b, fortune);
                        }
                    } else {
                        updateBlock(p, block, fortune);
                    }
                }
            } else {
                updateBlock(p, block, fortune);
            }
        }
    }

    private void updateBlock(Player p, Block block, int fortune) {
        if(Utils.isHarvestable(block)){
            Collection<ItemStack> drops = block.getDrops();
            Location loc = block.getLocation();
            for (ItemStack is : drops){
                is.setAmount((int) (is.getAmount() * Utils.fortuneMulti(fortune)));
                p.getWorld().dropItem(loc, is);
            }
            block.setType(block.getType());
            Utils.durabilityDmg(p);
        }
    }
}
