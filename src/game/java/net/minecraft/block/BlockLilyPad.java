package net.minecraft.block;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
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
public class BlockLilyPad extends BlockBush {
	protected BlockLilyPad() {
		float f = 0.5F;
		float f1 = 0.015625F;
		this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f1, 0.5F + f);
		this.setCreativeTab(CreativeTabs.tabDecorations);
	}

	/**+
	 * Add all collision boxes of this Block to the list that
	 * intersect with the given mask.
	 */
	public void addCollisionBoxesToList(World world, BlockPos blockpos, IBlockState iblockstate,
			AxisAlignedBB axisalignedbb, List<AxisAlignedBB> list, Entity entity) {
		if (entity == null || !(entity instanceof EntityBoat)) {
			super.addCollisionBoxesToList(world, blockpos, iblockstate, axisalignedbb, list, entity);
		}

	}

	public AxisAlignedBB getCollisionBoundingBox(World var1, BlockPos blockpos, IBlockState var3) {
		return new AxisAlignedBB((double) blockpos.getX() + this.minX, (double) blockpos.getY() + this.minY,
				(double) blockpos.getZ() + this.minZ, (double) blockpos.getX() + this.maxX,
				(double) blockpos.getY() + this.maxY, (double) blockpos.getZ() + this.maxZ);
	}

	public int getBlockColor() {
		return 7455580;
	}

	public int getRenderColor(IBlockState var1) {
		return 7455580;
	}

	public int colorMultiplier(IBlockAccess var1, BlockPos var2, int var3) {
		return 2129968;
	}

	/**+
	 * is the block grass, dirt or farmland
	 */
	protected boolean canPlaceBlockOn(Block block) {
		return block == Blocks.water;
	}

	public boolean canBlockStay(World world, BlockPos blockpos, IBlockState var3) {
		if (blockpos.getY() >= 0 && blockpos.getY() < 256) {
			IBlockState iblockstate = world.getBlockState(blockpos.down());
			return iblockstate.getBlock().getMaterial() == Material.water
					&& ((Integer) iblockstate.getValue(BlockLiquid.LEVEL)).intValue() == 0;
		} else {
			return false;
		}
	}

	/**+
	 * Convert the BlockState into the correct metadata value
	 */
	public int getMetaFromState(IBlockState var1) {
		return 0;
	}
}