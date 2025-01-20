package net.minecraft.entity.ai;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.block.Block;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockStateHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
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
public class EntityAIEatGrass extends EntityAIBase {
	private static final Predicate<IBlockState> field_179505_b = BlockStateHelper.forBlock(Blocks.tallgrass)
			.where(BlockTallGrass.TYPE, Predicates.equalTo(BlockTallGrass.EnumType.GRASS));
	private EntityLiving grassEaterEntity;
	private World entityWorld;
	int eatingGrassTimer;

	public EntityAIEatGrass(EntityLiving grassEaterEntityIn) {
		this.grassEaterEntity = grassEaterEntityIn;
		this.entityWorld = grassEaterEntityIn.worldObj;
		this.setMutexBits(7);
	}

	/**+
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute() {
		if (this.grassEaterEntity.getRNG().nextInt(this.grassEaterEntity.isChild() ? 50 : 1000) != 0) {
			return false;
		} else {
			BlockPos blockpos = new BlockPos(this.grassEaterEntity.posX, this.grassEaterEntity.posY,
					this.grassEaterEntity.posZ);
			return field_179505_b.apply(this.entityWorld.getBlockState(blockpos)) ? true
					: this.entityWorld.getBlockState(blockpos.down()).getBlock() == Blocks.grass;
		}
	}

	/**+
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting() {
		this.eatingGrassTimer = 40;
		this.entityWorld.setEntityState(this.grassEaterEntity, (byte) 10);
		this.grassEaterEntity.getNavigator().clearPathEntity();
	}

	/**+
	 * Resets the task
	 */
	public void resetTask() {
		this.eatingGrassTimer = 0;
	}

	/**+
	 * Returns whether an in-progress EntityAIBase should continue
	 * executing
	 */
	public boolean continueExecuting() {
		return this.eatingGrassTimer > 0;
	}

	/**+
	 * Number of ticks since the entity started to eat grass
	 */
	public int getEatingGrassTimer() {
		return this.eatingGrassTimer;
	}

	/**+
	 * Updates the task
	 */
	public void updateTask() {
		this.eatingGrassTimer = Math.max(0, this.eatingGrassTimer - 1);
		if (this.eatingGrassTimer == 4) {
			BlockPos blockpos = new BlockPos(this.grassEaterEntity.posX, this.grassEaterEntity.posY,
					this.grassEaterEntity.posZ);
			if (field_179505_b.apply(this.entityWorld.getBlockState(blockpos))) {
				if (this.entityWorld.getGameRules().getBoolean("mobGriefing")) {
					this.entityWorld.destroyBlock(blockpos, false);
				}

				this.grassEaterEntity.eatGrassBonus();
			} else {
				BlockPos blockpos1 = blockpos.down();
				if (this.entityWorld.getBlockState(blockpos1).getBlock() == Blocks.grass) {
					if (this.entityWorld.getGameRules().getBoolean("mobGriefing")) {
						this.entityWorld.playAuxSFX(2001, blockpos1, Block.getIdFromBlock(Blocks.grass));
						this.entityWorld.setBlockState(blockpos1, Blocks.dirt.getDefaultState(), 2);
					}

					this.grassEaterEntity.eatGrassBonus();
				}
			}

		}
	}
}