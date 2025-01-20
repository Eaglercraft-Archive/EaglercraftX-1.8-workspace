package net.minecraft.block;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
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
public class BlockPressurePlate extends BlockBasePressurePlate {
	public static final PropertyBool POWERED = PropertyBool.create("powered");
	private final BlockPressurePlate.Sensitivity sensitivity;

	protected BlockPressurePlate(Material materialIn, BlockPressurePlate.Sensitivity sensitivityIn) {
		super(materialIn);
		this.setDefaultState(this.blockState.getBaseState().withProperty(POWERED, Boolean.valueOf(false)));
		this.sensitivity = sensitivityIn;
	}

	protected int getRedstoneStrength(IBlockState iblockstate) {
		return ((Boolean) iblockstate.getValue(POWERED)).booleanValue() ? 15 : 0;
	}

	protected IBlockState setRedstoneStrength(IBlockState iblockstate, int i) {
		return iblockstate.withProperty(POWERED, Boolean.valueOf(i > 0));
	}

	protected int computeRedstoneStrength(World world, BlockPos blockpos) {
		AxisAlignedBB axisalignedbb = this.getSensitiveAABB(blockpos);
		List<Entity> list;
		switch (this.sensitivity) {
		case EVERYTHING:
			list = world.getEntitiesWithinAABBExcludingEntity((Entity) null, axisalignedbb);
			break;
		case MOBS:
			list = world.getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb);
			break;
		default:
			return 0;
		}

		if (!list.isEmpty()) {
			for (int i = 0, l = list.size(); i < l; ++i) {
				Entity entity = list.get(i);
				if (!entity.doesEntityNotTriggerPressurePlate()) {
					return 15;
				}
			}
		}

		return 0;
	}

	/**+
	 * Convert the given metadata into a BlockState for this Block
	 */
	public IBlockState getStateFromMeta(int i) {
		return this.getDefaultState().withProperty(POWERED, Boolean.valueOf(i == 1));
	}

	/**+
	 * Convert the BlockState into the correct metadata value
	 */
	public int getMetaFromState(IBlockState iblockstate) {
		return ((Boolean) iblockstate.getValue(POWERED)).booleanValue() ? 1 : 0;
	}

	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { POWERED });
	}

	public static enum Sensitivity {
		EVERYTHING, MOBS;
	}
}