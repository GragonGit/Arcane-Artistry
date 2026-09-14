package gragongit.arcaneartistry.common.staff;

import java.util.List;
import gragongit.arcaneartistry.common.ArcaneArtistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.network.codec.ByteBufCodecs;

public final class StaffCastAttachments {

  public static final AttachmentType<Boolean> IS_CASTING = AttachmentRegistry
      .create(ArcaneArtistry.id("is_casting"),
          builder -> builder.initializer(() -> false).syncWith(ByteBufCodecs.BOOL, AttachmentSyncPredicate.all()));

  public static final AttachmentType<Double> ACCUMULATED_YAW = AttachmentRegistry
      .create(ArcaneArtistry.id("accumulated_yaw"),
          builder -> builder.initializer(() -> 0.0).syncWith(ByteBufCodecs.DOUBLE, AttachmentSyncPredicate.all()));

  public static final AttachmentType<Double> ACCUMULATED_PITCH = AttachmentRegistry
      .create(ArcaneArtistry.id("accumulated_pitch"),
          builder -> builder.initializer(() -> 0.0).syncWith(ByteBufCodecs.DOUBLE, AttachmentSyncPredicate.all()));

  public static final AttachmentType<List<StaffDirection>> STROKES = AttachmentRegistry.create(ArcaneArtistry.id("strokes"));

  public static void register() {}
}
