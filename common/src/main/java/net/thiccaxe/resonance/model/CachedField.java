/*
 * This file is part of Resonance, licensed under the MIT License.
 *
 * Copyright (c) 2021 thiccaxe
 * Copyright (c) 2021 contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.thiccaxe.resonance.model;

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
