package net.arcaneartistry.base.util;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;

/**
 * A {@link Codec} that does no real (de)serialization of its own -- it just
 * hands back the raw {@link JsonElement} for a file, the same shape
 * {@code SimpleJsonResourceReloadListener} used to expose directly before
 * that class became generic/codec-driven.
 *
 * <p>
 * Used by {@code SpellDefinitionLoader} and {@code StaffDefinitionLoader} so
 * their existing hand-rolled {@code fromJson(JsonObject)} parsing keeps
 * working unchanged, without committing either definition type to a full
 * vanilla {@code Codec} right now. Moving to real
 * {@code Codec<SpellDefinition>} / {@code Codec<StaffDefinition>} later is a
 * reasonable follow-up, not something the build needs today.
 */
public final class PassthroughJsonCodec {
  public static final Codec<JsonElement> INSTANCE = Codec.PASSTHROUGH.xmap(
      dynamic -> dynamic.convert(JsonOps.INSTANCE).getValue(),
      json -> new Dynamic<>(JsonOps.INSTANCE, json));

  private PassthroughJsonCodec() {
  }
}
