package net.minecraft.world;

import net.minecraft.block.Block;
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
public class NextTickListEntry implements Comparable<NextTickListEntry> {
	private static long nextTickEntryID;
	private final Block block;
	public final BlockPos position;
	public long scheduledTime;
	public int priority;
	private long tickEntryID;

	public NextTickListEntry(BlockPos parBlockPos, Block parBlock) {
		this.tickEntryID = (long) (nextTickEntryID++);
		this.position = parBlockPos;
		this.block = parBlock;
	}

	public boolean equals(Object object) {
		if (!(object instanceof NextTickListEntry)) {
			return false;
		} else {
			NextTickListEntry nextticklistentry = (NextTickListEntry) object;
			return this.position.equals(nextticklistentry.position)
					&& Block.isEqualTo(this.block, nextticklistentry.block);
		}
	}

	public int hashCode() {
		return this.position.hashCode();
	}

	/**+
	 * Sets the scheduled time for this tick entry
	 */
	public NextTickListEntry setScheduledTime(long parLong1) {
		this.scheduledTime = parLong1;
		return this;
	}

	public void setPriority(int parInt1) {
		this.priority = parInt1;
	}

	public int compareTo(NextTickListEntry parNextTickListEntry) {
		return this.scheduledTime < parNextTickListEntry.scheduledTime ? -1
				: (this.scheduledTime > parNextTickListEntry.scheduledTime ? 1
						: (this.priority != parNextTickListEntry.priority
								? this.priority - parNextTickListEntry.priority
								: (this.tickEntryID < parNextTickListEntry.tickEntryID ? -1
										: (this.tickEntryID > parNextTickListEntry.tickEntryID ? 1 : 0))));
	}

	public String toString() {
		return Block.getIdFromBlock(this.block) + ": " + this.position + ", " + this.scheduledTime + ", "
				+ this.priority + ", " + this.tickEntryID;
	}

	public Block getBlock() {
		return this.block;
	}
}