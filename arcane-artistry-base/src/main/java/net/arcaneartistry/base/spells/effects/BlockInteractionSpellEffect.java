package net.arcaneartistry.base.spells.effects;

import com.google.gson.JsonObject;
import net.arcaneartistry.base.spells.SpellEffect;
import net.minecraft.block.Blocks;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

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
        Vec3d start = caster.getEyePos();
        Vec3d end = start.add(caster.getRotationVec(1.0f).multiply(range));

        HitResult hit = context.world().raycast(new RaycastContext(
                start, end,
                RaycastContext.ShapeType.OUTLINE,
                RaycastContext.FluidHandling.NONE,
                caster));

        if (hit.getType() != HitResult.Type.BLOCK || !(hit instanceof BlockHitResult blockHit)) {
            return;
        }

        if ("ignite".equals(action)) {
            var firePos = blockHit.getBlockPos().offset(blockHit.getSide());
            if (context.world().isAir(firePos)) {
                context.world().setBlockState(firePos, Blocks.FIRE.getDefaultState());
            }
        }
    }
}
