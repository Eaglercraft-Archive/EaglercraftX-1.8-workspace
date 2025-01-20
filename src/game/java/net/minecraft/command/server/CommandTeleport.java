package net.minecraft.command.server;

import java.util.EnumSet;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

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
public class CommandTeleport extends CommandBase {

	/**+
	 * Gets the name of the command
	 */
	public String getCommandName() {
		return "tp";
	}

	/**+
	 * Return the required permission level for this command.
	 */
	public int getRequiredPermissionLevel() {
		return 2;
	}

	/**+
	 * Gets the usage string for the command.
	 */
	public String getCommandUsage(ICommandSender var1) {
		return "commands.tp.usage";
	}

	/**+
	 * Callback when the command is invoked
	 */
	public void processCommand(ICommandSender parICommandSender, String[] parArrayOfString) throws CommandException {
		if (parArrayOfString.length < 1) {
			throw new WrongUsageException("commands.tp.usage", new Object[0]);
		} else {
			byte b0 = 0;
			Object object;
			if (parArrayOfString.length != 2 && parArrayOfString.length != 4 && parArrayOfString.length != 6) {
				object = getCommandSenderAsPlayer(parICommandSender);
			} else {
				object = func_175768_b(parICommandSender, parArrayOfString[0]);
				b0 = 1;
			}

			if (parArrayOfString.length != 1 && parArrayOfString.length != 2) {
				if (parArrayOfString.length < b0 + 3) {
					throw new WrongUsageException("commands.tp.usage", new Object[0]);
				} else if (((Entity) object).worldObj != null) {
					int i = b0 + 1;
					CommandBase.CoordinateArg commandbase$coordinatearg = parseCoordinate(((Entity) object).posX,
							parArrayOfString[b0], true);
					CommandBase.CoordinateArg commandbase$coordinatearg1 = parseCoordinate(((Entity) object).posY,
							parArrayOfString[i++], 0, 0, false);
					CommandBase.CoordinateArg commandbase$coordinatearg2 = parseCoordinate(((Entity) object).posZ,
							parArrayOfString[i++], true);
					CommandBase.CoordinateArg commandbase$coordinatearg3 = parseCoordinate(
							(double) ((Entity) object).rotationYaw,
							parArrayOfString.length > i ? parArrayOfString[i++] : "~", false);
					CommandBase.CoordinateArg commandbase$coordinatearg4 = parseCoordinate(
							(double) ((Entity) object).rotationPitch,
							parArrayOfString.length > i ? parArrayOfString[i] : "~", false);
					if (object instanceof EntityPlayerMP) {
						EnumSet enumset = EnumSet.noneOf(S08PacketPlayerPosLook.EnumFlags.class);
						if (commandbase$coordinatearg.func_179630_c()) {
							enumset.add(S08PacketPlayerPosLook.EnumFlags.X);
						}

						if (commandbase$coordinatearg1.func_179630_c()) {
							enumset.add(S08PacketPlayerPosLook.EnumFlags.Y);
						}

						if (commandbase$coordinatearg2.func_179630_c()) {
							enumset.add(S08PacketPlayerPosLook.EnumFlags.Z);
						}

						if (commandbase$coordinatearg4.func_179630_c()) {
							enumset.add(S08PacketPlayerPosLook.EnumFlags.X_ROT);
						}

						if (commandbase$coordinatearg3.func_179630_c()) {
							enumset.add(S08PacketPlayerPosLook.EnumFlags.Y_ROT);
						}

						float f = (float) commandbase$coordinatearg3.func_179629_b();
						if (!commandbase$coordinatearg3.func_179630_c()) {
							f = MathHelper.wrapAngleTo180_float(f);
						}

						float f1 = (float) commandbase$coordinatearg4.func_179629_b();
						if (!commandbase$coordinatearg4.func_179630_c()) {
							f1 = MathHelper.wrapAngleTo180_float(f1);
						}

						if (f1 > 90.0F || f1 < -90.0F) {
							f1 = MathHelper.wrapAngleTo180_float(180.0F - f1);
							f = MathHelper.wrapAngleTo180_float(f + 180.0F);
						}

						((Entity) object).mountEntity((Entity) null);
						((EntityPlayerMP) object).playerNetServerHandler.setPlayerLocation(
								commandbase$coordinatearg.func_179629_b(), commandbase$coordinatearg1.func_179629_b(),
								commandbase$coordinatearg2.func_179629_b(), f, f1, enumset);
						((Entity) object).setRotationYawHead(f);
					} else {
						float f2 = (float) MathHelper.wrapAngleTo180_double(commandbase$coordinatearg3.func_179628_a());
						float f3 = (float) MathHelper.wrapAngleTo180_double(commandbase$coordinatearg4.func_179628_a());
						if (f3 > 90.0F || f3 < -90.0F) {
							f3 = MathHelper.wrapAngleTo180_float(180.0F - f3);
							f2 = MathHelper.wrapAngleTo180_float(f2 + 180.0F);
						}

						((Entity) object).setLocationAndAngles(commandbase$coordinatearg.func_179628_a(),
								commandbase$coordinatearg1.func_179628_a(), commandbase$coordinatearg2.func_179628_a(),
								f2, f3);
						((Entity) object).setRotationYawHead(f2);
					}

					notifyOperators(parICommandSender, this, "commands.tp.success.coordinates",
							new Object[] { ((Entity) object).getName(),
									Double.valueOf(commandbase$coordinatearg.func_179628_a()),
									Double.valueOf(commandbase$coordinatearg1.func_179628_a()),
									Double.valueOf(commandbase$coordinatearg2.func_179628_a()) });
				}
			} else {
				Entity entity = func_175768_b(parICommandSender, parArrayOfString[parArrayOfString.length - 1]);
				if (entity.worldObj != ((Entity) object).worldObj) {
					throw new CommandException("commands.tp.notSameDimension", new Object[0]);
				} else {
					((Entity) object).mountEntity((Entity) null);
					if (object instanceof EntityPlayerMP) {
						((EntityPlayerMP) object).playerNetServerHandler.setPlayerLocation(entity.posX, entity.posY,
								entity.posZ, entity.rotationYaw, entity.rotationPitch);
					} else {
						((Entity) object).setLocationAndAngles(entity.posX, entity.posY, entity.posZ,
								entity.rotationYaw, entity.rotationPitch);
					}

					notifyOperators(parICommandSender, this, "commands.tp.success",
							new Object[] { ((Entity) object).getName(), entity.getName() });
				}
			}
		}
	}

	/**+
	 * Return a list of options when the user types TAB
	 */
	public List<String> addTabCompletionOptions(ICommandSender var1, String[] astring, BlockPos var3) {
		return astring.length != 1 && astring.length != 2 ? null
				: getListOfStringsMatchingLastWord(astring, MinecraftServer.getServer().getAllUsernames());
	}

	/**+
	 * Return whether the specified command parameter index is a
	 * username parameter.
	 */
	public boolean isUsernameIndex(String[] var1, int i) {
		return i == 0;
	}
}