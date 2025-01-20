package net.minecraft.client.resources;

import java.io.Closeable;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import net.lax1dude.eaglercraft.v1_8.IOUtils;
import net.lax1dude.eaglercraft.v1_8.futures.ListenableFuture;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerFolderResourcePack;
import net.lax1dude.eaglercraft.v1_8.opengl.ImageData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.client.resources.data.PackMetadataSection;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.EnumChatFormatting;
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
public class ResourcePackRepository {
	private static final Logger logger = LogManager.getLogger();
	public final IResourcePack rprDefaultResourcePack;
	public final IMetadataSerializer rprMetadataSerializer;
	private IResourcePack resourcePackInstance;
	private ListenableFuture<Object> field_177322_i;
	private List<ResourcePackRepository.Entry> repositoryEntriesAll = Lists.newArrayList();
	private List<ResourcePackRepository.Entry> repositoryEntries = Lists.newArrayList();

	public ResourcePackRepository(IResourcePack rprDefaultResourcePackIn, IMetadataSerializer rprMetadataSerializerIn,
			GameSettings settings) {
		this.rprDefaultResourcePack = rprDefaultResourcePackIn;
		this.rprMetadataSerializer = rprMetadataSerializerIn;
		reconstruct(settings);
	}

	public void reconstruct(GameSettings settings) {
		this.repositoryEntriesAll.clear();
		this.repositoryEntries.clear();
		this.updateRepositoryEntriesAll();
		Iterator iterator = settings.resourcePacks.iterator();

		while (iterator.hasNext()) {
			String s = (String) iterator.next();

			for (int i = 0, l = this.repositoryEntriesAll.size(); i < l; ++i) {
				ResourcePackRepository.Entry resourcepackrepository$entry = this.repositoryEntriesAll.get(i);
				if (resourcepackrepository$entry.getResourcePackName().equals(s)) {
					if (resourcepackrepository$entry.func_183027_f() == 1
							|| settings.field_183018_l.contains(resourcepackrepository$entry.getResourcePackName())) {
						this.repositoryEntries.add(resourcepackrepository$entry);
						break;
					}

					iterator.remove();
					logger.warn("Removed selected resource pack {} because it\'s no longer compatible",
							new Object[] { resourcepackrepository$entry.getResourcePackName() });
				}
			}
		}

	}

	public void updateRepositoryEntriesAll() {
		List<ResourcePackRepository.Entry> list = Lists.<ResourcePackRepository.Entry>newArrayList();

		List<EaglerFolderResourcePack> list2 = EaglerFolderResourcePack
				.getFolderResourcePacks(EaglerFolderResourcePack.RESOURCE_PACKS);
		for (int j = 0, l = list2.size(); j < l; ++j) {
			ResourcePackRepository.Entry resourcepackrepository$entry = new ResourcePackRepository.Entry(list2.get(j));

			if (!this.repositoryEntriesAll.contains(resourcepackrepository$entry)) {
				try {
					resourcepackrepository$entry.updateResourcePack();
					list.add(resourcepackrepository$entry);
				} catch (Exception var6) {
					logger.error("Failed to call \"updateResourcePack\" for resource pack \"{}\"",
							resourcepackrepository$entry.reResourcePack.resourcePackFile);
					logger.error(var6);
					list.remove(resourcepackrepository$entry);
				}
			} else {
				int i = this.repositoryEntriesAll.indexOf(resourcepackrepository$entry);

				if (i > -1 && i < this.repositoryEntriesAll.size()) {
					list.add(this.repositoryEntriesAll.get(i));
				}
			}
		}

		this.repositoryEntriesAll.removeAll(list);

		for (int i = 0, l = this.repositoryEntriesAll.size(); i < l; ++i) {
			this.repositoryEntriesAll.get(i).closeResourcePack();
		}

		this.repositoryEntriesAll = list;
	}

	public List<ResourcePackRepository.Entry> getRepositoryEntriesAll() {
		return ImmutableList.copyOf(this.repositoryEntriesAll);
	}

	public List<ResourcePackRepository.Entry> getRepositoryEntries() {
		return ImmutableList.copyOf(this.repositoryEntries);
	}

	public void setRepositories(List<ResourcePackRepository.Entry> parList) {
		this.repositoryEntries.clear();
		this.repositoryEntries.addAll(parList);
	}

	public void downloadResourcePack(String s1, String s2, Consumer<Boolean> cb) {
		EaglerFolderResourcePack.loadRemoteResourcePack(s1, s2, res -> {
			if (res != null) {
				ResourcePackRepository.this.resourcePackInstance = res;
				Minecraft.getMinecraft().scheduleResourcesRefresh();
				cb.accept(true);
				return;
			}
			cb.accept(false);
		}, runnable -> {
			Minecraft.getMinecraft().addScheduledTask(runnable);
		}, () -> {
			Minecraft.getMinecraft().loadingScreen.eaglerShow(I18n.format("resourcePack.load.loading"),
					"Server resource pack");
		});
	}

	/**+
	 * Getter for the IResourcePack instance associated with this
	 * ResourcePackRepository
	 */
	public IResourcePack getResourcePackInstance() {
		return this.resourcePackInstance;
	}

	public void func_148529_f() {
		if (this.resourcePackInstance != null) {
			this.resourcePackInstance = null;
			Minecraft.getMinecraft().scheduleResourcesRefresh();
		}
	}

	public class Entry {
		private EaglerFolderResourcePack reResourcePack;
		private PackMetadataSection rePackMetadataSection;
		private ImageData texturePackIcon;
		private ResourceLocation locationTexturePackIcon;
		private TextureManager iconTextureManager;

		private Entry(EaglerFolderResourcePack resourcePackFileIn) {
			this.reResourcePack = resourcePackFileIn;
		}

		public void updateResourcePack() throws IOException {
			this.rePackMetadataSection = (PackMetadataSection) this.reResourcePack
					.getPackMetadata(ResourcePackRepository.this.rprMetadataSerializer, "pack");

			try {
				this.texturePackIcon = this.reResourcePack.getPackImage();
			} catch (Throwable var2) {
				logger.error("Failed to load resource pack icon for \"{}\"!", reResourcePack.resourcePackFile);
				logger.error(var2);
			}

			if (this.texturePackIcon == null) {
				this.texturePackIcon = ResourcePackRepository.this.rprDefaultResourcePack.getPackImage();
			}

			this.closeResourcePack();
		}

		public void bindTexturePackIcon(TextureManager textureManagerIn) {
			if (this.locationTexturePackIcon == null) {
				this.iconTextureManager = textureManagerIn;
				this.locationTexturePackIcon = textureManagerIn.getDynamicTextureLocation("texturepackicon",
						new DynamicTexture(this.texturePackIcon));
			}

			textureManagerIn.bindTexture(this.locationTexturePackIcon);
		}

		public void closeResourcePack() {
			if (this.locationTexturePackIcon != null) {
				this.iconTextureManager.deleteTexture(this.locationTexturePackIcon);
				this.locationTexturePackIcon = null;
			}
			if (this.reResourcePack instanceof Closeable) {
				IOUtils.closeQuietly((Closeable) this.reResourcePack);
			}

		}

		public IResourcePack getResourcePack() {
			return this.reResourcePack;
		}

		public String getResourcePackName() {
			return this.reResourcePack.getPackName();
		}

		public String getResourcePackEaglerDisplayName() {
			return this.reResourcePack.getDisplayName();
		}

		public String getTexturePackDescription() {
			return this.rePackMetadataSection == null
					? EnumChatFormatting.RED + "Invalid pack.mcmeta (or missing \'pack\' section)"
					: this.rePackMetadataSection.getPackDescription().getFormattedText();
		}

		public int func_183027_f() {
			return this.rePackMetadataSection.getPackFormat();
		}

		public boolean equals(Object object) {
			return this == object ? true
					: (object instanceof ResourcePackRepository.Entry ? this.toString().equals(object.toString())
							: false);
		}

		public int hashCode() {
			return this.toString().hashCode();
		}

		public String toString() {
			return this.reResourcePack.resourcePackFile;
		}
	}
}