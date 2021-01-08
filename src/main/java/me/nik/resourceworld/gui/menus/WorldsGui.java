package me.nik.resourceworld.gui.menus;

import me.nik.resourceworld.ResourceWorld;
import me.nik.resourceworld.gui.Menu;
import me.nik.resourceworld.gui.PlayerMenuUtility;
import me.nik.resourceworld.managers.MsgType;
import me.nik.resourceworld.managers.custom.ResourceWorldType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class WorldsGui extends Menu {
    public WorldsGui(PlayerMenuUtility playerMenuUtility, ResourceWorld plugin) {
        super(playerMenuUtility, plugin);
    }

    @Override
    protected String getMenuName() {
        return MsgType.WORLDS_GUI_NAME.getMessage();
    }

    @Override
    protected int getSlots() {
        return 36;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        switch (e.getSlot()) {
            case 13:
                if (this.plugin.getResourceWorld(ResourceWorldType.RESOURCE_WORLD).reset()) {
                    p.closeInventory();
                }
                break;
            case 11:
                if (this.plugin.getResourceWorld(ResourceWorldType.RESOURCE_NETHER).reset()) {
                    p.closeInventory();
                }
                break;
            case 15:
                if (this.plugin.getResourceWorld(ResourceWorldType.RESOURCE_END).reset()) {
                    p.closeInventory();
                }
                break;
            case 31:
                p.closeInventory();
                new MainGui(playerMenuUtility, plugin).open();
                break;
        }
    }

    @Override
    protected void setMenuItems() {
        ItemStack resource = makeItem(Material.DIRT, 1, "&aResource World", null);
        ItemStack nether = makeItem(Material.NETHERRACK, 1, "&cNether World", null);
        ItemStack end = makeItem(Material.OBSIDIAN, 1, "&9End World", null);
        ItemStack back = makeItem(Material.BARRIER, 1, "&cBack", null);

        inventory.setItem(11, nether);
        inventory.setItem(13, resource);
        inventory.setItem(15, end);
        inventory.setItem(31, back);
    }
}