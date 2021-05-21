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

package net.thiccaxe.resonance.feature;

/**
 * An exception thrown when a feature fails to enable.
 *
 * Inspired by Plan (https://github.com/plan-player-analytics/Plan).
 */
public class FeatureEnableException extends Exception {

    /**
     * Thrown when a {@link Feature} fails to enable.
     * @param message the exception message
     */
    public FeatureEnableException(String message) {
        super(message);
    }

    /**
     * Thrown when a {@link Feature} fails to enable.
     * @param message the exception message
     * @param cause the cause of the exception
     */
    public FeatureEnableException(String message, Throwable cause) {
        super(message, cause);
    }

}