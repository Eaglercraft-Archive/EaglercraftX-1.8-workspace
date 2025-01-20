package net.minecraft.world;

import net.minecraft.server.MinecraftServer;
import net.minecraft.village.VillageCollection;
import net.minecraft.world.border.IBorderListener;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.storage.DerivedWorldInfo;
import net.minecraft.world.storage.ISaveHandler;

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
public class WorldServerMulti extends WorldServer {
	private WorldServer delegate;

	public WorldServerMulti(MinecraftServer server, ISaveHandler saveHandlerIn, int dimensionId, WorldServer delegate) {
		super(server, saveHandlerIn, new DerivedWorldInfo(delegate.getWorldInfo()), dimensionId);
		this.delegate = delegate;
		delegate.getWorldBorder().addListener(new IBorderListener() {
			public void onSizeChanged(WorldBorder var1, double d0) {
				WorldServerMulti.this.getWorldBorder().setTransition(d0);
			}

			public void onTransitionStarted(WorldBorder var1, double d0, double d1, long i) {
				WorldServerMulti.this.getWorldBorder().setTransition(d0, d1, i);
			}

			public void onCenterChanged(WorldBorder var1, double d0, double d1) {
				WorldServerMulti.this.getWorldBorder().setCenter(d0, d1);
			}

			public void onWarningTimeChanged(WorldBorder var1, int i) {
				WorldServerMulti.this.getWorldBorder().setWarningTime(i);
			}

			public void onWarningDistanceChanged(WorldBorder var1, int i) {
				WorldServerMulti.this.getWorldBorder().setWarningDistance(i);
			}

			public void onDamageAmountChanged(WorldBorder var1, double d0) {
				WorldServerMulti.this.getWorldBorder().setDamageAmount(d0);
			}

			public void onDamageBufferChanged(WorldBorder var1, double d0) {
				WorldServerMulti.this.getWorldBorder().setDamageBuffer(d0);
			}
		});
	}

	public World init() {
		this.mapStorage = this.delegate.getMapStorage();
		this.worldScoreboard = this.delegate.getScoreboard();
		String s = VillageCollection.fileNameForProvider(this.provider);
		VillageCollection villagecollection = (VillageCollection) this.mapStorage.loadData(VillageCollection.class, s);
		if (villagecollection == null) {
			this.villageCollectionObj = new VillageCollection(this);
			this.mapStorage.setData(s, this.villageCollectionObj);
		} else {
			this.villageCollectionObj = villagecollection;
			this.villageCollectionObj.setWorldsForAll(this);
		}

		return this;
	}
}