package net.minecraft.block.state;

import com.google.common.base.Predicate;

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
public class BlockWorldState {
	private final World world;
	private final BlockPos pos;
	private final boolean field_181628_c;
	private IBlockState state;
	private TileEntity tileEntity;
	private boolean tileEntityInitialized;

	public BlockWorldState(World parWorld, BlockPos parBlockPos, boolean parFlag) {
		this.world = parWorld;
		this.pos = parBlockPos;
		this.field_181628_c = parFlag;
	}

	public IBlockState getBlockState() {
		if (this.state == null && (this.field_181628_c || this.world.isBlockLoaded(this.pos))) {
			this.state = this.world.getBlockState(this.pos);
		}

		return this.state;
	}

	public TileEntity getTileEntity() {
		if (this.tileEntity == null && !this.tileEntityInitialized) {
			this.tileEntity = this.world.getTileEntity(this.pos);
			this.tileEntityInitialized = true;
		}

		return this.tileEntity;
	}

	public BlockPos getPos() {
		return this.pos;
	}

	public static Predicate<BlockWorldState> hasState(final Predicate<IBlockState> parPredicate) {
		return new Predicate<BlockWorldState>() {
			public boolean apply(BlockWorldState blockworldstate) {
				return blockworldstate != null && parPredicate.apply(blockworldstate.getBlockState());
			}
		};
	}
}