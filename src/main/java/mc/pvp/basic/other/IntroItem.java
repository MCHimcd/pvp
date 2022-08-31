package mc.pvp.basic.other;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class IntroItem {
    String name,item;
    List<String> lore;
    public IntroItem(String name,List<String> lore,String itemID){
        this.name=name;
        this.lore=new ArrayList<>(lore);
        item=itemID;
    }
    public ItemStack getItem(){
        ItemStack i=new ItemStack(Objects.requireNonNull(Material.getMaterial(item)));
        i.editMeta(itemMeta -> {
            itemMeta.displayName(Component.text(name));
            itemMeta.lore(lore.stream().map(Component::text).collect(Collectors.toList()));
        });
        return i;
    }
}
