package net.minecraft.entity.ai;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

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
public class EntityAITasks {
	private static final Logger logger = LogManager.getLogger();
	/**+
	 * A list of EntityAITaskEntrys in EntityAITasks.
	 */
	private List<EntityAITasks.EntityAITaskEntry> taskEntries = Lists.newArrayList();
	/**+
	 * A list of EntityAITaskEntrys that are currently being
	 * executed.
	 */
	private List<EntityAITasks.EntityAITaskEntry> executingTaskEntries = Lists.newArrayList();
	private int tickCount;
	private int tickRate = 3;

	/**+
	 * Add a now AITask. Args : priority, task
	 */
	public void addTask(int priority, EntityAIBase task) {
		this.taskEntries.add(new EntityAITasks.EntityAITaskEntry(priority, task));
	}

	/**+
	 * removes the indicated task from the entity's AI tasks.
	 */
	public void removeTask(EntityAIBase task) {
		Iterator iterator = this.taskEntries.iterator();

		while (iterator.hasNext()) {
			EntityAITasks.EntityAITaskEntry entityaitasks$entityaitaskentry = (EntityAITasks.EntityAITaskEntry) iterator
					.next();
			EntityAIBase entityaibase = entityaitasks$entityaitaskentry.action;
			if (entityaibase == task) {
				if (this.executingTaskEntries.contains(entityaitasks$entityaitaskentry)) {
					entityaibase.resetTask();
					this.executingTaskEntries.remove(entityaitasks$entityaitaskentry);
				}

				iterator.remove();
			}
		}

	}

	public void onUpdateTasks() {
		if (this.tickCount++ % this.tickRate == 0) {
			Iterator iterator = this.taskEntries.iterator();

			label38: while (true) {
				EntityAITasks.EntityAITaskEntry entityaitasks$entityaitaskentry;
				while (true) {
					if (!iterator.hasNext()) {
						break label38;
					}

					entityaitasks$entityaitaskentry = (EntityAITasks.EntityAITaskEntry) iterator.next();
					boolean flag = this.executingTaskEntries.contains(entityaitasks$entityaitaskentry);
					if (!flag) {
						break;
					}

					if (!this.canUse(entityaitasks$entityaitaskentry)
							|| !this.canContinue(entityaitasks$entityaitaskentry)) {
						entityaitasks$entityaitaskentry.action.resetTask();
						this.executingTaskEntries.remove(entityaitasks$entityaitaskentry);
						break;
					}
				}

				if (this.canUse(entityaitasks$entityaitaskentry)
						&& entityaitasks$entityaitaskentry.action.shouldExecute()) {
					entityaitasks$entityaitaskentry.action.startExecuting();
					this.executingTaskEntries.add(entityaitasks$entityaitaskentry);
				}
			}
		} else {
			Iterator iterator1 = this.executingTaskEntries.iterator();

			while (iterator1.hasNext()) {
				EntityAITasks.EntityAITaskEntry entityaitasks$entityaitaskentry1 = (EntityAITasks.EntityAITaskEntry) iterator1
						.next();
				if (!this.canContinue(entityaitasks$entityaitaskentry1)) {
					entityaitasks$entityaitaskentry1.action.resetTask();
					iterator1.remove();
				}
			}
		}

		for (int i = 0, l = this.executingTaskEntries.size(); i < l; ++i) {
			this.executingTaskEntries.get(i).action.updateTask();
		}
	}

	/**+
	 * Determine if a specific AI Task should continue being
	 * executed.
	 */
	private boolean canContinue(EntityAITasks.EntityAITaskEntry taskEntry) {
		boolean flag = taskEntry.action.continueExecuting();
		return flag;
	}

	/**+
	 * Determine if a specific AI Task can be executed, which means
	 * that all running higher (= lower int value) priority tasks
	 * are compatible with it or all lower priority tasks can be
	 * interrupted.
	 */
	private boolean canUse(EntityAITasks.EntityAITaskEntry taskEntry) {
		for (int i = 0, l = this.taskEntries.size(); i < l; ++i) {
			EntityAITasks.EntityAITaskEntry entityaitasks$entityaitaskentry = this.taskEntries.get(i);
			if (entityaitasks$entityaitaskentry != taskEntry) {
				if (taskEntry.priority >= entityaitasks$entityaitaskentry.priority) {
					if (!this.areTasksCompatible(taskEntry, entityaitasks$entityaitaskentry)
							&& this.executingTaskEntries.contains(entityaitasks$entityaitaskentry)) {
						return false;
					}
				} else if (!entityaitasks$entityaitaskentry.action.isInterruptible()
						&& this.executingTaskEntries.contains(entityaitasks$entityaitaskentry)) {
					return false;
				}
			}
		}

		return true;
	}

	/**+
	 * Returns whether two EntityAITaskEntries can be executed
	 * concurrently
	 */
	private boolean areTasksCompatible(EntityAITasks.EntityAITaskEntry taskEntry1,
			EntityAITasks.EntityAITaskEntry taskEntry2) {
		return (taskEntry1.action.getMutexBits() & taskEntry2.action.getMutexBits()) == 0;
	}

	class EntityAITaskEntry {
		public EntityAIBase action;
		public int priority;

		public EntityAITaskEntry(int priorityIn, EntityAIBase task) {
			this.priority = priorityIn;
			this.action = task;
		}
	}
}