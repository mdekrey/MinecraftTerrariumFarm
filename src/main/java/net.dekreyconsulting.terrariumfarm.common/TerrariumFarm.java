package net.dekreyconsulting.terrariumfarm.common;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
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

    @Instance(value = TerrariumFarm.MODID) //Tell Forge what instance to use.
    public static TerrariumFarm instance;
    
    public static Block terrariumBase;
        
    @EventHandler
    public void init(FMLInitializationEvent event) {
        terrariumBase = new TerrariumBaseBlock();
        
        GameRegistry.registerBlock(terrariumBase, "terrariumBase");
        
        GameRegistry.addRecipe(new ItemStack(terrariumBase), new Object[]{
        	"AAA",
        	"ABA",
        	"CDC",
        	'A', Blocks.glass,
            'B', Items.bucket,
            'C', Items.redstone,
            'D', Blocks.dirt
        });
    }
}