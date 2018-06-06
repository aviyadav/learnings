/**
 * Copyright (c) 2012 scireum GmbH - Andreas Haufler - aha@scireum.de
 *
 * Permission is hereby granted, free of charge, to any person obtaining a 
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */
package com.scireum.open.commons;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Generates unique ids for each call of "next". Once that almost Long.MAX_VALUE
 * ids where generated, the internal counter is reset and ids are re-used.
 * Therefore these ids are not meant to be persisted, since there is no
 * guarantee that they are unique for ever, but for a long time.
 */
public class AtomicIdGenerator {
	private final AtomicLong gen = new AtomicLong();

	public long next() {
		long next = gen.incrementAndGet();
		if (next > Long.MAX_VALUE - 10) {
			// If we have an overflow, we get a real lock, check if another
			// thread was faster, and if not, we reset the counter.
			synchronized (gen) {
				if (gen.get() > Long.MAX_VALUE - 10) {
					gen.set(0l);
				}
			}
		}
		return next;
	}

	public String nextString() {
		return String.valueOf(next());
	}
}
