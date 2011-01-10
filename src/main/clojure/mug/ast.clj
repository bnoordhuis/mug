;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; mug.ast
;
; AST definition
;

(ns mug.ast
  (:gen-class))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; ast nodes
;

(defmacro defast [type parent types]
  (let [fnsym (symbol (name type))
        args (vec (concat ['ln] types))]
    `(do
       (derive ~type ~parent)
       (defn ~fnsym ~args (list ~type ~@args)))))
				
; contexts

(derive ::context ::ast-node)

(defast ::script-context ::closure [stats])
(defast ::closure-context ::closure [name args stats])

; literals

(derive ::literal ::expr)

(defast ::num-literal ::literal [value])
(defast ::str-literal ::literal [value])
(defast ::obj-literal ::literal [props])
(defast ::array-literal ::literal [exprs])
(defast ::undef-literal ::literal [])
(defast ::null-literal ::literal [])
(defast ::boolean-literal ::literal [value])
(defast ::func-literal ::literal [closure])
(defast ::regexp-literal ::literal [expr flags])

; operations

(derive ::op-expr ::expr)
(derive ::unary-op-expr ::op-expr)
(derive ::binary-op-expr ::op-expr)

(defast ::post-inc-op-expr ::unary-op-expr [expr])
(defast ::pre-inc-op-expr ::unary-op-expr [expr])
(defast ::num-op-expr ::unary-op-expr [expr])
(defast ::neg-op-expr ::unary-op-expr [expr])
(defast ::not-op-expr ::unary-op-expr [expr])
(defast ::bit-not-op-expr ::unary-op-expr [expr])
(defast ::lt-op-expr ::binary-op-expr [left right])
(defast ::lte-op-expr ::binary-op-expr [left right])
(defast ::gt-op-expr ::binary-op-expr [left right])
(defast ::gte-op-expr ::binary-op-expr [left right])
(defast ::add-op-expr ::binary-op-expr [left right])
(defast ::sub-op-expr ::binary-op-expr [left right])
(defast ::mul-op-expr ::binary-op-expr [left right])
(defast ::div-op-expr ::binary-op-expr [left right])
(defast ::mod-op-expr ::binary-op-expr [left right])
(defast ::or-op-expr ::binary-op-expr [left right])
(defast ::and-op-expr ::binary-op-expr [left right])
(defast ::lsh-op-expr ::binary-op-expr [left right])
(defast ::rsh-op-expr ::binary-op-expr [left right])
(defast ::bit-and-op-expr ::binary-op-expr [left right])
(defast ::bit-or-op-expr ::binary-op-expr [left right])
(defast ::bit-xor-op-expr ::binary-op-expr [left right])
(defast ::eq-op-expr ::binary-op-expr [left right])
(defast ::eqs-op-expr ::binary-op-expr [left right])
(defast ::neq-op-expr ::binary-op-expr [left right])
(defast ::neqs-op-expr ::binary-op-expr [left right])
(defast ::instanceof-op-expr ::binary-op-expr [left right])
(defast ::in-op-expr ::binary-op-expr [left right])

; expressions

(derive ::expr ::node)

(defast ::seq-expr ::expr [pre expr])
(defast ::this-expr ::expr [])
(defast ::scope-ref-expr ::expr [value])
(defast ::static-ref-expr ::expr [base value])
(defast ::dyn-ref-expr ::expr [base index])
(defast ::static-method-call-expr ::expr [base value args])
(defast ::dyn-method-call-expr ::expr [base index args])
(defast ::scope-delete-expr ::expr [value])
(defast ::static-delete-expr ::expr [base value])
(defast ::dyn-delete-expr ::expr [base index])
(defast ::new-expr ::expr [constructor args])
(defast ::call-expr ::expr [expr args])
(defast ::scope-assign-expr ::expr [value expr])
(defast ::static-assign-expr ::expr [base value expr])
(defast ::dyn-assign-expr ::expr [base index expr])
(defast ::typeof-expr ::expr [expr])
(defast ::void-expr ::expr [expr])
(defast ::if-expr ::expr [expr then-expr else-expr])

; statements

(derive ::stat ::node)

(defast ::block-stat ::stat [stats])
(defast ::expr-stat ::stat [expr])
(defast ::ret-stat ::stat [expr])
(defast ::if-stat ::stat [expr then-stat else-stat])
(defast ::while-stat ::stat [expr stat])
(defast ::do-while-stat ::stat [expr stat])
(defast ::for-stat ::stat [init expr step stat])
(defast ::for-in-stat ::stat [isvar value expr stat])
(defast ::switch-stat ::stat [expr cases])
(defast ::throw-stat ::stat [expr])
(defast ::try-stat ::stat [stats catch-block finally-stats])
(defast ::var-stat ::stat [vars]) 
(defast ::defn-stat ::stat [closure])
(defast ::break-stat ::stat [label])
(defast ::continue-stat ::stat [label])
