package com.example.addon.utils;

import meteordevelopment.meteorclient.utils.player.InvUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;

import java.util.function.Predicate;
import java.util.function.ToIntFunction;

/**
 * Handles common hotbar searches and slot changes.
 */
public final class Hotbar {
    private static final MinecraftClient mc = MinecraftClient.getInstance();

    private Hotbar() {}

    /**
     * Finds an item in the hotbar.
     *
     * @param item item to find
     * @return matching zero-based hotbar slot, or -1 when unavailable
     */
    public static int find(Item item) {
        return find(stack -> stack.isOf(item));
    }

    /**
     * Finds the first matching stack in the hotbar.
     *
     * @param predicate stack condition
     * @return matching zero-based hotbar slot, or -1 when unavailable
     */
    public static int find(Predicate<ItemStack> predicate) {
        return find(0, 8, predicate);
    }

    /**
     * Finds the first matching stack inside a hotbar range.
     *
     * @param first first zero-based hotbar slot
     * @param last last zero-based hotbar slot
     * @param predicate stack condition
     * @return matching zero-based hotbar slot, or -1 when unavailable
     */
    public static int find(int first, int last, Predicate<ItemStack> predicate) {
        if (mc.player == null) return -1;

        int start = Math.min(first, last);
        int end = Math.max(first, last);

        for (int slot = start; slot <= end; slot++) {
            ItemStack itemStack = stack(slot);
            if (!itemStack.isEmpty() && predicate.test(itemStack)) return slot;
        }

        return -1;
    }

    /**
     * Finds a matching stack while checking the selected slot first.
     *
     * @param predicate stack condition
     * @return matching zero-based hotbar slot, or -1 when unavailable
     */
    public static int first(Predicate<ItemStack> predicate) {
        if (mc.player == null) return -1;

        int selected = selected();
        ItemStack currentStack = stack(selected);
        if (!currentStack.isEmpty() && predicate.test(currentStack)) {
            return selected;
        }

        for (int slot = 0; slot < 9; slot++) {
            if (slot != selected) {
                ItemStack itemStack = stack(slot);
                if (!itemStack.isEmpty() && predicate.test(itemStack)) {
                    return slot;
                }
            }
        }

        return -1;
    }

    /**
     * Finds the highest scoring stack in the hotbar.
     *
     * @param predicate stack condition
     * @param score stack score
     * @return matching zero-based hotbar slot, or -1 when unavailable
     */
    public static int best(Predicate<ItemStack> predicate, ToIntFunction<ItemStack> score) {
        if (mc.player == null) return -1;

        int slot = -1;
        int best = Integer.MIN_VALUE;

        for (int idx = 0; idx < 9; idx++) {
            ItemStack itemStack = stack(idx);
            if (itemStack.isEmpty() || !predicate.test(itemStack)) continue;

            int current = score.applyAsInt(itemStack);
            if (current > best) {
                best = current;
                slot = idx;
            }
        }

        return slot;
    }

    /**
     * Counts an item across the hotbar.
     *
     * @param item item to count
     * @return total item count
     */
    public static int count(Item item) {
        if (mc.player == null) return 0;

        int count = 0;

        for (int slot = 0; slot < 9; slot++) {
            ItemStack itemStack = stack(slot);
            if (itemStack.isOf(item)) count += itemStack.getCount();
        }

        return count;
    }

    /**
     * Returns the currently selected hotbar slot.
     *
     * @return zero-based selected slot, or 0 if player is null
     */
    public static int selected() {
        if (mc.player == null) return 0;
        return mc.player.getInventory().getSelectedSlot();
    }

    /**
     * Returns a hotbar stack.
     *
     * @param slot zero-based hotbar slot
     * @return item stack stored in the slot, or ItemStack.EMPTY if unavailable
     */
    public static ItemStack stack(int slot) {
        if (mc.player == null || slot < 0 || slot > 8) return ItemStack.EMPTY;
        return mc.player.getInventory().getStack(slot);
    }

    /**
     * Changes the selected hotbar slot on the client.
     *
     * @param slot zero-based hotbar slot
     */
    public static void set(int slot) {
        if (mc.player == null || slot < 0 || slot > 8) return;
        mc.player.getInventory().setSelectedSlot(slot);
    }

    /**
     * Sends the selected hotbar slot to the server.
     *
     * @param slot zero-based hotbar slot
     */
    public static void sync(int slot) {
        if (mc.getNetworkHandler() == null || slot < 0 || slot > 8) return;
        mc.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(slot));
    }

    /**
     * Changes the selected hotbar slot on the client and server.
     *
     * @param slot zero-based hotbar slot
     */
    public static void select(int slot) {
        set(slot);
        sync(slot);
    }

    /**
     * Temporarily swaps an inventory item into the selected hotbar slot.
     *
     * @param slot hotbar or inventory slot to use
     * @return true when the swap was prepared
     */
    public static boolean swap(int slot) {
        return InvUtils.swap(slot, true);
    }

    /**
     * Restores the hotbar state saved by the last temporary swap.
     */
    public static void restore() {
        InvUtils.swapBack();
    }
}