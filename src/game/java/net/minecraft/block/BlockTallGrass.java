package net.minecraft.block;

import java.util.List;
import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**+
 * This portion of EaglercraftX contains deobfuscated Minecraft 1.8 source code.
 * 
 * Minecraft 1.8.8 bytecode is (c) 2015 Mojang AB. "Do not distribute!"
 * Mod Coder Pack v9.18 deobfuscation configs are (c) Copyright by the MCP Team
 * 
 * EaglercraftX 1.8 patch files (c) 2022-2025 lax1dude, ayunami2000. All Rights Reserved.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 */
public class BlockTallGrass extends BlockBush implements IGrowable {
	public static PropertyEnum<BlockTallGrass.EnumType> TYPE;

	protected BlockTallGrass() {
		super(Material.vine);
		this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, BlockTallGrass.EnumType.DEAD_BUSH));
		float f = 0.4F;
		this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.8F, 0.5F + f);
	}

	public static void bootstrapStates() {
		TYPE = PropertyEnum.<BlockTallGrass.EnumType>create("type", BlockTallGrass.EnumType.class);
	}

	public int getBlockColor() {
		return ColorizerGrass.getGrassColor(0.5D, 1.0D);
	}

	public boolean canBlockStay(World world, BlockPos blockpos, IBlockState var3) {
		return this.canPlaceBlockOn(world.getBlockState(blockpos.down()).getBlock());
	}

	/**+
	 * Whether this Block can be replaced directly by other blocks
	 * (true for e.g. tall grass)
	 */
	public boolean isReplaceable(World var1, BlockPos var2) {
		return true;
	}

	public int getRenderColor(IBlockState iblockstate) {
		if (iblockstate.getBlock() != this) {
			return super.getRenderColor(iblockstate);
		} else {
			BlockTallGrass.EnumType blocktallgrass$enumtype = (BlockTallGrass.EnumType) iblockstate.getValue(TYPE);
			return blocktallgrass$enumtype == BlockTallGrass.EnumType.DEAD_BUSH ? 16777215
					: ColorizerGrass.getGrassColor(0.5D, 1.0D);
		}
	}

	public int colorMultiplier(IBlockAccess iblockaccess, BlockPos blockpos, int var3) {
		return iblockaccess.getBiomeGenForCoords(blockpos).getGrassColorAtPos(blockpos);
	}

	/**+
	 * Get the Item that this Block should drop when harvested.
	 */
	public Item getItemDropped(IBlockState var1, EaglercraftRandom random, int var3) {
		return random.nextInt(8) == 0 ? Items.wheat_seeds : null;
	}

	/**+
	 * Get the quantity dropped based on the given fortune level
	 */
	public int quantityDroppedWithBonus(int i, EaglercraftRandom random) {
		return 1 + random.nextInt(i * 2 + 1);
	}

	public void harvestBlock(World world, EntityPlayer entityplayer, BlockPos blockpos, IBlockState iblockstate,
			TileEntity tileentity) {
		if (!world.isRemote && entityplayer.getCurrentEquippedItem() != null
				&& entityplayer.getCurrentEquippedItem().getItem() == Items.shears) {
			entityplayer.triggerAchievement(StatList.mineBlockStatArray[Block.getIdFromBlock(this)]);
			spawnAsEntity(world, blockpos, new ItemStack(Blocks.tallgrass, 1,
					((BlockTallGrass.EnumType) iblockstate.getValue(TYPE)).getMeta()));
		} else {
			super.harvestBlock(world, entityplayer, blockpos, iblockstate, tileentity);
		}

	}

	public int getDamageValue(World world, BlockPos blockpos) {
		IBlockState iblockstate = world.getBlockState(blockpos);
		return iblockstate.getBlock().getMetaFromState(iblockstate);
	}

	/**+
	 * returns a list of blocks with the same ID, but different meta
	 * (eg: wood returns 4 blocks)
	 */
	public void getSubBlocks(Item item, CreativeTabs var2, List<ItemStack> list) {
		for (int i = 1; i < 3; ++i) {
			list.add(new ItemStack(item, 1, i));
		}

	}

	/**+
	 * Whether this IGrowable can grow
	 */
	public boolean canGrow(World var1, BlockPos var2, IBlockState iblockstate, boolean var4) {
		return iblockstate.getValue(TYPE) != BlockTallGrass.EnumType.DEAD_BUSH;
	}

	public boolean canUseBonemeal(World var1, EaglercraftRandom var2, BlockPos var3, IBlockState var4) {
		return true;
	}

	public void grow(World world, EaglercraftRandom var2, BlockPos blockpos, IBlockState iblockstate) {
		BlockDoublePlant.EnumPlantType blockdoubleplant$enumplanttype = BlockDoublePlant.EnumPlantType.GRASS;
		if (iblockstate.getValue(TYPE) == BlockTallGrass.EnumType.FERN) {
			blockdoubleplant$enumplanttype = BlockDoublePlant.EnumPlantType.FERN;
		}

		if (Blocks.double_plant.canPlaceBlockAt(world, blockpos)) {
			Blocks.double_plant.placeAt(world, blockpos, blockdoubleplant$enumplanttype, 2);
		}

	}

	/**+
	 * Convert the given metadata into a BlockState for this Block
	 */
	public IBlockState getStateFromMeta(int i) {
		return this.getDefaultState().withProperty(TYPE, BlockTallGrass.EnumType.byMetadata(i));
	}

	/**+
	 * Convert the BlockState into the correct metadata value
	 */
	public int getMetaFromState(IBlockState iblockstate) {
		return ((BlockTallGrass.EnumType) iblockstate.getValue(TYPE)).getMeta();
	}

	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { TYPE });
	}

	/**+
	 * Get the OffsetType for this Block. Determines if the model is
	 * rendered slightly offset.
	 */
	public Block.EnumOffsetType getOffsetType() {
		return Block.EnumOffsetType.XYZ;
	}

	public static enum EnumType implements IStringSerializable {
		DEAD_BUSH(0, "dead_bush"), GRASS(1, "tall_grass"), FERN(2, "fern");

		private static final BlockTallGrass.EnumType[] META_LOOKUP = new BlockTallGrass.EnumType[3];
		private final int meta;
		private final String name;

		private EnumType(int meta, String name) {
			this.meta = meta;
			this.name = name;
		}

		public int getMeta() {
			return this.meta;
		}

		public String toString() {
			return this.name;
		}

		public static BlockTallGrass.EnumType byMetadata(int meta) {
			if (meta < 0 || meta >= META_LOOKUP.length) {
				meta = 0;
			}

			return META_LOOKUP[meta];
		}

		public String getName() {
			return this.name;
		}

		static {
			BlockTallGrass.EnumType[] types = values();
			for (int i = 0; i < types.length; ++i) {
				META_LOOKUP[types[i].getMeta()] = types[i];
			}

		}
	}
}