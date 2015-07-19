package net.dekreyconsulting.terrariumfarm.common;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class TerrariumBaseBlock extends Block {
	
	public TerrariumBaseBlock() {
		super(Material.ground);
		setBlockName(TerrariumFarm.MODID + "_" + "terrariumBaseBlock");
		setCreativeTab(CreativeTabs.tabBlock);
		setHardness(5.6F);
		setStepSound(Block.soundTypeGravel);
	}
	
}
