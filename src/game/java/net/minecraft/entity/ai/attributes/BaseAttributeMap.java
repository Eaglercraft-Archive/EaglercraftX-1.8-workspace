package net.minecraft.entity.ai.attributes;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

import net.minecraft.server.management.LowerStringMap;

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
public abstract class BaseAttributeMap {
	protected final Map<IAttribute, IAttributeInstance> attributes = Maps.newHashMap();
	protected final Map<String, IAttributeInstance> attributesByName = new LowerStringMap();
	protected final Multimap<IAttribute, IAttribute> field_180377_c = HashMultimap.create();

	public IAttributeInstance getAttributeInstance(IAttribute attribute) {
		return (IAttributeInstance) this.attributes.get(attribute);
	}

	public IAttributeInstance getAttributeInstanceByName(String attributeName) {
		return (IAttributeInstance) this.attributesByName.get(attributeName);
	}

	/**+
	 * Registers an attribute with this AttributeMap, returns a
	 * modifiable AttributeInstance associated with this map
	 */
	public IAttributeInstance registerAttribute(IAttribute attribute) {
		if (this.attributesByName.containsKey(attribute.getAttributeUnlocalizedName())) {
			throw new IllegalArgumentException("Attribute is already registered!");
		} else {
			IAttributeInstance iattributeinstance = this.func_180376_c(attribute);
			this.attributesByName.put(attribute.getAttributeUnlocalizedName(), iattributeinstance);
			this.attributes.put(attribute, iattributeinstance);

			for (IAttribute iattribute = attribute.func_180372_d(); iattribute != null; iattribute = iattribute
					.func_180372_d()) {
				this.field_180377_c.put(iattribute, attribute);
			}

			return iattributeinstance;
		}
	}

	protected abstract IAttributeInstance func_180376_c(IAttribute var1);

	public Collection<IAttributeInstance> getAllAttributes() {
		return this.attributesByName.values();
	}

	public void func_180794_a(IAttributeInstance parIAttributeInstance) {
	}

	public void removeAttributeModifiers(Multimap<String, AttributeModifier> parMultimap) {
		for (Entry entry : parMultimap.entries()) {
			IAttributeInstance iattributeinstance = this.getAttributeInstanceByName((String) entry.getKey());
			if (iattributeinstance != null) {
				iattributeinstance.removeModifier((AttributeModifier) entry.getValue());
			}
		}

	}

	public void applyAttributeModifiers(Multimap<String, AttributeModifier> parMultimap) {
		for (Entry entry : parMultimap.entries()) {
			IAttributeInstance iattributeinstance = this.getAttributeInstanceByName((String) entry.getKey());
			if (iattributeinstance != null) {
				iattributeinstance.removeModifier((AttributeModifier) entry.getValue());
				iattributeinstance.applyModifier((AttributeModifier) entry.getValue());
			}
		}

	}
}