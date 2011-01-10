(ns mug.asm.scopes
  (:gen-class)
  (:use 
    mug.ast
    [mug.asm util config analyze]))

(import (org.objectweb.asm ClassWriter Opcodes Label))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; scopes
;

(defn asm-compile-scope-fields [qn scope cw]
	(doseq [var scope]
		(.visitEnd (.visitField cw, 0, (str "_" var), (sig-obj qn-object), nil, nil))))

(defn asm-compile-scope-methods [qn scope cw]
	(doseq [var scope]
		(doto (.visitMethod cw, Opcodes/ACC_PUBLIC, (str "get_" var), (sig-call (sig-obj qn-object)), nil, nil)
			(.visitCode)
			(.visitVarInsn Opcodes/ALOAD, 0)
			(.visitFieldInsn Opcodes/GETFIELD, qn, (str "_" var), (sig-obj qn-object));
			(.visitInsn Opcodes/ARETURN)
			(.visitMaxs 1, 1)
			(.visitEnd))
		(doto (.visitMethod cw, Opcodes/ACC_PUBLIC, (str "set_" var), (sig-call (sig-obj qn-object) sig-void), nil, nil)
			(.visitCode)
			(.visitVarInsn Opcodes/ALOAD, 0)
			(.visitVarInsn Opcodes/ALOAD, 1)
			(.visitFieldInsn Opcodes/PUTFIELD, qn, (str "_" var), (sig-obj qn-object));
			(.visitInsn Opcodes/RETURN)
			(.visitMaxs 2, 2)
			(.visitEnd))))

(defn asm-compile-scope-init [qn super scope cw]
  (let [mw (.visitMethod cw, Opcodes/ACC_PUBLIC, "<init>", (sig-call sig-void), nil, nil)]
  	(.visitCode mw)
  	(.visitVarInsn mw Opcodes/ALOAD, 0)
  	(.visitMethodInsn mw Opcodes/INVOKESPECIAL, super, "<init>", "()V")  
  	(.visitInsn mw Opcodes/RETURN)
  	(.visitMaxs mw 1, 1)
  	(.visitEnd mw)))

(defn asm-compile-scope-classes [ast]
	(into {}
		(map-indexed (fn [i context]
			(let [qn (qn-js-scope i)
            scope (filter #(nil? (ref-reg context %)) (ast-context-vars context)) ; properties only for non-register references
            cw (new ClassWriter ClassWriter/COMPUTE_MAXS)
            super (if (= i 0) qn-js-toplevel qn-object)]
				(.visit cw, Opcodes/V1_6, (+ Opcodes/ACC_SUPER Opcodes/ACC_PUBLIC), qn, nil,
          super, nil)
				(asm-compile-scope-fields qn scope cw)
				(asm-compile-scope-methods qn scope cw)
				(asm-compile-scope-init qn super scope cw)
				(.visitEnd cw)
				[qn (.toByteArray cw)]))
		(ast-contexts ast))))