package net.minecraft.entity.ai;

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
public abstract class EntityAIBase {
	private int mutexBits;

	public abstract boolean shouldExecute();

	/**+
	 * Returns whether an in-progress EntityAIBase should continue
	 * executing
	 */
	public boolean continueExecuting() {
		return this.shouldExecute();
	}

	/**+
	 * Determine if this AI Task is interruptible by a higher (=
	 * lower value) priority task. All vanilla AITask have this
	 * value set to true.
	 */
	public boolean isInterruptible() {
		return true;
	}

	/**+
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting() {
	}

	/**+
	 * Resets the task
	 */
	public void resetTask() {
	}

	/**+
	 * Updates the task
	 */
	public void updateTask() {
	}

	/**+
	 * Sets a bitmask telling which other tasks may not run
	 * concurrently. The test is a simple bitwise AND - if it yields
	 * zero, the two tasks may run concurrently, if not - they must
	 * run exclusively from each other.
	 */
	public void setMutexBits(int mutexBitsIn) {
		this.mutexBits = mutexBitsIn;
	}

	/**+
	 * Get a bitmask telling which other tasks may not run
	 * concurrently. The test is a simple bitwise AND - if it yields
	 * zero, the two tasks may run concurrently, if not - they must
	 * run exclusively from each other.
	 */
	public int getMutexBits() {
		return this.mutexBits;
	}
}