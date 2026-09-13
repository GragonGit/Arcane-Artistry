package gragongit.arcaneartistry.common.staff;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import gragongit.arcaneartistry.common.registry.ModRegistries;

public record Staff(StaffType type) {
  public static final Codec<Staff> CODEC = RecordCodecBuilder
      .create(instance -> instance
          .group(ModRegistries.STAFF_TYPES.byNameCodec().fieldOf("type").forGetter(Staff::type))
          .apply(instance, Staff::new));
}
