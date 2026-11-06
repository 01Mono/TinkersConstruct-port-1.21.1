package modernmods.modernfoundry.library.recipe;

import modernmods.hilt.recipe.helper.ItemOutput;
import net.minecraft.world.item.ItemStack;

public final class SafeItemOutput {
  private SafeItemOutput() {}

  public static ItemStack get(ItemOutput output) {
    try {
      return output.get();
    } catch (IllegalStateException e) {
      return ItemStack.EMPTY;
    }
  }
}
