package net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.block;

import com.google.common.base.Predicate;
import java.util.List;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.block.Block;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.block.BlockLeaves;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.block.BlockPlanks;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.block.properties.IProperty;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.block.properties.PropertyEnum;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.block.state.BlockState;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.block.state.IBlockState;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.creativetab.CreativeTabs;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.entity.player.EntityPlayer;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.init.Items;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.item.Item;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.item.ItemStack;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.stats.StatList;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.tileentity.TileEntity;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.util.BlockPos;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.world.World;

import static net.lax1dude.eaglercraft.v1_8.sp.server.classes.ContextUtil.__checkIntegratedContextValid;

/**+
 * This portion of EaglercraftX contains deobfuscated Minecraft 1.8 source code.
 * 
 * Minecraft 1.8.8 bytecode is (c) 2015 Mojang AB. "Do not distribute!"
 * Mod Coder Pack v9.18 deobfuscation configs are (c) Copyright by the MCP Team
 * 
 * EaglercraftX 1.8 patch files (c) 2022-2024 lax1dude, ayunami2000. All Rights Reserved.
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
public class BlockNewLeaf extends BlockLeaves {

	static {
		__checkIntegratedContextValid("net/minecraft/block/BlockNewLeaf");
	}

	public static PropertyEnum<BlockPlanks.EnumType> VARIANT;

	public BlockNewLeaf() {
		this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, BlockPlanks.EnumType.ACACIA)
				.withProperty(CHECK_DECAY, Boolean.valueOf(true)).withProperty(DECAYABLE, Boolean.valueOf(true)));
	}

	public static void bootstrapStates() {
		VARIANT = PropertyEnum.create("variant", BlockPlanks.EnumType.class, new Predicate<BlockPlanks.EnumType>() {
			public boolean apply(BlockPlanks.EnumType blockplanks$enumtype) {
				return blockplanks$enumtype.getMetadata() >= 4;
			}
		});
	}

	protected void dropApple(World world, BlockPos blockpos, IBlockState iblockstate, int i) {
		if (iblockstate.getValue(VARIANT) == BlockPlanks.EnumType.DARK_OAK && world.rand.nextInt(i) == 0) {
			spawnAsEntity(world, blockpos, new ItemStack(Items.apple, 1, 0));
		}
	}

	/**+
	 * Gets the metadata of the item this Block can drop. This
	 * method is called when the block gets destroyed. It returns
	 * the metadata of the dropped item based on the old metadata of
	 * the block.
	 */
	public int damageDropped(IBlockState iblockstate) {
		return ((BlockPlanks.EnumType) iblockstate.getValue(VARIANT)).getMetadata();
	}

	public int getDamageValue(World world, BlockPos blockpos) {
		IBlockState iblockstate = world.getBlockState(blockpos);
		return iblockstate.getBlock().getMetaFromState(iblockstate) & 3;
	}

	/**+
	 * returns a list of blocks with the same ID, but different meta
	 * (eg: wood returns 4 blocks)
	 */
	public void getSubBlocks(Item item, CreativeTabs var2, List<ItemStack> list) {
		list.add(new ItemStack(item, 1, 0));
		list.add(new ItemStack(item, 1, 1));
	}

	protected ItemStack createStackedBlock(IBlockState iblockstate) {
		return new ItemStack(Item.getItemFromBlock(this), 1,
				((BlockPlanks.EnumType) iblockstate.getValue(VARIANT)).getMetadata() - 4);
	}

	/**+
	 * Convert the given metadata into a BlockState for this Block
	 */
	public IBlockState getStateFromMeta(int i) {
		return this.getDefaultState().withProperty(VARIANT, this.getWoodType(i))
				.withProperty(DECAYABLE, Boolean.valueOf((i & 4) == 0))
				.withProperty(CHECK_DECAY, Boolean.valueOf((i & 8) > 0));
	}

	/**+
	 * Convert the BlockState into the correct metadata value
	 */
	public int getMetaFromState(IBlockState iblockstate) {
		int i = 0;
		i = i | ((BlockPlanks.EnumType) iblockstate.getValue(VARIANT)).getMetadata() - 4;
		if (!((Boolean) iblockstate.getValue(DECAYABLE)).booleanValue()) {
			i |= 4;
		}

		if (((Boolean) iblockstate.getValue(CHECK_DECAY)).booleanValue()) {
			i |= 8;
		}

		return i;
	}

	public BlockPlanks.EnumType getWoodType(int i) {
		return BlockPlanks.EnumType.byMetadata((i & 3) + 4);
	}

	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { VARIANT, CHECK_DECAY, DECAYABLE });
	}

	public void harvestBlock(World world, EntityPlayer entityplayer, BlockPos blockpos, IBlockState iblockstate,
			TileEntity tileentity) {
		if (entityplayer.getCurrentEquippedItem() != null
				&& entityplayer.getCurrentEquippedItem().getItem() == Items.shears) {
			entityplayer.triggerAchievement(StatList.mineBlockStatArray[Block.getIdFromBlock(this)]);
			spawnAsEntity(world, blockpos, new ItemStack(Item.getItemFromBlock(this), 1,
					((BlockPlanks.EnumType) iblockstate.getValue(VARIANT)).getMetadata() - 4));
		} else {
			super.harvestBlock(world, entityplayer, blockpos, iblockstate, tileentity);
		}
	}
}