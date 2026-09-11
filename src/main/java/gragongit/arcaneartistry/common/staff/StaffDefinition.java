package gragongit.arcaneartistry.common.staff;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import gragongit.arcaneartistry.common.registry.ModRegistries;

public record StaffDefinition(StaffType type) {
  public static final Codec<StaffDefinition> CODEC = RecordCodecBuilder
      .create(instance -> instance
          .group(ModRegistries.STAFF_TYPES.byNameCodec().fieldOf("type").forGetter(StaffDefinition::type))
          .apply(instance, StaffDefinition::new));
}
