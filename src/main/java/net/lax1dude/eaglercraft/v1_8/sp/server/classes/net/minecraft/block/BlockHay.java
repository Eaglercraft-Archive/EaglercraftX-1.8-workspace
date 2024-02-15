package net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.block;

import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.block.BlockRotatedPillar;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.block.material.MapColor;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.block.material.Material;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.block.properties.IProperty;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.block.state.BlockState;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.block.state.IBlockState;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.creativetab.CreativeTabs;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.entity.EntityLivingBase;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.item.Item;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.item.ItemStack;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.util.BlockPos;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.util.EnumFacing;
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
public class BlockHay extends BlockRotatedPillar {

	static {
		__checkIntegratedContextValid("net/minecraft/block/BlockHay");
	}

	public BlockHay() {
		super(Material.grass, MapColor.yellowColor);
		this.setDefaultState(this.blockState.getBaseState().withProperty(AXIS, EnumFacing.Axis.Y));
		this.setCreativeTab(CreativeTabs.tabBlock);
	}

	/**+
	 * Convert the given metadata into a BlockState for this Block
	 */
	public IBlockState getStateFromMeta(int i) {
		EnumFacing.Axis enumfacing$axis = EnumFacing.Axis.Y;
		int j = i & 12;
		if (j == 4) {
			enumfacing$axis = EnumFacing.Axis.X;
		} else if (j == 8) {
			enumfacing$axis = EnumFacing.Axis.Z;
		}

		return this.getDefaultState().withProperty(AXIS, enumfacing$axis);
	}

	/**+
	 * Convert the BlockState into the correct metadata value
	 */
	public int getMetaFromState(IBlockState iblockstate) {
		int i = 0;
		EnumFacing.Axis enumfacing$axis = (EnumFacing.Axis) iblockstate.getValue(AXIS);
		if (enumfacing$axis == EnumFacing.Axis.X) {
			i |= 4;
		} else if (enumfacing$axis == EnumFacing.Axis.Z) {
			i |= 8;
		}

		return i;
	}

	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { AXIS });
	}

	protected ItemStack createStackedBlock(IBlockState var1) {
		return new ItemStack(Item.getItemFromBlock(this), 1, 0);
	}

	/**+
	 * Called by ItemBlocks just before a block is actually set in
	 * the world, to allow for adjustments to the IBlockstate
	 */
	public IBlockState onBlockPlaced(World world, BlockPos blockpos, EnumFacing enumfacing, float f, float f1, float f2,
			int i, EntityLivingBase entitylivingbase) {
		return super.onBlockPlaced(world, blockpos, enumfacing, f, f1, f2, i, entitylivingbase).withProperty(AXIS,
				enumfacing.getAxis());
	}
}