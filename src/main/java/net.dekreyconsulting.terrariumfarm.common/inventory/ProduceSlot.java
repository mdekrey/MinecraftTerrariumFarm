package net.dekreyconsulting.terrariumfarm.common;

import net.minecraft.inventory.Slot;
import net.minecraft.inventory.IInventory;

class ProduceSlot extends Slot {
	
	public ProduceSlot(IInventory inventory, int slotIndex, int x, int y) {
		super(inventory, slotIndex, x, y);
	}
	
    public boolean isItemValid(net.minecraft.item.ItemStack itemStack)
    {
        return false;
    }
	
    public int getSlotStackLimit()
    {
        // TODO - not actually letting us get above 64
        return 80;
    }
    
    public static boolean isValid(net.minecraft.item.ItemStack itemStack) {
        return itemStack.getItem() instanceof net.minecraft.item.ItemSeeds
            || itemStack.getItem() instanceof net.minecraft.item.ItemSeedFood;
    }
}