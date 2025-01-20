package net.minecraft.world.gen.structure;

import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.util.MathHelper;

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
public class MapGenMineshaft extends MapGenStructure {
	private double field_82673_e = 0.004D;

	public MapGenMineshaft(boolean scramble) {
		super(scramble);
	}

	public String getStructureName() {
		return "Mineshaft";
	}

	public MapGenMineshaft(Map<String, String> parMap, boolean scramble) {
		super(scramble);
		for (Entry entry : parMap.entrySet()) {
			if (((String) entry.getKey()).equals("chance")) {
				this.field_82673_e = MathHelper.parseDoubleWithDefault((String) entry.getValue(), this.field_82673_e);
			}
		}

	}

	protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ) {
		return this.rand.nextDouble() < this.field_82673_e
				&& this.rand.nextInt(80) < Math.max(Math.abs(chunkX), Math.abs(chunkZ));
	}

	protected StructureStart getStructureStart(int chunkX, int chunkZ) {
		return new StructureMineshaftStart(this.worldObj, this.rand, chunkX, chunkZ);
	}
}