package net.minecraft.world.pathfinder;

import com.carrotsearch.hppc.IntObjectHashMap;
import com.carrotsearch.hppc.IntObjectMap;

import net.minecraft.entity.Entity;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;

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
public abstract class NodeProcessor {
	protected IBlockAccess blockaccess;
	protected IntObjectMap<PathPoint> pointMap = new IntObjectHashMap<>();
	protected int entitySizeX;
	protected int entitySizeY;
	protected int entitySizeZ;

	public void initProcessor(IBlockAccess iblockaccessIn, Entity entityIn) {
		this.blockaccess = iblockaccessIn;
		this.pointMap.clear();
		this.entitySizeX = MathHelper.floor_float(entityIn.width + 1.0F);
		this.entitySizeY = MathHelper.floor_float(entityIn.height + 1.0F);
		this.entitySizeZ = MathHelper.floor_float(entityIn.width + 1.0F);
	}

	/**+
	 * This method is called when all nodes have been processed and
	 * PathEntity is created.\n {@link
	 * net.minecraft.world.pathfinder.WalkNodeProcessor
	 * WalkNodeProcessor} uses this to change its field {@link
	 * net.minecraft.world.pathfinder.WalkNodeProcessor#avoidsWater
	 * avoidsWater}
	 */
	public void postProcess() {
	}

	/**+
	 * Returns a mapped point or creates and adds one
	 */
	protected PathPoint openPoint(int x, int y, int z) {
		int i = PathPoint.makeHash(x, y, z);
		PathPoint pathpoint = this.pointMap.get(i);
		if (pathpoint == null) {
			pathpoint = new PathPoint(x, y, z);
			this.pointMap.put(i, pathpoint);
		}

		return pathpoint;
	}

	public abstract PathPoint getPathPointTo(Entity var1);

	public abstract PathPoint getPathPointToCoords(Entity var1, double var2, double var4, double var6);

	public abstract int findPathOptions(PathPoint[] var1, Entity var2, PathPoint var3, PathPoint var4, float var5);
}