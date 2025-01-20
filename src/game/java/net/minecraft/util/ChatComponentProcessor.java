package net.minecraft.util;

import java.util.List;

import net.minecraft.command.CommandException;
import net.minecraft.command.EntityNotFoundException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerSelector;
import net.minecraft.entity.Entity;

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
public class ChatComponentProcessor {
	public static IChatComponent processComponent(ICommandSender commandSender, IChatComponent component,
			Entity entityIn) throws CommandException {
		Object object = null;
		if (component instanceof ChatComponentScore) {
			ChatComponentScore chatcomponentscore = (ChatComponentScore) component;
			String s = chatcomponentscore.getName();
			if (PlayerSelector.hasArguments(s)) {
				List list = PlayerSelector.matchEntities(commandSender, s, Entity.class);
				if (list.size() != 1) {
					throw new EntityNotFoundException();
				}

				s = ((Entity) list.get(0)).getName();
			}

			object = entityIn != null && s.equals("*")
					? new ChatComponentScore(entityIn.getName(), chatcomponentscore.getObjective())
					: new ChatComponentScore(s, chatcomponentscore.getObjective());
			((ChatComponentScore) object).setValue(chatcomponentscore.getUnformattedTextForChat());
		} else if (component instanceof ChatComponentSelector) {
			String s1 = ((ChatComponentSelector) component).getSelector();
			object = PlayerSelector.matchEntitiesToChatComponent(commandSender, s1);
			if (object == null) {
				object = new ChatComponentText("");
			}
		} else if (component instanceof ChatComponentText) {
			object = new ChatComponentText(((ChatComponentText) component).getChatComponentText_TextValue());
		} else {
			if (!(component instanceof ChatComponentTranslation)) {
				return component;
			}

			Object[] aobject = ((ChatComponentTranslation) component).getFormatArgs();

			for (int i = 0; i < aobject.length; ++i) {
				Object object1 = aobject[i];
				if (object1 instanceof IChatComponent) {
					aobject[i] = processComponent(commandSender, (IChatComponent) object1, entityIn);
				}
			}

			object = new ChatComponentTranslation(((ChatComponentTranslation) component).getKey(), aobject);
		}

		ChatStyle chatstyle = component.getChatStyle();
		if (chatstyle != null) {
			((IChatComponent) object).setChatStyle(chatstyle.createShallowCopy());
		}

		List<IChatComponent> lst = component.getSiblings();
		for (int i = 0, l = lst.size(); i < l; ++i) {
			((IChatComponent) object).appendSibling(processComponent(commandSender, lst.get(i), entityIn));
		}

		return (IChatComponent) object;
	}
}