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
package com.scireum.open.nucleus.core;

import java.util.ArrayList;
import java.util.List;

import com.scireum.open.nucleus.Nucleus;
import com.scireum.open.nucleus.Nucleus.ClassLoadAction;

/**
 * Loads all classes wearing the @Register annotation.
 */
public class ServiceLoadAction implements ClassLoadAction {

	private List<Object> createdObjects = new ArrayList<Object>();

	@Override
	public void handle(Class<?> clazz) throws Exception {
		if (clazz.isAnnotationPresent(Register.class)) {
			Object instance = clazz.newInstance();
			createdObjects.add(instance);
			for (Class<?> marker : clazz.getAnnotation(Register.class)
					.classes()) {
				Nucleus.register(marker, instance);
			}
		}

	}

	@Override
	public void loadingCompleted() throws Exception {
		for (Object obj : createdObjects) {
			Factory.inject(obj);
		}
		createdObjects.clear();
	}

}
