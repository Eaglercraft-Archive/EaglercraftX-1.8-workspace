package net.minecraft.client.resources.data;

import java.util.List;
import com.carrotsearch.hppc.IntHashSet;
import com.carrotsearch.hppc.IntSet;

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
public class AnimationMetadataSection implements IMetadataSection {
	private final List<AnimationFrame> animationFrames;
	private final int frameWidth;
	private final int frameHeight;
	private final int frameTime;
	private final boolean interpolate;

	public AnimationMetadataSection(List<AnimationFrame> parList, int parInt1, int parInt2, int parInt3,
			boolean parFlag) {
		this.animationFrames = parList;
		this.frameWidth = parInt1;
		this.frameHeight = parInt2;
		this.frameTime = parInt3;
		this.interpolate = parFlag;
	}

	public int getFrameHeight() {
		return this.frameHeight;
	}

	public int getFrameWidth() {
		return this.frameWidth;
	}

	public int getFrameCount() {
		return this.animationFrames.size();
	}

	public int getFrameTime() {
		return this.frameTime;
	}

	public boolean isInterpolate() {
		return this.interpolate;
	}

	private AnimationFrame getAnimationFrame(int parInt1) {
		return (AnimationFrame) this.animationFrames.get(parInt1);
	}

	public int getFrameTimeSingle(int parInt1) {
		AnimationFrame animationframe = this.getAnimationFrame(parInt1);
		return animationframe.hasNoTime() ? this.frameTime : animationframe.getFrameTime();
	}

	public boolean frameHasTime(int parInt1) {
		return !((AnimationFrame) this.animationFrames.get(parInt1)).hasNoTime();
	}

	public int getFrameIndex(int parInt1) {
		return ((AnimationFrame) this.animationFrames.get(parInt1)).getFrameIndex();
	}

	public IntSet getFrameIndexSet() {
		IntHashSet hashset = new IntHashSet();

		for (int i = 0, l = this.animationFrames.size(); i < l; ++i) {
			hashset.add(this.animationFrames.get(i).getFrameIndex());
		}

		return hashset;
	}
}