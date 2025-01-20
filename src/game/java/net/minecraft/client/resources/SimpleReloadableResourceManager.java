package net.minecraft.client.resources;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.util.ResourceLocation;

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
public class SimpleReloadableResourceManager implements IReloadableResourceManager {
	private static final Logger logger = LogManager.getLogger();
	private static final Joiner joinerResourcePacks = Joiner.on(", ");
	private final Map<String, FallbackResourceManager> domainResourceManagers = Maps.newHashMap();
	private final List<IResourceManagerReloadListener> reloadListeners = Lists.newArrayList();
	private final Set<String> setResourceDomains = Sets.newLinkedHashSet();
	private final IMetadataSerializer rmMetadataSerializer;

	public SimpleReloadableResourceManager(IMetadataSerializer rmMetadataSerializerIn) {
		this.rmMetadataSerializer = rmMetadataSerializerIn;
	}

	public void reloadResourcePack(IResourcePack resourcePack) {
		for (String s : resourcePack.getResourceDomains()) {
			this.setResourceDomains.add(s);
			FallbackResourceManager fallbackresourcemanager = (FallbackResourceManager) this.domainResourceManagers
					.get(s);
			if (fallbackresourcemanager == null) {
				fallbackresourcemanager = new FallbackResourceManager(this.rmMetadataSerializer);
				this.domainResourceManagers.put(s, fallbackresourcemanager);
			}

			fallbackresourcemanager.addResourcePack(resourcePack);
		}

	}

	public Set<String> getResourceDomains() {
		return this.setResourceDomains;
	}

	public IResource getResource(ResourceLocation parResourceLocation) throws IOException {
		IResourceManager iresourcemanager = (IResourceManager) this.domainResourceManagers
				.get(parResourceLocation.getResourceDomain());
		if (iresourcemanager != null) {
			return iresourcemanager.getResource(parResourceLocation);
		} else {
			throw new FileNotFoundException(parResourceLocation.toString());
		}
	}

	public List<IResource> getAllResources(ResourceLocation parResourceLocation) throws IOException {
		IResourceManager iresourcemanager = (IResourceManager) this.domainResourceManagers
				.get(parResourceLocation.getResourceDomain());
		if (iresourcemanager != null) {
			return iresourcemanager.getAllResources(parResourceLocation);
		} else {
			throw new FileNotFoundException(parResourceLocation.toString());
		}
	}

	private void clearResources() {
		this.domainResourceManagers.clear();
		this.setResourceDomains.clear();
	}

	public void reloadResources(List<IResourcePack> list) {
		this.clearResources();
		logger.info("Reloading ResourceManager: "
				+ joinerResourcePacks.join(Iterables.transform(list, new Function<IResourcePack, String>() {
					public String apply(IResourcePack iresourcepack1) {
						return iresourcepack1.getPackName();
					}
				})));

		for (IResourcePack iresourcepack : list) {
			this.reloadResourcePack(iresourcepack);
		}

		this.notifyReloadListeners();
	}

	public void registerReloadListener(IResourceManagerReloadListener iresourcemanagerreloadlistener) {
		this.reloadListeners.add(iresourcemanagerreloadlistener);
		iresourcemanagerreloadlistener.onResourceManagerReload(this);
	}

	private void notifyReloadListeners() {
		for (IResourceManagerReloadListener iresourcemanagerreloadlistener : this.reloadListeners) {
			iresourcemanagerreloadlistener.onResourceManagerReload(this);
		}

	}
}