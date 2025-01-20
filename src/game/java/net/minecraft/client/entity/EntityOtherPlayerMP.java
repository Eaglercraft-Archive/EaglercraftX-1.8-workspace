package net.minecraft.client.entity;

import net.lax1dude.eaglercraft.v1_8.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IChatComponent;
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
public class EntityOtherPlayerMP extends AbstractClientPlayer {
	private boolean isItemInUse;
	private int otherPlayerMPPosRotationIncrements;
	private double otherPlayerMPX;
	private double otherPlayerMPY;
	private double otherPlayerMPZ;
	private double otherPlayerMPYaw;
	private double otherPlayerMPPitch;

	public EntityOtherPlayerMP(World worldIn, GameProfile gameProfileIn) {
		super(worldIn, gameProfileIn);
		this.stepHeight = 0.0F;
		this.noClip = true;
		this.renderOffsetY = 0.25F;
		this.renderDistanceWeight = 10.0D;
	}

	/**+
	 * Called when the entity is attacked.
	 */
	public boolean attackEntityFrom(DamageSource var1, float var2) {
		return true;
	}

	public void setPositionAndRotation2(double x, double y, double z, float yaw, float pitch, int posRotationIncrements,
			boolean parFlag) {
		this.otherPlayerMPX = x;
		this.otherPlayerMPY = y;
		this.otherPlayerMPZ = z;
		this.otherPlayerMPYaw = (double) yaw;
		this.otherPlayerMPPitch = (double) pitch;
		this.otherPlayerMPPosRotationIncrements = posRotationIncrements;
	}

	/**+
	 * Called to update the entity's position/logic.
	 */
	public void onUpdate() {
		this.renderOffsetY = 0.0F;
		super.onUpdate();
		this.prevLimbSwingAmount = this.limbSwingAmount;
		double d0 = this.posX - this.prevPosX;
		double d1 = this.posZ - this.prevPosZ;
		float f = MathHelper.sqrt_double(d0 * d0 + d1 * d1) * 4.0F;
		if (f > 1.0F) {
			f = 1.0F;
		}

		this.limbSwingAmount += (f - this.limbSwingAmount) * 0.4F;
		this.limbSwing += this.limbSwingAmount;
		if (!this.isItemInUse && this.isEating() && this.inventory.mainInventory[this.inventory.currentItem] != null) {
			ItemStack itemstack = this.inventory.mainInventory[this.inventory.currentItem];
			this.setItemInUse(this.inventory.mainInventory[this.inventory.currentItem],
					itemstack.getItem().getMaxItemUseDuration(itemstack));
			this.isItemInUse = true;
		} else if (this.isItemInUse && !this.isEating()) {
			this.clearItemInUse();
			this.isItemInUse = false;
		}

	}

	/**+
	 * Called frequently so the entity can update its state every
	 * tick as required. For example, zombies and skeletons use this
	 * to react to sunlight and start to burn.
	 */
	public void onLivingUpdate() {
		if (this.otherPlayerMPPosRotationIncrements > 0) {
			double d0 = this.posX
					+ (this.otherPlayerMPX - this.posX) / (double) this.otherPlayerMPPosRotationIncrements;
			double d1 = this.posY
					+ (this.otherPlayerMPY - this.posY) / (double) this.otherPlayerMPPosRotationIncrements;
			double d2 = this.posZ
					+ (this.otherPlayerMPZ - this.posZ) / (double) this.otherPlayerMPPosRotationIncrements;

			double d3;
			for (d3 = this.otherPlayerMPYaw - (double) this.rotationYaw; d3 < -180.0D; d3 += 360.0D) {
				;
			}

			while (d3 >= 180.0D) {
				d3 -= 360.0D;
			}

			this.rotationYaw = (float) ((double) this.rotationYaw
					+ d3 / (double) this.otherPlayerMPPosRotationIncrements);
			this.rotationPitch = (float) ((double) this.rotationPitch
					+ (this.otherPlayerMPPitch - (double) this.rotationPitch)
							/ (double) this.otherPlayerMPPosRotationIncrements);
			--this.otherPlayerMPPosRotationIncrements;
			this.setPosition(d0, d1, d2);
			this.setRotation(this.rotationYaw, this.rotationPitch);
		}

		this.prevCameraYaw = this.cameraYaw;
		this.updateArmSwingProgress();
		float f1 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
		float f = (float) Math.atan(-this.motionY * 0.20000000298023224D) * 15.0F;
		if (f1 > 0.1F) {
			f1 = 0.1F;
		}

		if (!this.onGround || this.getHealth() <= 0.0F) {
			f1 = 0.0F;
		}

		if (this.onGround || this.getHealth() <= 0.0F) {
			f = 0.0F;
		}

		this.cameraYaw += (f1 - this.cameraYaw) * 0.4F;
		this.cameraPitch += (f - this.cameraPitch) * 0.8F;
	}

	/**+
	 * Sets the held item, or an armor slot. Slot 0 is held item.
	 * Slot 1-4 is armor. Params: Item, slot
	 */
	public void setCurrentItemOrArmor(int slotIn, ItemStack stack) {
		if (slotIn == 0) {
			this.inventory.mainInventory[this.inventory.currentItem] = stack;
		} else {
			this.inventory.armorInventory[slotIn - 1] = stack;
		}

	}

	/**+
	 * Send a chat message to the CommandSender
	 */
	public void addChatMessage(IChatComponent ichatcomponent) {
		Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(ichatcomponent);
	}

	/**+
	 * Returns {@code true} if the CommandSender is allowed to
	 * execute the command, {@code false} if not
	 */
	public boolean canCommandSenderUseCommand(int var1, String var2) {
		return false;
	}

	/**+
	 * Get the position in the world. <b>{@code null} is not
	 * allowed!</b> If you are not an entity in the world, return
	 * the coordinates 0, 0, 0
	 */
	public BlockPos getPosition() {
		return new BlockPos(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D);
	}
}