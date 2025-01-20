package net.minecraft.block;

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
public class BlockEventData {
	private BlockPos position;
	private Block blockType;
	private int eventID;
	private int eventParameter;

	public BlockEventData(BlockPos pos, Block blockType, int eventId, int parInt1) {
		this.position = pos;
		this.eventID = eventId;
		this.eventParameter = parInt1;
		this.blockType = blockType;
	}

	public BlockPos getPosition() {
		return this.position;
	}

	/**+
	 * Get the Event ID (different for each BlockID)
	 */
	public int getEventID() {
		return this.eventID;
	}

	public int getEventParameter() {
		return this.eventParameter;
	}

	public Block getBlock() {
		return this.blockType;
	}

	public boolean equals(Object parObject) {
		if (!(parObject instanceof BlockEventData)) {
			return false;
		} else {
			BlockEventData blockeventdata = (BlockEventData) parObject;
			return this.position.equals(blockeventdata.position) && this.eventID == blockeventdata.eventID
					&& this.eventParameter == blockeventdata.eventParameter
					&& this.blockType == blockeventdata.blockType;
		}
	}

	public String toString() {
		return "TE(" + this.position + ")," + this.eventID + "," + this.eventParameter + "," + this.blockType;
	}
}