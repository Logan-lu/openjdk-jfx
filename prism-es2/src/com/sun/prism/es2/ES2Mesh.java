/*
 * Copyright (c) 2013, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package com.sun.prism.es2;

import com.sun.prism.impl.BaseMesh;
import com.sun.prism.impl.Disposer;

/**
 * TODO: 3D - Need documentation
 */
class ES2Mesh extends BaseMesh {
    static int count = 0;

    private final ES2Context context;
    private final long nativeHandle;

    private ES2Mesh(ES2Context context, long nativeHandle, Disposer.Record disposerRecord) {
        super(disposerRecord);
        this.context = context;
        this.nativeHandle = nativeHandle;
        count++;
    }

    static ES2Mesh create(ES2Context context) {
        long nativeHandle = context.createES2Mesh();
        return new ES2Mesh(context, nativeHandle, new ES2MeshDisposerRecord(context, nativeHandle));
    }

    long getNativeHandle() {
        return nativeHandle;
    }

    @Override
    public void dispose() {
        disposerRecord.dispose();
        count--;
    }

    public int getCount() {
        return count;
    }

    @Override
    public boolean buildNativeGeometry() {
        float vertexBuffer[] = getVertsGM();
        int indexBuffer[] = getIndexGM();
        return context.buildNativeGeometry(nativeHandle, vertexBuffer, indexBuffer);

    }

    static class ES2MeshDisposerRecord implements Disposer.Record {

        private final ES2Context context;
        private long nativeHandle;

        ES2MeshDisposerRecord(ES2Context context, long nativeHandle) {
            this.context = context;
            this.nativeHandle = nativeHandle;
        }

        void traceDispose() {
        }

        public void dispose() {
            if (nativeHandle != 0L) {
                traceDispose();
                context.releaseES2Mesh(nativeHandle);
                nativeHandle = 0L;
            }
        }
    }
}
