/***
 * ASM examples: examples showing how ASM can be used
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
 *
 * Contact: Eric.Bruneton@rd.francetelecom.com
 *
 * Author: Eric Bruneton
 */

import org.objectweb.asm.*;
import java.lang.reflect.*;
import java.io.FileOutputStream;

public class Helloworld extends ClassLoader implements Constants {

  public static void main (final String args[]) throws Exception {

    /*
     * Generates the bytecode corresponding to the following Java class:
     *
     * public class Example {
     *   public static void main (String[] args) {
     *     System.out.println("Hello world!");
     *   }
     * }
     *
     */

    // creates a ClassWriter for the Example public class,
    // which inherits from Object
    ClassWriter cw = new ClassWriter(false);
    cw.visit(ACC_PUBLIC, "Example", "java/lang/Object", null, null);

    // creates a MethodWriter for the (implicit) constructor
    CodeVisitor mw = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
    // pushes the 'this' variable
    mw.visitVarInsn(ALOAD, 0);
    // invokes the super class constructor
    mw.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
    mw.visitInsn(RETURN);
    // this code uses a maximum of one stack element and one local variable
    mw.visitMaxs(1, 1);

    // creates a MethodWriter for the 'main' method
    mw = cw.visitMethod(
      ACC_PUBLIC + ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);
    // pushes the 'out' field (of type PrintStream) of the System class
    mw.visitFieldInsn(
      GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
    // pushes the "Hello World!" String constant
    mw.visitLdcInsn("Hello world!");
    // invokes the 'println' method (defined in the PrintStream class)
    mw.visitMethodInsn(
      INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V");
    mw.visitInsn(RETURN);
    // this code uses a maximum of two stack elements and two local variables
    mw.visitMaxs(2, 2);

    // gets the bytecode of the Example class, and loads it dynamically
    byte[] code = cw.toByteArray();

    FileOutputStream fos = new FileOutputStream("Example.class");
    fos.write(code);
    fos.close();

    Helloworld loader = new Helloworld();
    Class exampleClass = loader.defineClass("Example", code, 0, code.length);

    // uses the dynamically generated class to print 'Helloworld'
    Method main = exampleClass.getMethods()[0];
    main.invoke(null, new Object[] {null});
  }
}
