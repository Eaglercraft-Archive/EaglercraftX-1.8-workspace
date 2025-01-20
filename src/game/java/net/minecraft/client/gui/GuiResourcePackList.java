package net.minecraft.client.gui;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.ResourcePackListEntry;
import net.minecraft.util.EnumChatFormatting;

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
public abstract class GuiResourcePackList extends GuiListExtended {
	protected final Minecraft mc;
	protected final List<ResourcePackListEntry> field_148204_l;

	public GuiResourcePackList(Minecraft mcIn, int parInt1, int parInt2, List<ResourcePackListEntry> parList) {
		super(mcIn, parInt1, parInt2, 32, parInt2 - 55 + 4, 36);
		this.mc = mcIn;
		this.field_148204_l = parList;
		this.field_148163_i = false;
		this.setHasListHeader(true, (int) ((float) mcIn.fontRendererObj.FONT_HEIGHT * 1.5F));
	}

	/**+
	 * Handles drawing a list's header row.
	 */
	protected void drawListHeader(int i, int j, Tessellator var3) {
		String s = EnumChatFormatting.UNDERLINE + "" + EnumChatFormatting.BOLD + this.getListHeader();
		this.mc.fontRendererObj.drawString(s, i + this.width / 2 - this.mc.fontRendererObj.getStringWidth(s) / 2,
				Math.min(this.top + 3, j), 16777215);
	}

	protected abstract String getListHeader();

	public List<ResourcePackListEntry> getList() {
		return this.field_148204_l;
	}

	protected int getSize() {
		return this.getList().size();
	}

	/**+
	 * Gets the IGuiListEntry object for the given index
	 */
	public ResourcePackListEntry getListEntry(int i) {
		return (ResourcePackListEntry) this.getList().get(i);
	}

	/**+
	 * Gets the width of the list
	 */
	public int getListWidth() {
		return this.width;
	}

	protected int getScrollBarX() {
		return this.right - 6;
	}
}