package net.minecraft.client.gui;

import java.util.List;

import net.lax1dude.eaglercraft.v1_8.netty.Unpooled;
import net.lax1dude.eaglercraft.v1_8.Keyboard;
import net.lax1dude.eaglercraft.v1_8.minecraft.EnumInputEvent;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerRepair;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.util.ResourceLocation;
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
public class GuiRepair extends GuiContainer implements ICrafting {
	private static final ResourceLocation anvilResource = new ResourceLocation("textures/gui/container/anvil.png");
	private ContainerRepair anvil;
	private GuiTextField nameField;
	private InventoryPlayer playerInventory;

	public GuiRepair(InventoryPlayer inventoryIn, World worldIn) {
		super(new ContainerRepair(inventoryIn, worldIn, Minecraft.getMinecraft().thePlayer));
		this.playerInventory = inventoryIn;
		this.anvil = (ContainerRepair) this.inventorySlots;
	}

	/**+
	 * Adds the buttons (and other controls) to the screen in
	 * question. Called when the GUI is displayed and when the
	 * window resizes, the buttonList is cleared beforehand.
	 */
	public void initGui() {
		super.initGui();
		Keyboard.enableRepeatEvents(true);
		int i = (this.width - this.xSize) / 2;
		int j = (this.height - this.ySize) / 2;
		this.nameField = new GuiTextField(0, this.fontRendererObj, i + 62, j + 24, 103, 12);
		this.nameField.setTextColor(-1);
		this.nameField.setDisabledTextColour(-1);
		this.nameField.setEnableBackgroundDrawing(false);
		this.nameField.setMaxStringLength(30);
		this.inventorySlots.removeCraftingFromCrafters(this);
		this.inventorySlots.onCraftGuiOpened(this);
	}

	/**+
	 * Called when the screen is unloaded. Used to disable keyboard
	 * repeat events
	 */
	public void onGuiClosed() {
		super.onGuiClosed();
		Keyboard.enableRepeatEvents(false);
		this.inventorySlots.removeCraftingFromCrafters(this);
	}

	/**+
	 * Draw the foreground layer for the GuiContainer (everything in
	 * front of the items). Args : mouseX, mouseY
	 */
	protected void drawGuiContainerForegroundLayer(int var1, int var2) {
		GlStateManager.disableLighting();
		GlStateManager.disableBlend();
		this.fontRendererObj.drawString(I18n.format("container.repair", new Object[0]), 60, 6, 4210752);
		if (this.anvil.maximumCost > 0) {
			int i = 8453920;
			boolean flag = true;
			String s = I18n.format("container.repair.cost", new Object[] { Integer.valueOf(this.anvil.maximumCost) });
			if (this.anvil.maximumCost >= 40 && !this.mc.thePlayer.capabilities.isCreativeMode) {
				s = I18n.format("container.repair.expensive", new Object[0]);
				i = 16736352;
			} else if (!this.anvil.getSlot(2).getHasStack()) {
				flag = false;
			} else if (!this.anvil.getSlot(2).canTakeStack(this.playerInventory.player)) {
				i = 16736352;
			}

			if (flag) {
				int j = -16777216 | (i & 16579836) >> 2 | i & -16777216;
				int k = this.xSize - 8 - this.fontRendererObj.getStringWidth(s);
				byte b0 = 67;
				if (this.fontRendererObj.getUnicodeFlag()) {
					drawRect(k - 3, b0 - 2, this.xSize - 7, b0 + 10, -16777216);
					drawRect(k - 2, b0 - 1, this.xSize - 8, b0 + 9, -12895429);
				} else {
					this.fontRendererObj.drawString(s, k, b0 + 1, j);
					this.fontRendererObj.drawString(s, k + 1, b0, j);
					this.fontRendererObj.drawString(s, k + 1, b0 + 1, j);
				}

				this.fontRendererObj.drawString(s, k, b0, i);
			}
		}

		GlStateManager.enableLighting();
	}

	/**+
	 * Fired when a key is typed (except F11 which toggles full
	 * screen). This is the equivalent of
	 * KeyListener.keyTyped(KeyEvent e). Args : character (character
	 * on the key), keyCode (lwjgl Keyboard key code)
	 */
	protected void keyTyped(char parChar1, int parInt1) {
		if (this.nameField.textboxKeyTyped(parChar1, parInt1)) {
			this.renameItem();
		} else {
			super.keyTyped(parChar1, parInt1);
		}

	}

	private void renameItem() {
		String s = this.nameField.getText();
		Slot slot = this.anvil.getSlot(0);
		if (slot != null && slot.getHasStack() && !slot.getStack().hasDisplayName()
				&& s.equals(slot.getStack().getDisplayName())) {
			s = "";
		}

		this.anvil.updateItemName(s);
		this.mc.thePlayer.sendQueue.addToSendQueue(
				new C17PacketCustomPayload("MC|ItemName", (new PacketBuffer(Unpooled.buffer())).writeString(s)));
	}

	/**+
	 * Called when the mouse is clicked. Args : mouseX, mouseY,
	 * clickedButton
	 */
	protected void mouseClicked(int parInt1, int parInt2, int parInt3) {
		super.mouseClicked(parInt1, parInt2, parInt3);
		this.nameField.mouseClicked(parInt1, parInt2, parInt3);
	}

	/**+
	 * Draws the screen and all the components in it. Args : mouseX,
	 * mouseY, renderPartialTicks
	 */
	public void drawScreen(int i, int j, float f) {
		super.drawScreen(i, j, f);
		GlStateManager.disableLighting();
		GlStateManager.disableBlend();
		this.nameField.drawTextBox();
	}

	/**+
	 * Args : renderPartialTicks, mouseX, mouseY
	 */
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(anvilResource);
		int i = (this.width - this.xSize) / 2;
		int j = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
		this.drawTexturedModalRect(i + 59, j + 20, 0, this.ySize + (this.anvil.getSlot(0).getHasStack() ? 0 : 16), 110,
				16);
		if ((this.anvil.getSlot(0).getHasStack() || this.anvil.getSlot(1).getHasStack())
				&& !this.anvil.getSlot(2).getHasStack()) {
			this.drawTexturedModalRect(i + 99, j + 45, this.xSize, 0, 28, 21);
		}

	}

	/**+
	 * update the crafting window inventory with the items in the
	 * list
	 */
	public void updateCraftingInventory(Container containerToSend, List<ItemStack> itemsList) {
		this.sendSlotContents(containerToSend, 0, containerToSend.getSlot(0).getStack());
	}

	/**+
	 * Sends the contents of an inventory slot to the client-side
	 * Container. This doesn't have to match the actual contents of
	 * that slot. Args: Container, slot number, slot contents
	 */
	public void sendSlotContents(Container containerToSend, int slotInd, ItemStack stack) {
		if (slotInd == 0) {
			this.nameField.setText(stack == null ? "" : stack.getDisplayName());
			this.nameField.setEnabled(stack != null);
			if (stack != null) {
				this.renameItem();
			}
		}

	}

	/**+
	 * Sends two ints to the client-side Container. Used for furnace
	 * burning time, smelting progress, brewing progress, and
	 * enchanting level. Normally the first int identifies which
	 * variable to update, and the second contains the new value.
	 * Both are truncated to shorts in non-local SMP.
	 */
	public void sendProgressBarUpdate(Container containerIn, int varToUpdate, int newValue) {
	}

	public void func_175173_a(Container parContainer, IInventory parIInventory) {
	}

	public boolean blockPTTKey() {
		return nameField.isFocused();
	}

	@Override
	public boolean showCopyPasteButtons() {
		return nameField.isFocused();
	}

	@Override
	public void fireInputEvent(EnumInputEvent event, String param) {
		nameField.fireInputEvent(event, param);
	}

}