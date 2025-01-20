package net.minecraft.client.renderer;

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
public class DestroyBlockProgress {
	private final int miningPlayerEntId;
	private final BlockPos position;
	private int partialBlockProgress;
	private int createdAtCloudUpdateTick;

	public DestroyBlockProgress(int miningPlayerEntIdIn, BlockPos positionIn) {
		this.miningPlayerEntId = miningPlayerEntIdIn;
		this.position = positionIn;
	}

	public BlockPos getPosition() {
		return this.position;
	}

	/**+
	 * inserts damage value into this partially destroyed Block. -1
	 * causes client renderer to delete it, otherwise ranges from 1
	 * to 10
	 */
	public void setPartialBlockDamage(int damage) {
		if (damage > 10) {
			damage = 10;
		}

		this.partialBlockProgress = damage;
	}

	public int getPartialBlockDamage() {
		return this.partialBlockProgress;
	}

	/**+
	 * saves the current Cloud update tick into the
	 * PartiallyDestroyedBlock
	 */
	public void setCloudUpdateTick(int createdAtCloudUpdateTickIn) {
		this.createdAtCloudUpdateTick = createdAtCloudUpdateTickIn;
	}

	/**+
	 * retrieves the 'date' at which the PartiallyDestroyedBlock was
	 * created
	 */
	public int getCreationCloudUpdateTick() {
		return this.createdAtCloudUpdateTick;
	}
}