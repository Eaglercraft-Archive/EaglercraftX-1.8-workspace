package net.minecraft.entity.ai;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;

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
public class EntitySenses {
	EntityLiving entityObj;
	List<Entity> seenEntities = Lists.newArrayList();
	List<Entity> unseenEntities = Lists.newArrayList();

	public EntitySenses(EntityLiving entityObjIn) {
		this.entityObj = entityObjIn;
	}

	/**+
	 * Clears canSeeCachePositive and canSeeCacheNegative.
	 */
	public void clearSensingCache() {
		this.seenEntities.clear();
		this.unseenEntities.clear();
	}

	/**+
	 * Checks, whether 'our' entity can see the entity given as
	 * argument (true) or not (false), caching the result.
	 */
	public boolean canSee(Entity entityIn) {
		if (this.seenEntities.contains(entityIn)) {
			return true;
		} else if (this.unseenEntities.contains(entityIn)) {
			return false;
		} else {
			boolean flag = this.entityObj.canEntityBeSeen(entityIn);
			if (flag) {
				this.seenEntities.add(entityIn);
			} else {
				this.unseenEntities.add(entityIn);
			}

			return flag;
		}
	}
}