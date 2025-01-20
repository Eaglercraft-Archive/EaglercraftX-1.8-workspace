package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
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
public abstract class BlockLog extends BlockRotatedPillar {
	public static PropertyEnum<BlockLog.EnumAxis> LOG_AXIS = null;

	public BlockLog() {
		super(Material.wood);
		this.setCreativeTab(CreativeTabs.tabBlock);
		this.setHardness(2.0F);
		this.setStepSound(soundTypeWood);
	}

	public static void bootstrapStates() {
		LOG_AXIS = PropertyEnum.<BlockLog.EnumAxis>create("axis", BlockLog.EnumAxis.class);
	}

	public void breakBlock(World world, BlockPos blockpos, IBlockState var3) {
		byte b0 = 4;
		int i = b0 + 1;
		if (world.isAreaLoaded(blockpos.add(-i, -i, -i), blockpos.add(i, i, i))) {
			for (BlockPos blockpos1 : BlockPos.getAllInBox(blockpos.add(-b0, -b0, -b0), blockpos.add(b0, b0, b0))) {
				IBlockState iblockstate = world.getBlockState(blockpos1);
				if (iblockstate.getBlock().getMaterial() == Material.leaves
						&& !((Boolean) iblockstate.getValue(BlockLeaves.CHECK_DECAY)).booleanValue()) {
					world.setBlockState(blockpos1,
							iblockstate.withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf(true)), 4);
				}
			}

		}
	}

	/**+
	 * Called by ItemBlocks just before a block is actually set in
	 * the world, to allow for adjustments to the IBlockstate
	 */
	public IBlockState onBlockPlaced(World world, BlockPos blockpos, EnumFacing enumfacing, float f, float f1, float f2,
			int i, EntityLivingBase entitylivingbase) {
		return super.onBlockPlaced(world, blockpos, enumfacing, f, f1, f2, i, entitylivingbase).withProperty(LOG_AXIS,
				BlockLog.EnumAxis.fromFacingAxis(enumfacing.getAxis()));
	}

	public static enum EnumAxis implements IStringSerializable {
		X("x"), Y("y"), Z("z"), NONE("none");

		private final String name;

		private EnumAxis(String name) {
			this.name = name;
		}

		public String toString() {
			return this.name;
		}

		public static BlockLog.EnumAxis fromFacingAxis(EnumFacing.Axis axis) {
			switch (axis) {
			case X:
				return X;
			case Y:
				return Y;
			case Z:
				return Z;
			default:
				return NONE;
			}
		}

		public String getName() {
			return this.name;
		}
	}
}