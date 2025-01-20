package net.minecraft.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
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
public class BlockPressurePlateWeighted extends BlockBasePressurePlate {
	public static final PropertyInteger POWER = PropertyInteger.create("power", 0, 15);
	private final int field_150068_a;

	protected BlockPressurePlateWeighted(Material parMaterial, int parInt1) {
		this(parMaterial, parInt1, parMaterial.getMaterialMapColor());
	}

	protected BlockPressurePlateWeighted(Material parMaterial, int parInt1, MapColor parMapColor) {
		super(parMaterial, parMapColor);
		this.setDefaultState(this.blockState.getBaseState().withProperty(POWER, Integer.valueOf(0)));
		this.field_150068_a = parInt1;
	}

	protected int computeRedstoneStrength(World world, BlockPos blockpos) {
		int i = Math.min(world.getEntitiesWithinAABB(Entity.class, this.getSensitiveAABB(blockpos)).size(),
				this.field_150068_a);
		if (i > 0) {
			float f = (float) Math.min(this.field_150068_a, i) / (float) this.field_150068_a;
			return MathHelper.ceiling_float_int(f * 15.0F);
		} else {
			return 0;
		}
	}

	protected int getRedstoneStrength(IBlockState iblockstate) {
		return ((Integer) iblockstate.getValue(POWER)).intValue();
	}

	protected IBlockState setRedstoneStrength(IBlockState iblockstate, int i) {
		return iblockstate.withProperty(POWER, Integer.valueOf(i));
	}

	/**+
	 * How many world ticks before ticking
	 */
	public int tickRate(World var1) {
		return 10;
	}

	/**+
	 * Convert the given metadata into a BlockState for this Block
	 */
	public IBlockState getStateFromMeta(int i) {
		return this.getDefaultState().withProperty(POWER, Integer.valueOf(i));
	}

	/**+
	 * Convert the BlockState into the correct metadata value
	 */
	public int getMetaFromState(IBlockState iblockstate) {
		return ((Integer) iblockstate.getValue(POWER)).intValue();
	}

	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { POWER });
	}
}