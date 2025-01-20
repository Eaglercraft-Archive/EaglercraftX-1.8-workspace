package net.minecraft.entity;

import java.util.Collection;
import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;

import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.BaseAttributeMap;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

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
public class SharedMonsterAttributes {
	private static final Logger logger = LogManager.getLogger();
	public static final IAttribute maxHealth = (new RangedAttribute((IAttribute) null, "generic.maxHealth", 20.0D, 0.0D,
			1024.0D)).setDescription("Max Health").setShouldWatch(true);
	public static final IAttribute followRange = (new RangedAttribute((IAttribute) null, "generic.followRange", 32.0D,
			0.0D, 2048.0D)).setDescription("Follow Range");
	public static final IAttribute knockbackResistance = (new RangedAttribute((IAttribute) null,
			"generic.knockbackResistance", 0.0D, 0.0D, 1.0D)).setDescription("Knockback Resistance");
	public static final IAttribute movementSpeed = (new RangedAttribute((IAttribute) null, "generic.movementSpeed",
			0.699999988079071D, 0.0D, 1024.0D)).setDescription("Movement Speed").setShouldWatch(true);
	public static final IAttribute attackDamage = new RangedAttribute((IAttribute) null, "generic.attackDamage", 2.0D,
			0.0D, 2048.0D);

	/**+
	 * Creates an NBTTagList from a BaseAttributeMap, including all
	 * its AttributeInstances
	 */
	public static NBTTagList writeBaseAttributeMapToNBT(BaseAttributeMap parBaseAttributeMap) {
		NBTTagList nbttaglist = new NBTTagList();

		for (IAttributeInstance iattributeinstance : parBaseAttributeMap.getAllAttributes()) {
			nbttaglist.appendTag(writeAttributeInstanceToNBT(iattributeinstance));
		}

		return nbttaglist;
	}

	/**+
	 * Creates an NBTTagCompound from an AttributeInstance,
	 * including its AttributeModifiers
	 */
	private static NBTTagCompound writeAttributeInstanceToNBT(IAttributeInstance parIAttributeInstance) {
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		IAttribute iattribute = parIAttributeInstance.getAttribute();
		nbttagcompound.setString("Name", iattribute.getAttributeUnlocalizedName());
		nbttagcompound.setDouble("Base", parIAttributeInstance.getBaseValue());
		Collection collection = parIAttributeInstance.func_111122_c();
		if (collection != null && !collection.isEmpty()) {
			NBTTagList nbttaglist = new NBTTagList();

			for (AttributeModifier attributemodifier : (Collection<AttributeModifier>) collection) {
				if (attributemodifier.isSaved()) {
					nbttaglist.appendTag(writeAttributeModifierToNBT(attributemodifier));
				}
			}

			nbttagcompound.setTag("Modifiers", nbttaglist);
		}

		return nbttagcompound;
	}

	/**+
	 * Creates an NBTTagCompound from an AttributeModifier
	 */
	private static NBTTagCompound writeAttributeModifierToNBT(AttributeModifier parAttributeModifier) {
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		nbttagcompound.setString("Name", parAttributeModifier.getName());
		nbttagcompound.setDouble("Amount", parAttributeModifier.getAmount());
		nbttagcompound.setInteger("Operation", parAttributeModifier.getOperation());
		nbttagcompound.setLong("UUIDMost", parAttributeModifier.getID().getMostSignificantBits());
		nbttagcompound.setLong("UUIDLeast", parAttributeModifier.getID().getLeastSignificantBits());
		return nbttagcompound;
	}

	public static void func_151475_a(BaseAttributeMap parBaseAttributeMap, NBTTagList parNBTTagList) {
		for (int i = 0; i < parNBTTagList.tagCount(); ++i) {
			NBTTagCompound nbttagcompound = parNBTTagList.getCompoundTagAt(i);
			IAttributeInstance iattributeinstance = parBaseAttributeMap
					.getAttributeInstanceByName(nbttagcompound.getString("Name"));
			if (iattributeinstance != null) {
				applyModifiersToAttributeInstance(iattributeinstance, nbttagcompound);
			} else {
				logger.warn("Ignoring unknown attribute \'" + nbttagcompound.getString("Name") + "\'");
			}
		}

	}

	private static void applyModifiersToAttributeInstance(IAttributeInstance parIAttributeInstance,
			NBTTagCompound parNBTTagCompound) {
		parIAttributeInstance.setBaseValue(parNBTTagCompound.getDouble("Base"));
		if (parNBTTagCompound.hasKey("Modifiers", 9)) {
			NBTTagList nbttaglist = parNBTTagCompound.getTagList("Modifiers", 10);

			for (int i = 0; i < nbttaglist.tagCount(); ++i) {
				AttributeModifier attributemodifier = readAttributeModifierFromNBT(nbttaglist.getCompoundTagAt(i));
				if (attributemodifier != null) {
					AttributeModifier attributemodifier1 = parIAttributeInstance.getModifier(attributemodifier.getID());
					if (attributemodifier1 != null) {
						parIAttributeInstance.removeModifier(attributemodifier1);
					}

					parIAttributeInstance.applyModifier(attributemodifier);
				}
			}
		}

	}

	/**+
	 * Creates an AttributeModifier from an NBTTagCompound
	 */
	public static AttributeModifier readAttributeModifierFromNBT(NBTTagCompound parNBTTagCompound) {
		EaglercraftUUID uuid = new EaglercraftUUID(parNBTTagCompound.getLong("UUIDMost"),
				parNBTTagCompound.getLong("UUIDLeast"));

		try {
			return new AttributeModifier(uuid, parNBTTagCompound.getString("Name"),
					parNBTTagCompound.getDouble("Amount"), parNBTTagCompound.getInteger("Operation"));
		} catch (Exception exception) {
			logger.warn("Unable to create attribute: " + exception.getMessage());
			return null;
		}
	}
}