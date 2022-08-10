package co.vangar.simple_harvest.harvest;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Utils {

    private Utils() {}

    private static final List<BlockFace> directories = Arrays.asList(
            BlockFace.NORTH,
            BlockFace.NORTH_EAST,
            BlockFace.NORTH_WEST,
            BlockFace.SOUTH,
            BlockFace.SOUTH_EAST,
            BlockFace.SOUTH_WEST,
            BlockFace.EAST,
            BlockFace.WEST
    );

    private static final Random random = new Random();


    public static int getRandomInt(Integer max){
        return random.nextInt(max);
    }

    public static boolean canBreakAll(ItemStack item, int i){
        Damageable dam = (Damageable) item.getItemMeta();
        if(dam != null && dam.hasDamage()){
            return (item.getType().getMaxDurability() - dam.getDamage()) > i;
        }
        return true;
    }

    public static void durabilityDmg(Player p){
        if(p.getGameMode() != GameMode.CREATIVE){
            ItemStack item = p.getInventory().getItemInMainHand();
            Damageable dam = (Damageable) item.getItemMeta();
            if (dam != null && dam.hasEnchants() && dam.hasEnchant(Enchantment.DURABILITY)) {
                int ranI = getRandomInt(100);
                switch (dam.getEnchantLevel(Enchantment.DURABILITY)) {
                    case 1 -> doDmgMethod(p, item, dam, ranI > 20);
                    case 2 -> doDmgMethod(p, item, dam, ranI > 27);
                    case 3 -> doDmgMethod(p, item, dam, ranI > 30);
                    default -> doDmgMethod(p, item, dam, true);
                }
            }
        }
    }

    private static void doDmgMethod(Player p, ItemStack item, Damageable dam, boolean isDmg) {
        if (isDmg) {
            if((item.getType().getMaxDurability() - dam.getDamage()) - 1 < 1){
                p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
                p.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                return;
            }
            dam.setDamage(dam.getDamage() + 1);
            item.setItemMeta(dam);
            p.getInventory().setItemInMainHand(item);
        }
    }

    public static double fortuneMulti(int fortuneLevel){
        int random = getRandomInt(20);

        switch (fortuneLevel) {
            case 0 -> { return random == 2 ? 1.5 : 1.0; }
            case 1 -> { return getFortuneMultiByRandom(random, 2.5, 1.5, 1.0); }
            case 2 -> { return getFortuneMultiByRandom(random, 2.5, 2.0, 1.0); }
            case 3 -> { return getFortuneMultiByRandom(random, 3.0, 2.0, 1.5); }
            default -> { return 1.0; }
        }
    }

    private static double getFortuneMultiByRandom(int random, double v1, double v2, double v3) {
        if (random == 20) {
            return v1;
        } else if (random >= 10) {
            return v2;
        }
        return v3;
    }

    public static boolean isHarvestable(Block b) {
        if(isAllowedBlockType(b)){
            return ((Ageable) b.getBlockData()).getAge() == 7;
        }
        return false;
    }

    public static boolean isAreaHarvestable(Block b){
        return !getHarvestArea(b).isEmpty();
    }

    private static boolean isAllowedBlockType(Block b) {
        Material type = b.getType();
        return type == Material.POTATOES || type == Material.CARROTS || type == Material.WHEAT || type == Material.BEETROOTS;
    }

    public static List<Block> getHarvestArea(Block b){
        List<Block> blocks = new ArrayList<>();
        if(isHarvestable(b)) {
            blocks.add(b);
        }
        for(BlockFace dir : directories){
            if(isHarvestable(b.getRelative(dir)) && !blocks.contains(b.getRelative(dir))) {
                blocks.add(b.getRelative(dir));
            }
        }
        return blocks;
    }
}
