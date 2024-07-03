package de.arcane_artistry.model;

import java.util.Optional;

import de.arcane_artistry.ArcaneArtistry;
import net.minecraft.data.client.Model;
import net.minecraft.data.client.TextureKey;

public class ModModels {
  public static final Model LAPIS_CRYSTAL = block("lapis_crystal", TextureKey.ALL);

  private static Model block(String parent, TextureKey... requiredTextureKeys) {
    return new Model(Optional.of(ArcaneArtistry.newIdentifier("block/" + parent)),
        Optional.empty(),
        requiredTextureKeys);
  }
}
