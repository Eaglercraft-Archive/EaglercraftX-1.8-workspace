package net.minecraft.tileentity;

import org.json.JSONException;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S33PacketUpdateSign;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentProcessor;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

/**+
 * This portion of EaglercraftX contains deobfuscated Minecraft 1.8 source code.
 * 
 * Minecraft 1.8.8 bytecode is (c) 2015 Mojang AB. "Do not distribute!"
 * Mod Coder Pack v9.18 deobfuscation configs are (c) Copyright by the MCP Team
 * 
 * EaglercraftX 1.8 patch files (c) 2022-2024 lax1dude, ayunami2000. All Rights Reserved.
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
public class TileEntitySign extends TileEntity {
	public final IChatComponent[] signText = new IChatComponent[] { new ChatComponentText(""),
			new ChatComponentText(""), new ChatComponentText(""), new ChatComponentText("") };
	/**+
	 * The index of the line currently being edited. Only used on
	 * client side, but defined on both. Note this is only really
	 * used when the > < are going to be visible.
	 */
	public int lineBeingEdited = -1;
	private boolean isEditable = true;
	private EntityPlayer player;

	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);

		for (int i = 0; i < 4; ++i) {
			String s = IChatComponent.Serializer.componentToJson(this.signText[i]);
			nbttagcompound.setString("Text" + (i + 1), s);
		}

	}

	public void readFromNBT(NBTTagCompound nbttagcompound) {
		this.isEditable = false;
		super.readFromNBT(nbttagcompound);
		ICommandSender icommandsender = new ICommandSender() {
			public String getName() {
				return "Sign";
			}

			public IChatComponent getDisplayName() {
				return new ChatComponentText(this.getName());
			}

			public void addChatMessage(IChatComponent var1) {
			}

			public boolean canCommandSenderUseCommand(int var1, String var2) {
				return true;
			}

			public BlockPos getPosition() {
				return TileEntitySign.this.pos;
			}

			public Vec3 getPositionVector() {
				return new Vec3((double) TileEntitySign.this.pos.getX() + 0.5D,
						(double) TileEntitySign.this.pos.getY() + 0.5D, (double) TileEntitySign.this.pos.getZ() + 0.5D);
			}

			public World getEntityWorld() {
				return TileEntitySign.this.worldObj;
			}

			public Entity getCommandSenderEntity() {
				return null;
			}

			public boolean sendCommandFeedback() {
				return false;
			}

		};

		for (int i = 0; i < 4; ++i) {
			String s = nbttagcompound.getString("Text" + (i + 1));

			try {
				IChatComponent ichatcomponent = IChatComponent.Serializer.jsonToComponent(s);

				try {
					this.signText[i] = ChatComponentProcessor.processComponent(icommandsender, ichatcomponent,
							(Entity) null);
				} catch (CommandException var7) {
					this.signText[i] = ichatcomponent;
				}
			} catch (JSONException var8) {
				this.signText[i] = new ChatComponentText(s);
			}
		}

	}

	/**+
	 * Allows for a specialized description packet to be created.
	 * This is often used to sync tile entity data from the server
	 * to the client easily. For example this is used by signs to
	 * synchronise the text to be displayed.
	 */
	public Packet getDescriptionPacket() {
		IChatComponent[] aichatcomponent = new IChatComponent[4];
		System.arraycopy(this.signText, 0, aichatcomponent, 0, 4);
		return new S33PacketUpdateSign(this.worldObj, this.pos, aichatcomponent);
	}

	public boolean func_183000_F() {
		return true;
	}

	public boolean getIsEditable() {
		return this.isEditable;
	}

	/**+
	 * Sets the sign's isEditable flag to the specified parameter.
	 */
	public void setEditable(boolean isEditableIn) {
		this.isEditable = isEditableIn;
		if (!isEditableIn) {
			this.player = null;
		}

	}

	public void setPlayer(EntityPlayer playerIn) {
		this.player = playerIn;
	}

	public EntityPlayer getPlayer() {
		return this.player;
	}

	public boolean executeCommand(final EntityPlayer playerIn) {
		return true;
	}

}