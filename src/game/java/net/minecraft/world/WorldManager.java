package net.minecraft.world;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S25PacketBlockBreakAnim;
import net.minecraft.network.play.server.S28PacketEffect;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;

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
public class WorldManager implements IWorldAccess {
	private MinecraftServer mcServer;
	private WorldServer theWorldServer;

	public WorldManager(MinecraftServer parMinecraftServer, WorldServer parWorldServer) {
		this.mcServer = parMinecraftServer;
		this.theWorldServer = parWorldServer;
	}

	public void spawnParticle(int var1, boolean var2, double var3, double var5, double var7, double var9, double var11,
			double var13, int... var15) {
	}

	/**+
	 * Called on all IWorldAccesses when an entity is created or
	 * loaded. On client worlds, starts downloading any necessary
	 * textures. On server worlds, adds the entity to the entity
	 * tracker.
	 */
	public void onEntityAdded(Entity entity) {
		this.theWorldServer.getEntityTracker().trackEntity(entity);
	}

	/**+
	 * Called on all IWorldAccesses when an entity is unloaded or
	 * destroyed. On client worlds, releases any downloaded
	 * textures. On server worlds, removes the entity from the
	 * entity tracker.
	 */
	public void onEntityRemoved(Entity entity) {
		this.theWorldServer.getEntityTracker().untrackEntity(entity);
		this.theWorldServer.getScoreboard().func_181140_a(entity);
	}

	/**+
	 * Plays the specified sound. Arg: soundName, x, y, z, volume,
	 * pitch
	 */
	public void playSound(String s, double d0, double d1, double d2, float f, float f1) {
		this.mcServer.getConfigurationManager().sendToAllNear(d0, d1, d2, f > 1.0F ? (double) (16.0F * f) : 16.0D,
				this.theWorldServer.provider.getDimensionId(), new S29PacketSoundEffect(s, d0, d1, d2, f, f1));
	}

	/**+
	 * Plays sound to all near players except the player reference
	 * given
	 */
	public void playSoundToNearExcept(EntityPlayer entityplayer, String s, double d0, double d1, double d2, float f,
			float f1) {
		this.mcServer.getConfigurationManager().sendToAllNearExcept(entityplayer, d0, d1, d2,
				f > 1.0F ? (double) (16.0F * f) : 16.0D, this.theWorldServer.provider.getDimensionId(),
				new S29PacketSoundEffect(s, d0, d1, d2, f, f1));
	}

	/**+
	 * On the client, re-renders all blocks in this range,
	 * inclusive. On the server, does nothing. Args: min x, min y,
	 * min z, max x, max y, max z
	 */
	public void markBlockRangeForRenderUpdate(int var1, int var2, int var3, int var4, int var5, int var6) {
	}

	public void markBlockForUpdate(BlockPos blockpos) {
		this.theWorldServer.getPlayerManager().markBlockForUpdate(blockpos);
	}

	public void notifyLightSet(BlockPos var1) {
	}

	public void playRecord(String var1, BlockPos var2) {
	}

	public void playAuxSFX(EntityPlayer entityplayer, int i, BlockPos blockpos, int j) {
		this.mcServer.getConfigurationManager().sendToAllNearExcept(entityplayer, (double) blockpos.getX(),
				(double) blockpos.getY(), (double) blockpos.getZ(), 64.0D,
				this.theWorldServer.provider.getDimensionId(), new S28PacketEffect(i, blockpos, j, false));
	}

	public void broadcastSound(int i, BlockPos blockpos, int j) {
		this.mcServer.getConfigurationManager().sendPacketToAllPlayers(new S28PacketEffect(i, blockpos, j, true));
	}

	public void sendBlockBreakProgress(int i, BlockPos blockpos, int j) {
		for (EntityPlayerMP entityplayermp : this.mcServer.getConfigurationManager().func_181057_v()) {
			if (entityplayermp != null && entityplayermp.worldObj == this.theWorldServer
					&& entityplayermp.getEntityId() != i) {
				double d0 = (double) blockpos.getX() - entityplayermp.posX;
				double d1 = (double) blockpos.getY() - entityplayermp.posY;
				double d2 = (double) blockpos.getZ() - entityplayermp.posZ;
				if (d0 * d0 + d1 * d1 + d2 * d2 < 1024.0D) {
					entityplayermp.playerNetServerHandler.sendPacket(new S25PacketBlockBreakAnim(i, blockpos, j));
				}
			}
		}

	}
}