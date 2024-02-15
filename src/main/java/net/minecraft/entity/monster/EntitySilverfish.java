package net.minecraft.entity.monster;

import net.minecraft.block.Block;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

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
public class EntitySilverfish extends EntityMob {

	public EntitySilverfish(World worldIn) {
		super(worldIn);
		this.setSize(0.4F, 0.3F);
	}

	/**+
	 * Returns the Y Offset of this entity.
	 */
	public double getYOffset() {
		return 0.2D;
	}

	public float getEyeHeight() {
		return 0.1F;
	}

	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(8.0D);
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
		this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(1.0D);
	}

	/**+
	 * returns if this entity triggers Block.onEntityWalking on the
	 * blocks they walk on. used for spiders and wolves to prevent
	 * them from trampling crops
	 */
	protected boolean canTriggerWalking() {
		return false;
	}

	/**+
	 * Returns the sound this mob makes while it's alive.
	 */
	protected String getLivingSound() {
		return "mob.silverfish.say";
	}

	/**+
	 * Returns the sound this mob makes when it is hurt.
	 */
	protected String getHurtSound() {
		return "mob.silverfish.hit";
	}

	/**+
	 * Returns the sound this mob makes on death.
	 */
	protected String getDeathSound() {
		return "mob.silverfish.kill";
	}

	protected void playStepSound(BlockPos var1, Block var2) {
		this.playSound("mob.silverfish.step", 0.15F, 1.0F);
	}

	protected Item getDropItem() {
		return null;
	}

	/**+
	 * Called to update the entity's position/logic.
	 */
	public void onUpdate() {
		this.renderYawOffset = this.rotationYaw;
		super.onUpdate();
	}

	public float getBlockPathWeight(BlockPos blockpos) {
		return this.worldObj.getBlockState(blockpos.down()).getBlock() == Blocks.stone ? 10.0F
				: super.getBlockPathWeight(blockpos);
	}

	/**+
	 * Checks to make sure the light is not too bright where the mob
	 * is spawning
	 */
	protected boolean isValidLightLevel() {
		return true;
	}

	/**+
	 * Checks if the entity's current position is a valid location
	 * to spawn this entity.
	 */
	public boolean getCanSpawnHere() {
		if (super.getCanSpawnHere()) {
			EntityPlayer entityplayer = this.worldObj.getClosestPlayerToEntity(this, 5.0D);
			return entityplayer == null;
		} else {
			return false;
		}
	}

	/**+
	 * Get this Entity's EnumCreatureAttribute
	 */
	public EnumCreatureAttribute getCreatureAttribute() {
		return EnumCreatureAttribute.ARTHROPOD;
	}

}