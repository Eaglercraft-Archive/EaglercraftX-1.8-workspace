package net.minecraft.block;

import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
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
public class BlockCommandBlock extends BlockContainer {
	public static final PropertyBool TRIGGERED = PropertyBool.create("triggered");

	public BlockCommandBlock() {
		super(Material.iron, MapColor.adobeColor);
		this.setDefaultState(this.blockState.getBaseState().withProperty(TRIGGERED, Boolean.valueOf(false)));
	}

	/**+
	 * Returns a new instance of a block's tile entity class. Called
	 * on placing the block.
	 */
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntityCommandBlock();
	}

	/**+
	 * Called when a neighboring block changes.
	 */
	public void onNeighborBlockChange(World world, BlockPos blockpos, IBlockState iblockstate, Block var4) {
		if (!world.isRemote) {
			boolean flag = world.isBlockPowered(blockpos);
			boolean flag1 = ((Boolean) iblockstate.getValue(TRIGGERED)).booleanValue();
			if (flag && !flag1) {
				world.setBlockState(blockpos, iblockstate.withProperty(TRIGGERED, Boolean.valueOf(true)), 4);
				world.scheduleUpdate(blockpos, this, this.tickRate(world));
			} else if (!flag && flag1) {
				world.setBlockState(blockpos, iblockstate.withProperty(TRIGGERED, Boolean.valueOf(false)), 4);
			}
		}
	}

	public void updateTick(World world, BlockPos blockpos, IBlockState var3, EaglercraftRandom var4) {
		TileEntity tileentity = world.getTileEntity(blockpos);
		if (tileentity instanceof TileEntityCommandBlock) {
			((TileEntityCommandBlock) tileentity).getCommandBlockLogic().trigger(world);
			world.updateComparatorOutputLevel(blockpos, this);
		}

	}

	/**+
	 * How many world ticks before ticking
	 */
	public int tickRate(World var1) {
		return 1;
	}

	public boolean onBlockActivated(World world, BlockPos blockpos, IBlockState var3, EntityPlayer entityplayer,
			EnumFacing var5, float var6, float var7, float var8) {
		TileEntity tileentity = world.getTileEntity(blockpos);
		return tileentity instanceof TileEntityCommandBlock
				? ((TileEntityCommandBlock) tileentity).getCommandBlockLogic().tryOpenEditCommandBlock(entityplayer)
				: false;
	}

	public boolean hasComparatorInputOverride() {
		return true;
	}

	public int getComparatorInputOverride(World world, BlockPos blockpos) {
		TileEntity tileentity = world.getTileEntity(blockpos);
		return tileentity instanceof TileEntityCommandBlock
				? ((TileEntityCommandBlock) tileentity).getCommandBlockLogic().getSuccessCount()
				: 0;
	}

	/**+
	 * Called by ItemBlocks after a block is set in the world, to
	 * allow post-place logic
	 */
	public void onBlockPlacedBy(World world, BlockPos blockpos, IBlockState var3, EntityLivingBase var4,
			ItemStack itemstack) {
		TileEntity tileentity = world.getTileEntity(blockpos);
		if (tileentity instanceof TileEntityCommandBlock) {
			CommandBlockLogic commandblocklogic = ((TileEntityCommandBlock) tileentity).getCommandBlockLogic();
			if (itemstack.hasDisplayName()) {
				commandblocklogic.setName(itemstack.getDisplayName());
			}

			if (!world.isRemote) {
				commandblocklogic.setTrackOutput(world.getGameRules().getBoolean("sendCommandFeedback"));
			}
		}
	}

	/**+
	 * Returns the quantity of items to drop on block destruction.
	 */
	public int quantityDropped(EaglercraftRandom var1) {
		return 0;
	}

	/**+
	 * The type of render function called. 3 for standard block
	 * models, 2 for TESR's, 1 for liquids, -1 is no render
	 */
	public int getRenderType() {
		return 3;
	}

	/**+
	 * Convert the given metadata into a BlockState for this Block
	 */
	public IBlockState getStateFromMeta(int i) {
		return this.getDefaultState().withProperty(TRIGGERED, Boolean.valueOf((i & 1) > 0));
	}

	/**+
	 * Convert the BlockState into the correct metadata value
	 */
	public int getMetaFromState(IBlockState iblockstate) {
		int i = 0;
		if (((Boolean) iblockstate.getValue(TRIGGERED)).booleanValue()) {
			i |= 1;
		}

		return i;
	}

	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { TRIGGERED });
	}

	/**+
	 * Called by ItemBlocks just before a block is actually set in
	 * the world, to allow for adjustments to the IBlockstate
	 */
	public IBlockState onBlockPlaced(World var1, BlockPos var2, EnumFacing var3, float var4, float var5, float var6,
			int var7, EntityLivingBase var8) {
		return this.getDefaultState().withProperty(TRIGGERED, Boolean.valueOf(false));
	}
}