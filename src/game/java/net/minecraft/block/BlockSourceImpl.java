package net.minecraft.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.tileentity.TileEntity;
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
public class BlockSourceImpl implements IBlockSource {
	private final World worldObj;
	private final BlockPos pos;

	public BlockSourceImpl(World worldIn, BlockPos posIn) {
		this.worldObj = worldIn;
		this.pos = posIn;
	}

	public World getWorld() {
		return this.worldObj;
	}

	public double getX() {
		return (double) this.pos.getX() + 0.5D;
	}

	public double getY() {
		return (double) this.pos.getY() + 0.5D;
	}

	public double getZ() {
		return (double) this.pos.getZ() + 0.5D;
	}

	public BlockPos getBlockPos() {
		return this.pos;
	}

	public int getBlockMetadata() {
		IBlockState iblockstate = this.worldObj.getBlockState(this.pos);
		return iblockstate.getBlock().getMetaFromState(iblockstate);
	}

	public <T extends TileEntity> T getBlockTileEntity() {
		return (T) this.worldObj.getTileEntity(this.pos);
	}
}