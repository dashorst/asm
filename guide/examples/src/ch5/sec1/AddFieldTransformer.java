/***
 * ASM Guide
 * Copyright (c) 2007 Eric Bruneton, 2011 Google
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the copyright holders nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package ch5.sec1;

import java.util.List;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

/**
 * ASM Guide example class.
 * 
 * @author Eric Bruneton
 */
public class AddFieldTransformer extends ClassTransformer {

  private int fieldAccess;

  private String fieldName;

  private String fieldDesc;

  public AddFieldTransformer(ClassTransformer ct, int fieldAccess,
      String fieldName, String fieldDesc) {
    super(ct);
    this.fieldAccess = fieldAccess;
    this.fieldName = fieldName;
    this.fieldDesc = fieldDesc;
  }

  public void transform(ClassNode cn) {
    boolean isPresent = false;
    for (FieldNode fn : (List<FieldNode>) cn.fields) {
      if (fieldName.equals(fn.name)) {
        isPresent = true;
        break;
      }
    }
    if (!isPresent) {
      cn.fields.add(new FieldNode(fieldAccess, fieldName, fieldDesc,
          null, null));
    }
    super.transform(cn);
  }
}
