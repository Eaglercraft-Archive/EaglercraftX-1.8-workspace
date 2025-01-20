package net.minecraft.block;

import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.StatCollector;
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
public class BlockRedstoneRepeater extends BlockRedstoneDiode {
	public static final PropertyBool LOCKED = PropertyBool.create("locked");
	public static final PropertyInteger DELAY = PropertyInteger.create("delay", 1, 4);

	protected BlockRedstoneRepeater(boolean powered) {
		super(powered);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH)
				.withProperty(DELAY, Integer.valueOf(1)).withProperty(LOCKED, Boolean.valueOf(false)));
	}

	/**+
	 * Gets the localized name of this block. Used for the
	 * statistics page.
	 */
	public String getLocalizedName() {
		return StatCollector.translateToLocal("item.diode.name");
	}

	/**+
	 * Get the actual Block state of this Block at the given
	 * position. This applies properties not visible in the
	 * metadata, such as fence connections.
	 */
	public IBlockState getActualState(IBlockState iblockstate, IBlockAccess iblockaccess, BlockPos blockpos) {
		return iblockstate.withProperty(LOCKED, Boolean.valueOf(this.isLocked(iblockaccess, blockpos, iblockstate)));
	}

	public boolean onBlockActivated(World world, BlockPos blockpos, IBlockState iblockstate, EntityPlayer entityplayer,
			EnumFacing var5, float var6, float var7, float var8) {
		if (!entityplayer.capabilities.allowEdit) {
			return false;
		} else {
			world.setBlockState(blockpos, iblockstate.cycleProperty(DELAY), 3);
			return true;
		}
	}

	protected int getDelay(IBlockState iblockstate) {
		return ((Integer) iblockstate.getValue(DELAY)).intValue() * 2;
	}

	protected IBlockState getPoweredState(IBlockState iblockstate) {
		Integer integer = (Integer) iblockstate.getValue(DELAY);
		Boolean obool = (Boolean) iblockstate.getValue(LOCKED);
		EnumFacing enumfacing = (EnumFacing) iblockstate.getValue(FACING);
		return Blocks.powered_repeater.getDefaultState().withProperty(FACING, enumfacing).withProperty(DELAY, integer)
				.withProperty(LOCKED, obool);
	}

	protected IBlockState getUnpoweredState(IBlockState iblockstate) {
		Integer integer = (Integer) iblockstate.getValue(DELAY);
		Boolean obool = (Boolean) iblockstate.getValue(LOCKED);
		EnumFacing enumfacing = (EnumFacing) iblockstate.getValue(FACING);
		return Blocks.unpowered_repeater.getDefaultState().withProperty(FACING, enumfacing).withProperty(DELAY, integer)
				.withProperty(LOCKED, obool);
	}

	/**+
	 * Get the Item that this Block should drop when harvested.
	 */
	public Item getItemDropped(IBlockState var1, EaglercraftRandom var2, int var3) {
		return Items.repeater;
	}

	public Item getItem(World var1, BlockPos var2) {
		return Items.repeater;
	}

	public boolean isLocked(IBlockAccess iblockaccess, BlockPos blockpos, IBlockState iblockstate) {
		return this.getPowerOnSides(iblockaccess, blockpos, iblockstate) > 0;
	}

	protected boolean canPowerSide(Block block) {
		return isRedstoneRepeaterBlockID(block);
	}

	public void randomDisplayTick(World world, BlockPos blockpos, IBlockState iblockstate, EaglercraftRandom random) {
		if (this.isRepeaterPowered) {
			EnumFacing enumfacing = (EnumFacing) iblockstate.getValue(FACING);
			double d0 = (double) ((float) blockpos.getX() + 0.5F) + (double) (random.nextFloat() - 0.5F) * 0.2D;
			double d1 = (double) ((float) blockpos.getY() + 0.4F) + (double) (random.nextFloat() - 0.5F) * 0.2D;
			double d2 = (double) ((float) blockpos.getZ() + 0.5F) + (double) (random.nextFloat() - 0.5F) * 0.2D;
			float f = -5.0F;
			if (random.nextBoolean()) {
				f = (float) (((Integer) iblockstate.getValue(DELAY)).intValue() * 2 - 1);
			}

			f = f / 16.0F;
			double d3 = (double) (f * (float) enumfacing.getFrontOffsetX());
			double d4 = (double) (f * (float) enumfacing.getFrontOffsetZ());
			world.spawnParticle(EnumParticleTypes.REDSTONE, d0 + d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D, new int[0]);
		}
	}

	public void breakBlock(World world, BlockPos blockpos, IBlockState iblockstate) {
		super.breakBlock(world, blockpos, iblockstate);
		this.notifyNeighbors(world, blockpos, iblockstate);
	}

	/**+
	 * Convert the given metadata into a BlockState for this Block
	 */
	public IBlockState getStateFromMeta(int i) {
		return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(i))
				.withProperty(LOCKED, Boolean.valueOf(false)).withProperty(DELAY, Integer.valueOf(1 + (i >> 2)));
	}

	/**+
	 * Convert the BlockState into the correct metadata value
	 */
	public int getMetaFromState(IBlockState iblockstate) {
		int i = 0;
		i = i | ((EnumFacing) iblockstate.getValue(FACING)).getHorizontalIndex();
		i = i | ((Integer) iblockstate.getValue(DELAY)).intValue() - 1 << 2;
		return i;
	}

	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { FACING, DELAY, LOCKED });
	}
}