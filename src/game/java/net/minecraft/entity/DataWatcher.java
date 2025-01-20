package net.minecraft.entity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;

import com.carrotsearch.hppc.IntObjectHashMap;
import com.carrotsearch.hppc.IntObjectMap;
import com.carrotsearch.hppc.ObjectIntHashMap;
import com.carrotsearch.hppc.ObjectIntMap;
import com.carrotsearch.hppc.cursors.ObjectCursor;
import com.google.common.collect.Lists;

import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ReportedException;
import net.minecraft.util.Rotations;

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
public class DataWatcher {
	private final Entity owner;
	/**+
	 * When isBlank is true the DataWatcher is not watching any
	 * objects
	 */
	private boolean isBlank = true;
	private static final ObjectIntMap<Class<?>> dataTypes = new ObjectIntHashMap<>();
	private final IntObjectMap<DataWatcher.WatchableObject> watchedObjects = new IntObjectHashMap<>();
	private boolean objectChanged;

	public DataWatcher(Entity owner) {
		this.owner = owner;
	}

	public <T> void addObject(int id, T object) {
		int integer = dataTypes.getOrDefault(object.getClass(), -1);
		if (integer == -1) {
			throw new IllegalArgumentException("Unknown data type: " + object.getClass());
		} else if (id > 31) {
			throw new IllegalArgumentException("Data value id is too big with " + id + "! (Max is " + 31 + ")");
		} else if (this.watchedObjects.containsKey(id)) {
			throw new IllegalArgumentException("Duplicate id value for " + id + "!");
		} else {
			DataWatcher.WatchableObject datawatcher$watchableobject = new DataWatcher.WatchableObject(integer, id,
					object);
			this.watchedObjects.put(id, datawatcher$watchableobject);
			this.isBlank = false;
		}
	}

	/**+
	 * Add a new object for the DataWatcher to watch, using the
	 * specified data type.
	 */
	public void addObjectByDataType(int id, int type) {
		DataWatcher.WatchableObject datawatcher$watchableobject = new DataWatcher.WatchableObject(type, id,
				(Object) null);
		this.watchedObjects.put(id, datawatcher$watchableobject);
		this.isBlank = false;
	}

	/**+
	 * gets the bytevalue of a watchable object
	 */
	public byte getWatchableObjectByte(int id) {
		return ((Byte) this.getWatchedObject(id).getObject()).byteValue();
	}

	public short getWatchableObjectShort(int id) {
		return ((Short) this.getWatchedObject(id).getObject()).shortValue();
	}

	/**+
	 * gets a watchable object and returns it as a Integer
	 */
	public int getWatchableObjectInt(int id) {
		return ((Integer) this.getWatchedObject(id).getObject()).intValue();
	}

	public float getWatchableObjectFloat(int id) {
		return ((Float) this.getWatchedObject(id).getObject()).floatValue();
	}

	/**+
	 * gets a watchable object and returns it as a String
	 */
	public String getWatchableObjectString(int id) {
		return (String) this.getWatchedObject(id).getObject();
	}

	/**+
	 * Get a watchable object as an ItemStack.
	 */
	public ItemStack getWatchableObjectItemStack(int id) {
		return (ItemStack) this.getWatchedObject(id).getObject();
	}

	/**+
	 * is threadsafe, unless it throws an exception, then
	 */
	private DataWatcher.WatchableObject getWatchedObject(int id) {
		DataWatcher.WatchableObject datawatcher$watchableobject;
		try {
			datawatcher$watchableobject = (DataWatcher.WatchableObject) this.watchedObjects.get(id);
		} catch (Throwable throwable) {
			CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Getting synched entity data");
			CrashReportCategory crashreportcategory = crashreport.makeCategory("Synched entity data");
			crashreportcategory.addCrashSection("Data ID", Integer.valueOf(id));
			throw new ReportedException(crashreport);
		}
		return datawatcher$watchableobject;
	}

	public Rotations getWatchableObjectRotations(int id) {
		return (Rotations) this.getWatchedObject(id).getObject();
	}

	public <T> void updateObject(int id, T newData) {
		DataWatcher.WatchableObject datawatcher$watchableobject = this.getWatchedObject(id);
		if (ObjectUtils.notEqual(newData, datawatcher$watchableobject.getObject())) {
			datawatcher$watchableobject.setObject(newData);
			this.owner.onDataWatcherUpdate(id);
			datawatcher$watchableobject.setWatched(true);
			this.objectChanged = true;
		}

	}

	public void setObjectWatched(int id) {
		this.getWatchedObject(id).watched = true;
		this.objectChanged = true;
	}

	/**+
	 * true if one or more object was changed
	 */
	public boolean hasObjectChanged() {
		return this.objectChanged;
	}

	/**+
	 * Writes the list of watched objects (entity attribute of type
	 * {byte, short, int, float, string, ItemStack,
	 * ChunkCoordinates}) to the specified PacketBuffer
	 */
	public static void writeWatchedListToPacketBuffer(List<DataWatcher.WatchableObject> objectsList,
			PacketBuffer buffer) throws IOException {
		if (objectsList != null) {
			for (int i = 0, l = objectsList.size(); i < l; ++i) {
				writeWatchableObjectToPacketBuffer(buffer, objectsList.get(i));
			}
		}

		buffer.writeByte(127);
	}

	public List<DataWatcher.WatchableObject> getChanged() {
		ArrayList arraylist = null;
		if (this.objectChanged) {
			for (ObjectCursor<DataWatcher.WatchableObject> datawatcher$watchableobject_ : this.watchedObjects
					.values()) {
				DataWatcher.WatchableObject datawatcher$watchableobject = datawatcher$watchableobject_.value;
				if (datawatcher$watchableobject.isWatched()) {
					datawatcher$watchableobject.setWatched(false);
					if (arraylist == null) {
						arraylist = Lists.newArrayList();
					}

					arraylist.add(datawatcher$watchableobject);
				}
			}
		}

		this.objectChanged = false;
		return arraylist;
	}

	public void writeTo(PacketBuffer buffer) throws IOException {
		for (ObjectCursor<DataWatcher.WatchableObject> datawatcher$watchableobject : this.watchedObjects.values()) {
			writeWatchableObjectToPacketBuffer(buffer, datawatcher$watchableobject.value);
		}
		buffer.writeByte(127);
	}

	public List<DataWatcher.WatchableObject> getAllWatched() {
		ArrayList arraylist = null;

		for (ObjectCursor<DataWatcher.WatchableObject> datawatcher$watchableobject : this.watchedObjects.values()) {
			if (arraylist == null) {
				arraylist = Lists.newArrayList();
			}

			arraylist.add(datawatcher$watchableobject.value);
		}

		return arraylist;
	}

	/**+
	 * Writes a watchable object (entity attribute of type {byte,
	 * short, int, float, string, ItemStack, ChunkCoordinates}) to
	 * the specified PacketBuffer
	 */
	private static void writeWatchableObjectToPacketBuffer(PacketBuffer buffer, DataWatcher.WatchableObject object)
			throws IOException {
		int i = (object.getObjectType() << 5 | object.getDataValueId() & 31) & 255;
		buffer.writeByte(i);
		switch (object.getObjectType()) {
		case 0:
			buffer.writeByte(((Byte) object.getObject()).byteValue());
			break;
		case 1:
			buffer.writeShort(((Short) object.getObject()).shortValue());
			break;
		case 2:
			buffer.writeInt(((Integer) object.getObject()).intValue());
			break;
		case 3:
			buffer.writeFloat(((Float) object.getObject()).floatValue());
			break;
		case 4:
			buffer.writeString((String) object.getObject());
			break;
		case 5:
			ItemStack itemstack = (ItemStack) object.getObject();
			buffer.writeItemStackToBuffer(itemstack);
			break;
		case 6:
			BlockPos blockpos = (BlockPos) object.getObject();
			buffer.writeInt(blockpos.getX());
			buffer.writeInt(blockpos.getY());
			buffer.writeInt(blockpos.getZ());
			break;
		case 7:
			Rotations rotations = (Rotations) object.getObject();
			buffer.writeFloat(rotations.getX());
			buffer.writeFloat(rotations.getY());
			buffer.writeFloat(rotations.getZ());
		}

	}

	/**+
	 * Reads a list of watched objects (entity attribute of type
	 * {byte, short, int, float, string, ItemStack,
	 * ChunkCoordinates}) from the supplied PacketBuffer
	 */
	public static List<DataWatcher.WatchableObject> readWatchedListFromPacketBuffer(PacketBuffer buffer)
			throws IOException {
		ArrayList arraylist = null;

		for (byte b0 = buffer.readByte(); b0 != 127; b0 = buffer.readByte()) {
			if (arraylist == null) {
				arraylist = Lists.newArrayList();
			}

			int i = (b0 & 224) >> 5;
			int j = b0 & 31;
			DataWatcher.WatchableObject datawatcher$watchableobject = null;
			switch (i) {
			case 0:
				datawatcher$watchableobject = new DataWatcher.WatchableObject(i, j, Byte.valueOf(buffer.readByte()));
				break;
			case 1:
				datawatcher$watchableobject = new DataWatcher.WatchableObject(i, j, Short.valueOf(buffer.readShort()));
				break;
			case 2:
				datawatcher$watchableobject = new DataWatcher.WatchableObject(i, j, Integer.valueOf(buffer.readInt()));
				break;
			case 3:
				datawatcher$watchableobject = new DataWatcher.WatchableObject(i, j, Float.valueOf(buffer.readFloat()));
				break;
			case 4:
				datawatcher$watchableobject = new DataWatcher.WatchableObject(i, j, buffer.readStringFromBuffer(32767));
				break;
			case 5:
				datawatcher$watchableobject = new DataWatcher.WatchableObject(i, j, buffer.readItemStackFromBuffer());
				break;
			case 6:
				int k = buffer.readInt();
				int l = buffer.readInt();
				int i1 = buffer.readInt();
				datawatcher$watchableobject = new DataWatcher.WatchableObject(i, j, new BlockPos(k, l, i1));
				break;
			case 7:
				float f = buffer.readFloat();
				float f1 = buffer.readFloat();
				float f2 = buffer.readFloat();
				datawatcher$watchableobject = new DataWatcher.WatchableObject(i, j, new Rotations(f, f1, f2));
			}

			arraylist.add(datawatcher$watchableobject);
		}

		return arraylist;
	}

	public void updateWatchedObjectsFromList(List<DataWatcher.WatchableObject> parList) {

		for (int i = 0, l = parList.size(); i < l; ++i) {
			DataWatcher.WatchableObject datawatcher$watchableobject = parList.get(i);
			DataWatcher.WatchableObject datawatcher$watchableobject1 = (DataWatcher.WatchableObject) this.watchedObjects
					.get(datawatcher$watchableobject.getDataValueId());
			if (datawatcher$watchableobject1 != null) {
				datawatcher$watchableobject1.setObject(datawatcher$watchableobject.getObject());
				this.owner.onDataWatcherUpdate(datawatcher$watchableobject.getDataValueId());
			}
		}

		this.objectChanged = true;
	}

	public boolean getIsBlank() {
		return this.isBlank;
	}

	public void func_111144_e() {
		this.objectChanged = false;
	}

	static {
		dataTypes.put(Byte.class, 0);
		dataTypes.put(Short.class, 1);
		dataTypes.put(Integer.class, 2);
		dataTypes.put(Float.class, 3);
		dataTypes.put(String.class, 4);
		dataTypes.put(ItemStack.class, 5);
		dataTypes.put(BlockPos.class, 6);
		dataTypes.put(Rotations.class, 7);
	}

	public static class WatchableObject {
		private final int objectType;
		private final int dataValueId;
		private Object watchedObject;
		private boolean watched;

		public WatchableObject(int type, int id, Object object) {
			this.dataValueId = id;
			this.watchedObject = object;
			this.objectType = type;
			this.watched = true;
		}

		public int getDataValueId() {
			return this.dataValueId;
		}

		public void setObject(Object object) {
			this.watchedObject = object;
		}

		public Object getObject() {
			return this.watchedObject;
		}

		public int getObjectType() {
			return this.objectType;
		}

		public boolean isWatched() {
			return this.watched;
		}

		public void setWatched(boolean watched) {
			this.watched = watched;
		}
	}
}