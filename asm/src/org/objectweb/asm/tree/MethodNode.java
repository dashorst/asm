/***
 * ASM: a very small and fast Java bytecode manipulation framework
 * Copyright (c) 2000,2002,2003 INRIA, France Telecom
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

package org.objectweb.asm.tree;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.CodeVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.Type;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A node that represents a method.
 * 
 * @author Eric Bruneton
 */

public class MethodNode extends AttributeNode implements CodeVisitor {

  /**
   * The method's access flags (see {@link org.objectweb.asm.Constants}). This
   * field also indicates if the method is synthetic and/or deprecated.
   */

  public int access;

  /**
   * The method's name.
   */

  public String name;

  /**
   * The method's descriptor (see {@link org.objectweb.asm.Type Type}).
   */

  public String desc;

  /**
   * The internal names of the method's exception classes (see {@link
   * org.objectweb.asm.Type#getInternalName() getInternalName}). This list is a
   * list of {@link String} objects.
   */

  public final List exceptions;

  /**
   * TODO.
   */
  
  public Object annotationDefault;
  
  /**
   * TODO.
   */
  
  public List[] visibleParameterAnnotations;
  
  /**
   * TODO.
   */
  
  public List[] invisibleParameterAnnotations;

  /**
   * The instructions of this method. This list is a list of {@link
   * AbstractInsnNode AbstractInsnNode} and {@link Label Label} objects.
   */

  public final List instructions;

  /**
   * The try catch blocks of this method. This list is a list of {@link
   * TryCatchBlockNode TryCatchBlockNode} objects.
   */

  public final List tryCatchBlocks;

  /**
   * The maximum stack size of this method.
   */

  public int maxStack;

  /**
   * The maximum number of local variables of this method.
   */

  public int maxLocals;

  /**
   * The local variables of this method. This list is a list of {@link
   * LocalVariableNode LocalVariableNode} objects.
   */

  public final List localVariables;

  /**
   * The line numbers of this method. This list is a list of {@link
   * LineNumberNode LineNumberNode} objects.
   */

  public final List lineNumbers;

  /**
   * Constructs a new {@link MethodNode MethodNode} object.
   *
   * @param access the method's access flags (see {@link
   *      org.objectweb.asm.Constants}). This parameter also indicates if the
   *      method is synthetic and/or deprecated.
   * @param name the method's name.
   * @param desc the method's descriptor (see {@link org.objectweb.asm.Type
   *      Type}).
   * @param exceptions the internal names of the method's exception
   *      classes (see {@link org.objectweb.asm.Type#getInternalName()
   *      getInternalName}). May be <tt>null</tt>.
   * @param attrs the non standard attributes of the method.
   */

  public MethodNode (
    final int access,
    final String name,
    final String desc,
    final String[] exceptions)
  {
    this.access = access;
    this.name = name;
    this.desc = desc;    
    this.exceptions = new ArrayList();
    int params = Type.getArgumentTypes(desc).length;
    this.visibleParameterAnnotations = new List[params];
    this.invisibleParameterAnnotations = new List[params];
    for (int i = 0; i < params; ++i) {
      this.visibleParameterAnnotations[i] = new ArrayList();
      this.invisibleParameterAnnotations[i] = new ArrayList();
    }
    this.instructions = new ArrayList();
    this.tryCatchBlocks = new ArrayList();
    this.localVariables = new ArrayList();
    this.lineNumbers = new ArrayList();
    if (exceptions != null) {
      this.exceptions.addAll(Arrays.asList(exceptions));
    }    
  }
  
  public AnnotationVisitor visitAnnotationDefault () {
    return new AnnotationNode(new ArrayList() {
      public boolean add (Object o) {
        annotationDefault = o;
        return super.add(o);
      }
    });
  }

  public AnnotationVisitor visitParameterAnnotation (
    final int parameter,
    final String type,
    final boolean visible) 
  {
    AnnotationNode an = new AnnotationNode(type);
    if (visible) {
      visibleParameterAnnotations[parameter].add(an);
    } else {
      invisibleParameterAnnotations[parameter].add(an);
    }
    return an;
  }
  
  public void visitInsn (final int opcode) {
    AbstractInsnNode n = new InsnNode(opcode);
    instructions.add(n);
  }

  public void visitVarInsn (final int opcode, final int var) {
    AbstractInsnNode n = new VarInsnNode(opcode, var);
    instructions.add(n);
  }

  public void visitTypeInsn (final int opcode, final String desc) {
    AbstractInsnNode n = new TypeInsnNode(opcode, desc);
    instructions.add(n);
  }

  public void visitFieldInsn (
    final int opcode,
    final String owner,
    final String name,
    final String desc)
  {
    AbstractInsnNode n = new FieldInsnNode(opcode, owner, name, desc);
    instructions.add(n);
  }

  public void visitMethodInsn (
    final int opcode,
    final String owner,
    final String name,
    final String desc)
  {
    AbstractInsnNode n = new MethodInsnNode(opcode, owner, name, desc);
    instructions.add(n);
  }

  public void visitJumpInsn (final int opcode, final Label label) {
    AbstractInsnNode n = new JumpInsnNode(opcode, label);
    instructions.add(n);
  }

  public void visitLabel (final Label label) {
    instructions.add(label);
  }

  public void visitLdcInsn (final Object cst) {
    AbstractInsnNode n = new LdcInsnNode(cst);
    instructions.add(n);
  }

  public void visitIincInsn (final int var, final int increment) {
    AbstractInsnNode n = new IincInsnNode(var, increment);
    instructions.add(n);
  }

  public void visitTableSwitchInsn (
    final int min,
    final int max,
    final Label dflt,
    final Label labels[])
  {
    AbstractInsnNode n = new TableSwitchInsnNode(min, max, dflt, labels);
    instructions.add(n);
  }

  public void visitLookupSwitchInsn (
    final Label dflt,
    final int keys[],
    final Label labels[])
  {
    AbstractInsnNode n = new LookupSwitchInsnNode(dflt, keys, labels);
    instructions.add(n);
  }

  public void visitMultiANewArrayInsn (final String desc, final int dims) {
    AbstractInsnNode n = new MultiANewArrayInsnNode(desc, dims);
    instructions.add(n);
  }

  public void visitTryCatchBlock (
    final Label start,
    final Label end,
    final Label handler,
    final String type)
  {
    TryCatchBlockNode n = new TryCatchBlockNode(start, end, handler, type);
    tryCatchBlocks.add(n);
  }

  public void visitMaxs (final int maxStack, final int maxLocals) {
    this.maxStack = maxStack;
    this.maxLocals = maxLocals;
  }

  public void visitLocalVariable (
    final String name,
    final String desc,
    final Label start,
    final Label end,
    final int index)
  {
    LocalVariableNode n = new LocalVariableNode(name, desc, start, end, index);
    localVariables.add(n);
  }

  public void visitLineNumber (final int line, final Label start) {
    LineNumberNode n = new LineNumberNode(line, start);
    lineNumbers.add(n);
  }
  

  /**
   * Makes the given class visitor visit this method.
   *
   * @param cv a class visitor.
   */

  public void accept (final ClassVisitor cv) {
    String[] exceptions = new String[this.exceptions.size()];
    this.exceptions.toArray(exceptions);
    CodeVisitor mv = cv.visitMethod(access, name, desc, exceptions);
    // visits the method attributes
    int i;
    if (annotationDefault != null) {
      AnnotationVisitor av = mv.visitAnnotationDefault();
      AnnotationNode.accept(av, null, annotationDefault);
    }
    for (i = 0; i < visibleAnnotations.size(); ++i) {
      AnnotationNode an = (AnnotationNode)visibleAnnotations.get(i); 
      an.accept(mv.visitParameterAnnotation(i, an.type, true));
    }
    for (i = 0; i < invisibleAnnotations.size(); ++i) {
      AnnotationNode an = (AnnotationNode)invisibleAnnotations.get(i); 
      an.accept(mv.visitParameterAnnotation(i, an.type, false));
    }
    super.accept(mv);
    
    if (mv != null && instructions.size() > 0) {
      // visits instructions
      for (i = 0; i < instructions.size(); ++i) {
        Object insn = instructions.get(i);
        if (insn instanceof Label) {
          mv.visitLabel((Label)insn);
        } else {
          ((AbstractInsnNode)insn).accept(mv);
        }
      }
      // visits try catch blocks
      for (i = 0; i < tryCatchBlocks.size(); ++i) {
        ((TryCatchBlockNode)tryCatchBlocks.get(i)).accept(mv);
      }
      // visits maxs
      mv.visitMaxs(maxStack, maxLocals);
      // visits local variables
      for (i = 0; i < localVariables.size(); ++i) {
        ((LocalVariableNode)localVariables.get(i)).accept(mv);
      }
      // visits line numbers
      for (i = 0; i < lineNumbers.size(); ++i) {
        ((LineNumberNode)lineNumbers.get(i)).accept(mv);
      }
    }
  }
}
