package net.arcaneartistry.base.staffs;

import net.arcaneartistry.core.api.ClientGestureBridgeHolder;
import net.arcaneartistry.core.api.GestureCastable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

/**
 * A gesture-castable staff (design document section 5). Its behaviour is
 * almost entirely data-driven via the {@link StaffDefinition} this item
 * points to by id -- the Java class here only wires the vanilla item-use
 * lifecycle up to core's gesture capture, the same way a bow wires charging
 * up to {@code onStoppedUsing} to fire an arrow.
 *
 * <p><b>IMPORTANT:</b> this class loads on BOTH the physical client and a
 * dedicated server. It must never reference a client-only type
 * ({@code ClientPlayerEntity}, {@code MinecraftClient}, any renderer, ...)
 * directly -- not even inside an {@code if (world.isClient())} guard --
 * since dedicated servers don't have those classes on the classpath at all,
 * and referencing them from shared code risks a {@code NoClassDefFoundError}
 * there. All client-only work is routed through
 * {@link ClientGestureBridgeHolder}, a common-code-safe indirection whose
 * real implementation is only ever installed from core's client entrypoint.
 */
public final class StaffItem extends Item implements GestureCastable {
    private final Identifier definitionId;

    public StaffItem(Settings settings, Identifier definitionId) {
        super(settings);
        this.definitionId = definitionId;
    }

    public Identifier getDefinitionId() {
        return definitionId;
    }

    @Override
    public boolean canAttemptCast(ItemStack stack, PlayerEntity player) {
        return StaffDefinitionLoader.get(definitionId).isPresent()
                && !player.getItemCooldownManager().isCoolingDown(this);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (!canAttemptCast(stack, user)) {
            return TypedActionResult.pass(stack);
        }

        if (world.isClient()) {
            ClientGestureBridgeHolder.get().startCast(stack);
        }

        // Keeps the item "in use" (arm raised, etc.) until the player
        // releases right-click or something else interrupts it -- the same
        // mechanism a bow uses to stay drawn.
        user.setCurrentHand(hand);
        return TypedActionResult.consume(stack);
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 72000;
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (world.isClient()) {
            Hand hand = user.getOffHandStack() == stack ? Hand.OFF_HAND : Hand.MAIN_HAND;
            ClientGestureBridgeHolder.get().endCast(stack, hand);
        }
    }
}
