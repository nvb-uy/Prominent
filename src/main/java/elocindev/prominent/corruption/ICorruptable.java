package elocindev.prominent.corruption;

import net.minecraft.entity.EquipmentSlot;

public interface ICorruptable {
    public boolean isCorrupted();

    public int corruptionAmount();
    
    public EquipmentSlot[] getSlots();
}
