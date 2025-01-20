package net.minecraft.entity.ai.attributes;

import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
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
public class ServersideAttributeMap extends BaseAttributeMap {
	private final Set<IAttributeInstance> attributeInstanceSet = Sets.newHashSet();
	protected final Map<String, IAttributeInstance> descriptionToAttributeInstanceMap = new LowerStringMap();

	public ModifiableAttributeInstance getAttributeInstance(IAttribute iattribute) {
		return (ModifiableAttributeInstance) super.getAttributeInstance(iattribute);
	}

	public ModifiableAttributeInstance getAttributeInstanceByName(String s) {
		IAttributeInstance iattributeinstance = super.getAttributeInstanceByName(s);
		if (iattributeinstance == null) {
			iattributeinstance = (IAttributeInstance) this.descriptionToAttributeInstanceMap.get(s);
		}

		return (ModifiableAttributeInstance) iattributeinstance;
	}

	/**+
	 * Registers an attribute with this AttributeMap, returns a
	 * modifiable AttributeInstance associated with this map
	 */
	public IAttributeInstance registerAttribute(IAttribute iattribute) {
		IAttributeInstance iattributeinstance = super.registerAttribute(iattribute);
		if (iattribute instanceof RangedAttribute && ((RangedAttribute) iattribute).getDescription() != null) {
			this.descriptionToAttributeInstanceMap.put(((RangedAttribute) iattribute).getDescription(),
					iattributeinstance);
		}

		return iattributeinstance;
	}

	protected IAttributeInstance func_180376_c(IAttribute iattribute) {
		return new ModifiableAttributeInstance(this, iattribute);
	}

	public void func_180794_a(IAttributeInstance iattributeinstance) {
		if (iattributeinstance.getAttribute().getShouldWatch()) {
			this.attributeInstanceSet.add(iattributeinstance);
		}

		for (IAttribute iattribute : this.field_180377_c.get(iattributeinstance.getAttribute())) {
			ModifiableAttributeInstance modifiableattributeinstance = this.getAttributeInstance(iattribute);
			if (modifiableattributeinstance != null) {
				modifiableattributeinstance.flagForUpdate();
			}
		}

	}

	public Set<IAttributeInstance> getAttributeInstanceSet() {
		return this.attributeInstanceSet;
	}

	public Collection<IAttributeInstance> getWatchedAttributes() {
		HashSet hashset = Sets.newHashSet();

		for (IAttributeInstance iattributeinstance : this.getAllAttributes()) {
			if (iattributeinstance.getAttribute().getShouldWatch()) {
				hashset.add(iattributeinstance);
			}
		}

		return hashset;
	}
}