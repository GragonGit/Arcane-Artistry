package net.arcaneartistry.base.spells.effects;

import com.google.gson.JsonObject;
import net.arcaneartistry.base.spells.SpellEffect;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

/**
 * Params: {@code action} (only {@code "ignite"} is implemented for v1),
 * {@code range}. Design document section 6.1 lists "block interaction" as
 * one of the four built-in effect categories without an example; this is a
 * deliberately small, safe starting implementation (no fire spreads to
 * flammable neighbours, unlike flint and steel) -- extend {@code action}
 * with more cases as needed.
 */
public final class BlockInteractionSpellEffect implements SpellEffect {
  @Override
  public void execute(SpellEffectContext context, JsonObject params) {
    String action = params.has("action") ? params.get("action").getAsString() : "ignite";
    double range = params.has("range") ? params.get("range").getAsDouble() : 5.0;

    var caster = context.caster();
    Vec3 start = caster.getEyePosition();
    Vec3 end = start.add(caster.getViewVector(1.0f).scale(range));

    HitResult hit = context.world().clip(new ClipContext(
        start, end,
        ClipContext.Block.OUTLINE,
        ClipContext.Fluid.NONE,
        caster));

    if (hit.getType() != HitResult.Type.BLOCK || !(hit instanceof BlockHitResult blockHit)) {
      return;
    }

    if ("ignite".equals(action)) {
      var firePos = blockHit.getBlockPos().relative(blockHit.getDirection());
      if (context.world().isEmptyBlock(firePos)) {
        context.world().setBlockAndUpdate(firePos, Blocks.FIRE.defaultBlockState());
      }
    }
  }
}
