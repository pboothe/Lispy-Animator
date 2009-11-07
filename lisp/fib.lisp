(((lambda (*f) ((lambda (f) (f f)) (lambda (f) (*f (lambda x (apply (f f) x)))))) (lambda (f) (lambda (n) (if (< n 2) n (+ (f (- n 1)) (f (- n 2))))))) 20)
