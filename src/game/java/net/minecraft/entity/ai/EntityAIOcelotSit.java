package net.minecraft.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
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
public class EntityAIOcelotSit extends EntityAIMoveToBlock {
	private final EntityOcelot field_151493_a;

	public EntityAIOcelotSit(EntityOcelot parEntityOcelot, double parDouble1) {
		super(parEntityOcelot, parDouble1, 8);
		this.field_151493_a = parEntityOcelot;
	}

	/**+
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute() {
		return this.field_151493_a.isTamed() && !this.field_151493_a.isSitting() && super.shouldExecute();
	}

	/**+
	 * Returns whether an in-progress EntityAIBase should continue
	 * executing
	 */
	public boolean continueExecuting() {
		return super.continueExecuting();
	}

	/**+
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting() {
		super.startExecuting();
		this.field_151493_a.getAISit().setSitting(false);
	}

	/**+
	 * Resets the task
	 */
	public void resetTask() {
		super.resetTask();
		this.field_151493_a.setSitting(false);
	}

	/**+
	 * Updates the task
	 */
	public void updateTask() {
		super.updateTask();
		this.field_151493_a.getAISit().setSitting(false);
		if (!this.getIsAboveDestination()) {
			this.field_151493_a.setSitting(false);
		} else if (!this.field_151493_a.isSitting()) {
			this.field_151493_a.setSitting(true);
		}

	}

	/**+
	 * Return true to set given position as destination
	 */
	protected boolean shouldMoveTo(World world, BlockPos blockpos) {
		if (!world.isAirBlock(blockpos.up())) {
			return false;
		} else {
			IBlockState iblockstate = world.getBlockState(blockpos);
			Block block = iblockstate.getBlock();
			if (block == Blocks.chest) {
				TileEntity tileentity = world.getTileEntity(blockpos);
				if (tileentity instanceof TileEntityChest && ((TileEntityChest) tileentity).numPlayersUsing < 1) {
					return true;
				}
			} else {
				if (block == Blocks.lit_furnace) {
					return true;
				}

				if (block == Blocks.bed && iblockstate.getValue(BlockBed.PART) != BlockBed.EnumPartType.HEAD) {
					return true;
				}
			}

			return false;
		}
	}
}