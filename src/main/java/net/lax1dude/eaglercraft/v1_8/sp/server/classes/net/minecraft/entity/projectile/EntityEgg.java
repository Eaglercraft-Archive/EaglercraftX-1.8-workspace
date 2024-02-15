package net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.entity.projectile;

import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.entity.EntityLivingBase;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.entity.passive.EntityChicken;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.entity.projectile.EntityThrowable;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.init.Items;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.item.Item;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.util.DamageSource;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.util.EnumParticleTypes;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.util.MovingObjectPosition;
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
public class EntityEgg extends EntityThrowable {

	static {
		__checkIntegratedContextValid("net/minecraft/entity/projectile/EntityEgg");
	}

	public EntityEgg(World worldIn) {
		super(worldIn);
	}

	public EntityEgg(World worldIn, EntityLivingBase throwerIn) {
		super(worldIn, throwerIn);
	}

	public EntityEgg(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
	}

	/**+
	 * Called when this EntityThrowable hits a block or entity.
	 */
	protected void onImpact(MovingObjectPosition movingobjectposition) {
		if (movingobjectposition.entityHit != null) {
			movingobjectposition.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()),
					0.0F);
		}

		if (this.rand.nextInt(8) == 0) {
			byte b0 = 1;
			if (this.rand.nextInt(32) == 0) {
				b0 = 4;
			}

			for (int i = 0; i < b0; ++i) {
				EntityChicken entitychicken = new EntityChicken(this.worldObj);
				entitychicken.setGrowingAge(-24000);
				entitychicken.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
				this.worldObj.spawnEntityInWorld(entitychicken);
			}
		}

		double d0 = 0.08D;

		for (int j = 0; j < 8; ++j) {
			this.worldObj.spawnParticle(EnumParticleTypes.ITEM_CRACK, this.posX, this.posY, this.posZ,
					((double) this.rand.nextFloat() - 0.5D) * 0.08D, ((double) this.rand.nextFloat() - 0.5D) * 0.08D,
					((double) this.rand.nextFloat() - 0.5D) * 0.08D, new int[] { Item.getIdFromItem(Items.egg) });
		}

		this.setDead();
	}
}