package net.arcaneartistry.base.staffs;

import net.arcaneartistry.core.api.ClientGestureBridgeHolder;
import net.arcaneartistry.core.api.GestureCastable;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
// import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * A gesture-castable staff (design document section 5). Its behaviour is
 * almost entirely data-driven via the {@link StaffDefinition} this item
 * points to by id -- the Java class here only wires the vanilla item-use
 * lifecycle up to core's gesture capture, the same way a bow wires charging
 * up to {@code releaseUsing} to fire an arrow.
 *
 * <p>
 * <b>IMPORTANT:</b> this class loads on BOTH the physical client and a
 * dedicated server. It must never reference a client-only type
 * ({@code LocalPlayer}, {@code Minecraft}, any renderer, ...) directly --
 * not even inside an {@code if (level.isClientSide())} guard -- since
 * dedicated servers don't have those classes on the classpath at all, and
 * referencing them from shared code risks a {@code NoClassDefFoundError}
 * there. All client-only work is routed through
 * {@link ClientGestureBridgeHolder}, a common-code-safe indirection whose
 * real implementation is only ever installed from core's client entrypoint.
 */
public final class StaffItem extends Item implements GestureCastable {
  private final Identifier definitionId;

  public StaffItem(Properties settings, Identifier definitionId) {
    super(settings);
    this.definitionId = definitionId;
  }

  public Identifier getDefinitionId() {
    return definitionId;
  }

  @Override
  public boolean canAttemptCast(ItemStack stack, Player player) {
    return StaffDefinitionLoader.get(definitionId).isPresent()
        && !player.getCooldowns().isOnCooldown(stack);
  }

  @Override
  public InteractionResult use(Level level, Player user, InteractionHand hand) {
    ItemStack stack = user.getItemInHand(hand);
    if (!canAttemptCast(stack, user)) {
      return InteractionResult.PASS;
    }

    if (level.isClientSide()) {
      ClientGestureBridgeHolder.get().startCast(stack);
    }

    // Keeps the item "in use" (arm raised, etc.) until the player
    // releases right-click or something else interrupts it -- the same
    // mechanism a bow uses to stay drawn.
    user.startUsingItem(hand);
    return InteractionResult.CONSUME;
  }

  @Override
  public int getUseDuration(ItemStack stack, LivingEntity user) {
    return 72000;
  }

  @Override
  public boolean releaseUsing(ItemStack stack, Level level, LivingEntity user, int remainingUseTicks) {
    if (level.isClientSide()) {
      InteractionHand hand = user.getOffhandItem() == stack ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
      ClientGestureBridgeHolder.get().endCast(stack, hand);
    }
    return false;
  }
}
