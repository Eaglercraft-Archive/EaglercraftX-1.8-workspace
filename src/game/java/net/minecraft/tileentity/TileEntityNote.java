package net.minecraft.tileentity;

import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
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
public class TileEntityNote extends TileEntity {
	public byte note;
	public boolean previousRedstoneState;

	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		nbttagcompound.setByte("note", this.note);
	}

	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		this.note = nbttagcompound.getByte("note");
		this.note = (byte) MathHelper.clamp_int(this.note, 0, 24);
	}

	/**+
	 * change pitch by -> (currentPitch + 1) % 25
	 */
	public void changePitch() {
		this.note = (byte) ((this.note + 1) % 25);
		this.markDirty();
	}

	public void triggerNote(World worldIn, BlockPos parBlockPos) {
		if (worldIn.getBlockState(parBlockPos.up()).getBlock().getMaterial() == Material.air) {
			Material material = worldIn.getBlockState(parBlockPos.down()).getBlock().getMaterial();
			byte b0 = 0;
			if (material == Material.rock) {
				b0 = 1;
			}

			if (material == Material.sand) {
				b0 = 2;
			}

			if (material == Material.glass) {
				b0 = 3;
			}

			if (material == Material.wood) {
				b0 = 4;
			}

			worldIn.addBlockEvent(parBlockPos, Blocks.noteblock, b0, this.note);
		}
	}
}