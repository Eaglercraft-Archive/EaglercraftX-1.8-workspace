package net.minecraft.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.ITickable;

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
public class TileEntityEnderChest extends TileEntity implements ITickable {
	public float lidAngle;
	public float prevLidAngle;
	public int numPlayersUsing;
	private int ticksSinceSync;

	/**+
	 * Like the old updateEntity(), except more generic.
	 */
	public void update() {
		if (++this.ticksSinceSync % 20 * 4 == 0) {
			this.worldObj.addBlockEvent(this.pos, Blocks.ender_chest, 1, this.numPlayersUsing);
		}

		this.prevLidAngle = this.lidAngle;
		int i = this.pos.getX();
		int j = this.pos.getY();
		int k = this.pos.getZ();
		float f = 0.1F;
		if (this.numPlayersUsing > 0 && this.lidAngle == 0.0F) {
			double d0 = (double) i + 0.5D;
			double d1 = (double) k + 0.5D;
			this.worldObj.playSoundEffect(d0, (double) j + 0.5D, d1, "random.chestopen", 0.5F,
					this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
		}

		if (this.numPlayersUsing == 0 && this.lidAngle > 0.0F || this.numPlayersUsing > 0 && this.lidAngle < 1.0F) {
			float f2 = this.lidAngle;
			if (this.numPlayersUsing > 0) {
				this.lidAngle += f;
			} else {
				this.lidAngle -= f;
			}

			if (this.lidAngle > 1.0F) {
				this.lidAngle = 1.0F;
			}

			float f1 = 0.5F;
			if (this.lidAngle < f1 && f2 >= f1) {
				double d3 = (double) i + 0.5D;
				double d2 = (double) k + 0.5D;
				this.worldObj.playSoundEffect(d3, (double) j + 0.5D, d2, "random.chestclosed", 0.5F,
						this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
			}

			if (this.lidAngle < 0.0F) {
				this.lidAngle = 0.0F;
			}
		}

	}

	public boolean receiveClientEvent(int i, int j) {
		if (i == 1) {
			this.numPlayersUsing = j;
			return true;
		} else {
			return super.receiveClientEvent(i, j);
		}
	}

	/**+
	 * invalidates a tile entity
	 */
	public void invalidate() {
		this.updateContainingBlockInfo();
		super.invalidate();
	}

	public void openChest() {
		++this.numPlayersUsing;
		this.worldObj.addBlockEvent(this.pos, Blocks.ender_chest, 1, this.numPlayersUsing);
	}

	public void closeChest() {
		--this.numPlayersUsing;
		this.worldObj.addBlockEvent(this.pos, Blocks.ender_chest, 1, this.numPlayersUsing);
	}

	public boolean canBeUsed(EntityPlayer parEntityPlayer) {
		return this.worldObj.getTileEntity(this.pos) != this ? false
				: parEntityPlayer.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D,
						(double) this.pos.getZ() + 0.5D) <= 64.0D;
	}
}