package net.minecraft.tileentity;

import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerEnchantment;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ITickable;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IInteractionObject;

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
public class TileEntityEnchantmentTable extends TileEntity implements ITickable, IInteractionObject {
	public int tickCount;
	public float pageFlip;
	public float pageFlipPrev;
	public float field_145932_k;
	public float field_145929_l;
	public float bookSpread;
	public float bookSpreadPrev;
	public float bookRotation;
	public float bookRotationPrev;
	public float field_145924_q;
	private static EaglercraftRandom rand = new EaglercraftRandom();
	private String customName;

	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		if (this.hasCustomName()) {
			nbttagcompound.setString("CustomName", this.customName);
		}

	}

	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		if (nbttagcompound.hasKey("CustomName", 8)) {
			this.customName = nbttagcompound.getString("CustomName");
		}

	}

	/**+
	 * Like the old updateEntity(), except more generic.
	 */
	public void update() {
		this.bookSpreadPrev = this.bookSpread;
		this.bookRotationPrev = this.bookRotation;
		EntityPlayer entityplayer = this.worldObj.getClosestPlayer((double) ((float) this.pos.getX() + 0.5F),
				(double) ((float) this.pos.getY() + 0.5F), (double) ((float) this.pos.getZ() + 0.5F), 3.0D);
		if (entityplayer != null) {
			double d0 = entityplayer.posX - (double) ((float) this.pos.getX() + 0.5F);
			double d1 = entityplayer.posZ - (double) ((float) this.pos.getZ() + 0.5F);
			this.field_145924_q = (float) MathHelper.func_181159_b(d1, d0);
			this.bookSpread += 0.1F;
			if (this.bookSpread < 0.5F || rand.nextInt(40) == 0) {
				float f1 = this.field_145932_k;

				while (true) {
					this.field_145932_k += (float) (rand.nextInt(4) - rand.nextInt(4));
					if (f1 != this.field_145932_k) {
						break;
					}
				}
			}
		} else {
			this.field_145924_q += 0.02F;
			this.bookSpread -= 0.1F;
		}

		while (this.bookRotation >= 3.1415927F) {
			this.bookRotation -= 6.2831855F;
		}

		while (this.bookRotation < -3.1415927F) {
			this.bookRotation += 6.2831855F;
		}

		while (this.field_145924_q >= 3.1415927F) {
			this.field_145924_q -= 6.2831855F;
		}

		while (this.field_145924_q < -3.1415927F) {
			this.field_145924_q += 6.2831855F;
		}

		float f2;
		for (f2 = this.field_145924_q - this.bookRotation; f2 >= 3.1415927F; f2 -= 6.2831855F) {
			;
		}

		while (f2 < -3.1415927F) {
			f2 += 6.2831855F;
		}

		this.bookRotation += f2 * 0.4F;
		this.bookSpread = MathHelper.clamp_float(this.bookSpread, 0.0F, 1.0F);
		++this.tickCount;
		this.pageFlipPrev = this.pageFlip;
		float f = (this.field_145932_k - this.pageFlip) * 0.4F;
		float f3 = 0.2F;
		f = MathHelper.clamp_float(f, -f3, f3);
		this.field_145929_l += (f - this.field_145929_l) * 0.9F;
		this.pageFlip += this.field_145929_l;
	}

	/**+
	 * Gets the name of this command sender (usually username, but
	 * possibly "Rcon")
	 */
	public String getName() {
		return this.hasCustomName() ? this.customName : "container.enchant";
	}

	/**+
	 * Returns true if this thing is named
	 */
	public boolean hasCustomName() {
		return this.customName != null && this.customName.length() > 0;
	}

	public void setCustomName(String customNameIn) {
		this.customName = customNameIn;
	}

	/**+
	 * Get the formatted ChatComponent that will be used for the
	 * sender's username in chat
	 */
	public IChatComponent getDisplayName() {
		return (IChatComponent) (this.hasCustomName() ? new ChatComponentText(this.getName())
				: new ChatComponentTranslation(this.getName(), new Object[0]));
	}

	public Container createContainer(InventoryPlayer inventoryplayer, EntityPlayer var2) {
		return new ContainerEnchantment(inventoryplayer, this.worldObj, this.pos);
	}

	public String getGuiID() {
		return "minecraft:enchanting_table";
	}
}