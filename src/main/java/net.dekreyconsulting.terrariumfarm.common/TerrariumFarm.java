package net.dekreyconsulting.terrariumfarm.common;

import net.dekreyconsulting.terrariumfarm.client.TerrariumFarmClientProxy;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

import net.minecraft.block.Block;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

@Mod(modid=TerrariumFarm.MODID, name=TerrariumFarm.MODNAME, version=TerrariumFarm.MODVER) //Tell forge "Oh hey, there's a new mod here to load."
public class TerrariumFarm
{
    //Set the ID of the mod (Should be lower case).
    public static final String MODID = "terrariumfarm";
    //Set the "Name" of the mod.
    public static final String MODNAME = "Terrarium Farm";
    //Set the version of the mod.
    public static final String MODVER = "0.0.1";

    public static int terrariumRenderType = 0;

    @Instance(value = TerrariumFarm.MODID) //Tell Forge what instance to use.
    public static TerrariumFarm instance;
    
    @SidedProxy(clientSide = "net.dekreyconsulting.terrariumfarm.client.TerrariumFarmClientProxy", serverSide = "net.dekreyconsulting.terrariumfarm.common.TerrariumFarmCommonProxy")
    public static TerrariumFarmCommonProxy proxy;

    @EventHandler
    public void init(FMLInitializationEvent event) {

        GameRegistry.registerBlock(TerrariumBlocks.base, "terrariumBase");
        GameRegistry.registerBlock(TerrariumBlocks.top, "terrariumTop");
        
        GameRegistry.registerItem(TerrariumItems.terrarium, "terrarium");
        
        GameRegistry.registerTileEntity(TileEntityTerrarium.class, "terrariumEntity");
        
        GameRegistry.addRecipe(new ItemStack(TerrariumItems.terrarium), new Object[]{
        	"AAA",
        	"ABA",
        	"CDC",
        	'A', Blocks.glass,
            'B', Items.water_bucket.setContainerItem(Items.bucket),
            'C', Blocks.iron_block,
            'D', Blocks.dirt
        });
        
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new TerrariumInventoryGuiHandler());
        
    }
}