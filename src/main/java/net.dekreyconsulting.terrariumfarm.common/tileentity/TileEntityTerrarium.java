package net.dekreyconsulting.terrariumfarm.common;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class TileEntityTerrarium extends TileEntity implements ISidedInventory
{
    private static final int[] SeedInventorySlots = new int[] {0};
    private static final int[] GroundInventorySlots = new int[] {1};
    private static final int[] ProduceInventorySlots = new int[] {2,3,4,5};
    private static final int[] AllInventorySlots = new int[] {0,1,2,3,4,5};
    private ItemStack[] inventory = new ItemStack[6];
    private String customName;
    private Item seedType;
    private Item groundType;
    private int growState;
    public net.minecraft.block.IGrowable growingBlock;
    
    
    // 1365 is a magic number of average number of ticks between getting a tick, 16*16*16/3 = 1365
    // BUT, we only grow 1/3 of the time if we use an optimal planting pattern (rows, all hydrated farmland)
    // And terrariums are packed, which drops it by another 1/2..
    // So, we'll use:
    private static final int GrowthRate = 16*16*16*2;
    // (If this is too fast, we can cut it down to 1/3 because it isn't hydrated, but we'll want to add water
    // being pumped in.)
    

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return this.inventory.length;
    }

    /**
     * Returns the stack in slot i
     */
    public ItemStack getStackInSlot(int slot)
    {
        return this.inventory[slot];
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     */
    public ItemStack decrStackSize(int slot, int numberToRemove)
    {
        if (this.inventory[slot] != null)
        {
            ItemStack var3;

            if (this.inventory[slot].stackSize <= numberToRemove)
            {
                var3 = this.inventory[slot];
                this.inventory[slot] = null;
                return var3;
            }
            else
            {
                var3 = this.inventory[slot].splitStack(numberToRemove);

                if (this.inventory[slot].stackSize == 0)
                {
                    this.inventory[slot] = null;
                }

                return var3;
            }
        }
        else
        {
            return null;
        }
    }

    /**
     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
     * like when you close a workbench GUI.
     */
    public ItemStack getStackInSlotOnClosing(int slot)
    {
        // copied from the furnace decompiled code - not sure why I would null the value out, it's going to get reset
        if (this.inventory[slot] != null)
        {
            ItemStack var2 = this.inventory[slot];
            this.inventory[slot] = null;
            return var2;
        }
        else
        {
            return null;
        }
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int slot, ItemStack contents)
    {
        this.inventory[slot] = contents;
    }

    /**
     * Returns the name of the inventory
     */
    public String getInventoryName()
    {
        return this.hasCustomInventoryName() ? this.customName : TerrariumFarm.MODID + "_" + "terrarium.container";
    }

    /**
     * Returns if the inventory name is localized
     */
    @Override
    public boolean hasCustomInventoryName()
    {
        return this.customName != null && this.customName.length() > 0;
    }

    public void setCustomName(String newName)
    {
        this.customName = newName;
    }

    public void readFromNBT(NBTTagCompound data)
    {
        super.readFromNBT(data);
        readSyncableDataFromNBT(data);
        if (data.hasKey("Items")) {
            NBTTagList var2 = data.getTagList("Items", 10);
            this.inventory = new ItemStack[this.getSizeInventory()];
    
            for (int var3 = 0; var3 < var2.tagCount(); ++var3)
            {
                NBTTagCompound var4 = var2.getCompoundTagAt(var3);
                byte var5 = var4.getByte("Slot");
    
                if (var5 >= 0 && var5 < this.inventory.length)
                {
                    this.inventory[var5] = ItemStack.loadItemStackFromNBT(var4);
                }
            }
        }
        
        if (inventory[0] != null) {
            seedType = inventory[0].getItem();
        }
        
        if (inventory[1] != null) {
            groundType = inventory[1].getItem();
        }

        if (data.hasKey("GrowState")) {
            this.growState = data.getInteger("GrowState");
        }
    }
    
    public void readSyncableDataFromNBT(NBTTagCompound data)
    {
        if (data.hasKey("CustomName"))
        {
            this.customName = data.getString("CustomName");
        }
        
        if (data.hasKey("GrowingBlock")) {
            this.growingBlock = (net.minecraft.block.IGrowable)Block.getBlockFromName(data.getString("GrowingBlock"));
        }
    }

    public void writeToNBT(NBTTagCompound data)
    {
        super.writeToNBT(data);
        writeSyncableDataToNBT(data);
        NBTTagList var2 = new NBTTagList();

        for (int var3 = 0; var3 < this.inventory.length; ++var3)
        {
            if (this.inventory[var3] != null)
            {
                NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte)var3);
                this.inventory[var3].writeToNBT(var4);
                var2.appendTag(var4);
            }
        }

        data.setTag("Items", var2);
        data.setInteger("GrowState", this.growState);
        
        if (this.hasCustomInventoryName())
        {
            data.setString("CustomName", this.customName);
        }
        
        if (growingBlock != null) {
            data.setString("GrowingBlock", Block.blockRegistry.getNameForObject(growingBlock));
        }
    }
    
    public void writeSyncableDataToNBT(NBTTagCompound data) {
        

        if (growingBlock != null) {
            data.setString("GrowingBlock", Block.blockRegistry.getNameForObject(growingBlock));
        }
    }

    /**
     * Returns the maximum stack size for a inventory slot.
     */
    public int getInventoryStackLimit()
    {
        // Choosing 80 because that's the number of dirt plots in a 9x9 farm
        return 80;
    }

    @Override
    public void updateEntity()
    {
        // check if our expected seed type matches actual seed type
        boolean needsNotify = false;
    
        int blockMetadata = 0;
        if (!worldObj.isRemote) {
            Item actualSeedType = null;
            Item actualGroundType = null;
            if (inventory[0] != null && inventory[1] != null) {
                actualSeedType = inventory[0].getItem();
                actualGroundType = inventory[1].getItem();
            }
            if (actualSeedType != seedType) {
                seedType = actualSeedType;
                growingBlock = null;
                this.growState = 0;
                needsNotify = true;
            }
            if (actualGroundType != groundType) {
                groundType = actualGroundType;
                growingBlock = null;
                this.growState = 0;
                needsNotify = true;
            }
            
            if (growingBlock == null && seedType != null && groundType != null) {
                //System.out.println(seedType.getUnlocalizedName() + " & " + groundType.getUnlocalizedName());
                growingBlock = getGrowingBlock(seedType, groundType);
                needsNotify = true;
            }
    
            if (growingBlock != null && inventory[1] != null) {
                
                this.growState += java.lang.Math.min(inventory[0].stackSize, inventory[1].stackSize);
                
                if (this.growState >= GrowthRate) {
                    int growth = this.growState / GrowthRate;
                    this.growState = this.growState % GrowthRate;
                    blockMetadata = worldObj.getBlockMetadata(xCoord, yCoord+1, zCoord);
                    if (blockMetadata < 0) {
                        blockMetadata = 0;
                    }
                
                    for (int i = 0; i < growth; i++) {
                        if (growingBlock.func_149851_a(worldObj, xCoord, yCoord+1, zCoord, worldObj.isRemote)) {
                            //System.out.println("Grow "+((Block)growingBlock).getUnlocalizedName()+"!");
                            // use bonemeal - not intended
                            //growingBlock.func_149853_b(worldObj, worldObj.rand, xCoord, yCoord+1, zCoord);
                            
                            blockMetadata++;
                            needsNotify = true;
                        } else {
                            // harvest
                            //System.out.println("Harvest "+((Block)growingBlock).getUnlocalizedName()+"!");
                            
                            // remove a seed!
                            if (--inventory[0].stackSize == 0) {
                                inventory[0] = null;
                            }
    
                            // pick up the stuff - first, scatter it!                        
                            ((Block)growingBlock).dropBlockAsItem(worldObj, xCoord, yCoord, zCoord, blockMetadata, 0);
                            
                            // second, find it!
                            net.minecraft.util.AxisAlignedBB myAABB = TerrariumBlocks.top.getCollisionBoundingBoxFromPool(worldObj, xCoord, yCoord, zCoord);
                            java.util.List entities = worldObj.getEntitiesWithinAABBExcludingEntity(null, myAABB);
                            
                            // third, destroy it and gather it!
                            for (int entityIndex = 0; entityIndex < entities.size(); ++entityIndex)
                            {
                                if (entities.get(entityIndex) instanceof net.minecraft.entity.item.EntityItem) {
                                    net.minecraft.entity.item.EntityItem item = (net.minecraft.entity.item.EntityItem)entities.get(entityIndex);
                                    item.setInvisible(true);
                                    worldObj.removeEntity(item);
                                    
                                    // gather the stuff!
                                    gatherItem(item.getEntityItem());
                                }
                            }
                                                    
                            blockMetadata = 0;
                            needsNotify = true;
                            break;
                        }
                    }
                }
            }
        }
        
        if (needsNotify) {
            if (growingBlock != null) {
                //System.out.println("Update "+((Block)growingBlock).getUnlocalizedName()+" with metadata " + blockMetadata);
            } else {
                blockMetadata = 0;
            }
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            worldObj.setBlockMetadataWithNotify(xCoord, yCoord+1, zCoord, blockMetadata, 2);
        }
    }
    
    private void gatherItem(ItemStack item) {
        if (SeedSlot.isValid(item) && canCombine(item, inventory[0])) {
            item = combine(item, 0);
        }
        
        for (int i = 0; i < ProduceInventorySlots.length && item != null; i++) {
            if (canCombine(item, inventory[ProduceInventorySlots[i]])) {
                item = combine(item, ProduceInventorySlots[i]);
            }
        }
    }
    
    private boolean canCombine(ItemStack item, ItemStack target) {
        if (target == null)
            return true;
        if (!target.isStackable())
            return false;
        
        return target.isItemEqual(item);
    }
    
    private ItemStack combine(ItemStack item, int targetIndex) {
        if (inventory[targetIndex] == null) {
            inventory[targetIndex] = item;
            return null;
        }
        
        int allowed = inventory[targetIndex].getMaxStackSize() - (inventory[targetIndex].stackSize);
        if (item.stackSize <= allowed) {
            inventory[targetIndex].stackSize += item.stackSize;
            return null;
        }
         
        inventory[targetIndex].stackSize += allowed;
        item.stackSize -= allowed;
        return item;
    }
    
    public net.minecraft.block.IGrowable getGrowingBlock(Item seeds, Item ground) {
        // This is a really ugly reflection hack - hopefully the instance only has two Blocks.
        // We're going to require one of them to return farmland; the other will be the one we return;
        boolean inFarmland = false;
        Item expectedGround = null;
        net.minecraft.block.IGrowable result = null;
        Class<?> target = seeds.getClass();
        while (!inFarmland && target != null) {
            java.lang.reflect.Field[] fields = target.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                if (fields[i].getType() == Block.class) {
                    fields[i].setAccessible(true);
                    try {
                        Block value = (Block)fields[i].get(seeds);
                        if (value instanceof net.minecraft.block.IGrowable) {
                            result = (net.minecraft.block.IGrowable)value;
                        }
                        else {
                            expectedGround = value.getItemDropped(0, worldObj.rand, 0);
                            if (expectedGround == ground) {
                                inFarmland = true;
                            }
                        } 
                    } catch (IllegalAccessException ex) {
                        continue;
                    }
                }
            }
            target = target.getSuperclass();
        }
        if (inFarmland) {
            return result;
        }
        return null;
    }
    
    /**
     * Do not make give this method the name canInteractWith because it clashes with Container
     */
    public boolean isUseableByPlayer(EntityPlayer player)
    {
        return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : player.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
    }

    public void openInventory() {
    }

    public void closeInventory() {
    }

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot.
     */
    public boolean isItemValidForSlot(int slot, ItemStack items)
    {
        // 0 - IPlantable only
        // 1 - dirt only
        // Others, none
        return (slot != 0 || SeedSlot.isValid(items))
            && (slot != 1 || GroundSlot.isValid(items));
    }

    /**
     * Returns an array containing the indices of the slots that can be accessed by automation on the given side of this
     * block.
     */
    public int[] getAccessibleSlotsFromSide(int side)
    {
        return AllInventorySlots;
    }

    /**
     * Returns true if automation can insert the given item in the given slot from the given side. Args: Slot, item,
     * side
     */
    public boolean canInsertItem(int slot, ItemStack stack, int side)
    {
        return this.isItemValidForSlot(slot, stack);
    }

    /**
     * Returns true if automation can extract the given item in the given slot from the given side. Args: Slot, item,
     * side
     */
    public boolean canExtractItem(int slot, ItemStack item, int side)
    {
        if (slot == 0 || slot == 1)
            return false; // Can't extract dirt or seed
        return true;
    }
    
    @Override
    public net.minecraft.network.Packet getDescriptionPacket()
    {
        if (!worldObj.isRemote) {
            NBTTagCompound syncData = new NBTTagCompound();
            this.writeSyncableDataToNBT(syncData);
            return new net.minecraft.network.play.server.S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, syncData);
        } else {
            return super.getDescriptionPacket();
        }
    }
    @Override
    public void onDataPacket(net.minecraft.network.NetworkManager net, net.minecraft.network.play.server.S35PacketUpdateTileEntity pkt)
    {
        if (worldObj.isRemote) {
            readFromNBT(pkt.func_148857_g());
            this.worldObj.notifyBlockChange(this.xCoord, this.yCoord + 1, this.zCoord, TerrariumBlocks.top);
        }
    }
}
