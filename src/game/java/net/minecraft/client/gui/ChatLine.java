package net.minecraft.client.gui;

import net.lax1dude.eaglercraft.v1_8.profanity_filter.ProfanityFilter;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IChatComponent;

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
public class ChatLine {
	private final int updateCounterCreated;
	private final IChatComponent lineString;
	private IChatComponent lineStringProfanityFilter;
	private final int chatLineID;

	public ChatLine(int parInt1, IChatComponent parIChatComponent, int parInt2) {
		this.lineString = parIChatComponent;
		this.updateCounterCreated = parInt1;
		this.chatLineID = parInt2;
	}

	public IChatComponent getChatComponent() {
		if (Minecraft.getMinecraft().isEnableProfanityFilter()) {
			if (lineStringProfanityFilter == null) {
				lineStringProfanityFilter = ProfanityFilter.getInstance().profanityFilterChatComponent(lineString);
			}
			return lineStringProfanityFilter;
		} else {
			return lineString;
		}
	}

	public int getUpdatedCounter() {
		return this.updateCounterCreated;
	}

	public int getChatLineID() {
		return this.chatLineID;
	}
}