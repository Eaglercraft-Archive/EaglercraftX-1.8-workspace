package net.minecraft.command;

import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Vec3;
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
public interface ICommandSender {
	/**+
	 * Gets the name of this command sender (usually username, but
	 * possibly "Rcon")
	 */
	String getName();

	/**+
	 * Get the formatted ChatComponent that will be used for the
	 * sender's username in chat
	 */
	IChatComponent getDisplayName();

	/**+
	 * Send a chat message to the CommandSender
	 */
	void addChatMessage(IChatComponent var1);

	/**+
	 * Returns {@code true} if the CommandSender is allowed to
	 * execute the command, {@code false} if not
	 */
	boolean canCommandSenderUseCommand(int var1, String var2);

	/**+
	 * Get the position in the world. <b>{@code null} is not
	 * allowed!</b> If you are not an entity in the world, return
	 * the coordinates 0, 0, 0
	 */
	BlockPos getPosition();

	/**+
	 * Get the position vector. <b>{@code null} is not allowed!</b>
	 * If you are not an entity in the world, return 0.0D, 0.0D,
	 * 0.0D
	 */
	Vec3 getPositionVector();

	/**+
	 * Get the world, if available. <b>{@code null} is not
	 * allowed!</b> If you are not an entity in the world, return
	 * the overworld
	 */
	World getEntityWorld();

	/**+
	 * Returns the entity associated with the command sender. MAY BE
	 * NULL!
	 */
	Entity getCommandSenderEntity();

	/**+
	 * Returns true if the command sender should be sent feedback
	 * about executed commands
	 */
	boolean sendCommandFeedback();

	void setCommandStat(CommandResultStats.Type var1, int var2);
}