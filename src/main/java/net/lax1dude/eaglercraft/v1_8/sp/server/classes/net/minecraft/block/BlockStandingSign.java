package net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.block;

import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.block.Block;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.block.BlockSign;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.block.properties.IProperty;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.block.properties.PropertyInteger;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.block.state.BlockState;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.block.state.IBlockState;
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
public class BlockStandingSign extends BlockSign {

	static {
		__checkIntegratedContextValid("net/minecraft/block/BlockStandingSign");
	}

	public static final PropertyInteger ROTATION = PropertyInteger.create("rotation", 0, 15);

	public BlockStandingSign() {
		this.setDefaultState(this.blockState.getBaseState().withProperty(ROTATION, Integer.valueOf(0)));
	}

	/**+
	 * Called when a neighboring block changes.
	 */
	public void onNeighborBlockChange(World world, BlockPos blockpos, IBlockState iblockstate, Block block) {
		if (!world.getBlockState(blockpos.down()).getBlock().getMaterial().isSolid()) {
			this.dropBlockAsItem(world, blockpos, iblockstate, 0);
			world.setBlockToAir(blockpos);
		}

		super.onNeighborBlockChange(world, blockpos, iblockstate, block);
	}

	/**+
	 * Convert the given metadata into a BlockState for this Block
	 */
	public IBlockState getStateFromMeta(int i) {
		return this.getDefaultState().withProperty(ROTATION, Integer.valueOf(i));
	}

	/**+
	 * Convert the BlockState into the correct metadata value
	 */
	public int getMetaFromState(IBlockState iblockstate) {
		return ((Integer) iblockstate.getValue(ROTATION)).intValue();
	}

	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { ROTATION });
	}
}