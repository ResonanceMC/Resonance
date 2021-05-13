package net.thiccaxe.resonance.model;

import net.thiccaxe.resonance.plugin.ResonancePlugin;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class CachedField<F, V>  {

    private long modifyTime;
    private final long cacheTime;
    private final FieldSupplier<F, V> supplier;

    private F field;


    public CachedField (long cacheTime, F initialValue, FieldSupplier<F, V> supplier) {
        this.cacheTime = cacheTime;
        modifyTime = System.currentTimeMillis();
        this.field = initialValue;
        this.supplier = supplier;
    }

    public F get(V basedValue) {
        if (System.currentTimeMillis() - modifyTime > cacheTime) {
            modifyTime = System.currentTimeMillis();
            field = supplier.get(basedValue);
        }
        return field;
    }
    @FunctionalInterface
    public interface FieldSupplier<F, V> {
        public F get(V val);
    }



}
