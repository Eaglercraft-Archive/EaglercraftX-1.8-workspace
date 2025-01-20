package net.minecraft.client.player.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.IInteractionObject;

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
public class LocalBlockIntercommunication implements IInteractionObject {
	private String guiID;
	private IChatComponent displayName;

	public LocalBlockIntercommunication(String guiIdIn, IChatComponent displayNameIn) {
		this.guiID = guiIdIn;
		this.displayName = displayNameIn;
	}

	public Container createContainer(InventoryPlayer var1, EntityPlayer var2) {
		throw new UnsupportedOperationException();
	}

	/**+
	 * Gets the name of this command sender (usually username, but
	 * possibly "Rcon")
	 */
	public String getName() {
		return this.displayName.getUnformattedText();
	}

	/**+
	 * Returns true if this thing is named
	 */
	public boolean hasCustomName() {
		return true;
	}

	public String getGuiID() {
		return this.guiID;
	}

	/**+
	 * Get the formatted ChatComponent that will be used for the
	 * sender's username in chat
	 */
	public IChatComponent getDisplayName() {
		return this.displayName;
	}
}