package net.minecraft.network;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;

import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;

import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.client.C00PacketLoginStart;
import net.minecraft.network.login.client.C01PacketEncryptionResponse;
import net.minecraft.network.login.server.S00PacketDisconnect;
import net.minecraft.network.login.server.S01PacketEncryptionRequest;
import net.minecraft.network.login.server.S02PacketLoginSuccess;
import net.minecraft.network.login.server.S03PacketEnableCompression;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import net.minecraft.network.play.client.C11PacketEnchantItem;
import net.minecraft.network.play.client.C12PacketUpdateSign;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.network.play.client.C15PacketClientSettings;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.network.play.client.C18PacketSpectate;
import net.minecraft.network.play.client.C19PacketResourcePackStatus;
import net.minecraft.network.play.server.S00PacketKeepAlive;
import net.minecraft.network.play.server.S01PacketJoinGame;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import net.minecraft.network.play.server.S04PacketEntityEquipment;
import net.minecraft.network.play.server.S05PacketSpawnPosition;
import net.minecraft.network.play.server.S06PacketUpdateHealth;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S09PacketHeldItemChange;
import net.minecraft.network.play.server.S0APacketUseBed;
import net.minecraft.network.play.server.S0BPacketAnimation;
import net.minecraft.network.play.server.S0CPacketSpawnPlayer;
import net.minecraft.network.play.server.S0DPacketCollectItem;
import net.minecraft.network.play.server.S0EPacketSpawnObject;
import net.minecraft.network.play.server.S0FPacketSpawnMob;
import net.minecraft.network.play.server.S10PacketSpawnPainting;
import net.minecraft.network.play.server.S11PacketSpawnExperienceOrb;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S13PacketDestroyEntities;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import net.minecraft.network.play.server.S19PacketEntityHeadLook;
import net.minecraft.network.play.server.S19PacketEntityStatus;
import net.minecraft.network.play.server.S1BPacketEntityAttach;
import net.minecraft.network.play.server.S1CPacketEntityMetadata;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.network.play.server.S1EPacketRemoveEntityEffect;
import net.minecraft.network.play.server.S1FPacketSetExperience;
import net.minecraft.network.play.server.S20PacketEntityProperties;
import net.minecraft.network.play.server.S21PacketChunkData;
import net.minecraft.network.play.server.S22PacketMultiBlockChange;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.network.play.server.S24PacketBlockAction;
import net.minecraft.network.play.server.S25PacketBlockBreakAnim;
import net.minecraft.network.play.server.S26PacketMapChunkBulk;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.network.play.server.S28PacketEffect;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.network.play.server.S2CPacketSpawnGlobalEntity;
import net.minecraft.network.play.server.S2DPacketOpenWindow;
import net.minecraft.network.play.server.S2EPacketCloseWindow;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraft.network.play.server.S30PacketWindowItems;
import net.minecraft.network.play.server.S31PacketWindowProperty;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import net.minecraft.network.play.server.S33PacketUpdateSign;
import net.minecraft.network.play.server.S34PacketMaps;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.network.play.server.S36PacketSignEditorOpen;
import net.minecraft.network.play.server.S37PacketStatistics;
import net.minecraft.network.play.server.S38PacketPlayerListItem;
import net.minecraft.network.play.server.S39PacketPlayerAbilities;
import net.minecraft.network.play.server.S3APacketTabComplete;
import net.minecraft.network.play.server.S3BPacketScoreboardObjective;
import net.minecraft.network.play.server.S3CPacketUpdateScore;
import net.minecraft.network.play.server.S3DPacketDisplayScoreboard;
import net.minecraft.network.play.server.S3EPacketTeams;
import net.minecraft.network.play.server.S3FPacketCustomPayload;
import net.minecraft.network.play.server.S40PacketDisconnect;
import net.minecraft.network.play.server.S41PacketServerDifficulty;
import net.minecraft.network.play.server.S42PacketCombatEvent;
import net.minecraft.network.play.server.S43PacketCamera;
import net.minecraft.network.play.server.S44PacketWorldBorder;
import net.minecraft.network.play.server.S45PacketTitle;
import net.minecraft.network.play.server.S46PacketSetCompressionLevel;
import net.minecraft.network.play.server.S47PacketPlayerListHeaderFooter;
import net.minecraft.network.play.server.S48PacketResourcePackSend;
import net.minecraft.network.play.server.S49PacketUpdateEntityNBT;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;

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
public enum EnumConnectionState {
	HANDSHAKING(-1) {
		{
			this.registerPacket(EnumPacketDirection.SERVERBOUND, C00Handshake.class, C00Handshake::new);
		}
	},
	PLAY(0) {
		{
			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S00PacketKeepAlive.class, S00PacketKeepAlive::new);
			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S01PacketJoinGame.class, S01PacketJoinGame::new);
			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S02PacketChat.class, S02PacketChat::new);
			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S03PacketTimeUpdate.class, S03PacketTimeUpdate::new);
			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S04PacketEntityEquipment.class,
					S04PacketEntityEquipment::new);
			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S05PacketSpawnPosition.class,
					S05PacketSpawnPosition::new);
			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S06PacketUpdateHealth.class,
					S06PacketUpdateHealth::new);
			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S07PacketRespawn.class, S07PacketRespawn::new);
			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S08PacketPlayerPosLook.class,
					S08PacketPlayerPosLook::new);
			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S09PacketHeldItemChange.class,
					S09PacketHeldItemChange::new);
			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S0APacketUseBed.class, S0APacketUseBed::new);
			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S0BPacketAnimation.class, S0BPacketAnimation::new);
			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S0CPacketSpawnPlayer.class, S0CPacketSpawnPlayer::new);
			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S0DPacketCollectItem.class, S0DPacketCollectItem::new);
			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S0EPacketSpawnObject.class, S0EPacketSpawnObject::new);
			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S0FPacketSpawnMob.class, S0FPacketSpawnMob::new);
			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S10PacketSpawnPainting.class,
					S10PacketSpawnPainting::new);
			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S11PacketSpawnExperienceOrb.class,
					S11PacketSpawnExperienceOrb::new);
			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S12PacketEntityVelocity.class,
					S12PacketEntityVelocity::new);
			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S13PacketDestroyEntities.class,
					S13PacketDestroyEntities::new);
			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S14PacketEntity.class, S14PacketEntity::new);
			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S14PacketEntity.S15PacketEntityRelMove.class,
					S14PacketEntity.S15PacketEntityRelMove::new);
			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S14PacketEntity.S16PacketEntityLook.class,
					S14PacketEntity.S16PacketEntityLook::new);
			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S14PacketEntity.S17PacketEntityLookMove.class,
					S14PacketEntity.S17PacketEntityLookMove::new);
			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S18PacketEntityTeleport.class,
					S18PacketEntityTeleport::new);
			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S19PacketEntityHeadLook.class,
					S19PacketEntityHeadLook::new);
			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S19PacketEntityStatus.class,
					S19PacketEntityStatus::new);
			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S1BPacketEntityAttach.class,
					S1BPacketEntityAttach::new);
			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S1CPacketEntityMetadata.class,
					S1CPacketEntityMetadata::new);
			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S1DPacketEntityEffect.class,
					S1DPacketEntityEffect::new);
			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S1EPacketRemoveEntityEffect.class,
					S1EPacketRemoveEntityEffect::new);
			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S1FPacketSetExperience.class,
					S1FPacketSetExperience::new);
			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S20PacketEntityProperties.class,
					S20PacketEntityProperties::new);
			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S21PacketChunkData.class, S21PacketChunkData::new);
			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S22PacketMultiBlockChange.class,
					S22PacketMultiBlockChange::new);
			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S23PacketBlockChange.class, S23PacketBlockChange::new);
			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S24PacketBlockAction.class, S24PacketBlockAction::new);
			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S25PacketBlockBreakAnim.class,
					S25PacketBlockBreakAnim::new);
			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S26PacketMapChunkBulk.class,
					S26PacketMapChunkBulk::new);
			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S27PacketExplosion.class, S27PacketExplosion::new);
			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S28PacketEffect.class, S28PacketEffect::new);
			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S29PacketSoundEffect.class, S29PacketSoundEffect::new);
			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S2APacketParticles.class, S2APacketParticles::new);
			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S2BPacketChangeGameState.class,
					S2BPacketChangeGameState::new);
			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S2CPacketSpawnGlobalEntity.class,
					S2CPacketSpawnGlobalEntity::new);
			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S2DPacketOpenWindow.class, S2DPacketOpenWindow::new);
			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S2EPacketCloseWindow.class, S2EPacketCloseWindow::new);
			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S2FPacketSetSlot.class, S2FPacketSetSlot::new);
			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S30PacketWindowItems.class, S30PacketWindowItems::new);
			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S31PacketWindowProperty.class,
					S31PacketWindowProperty::new);
			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S32PacketConfirmTransaction.class,
					S32PacketConfirmTransaction::new);
			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S33PacketUpdateSign.class, S33PacketUpdateSign::new);
			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S34PacketMaps.class, S34PacketMaps::new);
			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S35PacketUpdateTileEntity.class,
					S35PacketUpdateTileEntity::new);
			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S36PacketSignEditorOpen.class,
					S36PacketSignEditorOpen::new);
			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S37PacketStatistics.class, S37PacketStatistics::new);
			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S38PacketPlayerListItem.class,
					S38PacketPlayerListItem::new);
			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S39PacketPlayerAbilities.class,
					S39PacketPlayerAbilities::new);
			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S3APacketTabComplete.class, S3APacketTabComplete::new);
			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S3BPacketScoreboardObjective.class,
					S3BPacketScoreboardObjective::new);
			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S3CPacketUpdateScore.class, S3CPacketUpdateScore::new);
			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S3DPacketDisplayScoreboard.class,
					S3DPacketDisplayScoreboard::new);
			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S3EPacketTeams.class, S3EPacketTeams::new);
			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S3FPacketCustomPayload.class,
					S3FPacketCustomPayload::new);
			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S40PacketDisconnect.class, S40PacketDisconnect::new);
			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S41PacketServerDifficulty.class,
					S41PacketServerDifficulty::new);
			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S42PacketCombatEvent.class, S42PacketCombatEvent::new);
			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S43PacketCamera.class, S43PacketCamera::new);
			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S44PacketWorldBorder.class, S44PacketWorldBorder::new);
			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S45PacketTitle.class, S45PacketTitle::new);
			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S46PacketSetCompressionLevel.class,
					S46PacketSetCompressionLevel::new);
			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S47PacketPlayerListHeaderFooter.class,
					S47PacketPlayerListHeaderFooter::new);
			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S48PacketResourcePackSend.class,
					S48PacketResourcePackSend::new);
			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S49PacketUpdateEntityNBT.class,
					S49PacketUpdateEntityNBT::new);
			this.registerPacket(EnumPacketDirection.SERVERBOUND, C00PacketKeepAlive.class, C00PacketKeepAlive::new);
			this.registerPacket(EnumPacketDirection.SERVERBOUND, C01PacketChatMessage.class, C01PacketChatMessage::new);
			this.registerPacket(EnumPacketDirection.SERVERBOUND, C02PacketUseEntity.class, C02PacketUseEntity::new);
			this.registerPacket(EnumPacketDirection.SERVERBOUND, C03PacketPlayer.class, C03PacketPlayer::new);
			this.registerPacket(EnumPacketDirection.SERVERBOUND, C03PacketPlayer.C04PacketPlayerPosition.class,
					C03PacketPlayer.C04PacketPlayerPosition::new);
			this.registerPacket(EnumPacketDirection.SERVERBOUND, C03PacketPlayer.C05PacketPlayerLook.class,
					C03PacketPlayer.C05PacketPlayerLook::new);
			this.registerPacket(EnumPacketDirection.SERVERBOUND, C03PacketPlayer.C06PacketPlayerPosLook.class,
					C03PacketPlayer.C06PacketPlayerPosLook::new);
			this.registerPacket(EnumPacketDirection.SERVERBOUND, C07PacketPlayerDigging.class,
					C07PacketPlayerDigging::new);
			this.registerPacket(EnumPacketDirection.SERVERBOUND, C08PacketPlayerBlockPlacement.class,
					C08PacketPlayerBlockPlacement::new);
			this.registerPacket(EnumPacketDirection.SERVERBOUND, C09PacketHeldItemChange.class,
					C09PacketHeldItemChange::new);
			this.registerPacket(EnumPacketDirection.SERVERBOUND, C0APacketAnimation.class, C0APacketAnimation::new);
			this.registerPacket(EnumPacketDirection.SERVERBOUND, C0BPacketEntityAction.class,
					C0BPacketEntityAction::new);
			this.registerPacket(EnumPacketDirection.SERVERBOUND, C0CPacketInput.class, C0CPacketInput::new);
			this.registerPacket(EnumPacketDirection.SERVERBOUND, C0DPacketCloseWindow.class, C0DPacketCloseWindow::new);
			this.registerPacket(EnumPacketDirection.SERVERBOUND, C0EPacketClickWindow.class, C0EPacketClickWindow::new);
			this.registerPacket(EnumPacketDirection.SERVERBOUND, C0FPacketConfirmTransaction.class,
					C0FPacketConfirmTransaction::new);
			this.registerPacket(EnumPacketDirection.SERVERBOUND, C10PacketCreativeInventoryAction.class,
					C10PacketCreativeInventoryAction::new);
			this.registerPacket(EnumPacketDirection.SERVERBOUND, C11PacketEnchantItem.class, C11PacketEnchantItem::new);
			this.registerPacket(EnumPacketDirection.SERVERBOUND, C12PacketUpdateSign.class, C12PacketUpdateSign::new);
			this.registerPacket(EnumPacketDirection.SERVERBOUND, C13PacketPlayerAbilities.class,
					C13PacketPlayerAbilities::new);
			this.registerPacket(EnumPacketDirection.SERVERBOUND, C14PacketTabComplete.class, C14PacketTabComplete::new);
			this.registerPacket(EnumPacketDirection.SERVERBOUND, C15PacketClientSettings.class,
					C15PacketClientSettings::new);
			this.registerPacket(EnumPacketDirection.SERVERBOUND, C16PacketClientStatus.class,
					C16PacketClientStatus::new);
			this.registerPacket(EnumPacketDirection.SERVERBOUND, C17PacketCustomPayload.class,
					C17PacketCustomPayload::new);
			this.registerPacket(EnumPacketDirection.SERVERBOUND, C18PacketSpectate.class, C18PacketSpectate::new);
			this.registerPacket(EnumPacketDirection.SERVERBOUND, C19PacketResourcePackStatus.class,
					C19PacketResourcePackStatus::new);
		}
	},
	LOGIN(2) {
		{
			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S00PacketDisconnect.class, S00PacketDisconnect::new);
			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S01PacketEncryptionRequest.class,
					S01PacketEncryptionRequest::new);
			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S02PacketLoginSuccess.class,
					S02PacketLoginSuccess::new);
			this.registerPacket(EnumPacketDirection.CLIENTBOUND, S03PacketEnableCompression.class,
					S03PacketEnableCompression::new);
			this.registerPacket(EnumPacketDirection.SERVERBOUND, C00PacketLoginStart.class, C00PacketLoginStart::new);
			this.registerPacket(EnumPacketDirection.SERVERBOUND, C01PacketEncryptionResponse.class,
					C01PacketEncryptionResponse::new);
		}
	};

	private static int field_181136_e = -1;
	private static int field_181137_f = 2;
	private static final EnumConnectionState[] STATES_BY_ID = new EnumConnectionState[field_181137_f - field_181136_e
			+ 1];
	private static final Map<Class<? extends Packet>, EnumConnectionState> STATES_BY_CLASS = Maps.newHashMap();
	private final int id;
	private final Map<EnumPacketDirection, BiMap<Integer, Class<? extends Packet>>> directionMaps;
	private final Map<EnumPacketDirection, Map<Integer, Supplier<Packet<?>>>> directionCtors;

	private EnumConnectionState(int protocolId) {
		this.directionMaps = Maps.newEnumMap(EnumPacketDirection.class);
		this.directionCtors = Maps.newEnumMap(EnumPacketDirection.class);
		this.id = protocolId;
	}

	protected EnumConnectionState registerPacket(EnumPacketDirection direction, Class<? extends Packet> packetClass,
			Supplier<Packet<?>> packetCtor) {
		BiMap<Integer, Class<? extends Packet>> object = this.directionMaps.get(direction);
		Map<Integer, Supplier<Packet<?>>> object2;
		if (object == null) {
			object = HashBiMap.create();
			object2 = Maps.newHashMap();
			this.directionMaps.put(direction, object);
			this.directionCtors.put(direction, object2);
		} else {
			object2 = this.directionCtors.get(direction);
		}

		if (object.containsValue(packetClass)) {
			String s = direction + " packet " + packetClass + " is already known to ID "
					+ object.inverse().get(packetClass);
			LogManager.getLogger().fatal(s);
			throw new IllegalArgumentException(s);
		} else {
			object.put(Integer.valueOf(object.size()), packetClass);
			object2.put(Integer.valueOf(object2.size()), packetCtor);
			return this;
		}
	}

	public Integer getPacketId(EnumPacketDirection direction, Packet packetIn) {
		return (Integer) ((BiMap) this.directionMaps.get(direction)).inverse().get(packetIn.getClass());
	}

	public Packet getPacket(EnumPacketDirection direction, int packetId)
			throws IllegalAccessException, InstantiationException {
		Supplier<Packet<?>> oclass = this.directionCtors.get(direction).get(Integer.valueOf(packetId));
		return oclass == null ? null : oclass.get();
	}

	public int getId() {
		return this.id;
	}

	public static EnumConnectionState getById(int stateId) {
		return stateId >= field_181136_e && stateId <= field_181137_f ? STATES_BY_ID[stateId - field_181136_e] : null;
	}

	public static EnumConnectionState getFromPacket(Packet packetIn) {
		return (EnumConnectionState) STATES_BY_CLASS.get(packetIn.getClass());
	}

	static {
		EnumConnectionState[] states = values();
		for (int j = 0; j < states.length; ++j) {
			EnumConnectionState enumconnectionstate = states[j];
			int i = enumconnectionstate.getId();
			if (i < field_181136_e || i > field_181137_f) {
				throw new Error("Invalid protocol ID " + Integer.toString(i));
			}

			STATES_BY_ID[i - field_181136_e] = enumconnectionstate;

			for (EnumPacketDirection enumpacketdirection : enumconnectionstate.directionMaps.keySet()) {
				for (Class oclass : (Collection<Class>) ((BiMap) enumconnectionstate.directionMaps
						.get(enumpacketdirection)).values()) {
					if (STATES_BY_CLASS.containsKey(oclass) && STATES_BY_CLASS.get(oclass) != enumconnectionstate) {
						throw new Error("Packet " + oclass + " is already assigned to protocol "
								+ STATES_BY_CLASS.get(oclass) + " - can\'t reassign to " + enumconnectionstate);
					}

					STATES_BY_CLASS.put(oclass, enumconnectionstate);
				}
			}
		}

	}
}